package com.efrei.paymentmicroservice.exception.custom;

public class PaymentAttemptNotFoundException extends RuntimeException {

    public PaymentAttemptNotFoundException(String message){
        super(message);
    }
}
