package com.trinity.cart.service;

import com.trinity.cart.dto.CartItemRequest;
import com.trinity.cart.dto.CartRequest;
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
    private CartItemRequest cartItemRequest;

    @BeforeEach
    void setUp() {
        cartService = new CartService(new InMemoryCartRepositoryPort());
        customerId = UUID.randomUUID();
        cartItemRequest = CartItemRequest.builder()
                .productId(UUID.randomUUID())
                .productName("Test Product")
                .quantity(1)
                .unitPrice(BigDecimal.valueOf(100))
                .build();
    }

    @Test
    void testCreateCart() {
        cartService.createCart(customerId);
        CartRequest cartRequest = cartService.getCart(customerId);
        assertNotNull(cartRequest);
        assertEquals(customerId, cartRequest.getCustomerId());
    }

    @Test
    void testAddItemToCart() {
        cartService.createCart(customerId);
        cartService.addItemToCart(customerId, cartItemRequest);
        CartRequest cartRequest = cartService.getCart(customerId);
        assertEquals(1, cartRequest.getItems().size());
    }

    @Test
    void testRemoveItemFromCart() {
        cartService.createCart(customerId);
        cartService.addItemToCart(customerId, cartItemRequest);
        cartService.removeItemFromCart(customerId, cartItemRequest);
        CartRequest cartRequest = cartService.getCart(customerId);
        assertEquals(0, cartRequest.getItems().size());
    }

    @Test
    void testValidateCart() {
        cartService.createCart(customerId);
        cartService.addItemToCart(customerId, cartItemRequest);
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
        CartRequest cartRequest = cartService.getCart(customerId);
        assertTrue(cartRequest.getItems().isEmpty());
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
