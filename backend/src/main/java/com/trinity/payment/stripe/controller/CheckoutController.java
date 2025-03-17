// package com.trinity.payment.stripe.controller;

// import com.stripe.Stripe;
// import com.stripe.exception.StripeException;
// import com.stripe.model.checkout.Session;
// import com.stripe.param.checkout.SessionCreateParams;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.web.bind.annotation.*;

// import java.util.HashMap;
// import java.util.Map;

// @RestController
// @RequestMapping("/api/v1/stripe")
// @CrossOrigin(origins = "http://localhost:3000") // Allow frontend requests
// public class CheckoutController {

//     @Value("${stripe.api.key}")
//     private String stripeApiKey;

//     @PostMapping("/create-checkout-session")
//     public Map<String, String> createCheckoutSession(@RequestBody Map<String, Object> request) {
//         Stripe.apiKey = stripeApiKey;

//         try {
//             SessionCreateParams params = SessionCreateParams.builder()
//                     .setMode(SessionCreateParams.Mode.PAYMENT)
//                     .setSuccessUrl("http://localhost:3000/success") // Frontend handles success
//                     .setCancelUrl("http://localhost:3000/cancel") // Frontend handles cancellation
//                     .addLineItem(
//                             SessionCreateParams.LineItem.builder()
//                                     .setQuantity((long) 1)
//                                     .setPriceData(
//                                             SessionCreateParams.LineItem.PriceData.builder()
//                                                     .setCurrency("eur")
//                                                     .setUnitAmount(5000L) // 50.00 EUR (in cents)
//                                                     .setProductData(
//                                                             SessionCreateParams.LineItem.PriceData.ProductData.builder()
//                                                                     .setName("Premium Subscription")
//                                                                     .build()
//                                                     )
//                                                     .build()
//                                     )
//                                     .build()
//                     )
//                     .build();

//             Session session = Session.create(params);

//             // Return session URL to the frontend
//             Map<String, String> response = new HashMap<>();
//             response.put("checkoutUrl", session.getUrl());
//             return response;
//         } catch (StripeException e) {
//             throw new RuntimeException("Error creating checkout session", e);
//         }
//     }
// }
