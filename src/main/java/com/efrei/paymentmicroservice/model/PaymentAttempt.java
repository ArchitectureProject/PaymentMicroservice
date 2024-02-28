package com.efrei.paymentmicroservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PaymentAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private PaymentState paymentState;

    private PaymentType paymentType;

    private Long launchedAt;

    private String sessionId;

    private String userId;

    private float amount;

    private CreditCardInfos creditCardInfos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PaymentState getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(PaymentState paymentState) {
        this.paymentState = paymentState;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Long getLaunchedAt() {
        return launchedAt;
    }

    public void setLaunchedAt(Long launchedAt) {
        this.launchedAt = launchedAt;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public CreditCardInfos getCreditCardInfos() {
        return creditCardInfos;
    }

    public void setCreditCardInfos(CreditCardInfos creditCardInfos) {
        this.creditCardInfos = creditCardInfos;
    }
}
