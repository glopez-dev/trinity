package com.trinity.cart.domain.event;

import com.trinity.cart.domain.model.Cart;
import com.trinity.common.domain.vo.Money;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Domain event raised when a customer validates their cart. Self-contained
 * snapshot (ids + denormalized lines): consumers never need to reach back into
 * the cart module, and the payload stays serializable if it is ever
 * externalized to a broker.
 */
public record CartValidatedEvent(
        UUID customerId,
        List<Line> lines,
        Money totalAmount,
        Instant occurredAt
) {

    public record Line(UUID productId, String productName, int quantity, BigDecimal unitPrice) {
    }

    public static CartValidatedEvent from(Cart cart, Instant occurredAt) {
        List<Line> lines = cart.getItems().stream()
                .map(item -> new Line(
                        item.getProductId(),
                        item.getProductName(),
                        item.getQuantity(),
                        item.getUnitPrice()))
                .toList();
        return new CartValidatedEvent(cart.getCustomerId(), List.copyOf(lines), cart.getTotalAmount(), occurredAt);
    }
}
