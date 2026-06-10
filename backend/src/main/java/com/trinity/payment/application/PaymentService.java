package com.trinity.payment.application;

import com.trinity.common.domain.exception.BusinessRuleViolation;
import com.trinity.common.domain.vo.Money;
import com.trinity.payment.domain.model.Payment;
import com.trinity.payment.domain.model.PaymentLineItem;
import com.trinity.payment.domain.model.PaymentProvider;
import com.trinity.payment.domain.model.PaymentResult;
import com.trinity.payment.domain.model.PaymentStatus;
import com.trinity.common.domain.exception.NotFoundException;
import com.trinity.payment.application.command.CheckoutLine;
import com.trinity.payment.domain.port.PaymentGateway;
import com.trinity.payment.domain.port.PaymentRepositoryPort;
import com.trinity.payment.domain.port.ProductPricingPort;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Application service orchestrating payments through provider gateways. It only
 * knows the {@link PaymentGateway} port — never an SDK type. The right gateway
 * is resolved by provider from the injected list of adapters.
 *
 * <p>No method-level @Transactional: the gateway call is an external HTTP
 * request that must never hold a DB connection. Each repository save is its own
 * short transaction inside the adapter.
 */
@Service
public class PaymentService {

    private final Map<PaymentProvider, PaymentGateway> gateways = new EnumMap<>(PaymentProvider.class);
    private final PaymentRepositoryPort paymentRepository;
    private final ProductPricingPort productPricingPort;

    public PaymentService(List<PaymentGateway> gatewayList, PaymentRepositoryPort paymentRepository,
                          ProductPricingPort productPricingPort) {
        gatewayList.forEach(g -> gateways.put(g.provider(), g));
        this.paymentRepository = paymentRepository;
        this.productPricingPort = productPricingPort;
    }

    /**
     * Hosted checkout from product identities: every price is resolved
     * server-side against the catalogue — client-supplied amounts are gone.
     */
    public PaymentResult checkoutProducts(PaymentProvider provider, List<CheckoutLine> lines,
                                          String currency, String successUrl, String cancelUrl) {
        List<PaymentLineItem> items = lines.stream().map(line -> {
            ProductPricingPort.PricedProduct product = productPricingPort.findById(line.productId())
                    .orElseThrow(() -> new NotFoundException("Product not found: " + line.productId()));
            return new PaymentLineItem(Money.of(product.unitPrice(), currency), line.quantity(), product.name());
        }).toList();
        return createCheckout(provider, items, successUrl, cancelUrl);
    }

    public Payment charge(Money amount, PaymentProvider provider, String paymentMethodToken) {
        Payment payment = Payment.initiate(amount, provider);
        paymentRepository.save(payment);              // TX1: trace PENDING before the network call
        PaymentResult result;
        try {
            result = gatewayFor(provider).charge(payment, paymentMethodToken);   // HTTP, outside any TX
        } catch (RuntimeException e) {
            payment.markFailed(e.getMessage());
            paymentRepository.save(payment);          // TX2: the failure is persisted
            throw e;
        }
        applyResult(payment, result);
        paymentRepository.save(payment);              // TX2: the outcome is persisted
        return payment;
        // A crash between TX1 and the gateway leaves a PENDING row: accepted —
        // reconciliation/webhooks are out of scope for now.
    }

    public PaymentResult createCheckout(PaymentProvider provider, List<PaymentLineItem> items,
                                        String successUrl, String cancelUrl) {
        PaymentResult result = gatewayFor(provider).createCheckout(items, successUrl, cancelUrl);
        Payment payment = Payment.initiate(totalOf(items), provider);
        if (result.externalRef() != null) {
            payment.assignExternalRef(result.externalRef());
        }
        paymentRepository.save(payment);              // single TX, after the HTTP call
        return result;
    }

    private Money totalOf(List<PaymentLineItem> items) {
        return items.stream()
                .map(item -> item.unitAmount().multiply(item.quantity()))
                .reduce(Money::add)
                .orElseThrow(() -> new BusinessRuleViolation("A checkout requires at least one line item"));
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
