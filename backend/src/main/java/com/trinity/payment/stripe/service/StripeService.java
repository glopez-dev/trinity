package com.trinity.payment.stripe.service;

import com.trinity.payment.stripe.dto.ProductRequest;
import com.trinity.payment.stripe.dto.StripeResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
public class StripeService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(StripeService.class);
    private static final String DEFAULT_CURRENCY = "USD";
    private static final String SUCCESS_URL = "http://localhost:8080/success";
    private static final String CANCEL_URL = "http://localhost:8080/cancel";

    public StripeResponse checkoutProducts(List<ProductRequest> productRequests) {
        try {
            Session session = createCheckoutSession(productRequests);
            return new StripeResponse("SUCCESS", "Payment session created", session.getId(), session.getUrl());
        } catch (StripeException e) {
            LOGGER.error("Error creating Stripe session", e);
            return new StripeResponse("FAILED", "Error creating payment session", null, null);
        }
    }

    private Session createCheckoutSession(List<ProductRequest> productRequests) throws StripeException {
        List<SessionCreateParams.LineItem> lineItems = productRequests.stream()
                .map(this::buildLineItem)
                .toList();
        
        return Session.create(SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(SUCCESS_URL)
                .setCancelUrl(CANCEL_URL)
                .addAllLineItem(lineItems)
                .build());
    }

    private SessionCreateParams.LineItem buildLineItem(ProductRequest productRequest) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(productRequest.getQuantity())
                .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(productRequest.getCurrency() != null ? productRequest.getCurrency() : DEFAULT_CURRENCY)
                        .setUnitAmount(productRequest.getAmount())
                        .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(productRequest.getName())
                                .build())
                        .build())
                .build();
    }
}

