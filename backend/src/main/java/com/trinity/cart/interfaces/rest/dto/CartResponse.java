package com.trinity.cart.interfaces.rest.dto;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record CartResponse(
        UUID customerId,
        Set<CartItemResponse> items,
        BigDecimal totalAmount,
        String currency
) {
}
