package com.trinity.payment.interfaces.rest.controller;

import com.trinity.payment.infrastructure.config.PaymentProperties;
import com.trinity.payment.domain.model.PaymentProvider;
import com.trinity.payment.domain.model.PaymentResult;
import com.trinity.payment.domain.model.PaymentStatus;
import com.trinity.payment.interfaces.rest.dto.CheckoutLineItemRequest;
import com.trinity.payment.interfaces.rest.dto.CheckoutRequest;
import com.trinity.payment.interfaces.rest.dto.CheckoutResponse;
import com.trinity.payment.application.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ProductCheckoutControllerTest {

    @Mock
    private PaymentService paymentService;

    private ProductCheckoutController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ProductCheckoutController(paymentService, new PaymentProperties());
    }

    @Test
    void checkoutProducts_returnsCheckoutResponseWithRealStatus() {
        CheckoutRequest request = new CheckoutRequest(List.of(
                new CheckoutLineItemRequest(new BigDecimal("10.00"), "USD", 2, "Coffee")));
        when(paymentService.createCheckout(eq(PaymentProvider.STRIPE), any(), any(), any()))
                .thenReturn(new PaymentResult("cs_test_1", PaymentStatus.PENDING,
                        "https://checkout.stripe.com/cs_test_1", null));

        ResponseEntity<CheckoutResponse> response = controller.checkoutProducts(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        CheckoutResponse body = response.getBody();
        assertThat(body).isNotNull();
        // the session id lands in sessionId, the URL in redirectUrl (no field-order bug)
        assertThat(body.sessionId()).isEqualTo("cs_test_1");
        assertThat(body.redirectUrl()).isEqualTo("https://checkout.stripe.com/cs_test_1");
        assertThat(body.status()).isEqualTo("PENDING");
    }
}
