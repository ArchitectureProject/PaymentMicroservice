package com.efrei.paymentmicroservice.service;

import com.efrei.paymentmicroservice.exception.custom.PaymentAttemptNotFoundException;
import com.efrei.paymentmicroservice.exception.custom.WrongUserRoleException;
import com.efrei.paymentmicroservice.model.PaymentAttempt;
import com.efrei.paymentmicroservice.model.PaymentResult;
import com.efrei.paymentmicroservice.model.PaymentState;
import com.efrei.paymentmicroservice.model.PaymentType;
import com.efrei.paymentmicroservice.model.dto.PaymentToProcess;
import com.efrei.paymentmicroservice.model.dto.ProcessedPayment;
import com.efrei.paymentmicroservice.model.dto.ReceivedPaymentAttempt;
import com.efrei.paymentmicroservice.repository.PaymentRepository;
import com.efrei.paymentmicroservice.model.UserRole;
import com.efrei.paymentmicroservice.service.rabbitMq.MessagePublisherService;
import com.efrei.paymentmicroservice.utils.JwtUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final MessagePublisherService messagePublisherService;
    private final JwtUtils jwtUtils;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              MessagePublisherService messagePublisherService,
                              JwtUtils jwtUtils) {
        this.paymentRepository = paymentRepository;
        this.messagePublisherService = messagePublisherService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public PaymentAttempt createPaymentAttempt(String bearerToken, ReceivedPaymentAttempt receivedPaymentAttempt) {
        jwtUtils.validateJwt(bearerToken.substring(7), null);

        PaymentAttempt paymentAttempt = new PaymentAttempt();
        paymentAttempt.setLaunchedAt(Instant.now().toEpochMilli());
        paymentAttempt.setSessionId(receivedPaymentAttempt.sessionId());
        paymentAttempt.setUserId(receivedPaymentAttempt.userId());
        paymentAttempt.setAmount(receivedPaymentAttempt.amount());
        paymentAttempt.setPaymentType(receivedPaymentAttempt.paymentType());
        if(paymentAttempt.getPaymentType().equals(PaymentType.CREDIT_CARD)){
            paymentAttempt.setCreditCardInfos(receivedPaymentAttempt.creditCardInfos());
            paymentAttempt.setPaymentState(PaymentState.PENDING);
        }
        else {
            paymentAttempt.setPaymentState(PaymentState.DONE);
        }
        paymentAttempt = paymentRepository.save(paymentAttempt);

        if(paymentAttempt.getPaymentType().equals(PaymentType.CREDIT_CARD)){
            PaymentToProcess paymentToProcess = new PaymentToProcess(
                    paymentAttempt.getId(),
                    paymentAttempt.getAmount(),
                    paymentAttempt.getCreditCardInfos()
            );
            messagePublisherService.sendMessage(paymentToProcess);
        }
        return paymentAttempt;
    }

    @Override
    public PaymentAttempt handleProcessedPayments(ProcessedPayment processedPayment) {
        PaymentState paymentState = PaymentState.REFUSED;
        if(processedPayment.paymentResult().equals(PaymentResult.ACCEPTED)){
            paymentState = PaymentState.DONE;
        }

        paymentRepository.changePaymentAttemptState(paymentState, processedPayment.paymentId());

        return paymentRepository.findById(processedPayment.paymentId())
                .orElseThrow(() -> new PaymentAttemptNotFoundException("Payment attempt not found with id: " + processedPayment.paymentId()));
    }

    @Override
    public List<PaymentAttempt> getAllPaymentAttempts(String bearerToken) {
        if(!jwtUtils.validateJwt(bearerToken.substring(7), UserRole.AGENT)){
            throw new WrongUserRoleException("User role does not gives him rights to call this endpoint");
        }

        return paymentRepository.findAll();
    }

    @Override
    public PaymentAttempt getPaymentAttemptById(String bearerToken, String id) {
        if(!jwtUtils.validateJwt(bearerToken.substring(7), UserRole.AGENT)){
            throw new WrongUserRoleException("User role does not gives him rights to call this endpoint");
        }
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentAttemptNotFoundException("Payment attempt not found with id: " + id));
    }
}