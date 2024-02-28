package com.efrei.paymentmicroservice.controller;

import com.efrei.paymentmicroservice.model.PaymentAttempt;
import com.efrei.paymentmicroservice.model.dto.ReceivedPaymentAttempt;
import com.efrei.paymentmicroservice.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payment-attempt")
    public PaymentAttempt createPaymentAttempt(@RequestBody ReceivedPaymentAttempt receivedPaymentAttempt) {
        return paymentService.createPaymentAttempt(receivedPaymentAttempt);
    }

    @GetMapping("/payment-attempt")
    public List<PaymentAttempt> getAllPaymentAttempts(){
        return paymentService.getAllPaymentAttempts();
    }

    @GetMapping("/payment-attempt/{id}")
    public PaymentAttempt getPaymentAttemptById(@PathVariable String id){
        return paymentService.getPaymentAttemptById(id);
    }


}
