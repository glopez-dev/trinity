package com.trinity.payment.domain.port;

import com.trinity.payment.domain.model.Payment;
import com.trinity.payment.domain.model.PaymentLineItem;
import com.trinity.payment.domain.model.PaymentProvider;
import com.trinity.payment.domain.model.PaymentResult;

import java.util.List;

/**
 * Outbound port abstracting an external payment provider. All SDK types live
 * behind implementations of this interface (the anti-corruption boundary); the
 * domain and application layers only ever see domain vocabulary.
 */
public interface PaymentGateway {

    /** Charges a payment with a provider payment-method token. */
    PaymentResult charge(Payment payment, String paymentMethodToken);

    /** Creates a hosted checkout session for the given line items. */
    PaymentResult createCheckout(List<PaymentLineItem> items, String successUrl, String cancelUrl);

    /** Which provider this gateway speaks to. */
    PaymentProvider provider();
}
