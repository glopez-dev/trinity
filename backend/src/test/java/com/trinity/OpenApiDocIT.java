package com.trinity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Smoke test on the generated OpenAPI document: proves the contract clients
 * read actually reflects the refactored API. Guards the regressions that bit
 * before: the cart routes were invisible to springdoc while they were spelled
 * /api/V1 (pathsToMatch is case-sensitive), and request schemas must expose
 * the new {productId, quantity} shape — never client-supplied prices.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class OpenApiDocIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void apiDocs_exposeTheRefactoredContract() {
        String doc = restTemplate.getForObject("/api/v1/api-docs", String.class);

        assertThat(doc).isNotNull();

        // Cart endpoints are documented under the lowercase route, including the new cancel
        assertThat(doc).contains("/api/v1/carts/{customerId}");
        assertThat(doc).contains("/api/v1/carts/{customerId}/items");
        assertThat(doc).contains("/api/v1/carts/{customerId}/validate");
        assertThat(doc).contains("/api/v1/carts/{customerId}/cancel");

        // Checkout and payment endpoints are documented
        assertThat(doc).contains("/api/v1/stripe/checkout");
        assertThat(doc).contains("/api/v1/invoices");

        // Request schemas carry identity+quantity only: no client-supplied price anywhere
        assertThat(doc).contains("CartItemRequest");
        assertThat(doc).contains("CheckoutLineItemRequest");
        assertThat(doc).doesNotContain("unitAmount");

        // Removed surface is gone from the contract
        assertThat(doc).doesNotContain("/api/v1/hello");
        assertThat(doc).doesNotContain("tokenExpired");
    }
}
