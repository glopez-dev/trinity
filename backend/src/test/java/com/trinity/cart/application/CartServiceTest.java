package com.trinity.cart.application;

import com.trinity.cart.domain.model.Cart;
import com.trinity.cart.domain.model.CartItem;
import com.trinity.common.domain.exception.BusinessRuleViolation;
import com.trinity.common.domain.exception.NotFoundException;
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
        cartService = new CartService(new InMemoryCartRepositoryPort());
        customerId = UUID.randomUUID();
        cartItem = CartItem.builder()
                .productId(UUID.randomUUID())
                .productName("Test Product")
                .quantity(1)
                .unitPrice(BigDecimal.valueOf(100))
                .build();
    }

    @Test
    void testCreateCart() {
        cartService.createCart(customerId);
        Cart cart = cartService.getCart(customerId);
        assertNotNull(cart);
        assertEquals(customerId, cart.getCustomerId());
    }

    @Test
    void testAddItemToCart() {
        cartService.createCart(customerId);
        cartService.addItemToCart(customerId, cartItem);
        Cart cart = cartService.getCart(customerId);
        assertEquals(1, cart.getItems().size());
    }

    @Test
    void testRemoveItemFromCart() {
        cartService.createCart(customerId);
        cartService.addItemToCart(customerId, cartItem);
        cartService.removeItemFromCart(customerId, cartItem);
        Cart cart = cartService.getCart(customerId);
        assertEquals(0, cart.getItems().size());
    }

    @Test
    void testValidateCart() {
        cartService.createCart(customerId);
        cartService.addItemToCart(customerId, cartItem);
        cartService.validateCart(customerId);
        assertThrows(NotFoundException.class, () -> cartService.getCart(customerId));
    }

    @Test
    void testRemoveCart() {
        cartService.createCart(customerId);
        cartService.removeCart(customerId);
        assertThrows(NotFoundException.class, () -> cartService.getCart(customerId));
    }

    @Test
    void testCancelCart() {
        cartService.createCart(customerId);
        cartService.cancelCart(customerId);
        Cart cart = cartService.getCart(customerId);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void testGetCart_notFound_throwsNotFound() {
        assertThrows(NotFoundException.class, () -> cartService.getCart(customerId));
    }

    @Test
    void testCreateCart_duplicateCustomer_throwsBusinessRuleViolation() {
        cartService.createCart(customerId);
        assertThrows(BusinessRuleViolation.class, () -> cartService.createCart(customerId));
    }
}
