package com.trinity.cart.interfaces.rest.dto;

import java.math.BigDecimal;
import java.util.UUID;

@io.swagger.v3.oas.annotations.media.Schema(description = "A cart line with the server-resolved name and prices")
public record CartItemResponse(
        UUID productId,
        String productName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice
) {
}
