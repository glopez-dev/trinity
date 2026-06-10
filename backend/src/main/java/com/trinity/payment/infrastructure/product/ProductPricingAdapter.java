package com.trinity.payment.infrastructure.product;

import com.trinity.common.domain.exception.NotFoundException;
import com.trinity.payment.domain.port.ProductPricingPort;
import com.trinity.product.application.ProductService;
import com.trinity.product.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * In-process adapter from the payment's ProductPricingPort to the product
 * module's application service (its whitelisted public surface).
 */
@Component
@RequiredArgsConstructor
public class ProductPricingAdapter implements ProductPricingPort {

    private final ProductService productService;

    @Override
    public Optional<PricedProduct> findById(UUID productId) {
        try {
            Product product = productService.getProduct(productId);
            return Optional.of(new PricedProduct(product.getId(), product.getName(), product.getPrice()));
        } catch (NotFoundException e) {
            return Optional.empty();
        }
    }
}
