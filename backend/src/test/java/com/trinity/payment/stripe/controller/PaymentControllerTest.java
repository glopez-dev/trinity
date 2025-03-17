package com.trinity.payment.stripe.controller;

import com.google.gson.JsonObject;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.trinity.payment.stripe.dto.PaymentRequest;
import com.trinity.payment.stripe.service.StripePaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private StripePaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @Test
    void createPayment_Success() throws StripeException {
        // Arrange
        PaymentRequest request = new PaymentRequest();
        request.setAmount(1000L);
        request.setCurrency("USD");
        request.setPaymentMethodId("pm_123");

        PaymentIntent mockPaymentIntent = org.mockito.Mockito.mock(PaymentIntent.class); // <-- Mock PaymentIntent
        
        JsonObject rawJson = new JsonObject();
        rawJson.addProperty("id", "pi_123");
        rawJson.addProperty("status", "succeeded");

        when(paymentService.createPayment(eq(1000L), eq("USD"), eq("pm_123")))
                .thenReturn(mockPaymentIntent);
        
        when(mockPaymentIntent.getRawJsonObject()).thenReturn(rawJson); // <-- Now this works

        // Act
        ResponseEntity<Map<String, Object>> response = paymentController.createPayment(request);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        Map<String, Object> responseBody = response.getBody();
        assert responseBody != null : "Response body should not be null";
        assertEquals("pi_123", responseBody.get("id"));
        assertEquals("succeeded", responseBody.get("status"));
    }


    @Test
    void createPayment_Error() throws StripeException {
        // Arrange
        PaymentRequest request = new PaymentRequest();
        request.setAmount(1000L);
        request.setCurrency("USD");
        request.setPaymentMethodId("pm_123");

        when(paymentService.createPayment(anyLong(), anyString(), anyString()))
                .thenThrow(new com.stripe.exception.InvalidRequestException("Payment failed", null, null, null, null, null));

        // Act
        ResponseEntity<Map<String, Object>> response = paymentController.createPayment(request);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Payment failed", response.getBody().get("error"));
    }
}