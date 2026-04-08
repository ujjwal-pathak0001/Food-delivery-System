package com.fooddelivery.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        String itemName,
        int quantity,
        BigDecimal unitPrice
) {}
