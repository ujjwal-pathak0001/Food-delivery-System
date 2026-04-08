package com.fooddelivery.dto;

import java.math.BigDecimal;

public record CartItemResponse(
        Long id,
        Long menuItemId,
        String name,
        BigDecimal unitPrice,
        int quantity,
        boolean vegetarian,
        Long restaurantId,
        String restaurantName
) {}
