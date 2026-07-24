package com.trinity.cart.interfaces.rest.dto;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@io.swagger.v3.oas.annotations.media.Schema(description = "A customer's cart with its lines and total")
public record CartResponse(
        UUID customerId,
        Set<CartItemResponse> items,
        BigDecimal totalAmount,
        String currency
) {
}
