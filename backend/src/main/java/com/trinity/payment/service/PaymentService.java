package com.trinity.payment.service;

import com.trinity.common.domain.exception.BusinessRuleViolation;
import com.trinity.common.domain.vo.Money;
import com.trinity.payment.domain.Payment;
import com.trinity.payment.domain.PaymentLineItem;
import com.trinity.payment.domain.PaymentProvider;
import com.trinity.payment.domain.PaymentResult;
import com.trinity.payment.domain.PaymentStatus;
import com.trinity.payment.port.PaymentGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Application service orchestrating payments through provider gateways. It only
 * knows the {@link PaymentGateway} port — never an SDK type. The right gateway
 * is resolved by provider from the injected list of adapters.
 */
@Service
public class PaymentService {

    private final Map<PaymentProvider, PaymentGateway> gateways = new EnumMap<>(PaymentProvider.class);

    public PaymentService(List<PaymentGateway> gatewayList) {
        gatewayList.forEach(g -> gateways.put(g.provider(), g));
    }

    @Transactional
    public Payment charge(Money amount, PaymentProvider provider, String paymentMethodToken) {
        Payment payment = Payment.initiate(amount, provider);
        PaymentResult result = gatewayFor(provider).charge(payment, paymentMethodToken);
        applyResult(payment, result);
        return payment;
    }

    @Transactional
    public PaymentResult createCheckout(PaymentProvider provider, List<PaymentLineItem> items,
                                        String successUrl, String cancelUrl) {
        return gatewayFor(provider).createCheckout(items, successUrl, cancelUrl);
    }

    private void applyResult(Payment payment, PaymentResult result) {
        switch (result.status()) {
            case FAILED -> payment.markFailed(result.failureReason());
            case CANCELLED -> payment.cancel();
            case AUTHORIZED -> payment.markAuthorized(result.externalRef());
            case SUCCEEDED -> {
                payment.markAuthorized(result.externalRef());
                payment.markSucceeded();
            }
            // PENDING: the charge is still in flight — leave the aggregate PENDING.
            case PENDING -> { /* no-op */ }
        }
    }

    private PaymentGateway gatewayFor(PaymentProvider provider) {
        PaymentGateway gateway = gateways.get(provider);
        if (gateway == null) {
            throw new BusinessRuleViolation("No payment gateway configured for provider: " + provider);
        }
        return gateway;
    }
}
