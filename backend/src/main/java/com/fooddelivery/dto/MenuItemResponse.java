package com.fooddelivery.dto;

import java.math.BigDecimal;

public record MenuItemResponse(
        Long id,
        Long restaurantId,
        String name,
        String description,
        BigDecimal price,
        boolean vegetarian,
        String imageUrl
) {}
