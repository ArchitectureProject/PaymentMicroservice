package com.efrei.paymentmicroservice.model.dto;

import com.efrei.paymentmicroservice.model.PaymentResult;

public record ProcessedPayment(String paymentId, PaymentResult paymentResult) {
}
