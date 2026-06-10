package com.trinity.cart.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Cart line: product identity and quantity only — name and price are resolved server-side")
public class CartItemRequest {
    @Schema(description = "Identifier of the product in the catalogue", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Product ID is required")
    private UUID productId;

    @Schema(description = "Quantity to add or remove", example = "2")
    @Positive(message = "Quantity must be greater than zero")
    private int quantity;
}
