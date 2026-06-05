package com.trinity.payment.dto;

/** Clean API representation of a hosted checkout session. */
public record CheckoutResponse(
        String status,
        String sessionId,
        String redirectUrl
) {
}
