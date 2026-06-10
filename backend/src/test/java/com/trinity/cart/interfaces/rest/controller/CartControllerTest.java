package com.trinity.cart.interfaces.rest.controller;

import com.trinity.cart.domain.model.Cart;
import com.trinity.cart.domain.model.CartItem;
import com.trinity.cart.interfaces.rest.dto.CartItemRequest;
import com.trinity.cart.interfaces.rest.dto.CartResponse;
import com.trinity.cart.interfaces.rest.mapper.CartApiMapper;
import com.trinity.cart.application.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CartControllerTest {

    @Mock
    private CartService cartService;

    @Spy
    private CartApiMapper cartApiMapper = Mappers.getMapper(CartApiMapper.class);

    @InjectMocks
    private CartController cartController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    void testCreateCart() {
        UUID customerId = UUID.randomUUID();
        doNothing().when(cartService).createCart(customerId);

        ResponseEntity<Void> response = cartController.createCart(customerId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(cartService, times(1)).createCart(customerId);
    }

    @Test
    void testGetCart() {
        UUID customerId = UUID.randomUUID();
        Cart cart = Cart.builder().customerId(customerId).build();
        when(cartService.getCart(customerId)).thenReturn(cart);

        ResponseEntity<CartResponse> response = cartController.getCart(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customerId, response.getBody().customerId());
        verify(cartService, times(1)).getCart(customerId);
    }

    @Test
    void testAddItemToCart() {
        UUID customerId = UUID.randomUUID();
        CartItemRequest cartItemRequest = CartItemRequest.builder()
                .productId(UUID.randomUUID())
                .productName("Apples")
                .quantity(2)
                .unitPrice(new BigDecimal("3.00"))
                .build();

        ResponseEntity<Void> response = cartController.addItemToCart(customerId, cartItemRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(cartService, times(1)).addItemToCart(eq(customerId), any(CartItem.class));
    }

    @Test
    void testRemoveItemFromCart() {
        UUID customerId = UUID.randomUUID();
        CartItemRequest cartItemRequest = CartItemRequest.builder()
                .productId(UUID.randomUUID())
                .productName("Apples")
                .quantity(2)
                .unitPrice(new BigDecimal("3.00"))
                .build();

        ResponseEntity<Void> response = cartController.removeItemFromCart(customerId, cartItemRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cartService, times(1)).removeItemFromCart(eq(customerId), any(CartItem.class));
    }

    @Test
    void testValidateCart() {
        UUID customerId = UUID.randomUUID();
        doNothing().when(cartService).validateCart(customerId);

        ResponseEntity<Void> response = cartController.validateCart(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cartService, times(1)).validateCart(customerId);
    }

    @Test
    void testRemoveCart() {
        UUID customerId = UUID.randomUUID();
        doNothing().when(cartService).removeCart(customerId);

        ResponseEntity<Void> response = cartController.removeCart(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cartService, times(1)).removeCart(customerId);
    }
}