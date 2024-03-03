package com.efrei.paymentmicroservice.provider.user;

import com.efrei.paymentmicroservice.config.Properties;
import com.efrei.paymentmicroservice.exception.custom.UserMicroserviceException;
import com.efrei.paymentmicroservice.model.dto.User;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserProviderImpl implements UserProvider{

    private final RestTemplate restTemplate;
    private final Properties properties;

    public UserProviderImpl(RestTemplate restTemplate, Properties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    @Override
    public User getUserById(String bearerToken, String userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", bearerToken);

        HttpEntity<List<String>> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<User> responseEntity = restTemplate.exchange(
                    properties.getUserMicroserviceBaseUrl() + UserEndpoints.getUser + userId,
                    HttpMethod.GET,
                    requestEntity,
                    User.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            throw new UserMicroserviceException("User microservice /user/{id} sent back an error", e);
        }
    }
}
