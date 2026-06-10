package com.trinity.payment.interfaces.rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

/**
 * A single line of a checkout request: product identity and quantity only —
 * prices are resolved server-side against the product catalogue.
 */
public record CheckoutLineItemRequest(
        @NotNull UUID productId,
        @Positive int quantity
) {
}
