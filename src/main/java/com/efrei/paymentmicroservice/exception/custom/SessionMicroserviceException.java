package com.efrei.paymentmicroservice.exception.custom;

public class SessionMicroserviceException extends RuntimeException {
    public SessionMicroserviceException(String message, Exception e) {
        super(message, e);
    }
}