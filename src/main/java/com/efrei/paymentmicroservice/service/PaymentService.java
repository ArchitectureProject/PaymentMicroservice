package com.efrei.paymentmicroservice.service;

import com.efrei.paymentmicroservice.model.PaymentAttempt;
import com.efrei.paymentmicroservice.model.dto.ProcessedPayment;
import com.efrei.paymentmicroservice.model.dto.ReceivedPaymentAttempt;

import java.util.List;

public interface PaymentService {
    PaymentAttempt createPaymentAttempt(String bearerToken, ReceivedPaymentAttempt receivedPaymentAttempt);
    PaymentAttempt handleProcessedPayments(ProcessedPayment processedPayment);
    List<PaymentAttempt> getAllPaymentAttempts(String bearerToken);
    PaymentAttempt getPaymentAttemptById(String bearerToke, String paymentId);
}
