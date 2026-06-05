package com.trinity.payment.domain;

/**
 * Provider-agnostic outcome of a gateway call. This is the domain seam that
 * keeps SDK types (Stripe Session/PaymentIntent, PayPal objects) out of the
 * service and controller layers.
 */
public record PaymentResult(
        String externalRef,
        PaymentStatus status,
        String redirectUrl,
        String failureReason
) {
}
