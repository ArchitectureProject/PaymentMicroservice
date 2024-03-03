package com.efrei.paymentmicroservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "conf")
public class Properties {

    private String jwkUrl;

    private String emailSenderMicroserviceBaseUrl;

    private String userMicroserviceBaseUrl;

    public String getJwkUrl() {
        return jwkUrl;
    }

    public void setJwkUrl(String jwkUrl) {
        this.jwkUrl = jwkUrl;
    }

    public String getEmailSenderMicroserviceBaseUrl() {
        return emailSenderMicroserviceBaseUrl;
    }

    public void setEmailSenderMicroserviceBaseUrl(String emailSenderMicroserviceBaseUrl) {
        this.emailSenderMicroserviceBaseUrl = emailSenderMicroserviceBaseUrl;
    }

    public String getUserMicroserviceBaseUrl() {
        return userMicroserviceBaseUrl;
    }

    public void setUserMicroserviceBaseUrl(String userMicroserviceBaseUrl) {
        this.userMicroserviceBaseUrl = userMicroserviceBaseUrl;
    }
}
