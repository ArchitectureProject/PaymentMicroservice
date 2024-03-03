package com.efrei.paymentmicroservice.model;

public enum PaymentType {
    CREDIT_CARD("Credit card"),
    CASH("Cash");
    public final String emailName;

    PaymentType(String emailName) {
        this.emailName = emailName;
    }


}
