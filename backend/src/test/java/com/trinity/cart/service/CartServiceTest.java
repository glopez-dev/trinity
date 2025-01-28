package com.trinity.cart.service;

import com.trinity.cart.domain.Cart;
import com.trinity.cart.domain.CartItem;
import com.trinity.cart.domain.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;



class CartServiceTest {

    private CartService cartService;
    private UUID customerId;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        cartService = new CartService();
        customerId = UUID.randomUUID();
        cartItem = new CartItem(UUID.randomUUID(), "Test Product", BigDecimal.TEN, 1);
    }

    @Test
    void testCreateCart() {
        Cart cart = cartService.createCart(customerId);
        assertNotNull(cart);
        assertEquals(customerId, cart.getCustomerId());
    }

    @Test
    void testGetCart() {
        Cart cart = cartService.createCart(customerId);
        Cart retrievedCart = cartService.getCart(customerId);
        assertEquals(cart, retrievedCart);
    }

    @Test
    void testAddItemToCart() {
        cartService.addItemToCart(customerId, cartItem);
        Cart cart = cartService.getCart(customerId);
        assertTrue(cart.getItems().contains(cartItem));
    }

    @Test
    void testRemoveItemFromCart() {
        cartService.addItemToCart(customerId, cartItem);
        cartService.removeItemFromCart(customerId, cartItem.getProductId(), 1);
        Cart cart = cartService.getCart(customerId);
        assertFalse(cart.getItems().contains(cartItem));
    }

    @Test
    void testValidateCart() {
        cartService.createCart(customerId);
        assertThrows(IllegalStateException.class, () -> cartService.validateCart(customerId));
        cartService.addItemToCart(customerId, cartItem);
        cartService.validateCart(customerId);
    }

    @Test
    void testRemoveCart() {
        cartService.createCart(customerId);
        cartService.removeCart(customerId);
        Cart cart = cartService.getCart(customerId);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void testGetTotalAmount() {
        cartService.addItemToCart(customerId, cartItem);
        Money totalAmount = cartService.getTotalAmount(customerId);
        assertEquals(new Money(BigDecimal.TEN, "USD"), totalAmount);
    }
}