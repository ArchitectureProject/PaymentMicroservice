package com.efrei.paymentmicroservice.exception.custom;

public class WrongUserRoleException extends RuntimeException {
    public WrongUserRoleException(String message) {
        super(message);
    }
}
