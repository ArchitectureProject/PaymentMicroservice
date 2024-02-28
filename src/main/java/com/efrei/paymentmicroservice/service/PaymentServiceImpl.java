package com.efrei.paymentmicroservice.service;

import com.efrei.paymentmicroservice.exception.custom.PaymentAttemptNotFoundException;
import com.efrei.paymentmicroservice.model.PaymentAttempt;
import com.efrei.paymentmicroservice.model.PaymentResult;
import com.efrei.paymentmicroservice.model.PaymentState;
import com.efrei.paymentmicroservice.model.PaymentType;
import com.efrei.paymentmicroservice.model.dto.PaymentToProcess;
import com.efrei.paymentmicroservice.model.dto.ProcessedPayment;
import com.efrei.paymentmicroservice.model.dto.ReceivedPaymentAttempt;
import com.efrei.paymentmicroservice.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final MessagePublisherService messagePublisherService;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              MessagePublisherService messagePublisherService) {
        this.paymentRepository = paymentRepository;
        this.messagePublisherService = messagePublisherService;
    }

    @Override
    public PaymentAttempt createPaymentAttempt(ReceivedPaymentAttempt receivedPaymentAttempt) {
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
    public List<PaymentAttempt> getAllPaymentAttempts() {
        return paymentRepository.findAll();
    }

    @Override
    public PaymentAttempt getPaymentAttemptById(String paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentAttemptNotFoundException("Payment attempt not found with id: " + paymentId));
    }
}