package com.efrei.paymentmicroservice.model.dto;

import com.efrei.paymentmicroservice.model.CreditCardInfos;

public record PaymentToProcess(String paymentId, float amount, CreditCardInfos creditCardInfos){
}