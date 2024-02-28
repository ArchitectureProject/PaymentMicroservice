package com.efrei.paymentmicroservice.model.dto;

import com.efrei.paymentmicroservice.model.CreditCardInfos;
import com.efrei.paymentmicroservice.model.PaymentType;

public record ReceivedPaymentAttempt(PaymentType paymentType, String sessionId, String userId, float amount, CreditCardInfos creditCardInfos) {
}
