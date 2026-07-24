package com.trinity.cart.application;

import com.trinity.cart.domain.port.ProductInfoPort;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/** Test double: a tiny in-memory product catalogue, in the spirit of InMemoryCartRepositoryPort. */
class InMemoryProductInfoPort implements ProductInfoPort {

    private final Map<UUID, ProductInfo> products = new HashMap<>();

    ProductInfo register(String name, BigDecimal unitPrice) {
        ProductInfo info = new ProductInfo(UUID.randomUUID(), name, unitPrice);
        products.put(info.productId(), info);
        return info;
    }

    @Override
    public Optional<ProductInfo> findById(UUID productId) {
        return Optional.ofNullable(products.get(productId));
    }
}
