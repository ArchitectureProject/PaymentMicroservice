package com.efrei.paymentmicroservice.provider.user;

import com.efrei.paymentmicroservice.model.dto.User;

public interface UserProvider {

    User getUserById(String bearerToken, String userId);
}
