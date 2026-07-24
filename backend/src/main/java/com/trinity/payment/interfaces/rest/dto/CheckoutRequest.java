package com.trinity.payment.interfaces.rest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/** Request body to create a hosted checkout session from line items. */
public record CheckoutRequest(
        @NotEmpty @Valid List<CheckoutLineItemRequest> items
) {
}
