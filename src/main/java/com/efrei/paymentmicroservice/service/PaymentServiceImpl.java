package com.efrei.paymentmicroservice.service;

import com.efrei.paymentmicroservice.exception.custom.PaymentAttemptNotFoundException;
import com.efrei.paymentmicroservice.exception.custom.WrongUserRoleException;
import com.efrei.paymentmicroservice.model.PaymentAttempt;
import com.efrei.paymentmicroservice.model.PaymentResult;
import com.efrei.paymentmicroservice.model.PaymentState;
import com.efrei.paymentmicroservice.model.PaymentType;
import com.efrei.paymentmicroservice.model.dto.*;
import com.efrei.paymentmicroservice.provider.mailSender.MailSenderProvider;
import com.efrei.paymentmicroservice.provider.user.UserProvider;
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
    private final MailSenderProvider mailSenderProvider;
    private final UserProvider userProvider;

    public PaymentServiceImpl(PaymentRepository paymentRepository, MessagePublisherService messagePublisherService, JwtUtils jwtUtils, MailSenderProvider mailSenderProvider, UserProvider userProvider) {
        this.paymentRepository = paymentRepository;
        this.messagePublisherService = messagePublisherService;
        this.jwtUtils = jwtUtils;
        this.mailSenderProvider = mailSenderProvider;
        this.userProvider = userProvider;
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
        paymentAttempt.setBearerToken(bearerToken.substring(7));
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
        else if(paymentAttempt.getPaymentType().equals(PaymentType.CASH)){
            handleSuccessfulPayment(paymentAttempt);
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

        PaymentAttempt paymentAttempt = paymentRepository.findById(processedPayment.paymentId())
                .orElseThrow(() -> new PaymentAttemptNotFoundException("Payment attempt not found with id: " + processedPayment.paymentId()));

        if(paymentState.equals(PaymentState.DONE)){
            handleSuccessfulPayment(paymentAttempt);
        }
        else {
            handleUnsuccessfulPayment(paymentAttempt);
        }

        return paymentAttempt;
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

    private void handleSuccessfulPayment(PaymentAttempt paymentAttempt){
        //TODO
        sendEmailForSuccessfulPayment(paymentAttempt);
        //Send request to session to update the flag and the amount
    }

    private void sendEmailForSuccessfulPayment(PaymentAttempt paymentAttempt){
        User user = userProvider.getUserById("Bearer " + paymentAttempt.getBearerToken(), paymentAttempt.getUserId());
        MailSenderRequest mailSenderRequest = createSuccessfulPaymentMailRequest(paymentAttempt, user.email());
        mailSenderProvider.sendEmail("Bearer " + paymentAttempt.getBearerToken(), mailSenderRequest);
    }

    private MailSenderRequest createSuccessfulPaymentMailRequest(PaymentAttempt paymentAttempt, String userEmail){
        MailSenderParam mailSenderParam1 = new MailSenderParam("name",userEmail);
        MailSenderParam mailSenderParam2 = new MailSenderParam("paymentType", paymentAttempt.getPaymentType().emailName);
        MailSenderParam mailSenderParam3 = new MailSenderParam("amount", String.valueOf(paymentAttempt.getAmount()));
        return new MailSenderRequest(
                "successfulPayment",
                List.of(mailSenderParam1, mailSenderParam2, mailSenderParam3),
                userEmail,
                "Thank you for your purchase !");
    }

    private void handleUnsuccessfulPayment(PaymentAttempt paymentAttempt){
        //TODO
        sendEmailForUnsuccessfulPayment(paymentAttempt);
        //Send request to session to update the flag
    }

    private void sendEmailForUnsuccessfulPayment(PaymentAttempt paymentAttempt){
        User user = userProvider.getUserById("Bearer " + paymentAttempt.getBearerToken(), paymentAttempt.getUserId());
        MailSenderRequest mailSenderRequest = createUnsuccessfulPaymentMailRequest(paymentAttempt, user.email());
        mailSenderProvider.sendEmail("Bearer " + paymentAttempt.getBearerToken(), mailSenderRequest);
    }

    private MailSenderRequest createUnsuccessfulPaymentMailRequest(PaymentAttempt paymentAttempt, String userEmail){
        MailSenderParam mailSenderParam1 = new MailSenderParam("name", userEmail);
        MailSenderParam mailSenderParam2 = new MailSenderParam("amount", String.valueOf(paymentAttempt.getAmount()));
        return new MailSenderRequest(
                "refusedPayment",
                List.of(mailSenderParam1, mailSenderParam2),
                userEmail,
                "Your payment have been refused");
    }
}