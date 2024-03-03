package com.efrei.paymentmicroservice.exception.custom;

public class EmailSenderMicroserviceException extends RuntimeException {
    public EmailSenderMicroserviceException(String message, Exception e) {
        super(message, e);
    }
}
