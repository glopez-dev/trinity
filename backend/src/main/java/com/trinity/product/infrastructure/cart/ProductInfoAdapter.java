package com.trinity.product.infrastructure.cart;

import com.trinity.cart.domain.port.ProductInfoPort;
import com.trinity.common.domain.exception.NotFoundException;
import com.trinity.product.application.ProductService;
import com.trinity.product.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * The product module implements the cart's ProductInfoPort (consumer-owned
 * port, provider-side adapter). Hosting the adapter here keeps the module
 * graph acyclic: product depends on cart (port + domain event), cart depends
 * on nothing — required by Spring Modulith's cycle-free verification.
 */
@Component
@RequiredArgsConstructor
public class ProductInfoAdapter implements ProductInfoPort {

    private final ProductService productService;

    @Override
    public Optional<ProductInfo> findById(UUID productId) {
        try {
            Product product = productService.getProduct(productId);
            return Optional.of(new ProductInfo(product.getId(), product.getName(), product.getPrice()));
        } catch (NotFoundException e) {
            return Optional.empty();
        }
    }
}
