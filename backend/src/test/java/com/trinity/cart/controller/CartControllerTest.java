package com.trinity.cart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trinity.cart.domain.Cart;
import com.trinity.cart.domain.CartItem;
import com.trinity.cart.domain.Money;
import com.trinity.cart.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.beans.factory.annotation.Autowired;



class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private MockMvc mockMvc;

    private UUID customerId;
    private Cart cart;

    // @Autowired
    // private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
        customerId = UUID.randomUUID();
        cart = new Cart(customerId);
    }

    @Test
    void createCart() throws Exception {

        when(cartService.createCart(customerId)).thenReturn(cart);

        mockMvc.perform(post("/api/carts/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").exists())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())); // Print response to check

        verify(cartService).createCart(customerId);
    }

    @Test
    void getCart() throws Exception {
        when(cartService.getCart(customerId)).thenReturn(cart);

        mockMvc.perform(get("/api/carts/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").exists())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())); // Print response to check
        verify(cartService).getCart(customerId);
    }


    // @Test
    // void addItemToCart_ShouldReturn201_WhenSuccessful() throws Exception {
    //     // Given
    //     CartItem cartItem = new CartItem(
    //             UUID.randomUUID(),
    //             "Test Product",
    //             BigDecimal.valueOf(10.99),
    //             2
    //     );

    //     // Simuler le comportement du service pour éviter une exception
    //     doNothing().when(cartService).addItemToCart(Mockito.eq(customerId), Mockito.any(CartItem.class));

    //     // When & Then
    //     mockMvc.perform(post("/customers/{customerId}/items", customerId)
    //                     .contentType(MediaType.APPLICATION_JSON)
    //                     .content(objectMapper.writeValueAsString(cartItem)))
    //             .andExpect(status().isCreated()) // Vérifie si le statut est 201
    //             .andDo(result -> System.out.println("aaaaaaaaaaaaaaaaa" + result.getResponse().getContentAsString())); // Print response to check

    //             verify(cartService).getCart(customerId);
    // }


    @Test
    void addItemToCart() throws Exception {
        UUID productId = UUID.randomUUID();
        String productName = "Test Product";
        BigDecimal unitPrice = BigDecimal.valueOf(10.99);
        int quantity = 1;

        mockMvc.perform(post("/api/carts/{customerId}/items", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productId\":\"" + productId + "\",\"productName\":\"" + productName + "\",\"unitPrice\":" + unitPrice + ",\"quantity\":" + quantity + "}"))
                .andExpect(status().isCreated());

        verify(cartService).addItemToCart(eq(customerId), any(CartItem.class));
    }

    @Test
    void removeItemFromCart() throws Exception {
        UUID productId = UUID.randomUUID();
        int quantity = 1;

        mockMvc.perform(delete("/api/carts/{customerId}/items/{productId}", customerId, productId)
                .param("quantity", String.valueOf(quantity)))
                .andExpect(status().isOk());

        verify(cartService).removeItemFromCart(customerId, productId, quantity);
    }

    @Test
    void validateCart() throws Exception {
        mockMvc.perform(post("/api/carts/{customerId}/validate", customerId))
                .andExpect(status().isOk());

        verify(cartService).validateCart(customerId);
    }

    @Test
    void removeCart() throws Exception {
        mockMvc.perform(delete("/api/carts/{customerId}", customerId))
                .andExpect(status().isOk());

        verify(cartService).removeCart(customerId);
    }

    @Test
    void getTotalAmount() throws Exception {
        //return money
        when(cartService.getTotalAmount(customerId)).thenReturn(new Money(BigDecimal.TEN, "USD"));

        mockMvc.perform(get("/api/carts/{customerId}/total", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").exists())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())); // Print response to check

        verify(cartService).getTotalAmount(customerId);
    }
}