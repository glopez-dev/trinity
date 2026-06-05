package com.trinity.payment.adapter;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.trinity.common.domain.exception.BusinessRuleViolation;
import com.trinity.common.domain.vo.Money;
import com.trinity.payment.config.PaymentProperties;
import com.trinity.payment.domain.Payment;
import com.trinity.payment.domain.PaymentLineItem;
import com.trinity.payment.domain.PaymentProvider;
import com.trinity.payment.domain.PaymentResult;
import com.trinity.payment.domain.PaymentStatus;
import com.trinity.payment.port.PaymentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Stripe anti-corruption adapter: the ONLY place com.stripe.* is allowed to
 * live going forward. Translates domain vocabulary to Stripe params and Stripe
 * responses/exceptions back to domain {@link PaymentResult} / domain exceptions.
 */
@Component
@RequiredArgsConstructor
public class StripePaymentAdapter implements PaymentGateway {

    private final PaymentProperties properties;

    @Override
    public PaymentProvider provider() {
        return PaymentProvider.STRIPE;
    }

    @Override
    public PaymentResult charge(Payment payment, String paymentMethodToken) {
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(toMinorUnits(payment.getAmount()))
                    .setCurrency(payment.getAmount().currency().toLowerCase())
                    .setPaymentMethod(paymentMethodToken)
                    .setConfirm(true)
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .setAllowRedirects(
                                            PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                    .build())
                    .build();
            PaymentIntent intent = PaymentIntent.create(params);
            return new PaymentResult(intent.getId(), mapStatus(intent.getStatus()), null, null);
        } catch (StripeException e) {
            throw new BusinessRuleViolation("Stripe charge failed: " + e.getMessage());
        }
    }

    @Override
    public PaymentResult createCheckout(List<PaymentLineItem> items, String successUrl, String cancelUrl) {
        try {
            SessionCreateParams.Builder builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(successUrl != null ? successUrl : properties.getSuccessUrl())
                    .setCancelUrl(cancelUrl != null ? cancelUrl : properties.getCancelUrl());
            items.forEach(item -> builder.addLineItem(toLineItem(item)));

            Session session = Session.create(builder.build());
            return new PaymentResult(session.getId(), PaymentStatus.PENDING, session.getUrl(), null);
        } catch (StripeException e) {
            throw new BusinessRuleViolation("Stripe checkout failed: " + e.getMessage());
        }
    }

    private SessionCreateParams.LineItem toLineItem(PaymentLineItem item) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity((long) item.quantity())
                .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(item.unitAmount().currency().toLowerCase())
                        .setUnitAmount(toMinorUnits(item.unitAmount()))
                        .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(item.name())
                                .build())
                        .build())
                .build();
    }

    /** Stripe uses minor units (cents). Valid for 2-decimal currencies only. */
    private long toMinorUnits(Money money) {
        return money.amount().movePointRight(2).longValueExact();
    }

    private PaymentStatus mapStatus(String stripeStatus) {
        if (stripeStatus == null) {
            return PaymentStatus.PENDING;
        }
        return switch (stripeStatus) {
            case "succeeded" -> PaymentStatus.SUCCEEDED;
            case "requires_capture", "requires_confirmation", "requires_action" -> PaymentStatus.AUTHORIZED;
            case "canceled" -> PaymentStatus.CANCELLED;
            default -> PaymentStatus.PENDING;
        };
    }
}
