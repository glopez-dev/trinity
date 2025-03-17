package com.trinity.payment.stripe.controller;

import com.trinity.payment.stripe.dto.ProductRequest;
import com.trinity.payment.stripe.dto.StripeResponse;
import com.trinity.payment.stripe.service.StripeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;



class ProductCheckoutControllerTest {

    @Mock
    private StripeService stripeService;

    private ProductCheckoutController productCheckoutController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productCheckoutController = new ProductCheckoutController(stripeService);
    }

    @Test
    void checkoutProducts_ShouldReturnStripeResponse() {
        // Arrange
        List<ProductRequest> productRequests = new ArrayList<>();
        StripeResponse expectedResponse = new StripeResponse("session_id", null, null, null);
        when(stripeService.checkoutProducts(any())).thenReturn(expectedResponse);

        // Act
        ResponseEntity<StripeResponse> response = productCheckoutController.checkoutProducts(productRequests);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }
}