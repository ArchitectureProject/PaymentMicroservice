package com.efrei.paymentmicroservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

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

    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    private String bearerToken;

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

    public String getBearerToken() {
        return bearerToken;
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    @Override
    public String toString() {
        return "PaymentAttempt{" +
                "id='" + id + '\'' +
                ", paymentState=" + paymentState +
                ", paymentType=" + paymentType +
                ", launchedAt=" + launchedAt +
                ", sessionId='" + sessionId + '\'' +
                ", userId='" + userId + '\'' +
                ", bearerToken='" + bearerToken + '\'' +
                ", amount=" + amount +
                ", creditCardInfos=" + creditCardInfos +
                '}';
    }
}
