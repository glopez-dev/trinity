package com.trinity.payment.infrastructure.external.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.trinity.common.domain.exception.BusinessRuleViolation;
import com.trinity.common.domain.vo.Money;
import com.trinity.payment.infrastructure.config.PaymentProperties;
import com.trinity.payment.domain.model.Payment;
import com.trinity.payment.domain.model.PaymentLineItem;
import com.trinity.payment.domain.model.PaymentProvider;
import com.trinity.payment.domain.model.PaymentResult;
import com.trinity.payment.domain.model.PaymentStatus;
import com.trinity.payment.domain.port.PaymentGateway;
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

    /**
     * Converts a Money to the provider's smallest unit, honoring the currency's
     * actual fraction digits (e.g. JPY has 0, so 1000 JPY -> 1000, not 100000).
     */
    private long toMinorUnits(Money money) {
        int fractionDigits = fractionDigitsOf(money.currency());
        try {
            return money.amount().movePointRight(fractionDigits).longValueExact();
        } catch (ArithmeticException e) {
            throw new BusinessRuleViolation("Payment amount is out of range: " + money.amount());
        }
    }

    private int fractionDigitsOf(String currencyCode) {
        try {
            int digits = java.util.Currency.getInstance(currencyCode).getDefaultFractionDigits();
            return digits >= 0 ? digits : 2;
        } catch (IllegalArgumentException e) {
            throw new BusinessRuleViolation("Unsupported currency: " + currencyCode);
        }
    }

    private PaymentStatus mapStatus(String stripeStatus) {
        if (stripeStatus == null) {
            return PaymentStatus.PENDING;
        }
        return switch (stripeStatus) {
            case "succeeded" -> PaymentStatus.SUCCEEDED;
            // Only a captured-or-holdable state is a genuine authorization. Under this
            // server-only, no-redirect confirm flow, requires_action cannot complete.
            case "requires_capture" -> PaymentStatus.AUTHORIZED;
            case "requires_payment_method", "requires_action" -> PaymentStatus.FAILED;
            case "processing", "requires_confirmation" -> PaymentStatus.PENDING;
            case "canceled" -> PaymentStatus.CANCELLED;
            default -> PaymentStatus.PENDING;
        };
    }
}
