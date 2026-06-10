package com.trinity.cart.domain.port;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 * Cart-owned view of the product catalogue: name and current price by id. The
 * cart never trusts client-supplied prices — this port is the only price
 * source when adding an item.
 */
public interface ProductInfoPort {

    Optional<ProductInfo> findById(UUID productId);

    record ProductInfo(UUID productId, String name, BigDecimal unitPrice) {
    }
}
