package com.trinity.payment.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

/**
 * A single line of a checkout request: product identity and quantity only —
 * prices are resolved server-side against the product catalogue.
 */
public record CheckoutLineItemRequest(
        @Schema(description = "Identifier of the product in the catalogue", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull UUID productId,
        @Schema(description = "Quantity to charge", example = "2")
        @Positive int quantity
) {
}
