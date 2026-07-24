package com.trinity.cart.domain.event;

import com.trinity.cart.domain.model.Cart;
import com.trinity.cart.domain.model.CartItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class CartValidatedEventTest {

    @Test
    void from_capturesCustomerLinesAndTotal() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Instant now = Instant.now();
        Cart cart = Cart.builder().customerId(customerId).build();
        cart.addItem(CartItem.builder()
                .productId(productId)
                .productName("Apples")
                .quantity(3)
                .unitPrice(new BigDecimal("2.50"))
                .build());

        CartValidatedEvent event = CartValidatedEvent.from(cart, now);

        assertThat(event.customerId()).isEqualTo(customerId);
        assertThat(event.occurredAt()).isEqualTo(now);
        assertThat(event.totalAmount().amount()).isEqualByComparingTo("7.50");
        assertThat(event.lines()).hasSize(1);
        CartValidatedEvent.Line line = event.lines().get(0);
        assertThat(line.productId()).isEqualTo(productId);
        assertThat(line.productName()).isEqualTo("Apples");
        assertThat(line.quantity()).isEqualTo(3);
        assertThat(line.unitPrice()).isEqualByComparingTo("2.50");
    }

    @Test
    void lines_areImmutable() {
        Cart cart = Cart.builder().customerId(UUID.randomUUID()).build();
        cart.addItem(CartItem.builder()
                .productId(UUID.randomUUID())
                .productName("Apples")
                .quantity(1)
                .unitPrice(new BigDecimal("1.00"))
                .build());
        CartValidatedEvent event = CartValidatedEvent.from(cart, Instant.now());

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> event.lines().clear());
    }
}
