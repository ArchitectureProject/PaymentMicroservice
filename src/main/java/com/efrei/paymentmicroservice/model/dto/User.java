package com.efrei.paymentmicroservice.model.dto;

import com.efrei.paymentmicroservice.model.UserRole;

public record User(String id, UserRole userRole, String email, String passwordHash) {
}
