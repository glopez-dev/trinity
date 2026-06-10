package com.trinity.payment.application.command;

import java.util.UUID;

/** A checkout line as the client may express it: product identity and quantity only. */
public record CheckoutLine(UUID productId, int quantity) {
}
