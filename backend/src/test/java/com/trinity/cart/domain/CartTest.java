package com.trinity.cart.domain;

import com.trinity.cart.constant.CartStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;


class CartTest {

    private Cart cart;
    private UUID productId;
    private String productName;
    private BigDecimal unitPrice;
    private int quantity;
    private CartItem item;

    @BeforeEach
    public void setUp() {
        cart = new Cart(UUID.randomUUID());
        productId = UUID.randomUUID();
        productName = "Test Product";
        unitPrice = new BigDecimal("10.00");
        quantity = 2;
        item = new CartItem(productId, productName, unitPrice, quantity);
    }

    @Test
    void testAddItem() {
        cart.addItem(item);
        assertEquals(1, cart.getItems().size());
        assertEquals(CartStatus.EDITED, cart.getStatus());
        assertEquals(new BigDecimal("20.00"), cart.getTotalAmount().getAmount());
    }


    @Test
    void testAddSameItem() {
        cart.addItem(item);
        cart.addItem(item);
        assertEquals(1, cart.getItems().size());
        assertEquals(4, cart.getItems().get(0).getQuantity());
        assertEquals(CartStatus.EDITED, cart.getStatus());
        assertEquals(new BigDecimal("40.00"), cart.getTotalAmount().getAmount());
    }

    @Test
    void testRemoveItem() {
        cart.addItem(item);
        cart.removeItem(productId, quantity);
        assertEquals(0, cart.getItems().size());
        assertEquals(CartStatus.EDITED, cart.getStatus());
        assertEquals(BigDecimal.ZERO, cart.getTotalAmount().getAmount());
    }

    @Test
    void testValidate() {
        cart.addItem(item);
        cart.validate();
        assertEquals(CartStatus.VALIDATED, cart.getStatus());
    }

    @Test
    void testValidateEmptyCart() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            cart.validate();
        });
        assertEquals("Cannot validate an empty cart.", exception.getMessage());
    }

    @Test
    void testCancel() {
        cart.addItem(item);
        cart.cancel();
        assertEquals(0, cart.getItems().size());
        assertEquals(CartStatus.CANCELLED, cart.getStatus());
        assertEquals(BigDecimal.ZERO, cart.getTotalAmount().getAmount());
    }
}