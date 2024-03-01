package com.efrei.paymentmicroservice.service.rabbitMq;

import com.efrei.paymentmicroservice.model.dto.PaymentToProcess;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessagePublisherService {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey}")
    private String routingKey;

    public MessagePublisherService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(PaymentToProcess message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        System.out.println("Send message: " + message);
    }
}