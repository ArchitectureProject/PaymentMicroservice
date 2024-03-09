package com.efrei.paymentmicroservice.provider.mailSender;

import com.efrei.paymentmicroservice.config.Properties;
import com.efrei.paymentmicroservice.exception.custom.EmailSenderMicroserviceException;
import com.efrei.paymentmicroservice.model.dto.MailSenderRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MailSenderProviderImpl implements MailSenderProvider {

    private final RestTemplate restTemplate;
    private final Properties properties;

    public MailSenderProviderImpl(RestTemplate restTemplate, Properties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }
    @Override
    public void sendEmail(String bearerToken, MailSenderRequest mailSenderRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", bearerToken);

        HttpEntity<MailSenderRequest> requestEntity = new HttpEntity<>(mailSenderRequest, headers);

        try {
            restTemplate.exchange(
                    properties.getEmailSenderMicroserviceBaseUrl() + MailSenderEndpoints.sendEmail,
                    HttpMethod.POST,
                    requestEntity,
                    Void.class);
        } catch (Exception e) {
            throw new EmailSenderMicroserviceException("EmailSender microservice /mail-sender/send sent back an error", e);
        }
    }
}
