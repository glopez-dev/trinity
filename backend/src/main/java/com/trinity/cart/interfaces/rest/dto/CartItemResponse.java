package com.trinity.cart.interfaces.rest.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemResponse(
        UUID productId,
        String productName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice
) {
}
