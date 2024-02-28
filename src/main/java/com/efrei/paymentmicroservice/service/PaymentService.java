package com.efrei.paymentmicroservice.service;

import com.efrei.paymentmicroservice.model.PaymentAttempt;
import com.efrei.paymentmicroservice.model.dto.ProcessedPayment;
import com.efrei.paymentmicroservice.model.dto.ReceivedPaymentAttempt;

import java.util.List;

public interface PaymentService {
    PaymentAttempt createPaymentAttempt(ReceivedPaymentAttempt receivedPaymentAttempt);
    PaymentAttempt handleProcessedPayments(ProcessedPayment processedPayment);
    List<PaymentAttempt> getAllPaymentAttempts();
    PaymentAttempt getPaymentAttemptById(String paymentId);
}
