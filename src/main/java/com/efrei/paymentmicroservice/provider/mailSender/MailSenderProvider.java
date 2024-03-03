package com.efrei.paymentmicroservice.provider.mailSender;

import com.efrei.paymentmicroservice.model.dto.MailSenderRequest;

public interface MailSenderProvider {
    void sendEmail(String bearerToken, MailSenderRequest mailSenderRequest);
}
