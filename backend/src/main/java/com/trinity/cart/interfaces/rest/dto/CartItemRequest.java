package com.trinity.cart.interfaces.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * A cart line as the client may express it: product identity and quantity only.
 * Name and price are resolved server-side against the product module.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemRequest {
    @NotNull(message = "Product ID is required")
    private UUID productId;

    @Positive(message = "Quantity must be greater than zero")
    private int quantity;
}
