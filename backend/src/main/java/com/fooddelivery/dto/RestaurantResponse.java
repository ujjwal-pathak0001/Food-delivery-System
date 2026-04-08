package com.fooddelivery.dto;

public record RestaurantResponse(
        Long id,
        String name,
        String cuisine,
        Double rating,
        Integer deliveryTimeMinutes,
        String imageUrl,
        String description,
        String area
) {}
