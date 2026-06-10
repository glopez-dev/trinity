package com.trinity.cart.infrastructure.product;

import com.trinity.cart.domain.port.ProductInfoPort;
import com.trinity.common.domain.exception.NotFoundException;
import com.trinity.product.application.ProductService;
import com.trinity.product.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * In-process adapter from the cart's ProductInfoPort to the product module's
 * application service (its whitelisted public surface). If the product module
 * is ever extracted, only this adapter changes.
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
