package com.fooddelivery.dto;

import com.fooddelivery.model.Role;

public record AuthResponse(
        String token,
        Long userId,
        String email,
        String fullName,
        Role role
) {}
