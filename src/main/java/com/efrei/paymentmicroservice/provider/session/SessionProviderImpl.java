package com.efrei.paymentmicroservice.provider.session;

import com.efrei.paymentmicroservice.config.Properties;
import com.efrei.paymentmicroservice.exception.custom.SessionMicroserviceException;
import com.efrei.paymentmicroservice.model.dto.PaymentActualisationRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SessionProviderImpl implements SessionProvider {

    private final RestTemplate restTemplate;
    private final Properties properties;

    public SessionProviderImpl(RestTemplate restTemplate, Properties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    @Override
    public void sendPaymentActualisation(String bearerToken,
                                         String sessionId,
                                         PaymentActualisationRequest paymentActualisationRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", bearerToken);

        HttpEntity<PaymentActualisationRequest> requestEntity = new HttpEntity<>(paymentActualisationRequest, headers);

        try {
            restTemplate.exchange(
                    properties.getSessionMicroserviceBaseUrl() + SessionEndpoints.actualisePayment + sessionId,
                    HttpMethod.POST,
                    requestEntity,
                    Void.class);
        } catch (Exception e) {
            throw new SessionMicroserviceException("Session microservice /Session/actualisePayment/ sent back an error", e);
        }

    }
}
