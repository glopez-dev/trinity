package com.trinity.cart.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = new Cart(UUID.randomUUID());
    }

    @Test
    void shouldCreateCartWithCustomerId() {
        assertNotNull(cart.getCustomerId());
        assertEquals(CartStatus.ACTIVE, cart.getStatus());
    }

    @Test
    void shouldAddItemToCart() {
        UUID productId = UUID.randomUUID();
        cart.addItem(productId, "Product 1", BigDecimal.valueOf(10.00), 2);

        assertEquals(1, cart.getItems().size());
        CartItem item = cart.getItems().get(0);
        assertEquals(productId, item.getProductId());
        assertEquals("Product 1", item.getProductName());
        assertEquals(2, item.getQuantity());
        assertEquals(BigDecimal.valueOf(20.00), item.getTotalPrice());
    }

    @Test
    void shouldUpdateItemQuantityIfProductAlreadyExists() {
        UUID productId = UUID.randomUUID();
        cart.addItem(productId, "Product 1", BigDecimal.valueOf(10.00), 2);
        cart.addItem(productId, "Product 1", BigDecimal.valueOf(10.00), 3);

        assertEquals(1, cart.getItems().size());
        CartItem item = cart.getItems().get(0);
        assertEquals(5, item.getQuantity());
        assertEquals(BigDecimal.valueOf(50.00), item.getTotalPrice());
    }

    @Test
    void shouldRemoveItemFromCart() {
        UUID productId = UUID.randomUUID();
        cart.addItem(productId, "Product 1", BigDecimal.valueOf(10.00), 2);

        assertEquals(1, cart.getItems().size());
        cart.removeItem(productId);
        assertEquals(0, cart.getItems().size());
    }

    @Test
    void shouldValidateNonEmptyCart() {
        cart.addItem(UUID.randomUUID(), "Product 1", BigDecimal.valueOf(10.00), 2);

        cart.validate();

        assertEquals(CartStatus.VALIDATED, cart.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenValidatingEmptyCart() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, cart::validate);

        assertEquals("Cannot validate an empty cart.", exception.getMessage());
    }

    @Test
    void shouldCancelCart() {
        cart.cancel();

        assertEquals(CartStatus.CANCELLED, cart.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenAddingItemToNonActiveCart() {
        cart.cancel();
        UUID productId = UUID.randomUUID();

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                cart.addItem(productId, "Product 1", BigDecimal.valueOf(10.00), 2)
        );

        assertEquals("Cannot modify a cart that is not ACTIVE.", exception.getMessage());
    }

    @Test
    void shouldCalculateTotalAmountCorrectly() {
        cart.addItem(UUID.randomUUID(), "Product 1", BigDecimal.valueOf(10.00), 2);
        cart.addItem(UUID.randomUUID(), "Product 2", BigDecimal.valueOf(15.00), 1);

        Money totalAmount = cart.getTotalAmount();
        assertNotNull(totalAmount);
        assertEquals(BigDecimal.valueOf(35.00), totalAmount.getPrice());
    }
}
