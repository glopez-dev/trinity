// package com.trinity.payment.stripe.service;

// import com.google.gson.JsonObject;
// import com.stripe.exception.StripeException;
// import com.stripe.model.PaymentIntent;
// import com.stripe.model.PaymentMethod;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.util.Map;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.when;

// @ExtendWith(MockitoExtension.class)
// class StripePaymentServiceTest {

//     @InjectMocks
//     private StripePaymentService stripePaymentService;

//     @Mock
//     private PaymentIntent mockPaymentIntent;

//     @Mock
//     private PaymentMethod mockPaymentMethod;

//     @BeforeEach
//     void setUp() {
//         com.stripe.Stripe.apiKey = System.getenv("STRIPE_SECRET_KEY");
//     }

//     @Test
//     void createPayment_Success() throws StripeException {
//         Long amount = 1000L;
//         String currency = "USD";
//         String paymentMethodId = "pm_valid_123";

//         when(mockPaymentIntent.getId()).thenReturn("pi_valid_123");
//         when(mockPaymentIntent.getAmount()).thenReturn(amount);
//         when(mockPaymentIntent.getCurrency()).thenReturn(currency);
//         when(PaymentIntent.create(any(Map.class))).thenReturn(mockPaymentIntent);

//         PaymentIntent paymentIntent = stripePaymentService.createPayment(amount, currency, paymentMethodId);

//         assertNotNull(paymentIntent);
//         assertEquals(amount, paymentIntent.getAmount());
//         assertEquals(currency, paymentIntent.getCurrency());
//     }

//     @Test
//     void confirmPaymentIntent_Success() throws Exception {
//         String paymentIntentId = "pi_valid_123";

//         when(mockPaymentIntent.getId()).thenReturn(paymentIntentId);
//         when(mockPaymentIntent.getStatus()).thenReturn("succeeded");
//         when(PaymentIntent.retrieve(paymentIntentId)).thenReturn(mockPaymentIntent);
//         when(mockPaymentIntent.confirm()).thenReturn(mockPaymentIntent);

//         PaymentIntent confirmedPaymentIntent = stripePaymentService.confirmPaymentIntent(paymentIntentId);

//         assertNotNull(confirmedPaymentIntent);
//         assertEquals("succeeded", confirmedPaymentIntent.getStatus());
//     }

//     @Test
//     void convertJsonObject_Success() {
//         JsonObject jsonObject = new JsonObject();
//         jsonObject.addProperty("key1", "value1");
//         jsonObject.addProperty("key2", 123);

//         Map<String, Object> result = StripePaymentService.convertJsonObject(jsonObject);

//         assertNotNull(result);
//         assertEquals("value1", result.get("key1"));
//         assertEquals(123.0, ((Number) result.get("key2")).doubleValue());
//     }

//     @Test
//     void convertJsonObject_ThrowsException() {
//         JsonObject invalidJson = null;
//         assertThrows(NullPointerException.class, () -> StripePaymentService.convertJsonObject(invalidJson));
//     }
// }
