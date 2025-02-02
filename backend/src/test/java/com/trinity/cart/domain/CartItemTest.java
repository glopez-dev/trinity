package com.trinity.cart.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;


class CartItemTest {

    private CartItem cartItem;
    private UUID productId;
    private String productName;
    private BigDecimal unitPrice;
    private int quantity;

    @BeforeEach
    public void setUp() {
        productId = UUID.randomUUID();
        productName = "Test Product";
        unitPrice = new BigDecimal("10.00");
        quantity = 2;
        cartItem = new CartItem(productId, productName, unitPrice, quantity);
    }

    @Test
    void testCartItemCreation() {
        assertEquals(productId, cartItem.getProductId());
        assertEquals(productName, cartItem.getProductName());
        assertEquals(unitPrice, cartItem.getUnitPrice());
        assertEquals(quantity, cartItem.getQuantity());
        assertEquals(unitPrice.multiply(BigDecimal.valueOf(quantity)), cartItem.getTotalPrice());
    }

    @Test
    void testUpdateQuantity() {
        int newQuantity = 5;
        cartItem.updateQuantity(newQuantity);
        assertEquals(newQuantity, cartItem.getQuantity());
        assertEquals(unitPrice.multiply(BigDecimal.valueOf(newQuantity)), cartItem.getTotalPrice());
    }

    @Test
    void testUpdateQuantityThrowsExceptionForInvalidQuantity() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            cartItem.updateQuantity(0);
        });
        assertEquals("Quantity cannot be less than or equal to zero.", exception.getMessage());
    }

    @Test
    void testCalculateTotalPrice() {
        cartItem.updateQuantity(3);
        BigDecimal expectedTotalPrice = unitPrice.multiply(BigDecimal.valueOf(3));
        assertEquals(expectedTotalPrice, cartItem.getTotalPrice());
    }
}