package com.trinity.cart.controller;

import com.trinity.cart.dto.CartItemRequest;
import com.trinity.cart.dto.CartRequest;
import com.trinity.cart.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
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
        CartRequest cartRequest = new CartRequest();
        when(cartService.getCart(customerId)).thenReturn(cartRequest);

        ResponseEntity<CartRequest> response = cartController.getCart(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cartRequest, response.getBody());
        verify(cartService, times(1)).getCart(customerId);
    }

    @Test
    void testAddItemToCart() {
        UUID customerId = UUID.randomUUID();
        CartItemRequest cartItemRequest = new CartItemRequest();
        doNothing().when(cartService).addItemToCart(customerId, cartItemRequest);

        ResponseEntity<Void> response = cartController.addItemToCart(customerId, cartItemRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(cartService, times(1)).addItemToCart(customerId, cartItemRequest);
    }

    @Test
    void testRemoveItemFromCart() {
        UUID customerId = UUID.randomUUID();
        CartItemRequest cartItemRequest = new CartItemRequest();
        doNothing().when(cartService).removeItemFromCart(customerId, cartItemRequest);

        ResponseEntity<Void> response = cartController.removeItemFromCart(customerId, cartItemRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cartService, times(1)).removeItemFromCart(customerId, cartItemRequest);
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