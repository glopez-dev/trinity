package com.trinity.payment.infrastructure.external.stripe;

import com.stripe.exception.ApiException;
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
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class StripePaymentAdapterTest {

    private final PaymentProperties properties = new PaymentProperties();
    private final StripePaymentAdapter adapter = new StripePaymentAdapter(properties);

    @Test
    void provider_isStripe() {
        assertThat(adapter.provider()).isEqualTo(PaymentProvider.STRIPE);
    }

    @Test
    void createCheckout_returnsDomainResultWithSessionUrl() throws Exception {
        Session session = mock(Session.class);
        when(session.getId()).thenReturn("cs_test_123");
        when(session.getUrl()).thenReturn("https://checkout.stripe.com/cs_test_123");

        try (MockedStatic<Session> mocked = mockStatic(Session.class)) {
            mocked.when(() -> Session.create(any(SessionCreateParams.class))).thenReturn(session);

            PaymentResult result = adapter.createCheckout(
                    List.of(new PaymentLineItem(Money.of(new BigDecimal("10.00"), "USD"), 2, "Coffee")),
                    "https://ok", "https://cancel");

            assertThat(result.externalRef()).isEqualTo("cs_test_123");
            assertThat(result.redirectUrl()).isEqualTo("https://checkout.stripe.com/cs_test_123");
            assertThat(result.status()).isEqualTo(PaymentStatus.PENDING);
        }
    }

    @Test
    void charge_succeededIntent_mapsToSucceeded() throws Exception {
        PaymentIntent intent = mock(PaymentIntent.class);
        when(intent.getId()).thenReturn("pi_123");
        when(intent.getStatus()).thenReturn("succeeded");

        Payment payment = Payment.initiate(Money.of(new BigDecimal("20.00"), "USD"), PaymentProvider.STRIPE);

        try (MockedStatic<PaymentIntent> mocked = mockStatic(PaymentIntent.class)) {
            mocked.when(() -> PaymentIntent.create(any(PaymentIntentCreateParams.class))).thenReturn(intent);

            PaymentResult result = adapter.charge(payment, "pm_card_visa");

            assertThat(result.externalRef()).isEqualTo("pi_123");
            assertThat(result.status()).isEqualTo(PaymentStatus.SUCCEEDED);
        }
    }

    @Test
    void charge_requiresPaymentMethod_mapsToFailed() throws Exception {
        PaymentIntent intent = mock(PaymentIntent.class);
        when(intent.getId()).thenReturn("pi_decl");
        when(intent.getStatus()).thenReturn("requires_payment_method");

        Payment payment = Payment.initiate(Money.of(new BigDecimal("20.00"), "USD"), PaymentProvider.STRIPE);

        try (MockedStatic<PaymentIntent> mocked = mockStatic(PaymentIntent.class)) {
            mocked.when(() -> PaymentIntent.create(any(PaymentIntentCreateParams.class))).thenReturn(intent);

            PaymentResult result = adapter.charge(payment, "pm_card");

            assertThat(result.status()).isEqualTo(PaymentStatus.FAILED);
        }
    }

    @Test
    void charge_processing_mapsToPending() throws Exception {
        PaymentIntent intent = mock(PaymentIntent.class);
        when(intent.getId()).thenReturn("pi_proc");
        when(intent.getStatus()).thenReturn("processing");

        Payment payment = Payment.initiate(Money.of(new BigDecimal("20.00"), "USD"), PaymentProvider.STRIPE);

        try (MockedStatic<PaymentIntent> mocked = mockStatic(PaymentIntent.class)) {
            mocked.when(() -> PaymentIntent.create(any(PaymentIntentCreateParams.class))).thenReturn(intent);

            PaymentResult result = adapter.charge(payment, "pm_card");

            assertThat(result.status()).isEqualTo(PaymentStatus.PENDING);
        }
    }

    @Test
    void charge_zeroDecimalCurrency_doesNotMultiplyByHundred() throws Exception {
        PaymentIntent intent = mock(PaymentIntent.class);
        when(intent.getId()).thenReturn("pi_jpy");
        when(intent.getStatus()).thenReturn("succeeded");

        Payment payment = Payment.initiate(Money.of(new BigDecimal("1000"), "JPY"), PaymentProvider.STRIPE);

        try (MockedStatic<PaymentIntent> mocked = mockStatic(PaymentIntent.class)) {
            mocked.when(() -> PaymentIntent.create(any(PaymentIntentCreateParams.class)))
                    .thenAnswer(inv -> {
                        PaymentIntentCreateParams params = inv.getArgument(0);
                        // JPY is zero-decimal: 1000 JPY must be sent as 1000, not 100000.
                        assertThat(params.getAmount()).isEqualTo(1000L);
                        return intent;
                    });

            adapter.charge(payment, "pm_card");
        }
    }

    @Test
    void charge_overflowAmount_isRejectedAsBusinessRuleViolation() {
        Payment payment = Payment.initiate(
                Money.of(new BigDecimal("99999999999999999"), "USD"), PaymentProvider.STRIPE);

        // No Stripe call should be reached: the conversion overflow must surface as a
        // domain error (mapped to 4xx), not a raw ArithmeticException -> 500.
        assertThatThrownBy(() -> adapter.charge(payment, "pm_card"))
                .isInstanceOf(BusinessRuleViolation.class);
    }

    @Test
    void charge_translatesStripeExceptionToBusinessRuleViolation() {
        Payment payment = Payment.initiate(Money.of(new BigDecimal("20.00"), "USD"), PaymentProvider.STRIPE);

        try (MockedStatic<PaymentIntent> mocked = mockStatic(PaymentIntent.class)) {
            mocked.when(() -> PaymentIntent.create(any(PaymentIntentCreateParams.class)))
                    .thenThrow(new ApiException("card declined", null, null, 402, null));

            assertThatThrownBy(() -> adapter.charge(payment, "pm_card_declined"))
                    .isInstanceOf(BusinessRuleViolation.class);
        }
    }
}
