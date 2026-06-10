package com.trinity;

import com.trinity.cart.application.CartService;
import com.trinity.cart.domain.model.CartItem;
import com.trinity.common.domain.exception.NotFoundException;
import com.trinity.product.application.ProductService;
import com.trinity.product.domain.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * End-to-end proof of the in-process purchase flow: validating a cart publishes
 * CartValidatedEvent, whose AFTER_COMMIT listener deducts the sold quantity
 * from the product stock in a new transaction.
 *
 * <p>Deliberately NOT annotated @Transactional: a test-managed transaction
 * would never commit, so the AFTER_COMMIT listener would never fire. The
 * listener runs synchronously post-commit, so assertions can follow directly.
 */
@SpringBootTest
@ActiveProfiles("test")
class CartValidationStockIT {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Test
    void validatingACart_deductsStock_andDeletesTheCart() {
        // Given a product with stock 10
        Product product = productService.createProduct(Product.builder()
                .name("IT Apples")
                .barcode("4711".concat(String.valueOf(System.nanoTime() % 1_000_000_000L)))
                .price(new BigDecimal("2.50"))
                .stock(Product.Stock.builder().quantity(10).minThreshold(2).maxThreshold(100).build())
                .build());

        // And a cart holding 2 of it
        UUID customerId = UUID.randomUUID();
        cartService.createCart(customerId);
        cartService.addItemToCart(customerId, CartItem.builder()
                .productId(product.getId())
                .productName(product.getName())
                .quantity(2)
                .unitPrice(product.getPrice())
                .build());

        // When the cart is validated
        cartService.validateCart(customerId);

        // Then the stock was deducted by the AFTER_COMMIT listener...
        Product reloaded = productService.getProduct(product.getId());
        assertThat(reloaded.getStock().getQuantity()).isEqualTo(8);

        // ...and the cart is gone
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> cartService.getCart(customerId));
    }
}
