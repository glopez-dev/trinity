package com.trinity.payment.stripe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class StripePaymentService {

    // @JsonIgnoreProperties({"lastResponse"})
    public PaymentIntent createPayment(Long amount, String currency, String paymentMethodId) throws StripeException {

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount) // Amount in cents
                .setCurrency(currency)
                .setPaymentMethod(paymentMethodId)
                .setConfirm(true)
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true) // Enables automatic payment methods
                                .setAllowRedirects(
                                        PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER) // No
                                                                                                                // redirects
                                .build())
                .build();
        return PaymentIntent.create(params);
    }

    public PaymentIntent confirmPaymentIntent(String paymentIntentId) throws Exception {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        return paymentIntent.confirm();
    }

    // public Map<String, Object> convertJsonObject(JsonObject jsonObject) {
    //     Map<String, Object> result = new HashMap<>();
    //     jsonObject.keySet().forEach(key -> result.put(key, jsonObject.get(key)));
    //     return result;
    // }

    public static Map<String, Object> convertJsonObject(JsonObject jsonObject) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(jsonObject);
        ObjectMapper objectMapper = new ObjectMapper();
        
        try {
            return objectMapper.readValue(jsonString, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JsonObject to Map", e);
        }
    }    
}
