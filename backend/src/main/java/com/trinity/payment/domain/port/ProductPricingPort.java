package com.trinity.payment.domain.port;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 * Payment-owned view of the product catalogue. Deliberately duplicated from the
 * cart's ProductInfoPort: a trivial port per consumer beats coupling the two
 * modules to a shared contract.
 */
public interface ProductPricingPort {

    Optional<PricedProduct> findById(UUID productId);

    record PricedProduct(UUID productId, String name, BigDecimal unitPrice) {
    }
}
