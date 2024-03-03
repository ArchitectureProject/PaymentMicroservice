package com.efrei.paymentmicroservice.exception.custom;

public class UserMicroserviceException extends RuntimeException {
    public UserMicroserviceException(String message, Exception e) {
        super(message, e);
    }
}
