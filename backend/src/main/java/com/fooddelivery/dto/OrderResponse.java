package com.fooddelivery.dto;

import com.fooddelivery.model.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        OrderStatus status,
        BigDecimal totalAmount,
        String deliveryAddress,
        Instant placedAt,
        String restaurantName,
        List<OrderItemResponse> items
) {}
