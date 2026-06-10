package com.trinity.cart.application;

import com.trinity.cart.domain.event.CartValidatedEvent;
import com.trinity.cart.domain.model.Cart;
import com.trinity.cart.domain.model.CartItem;
import com.trinity.common.domain.exception.BusinessRuleViolation;
import com.trinity.common.domain.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartServiceTest {

    /** In-memory publisher in the spirit of InMemoryCartRepositoryPort. */
    static class RecordingEventPublisher implements ApplicationEventPublisher {
        final List<Object> published = new ArrayList<>();

        @Override
        public void publishEvent(ApplicationEvent event) {
            published.add(event);
        }

        @Override
        public void publishEvent(Object event) {
            published.add(event);
        }
    }

    private CartService cartService;
    private RecordingEventPublisher eventPublisher;
    private UUID customerId;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        eventPublisher = new RecordingEventPublisher();
        cartService = new CartService(new InMemoryCartRepositoryPort(), eventPublisher);
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
    void testValidateCart_publishesCartValidatedEvent() {
        cartService.createCart(customerId);
        cartService.addItemToCart(customerId, cartItem);

        cartService.validateCart(customerId);

        assertEquals(1, eventPublisher.published.size());
        CartValidatedEvent event = (CartValidatedEvent) eventPublisher.published.get(0);
        assertEquals(customerId, event.customerId());
        assertEquals(1, event.lines().size());
        assertEquals(cartItem.getProductId(), event.lines().get(0).productId());
        assertEquals(0, event.totalAmount().amount().compareTo(new BigDecimal("100.00")));
    }

    @Test
    void testValidateCart_emptyCart_publishesNothing() {
        cartService.createCart(customerId);

        assertThrows(IllegalStateException.class, () -> cartService.validateCart(customerId));

        assertTrue(eventPublisher.published.isEmpty());
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
