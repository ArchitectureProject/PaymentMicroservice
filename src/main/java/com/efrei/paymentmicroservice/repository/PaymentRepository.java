package com.efrei.paymentmicroservice.repository;

import com.efrei.paymentmicroservice.model.PaymentAttempt;
import com.efrei.paymentmicroservice.model.PaymentState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PaymentRepository extends JpaRepository<PaymentAttempt, String> {
    @Transactional
    @Modifying
    @Query("UPDATE PaymentAttempt p SET p.paymentState = :paymentState WHERE p.id = :paymentId")
    void changePaymentAttemptState(PaymentState paymentState, String paymentId);
}
