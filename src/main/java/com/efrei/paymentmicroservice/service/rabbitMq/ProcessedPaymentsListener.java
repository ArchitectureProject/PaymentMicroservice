package com.efrei.paymentmicroservice.service.rabbitMq;

import com.efrei.paymentmicroservice.model.dto.ProcessedPayment;
import com.efrei.paymentmicroservice.service.PaymentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ProcessedPaymentsListener {

    private final PaymentService paymentService;

    public ProcessedPaymentsListener(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @RabbitListener(queues = "${app.queue.name}")
    public void receiveMessage(ProcessedPayment processedPayment) {
        System.out.println("Received message from processedPaymentQueue: " + processedPayment);
        paymentService.handleProcessedPayments(processedPayment);
    }
}
