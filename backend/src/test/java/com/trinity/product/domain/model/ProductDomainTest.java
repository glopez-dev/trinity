package com.trinity.product.domain.model;

import com.trinity.common.domain.exception.BusinessRuleViolation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Behavioral tests for the Product aggregate and its Stock value object.
 */
class ProductDomainTest {

    private Product.Stock stock(int quantity, int min, int max) {
        return Product.Stock.builder()
                .quantity(quantity).minThreshold(min).maxThreshold(max).build();
    }

    @Test
    void stock_reduceBy_decreasesQuantity() {
        Product.Stock s = stock(10, 2, 100);
        s.reduceBy(3);
        assertEquals(7, s.getQuantity());
    }

    @Test
    void stock_reduceBy_rejectsBelowZero() {
        Product.Stock s = stock(2, 0, 100);
        assertThrows(BusinessRuleViolation.class, () -> s.reduceBy(5));
    }

    @Test
    void stock_reduceBy_rejectsNegativeAmount() {
        Product.Stock s = stock(10, 0, 100);
        assertThrows(BusinessRuleViolation.class, () -> s.reduceBy(-1));
    }

    @Test
    void stock_increaseBy_increasesQuantity() {
        Product.Stock s = stock(10, 2, 100);
        s.increaseBy(5);
        assertEquals(15, s.getQuantity());
    }

    @Test
    void stock_increaseBy_rejectsNegativeAmount() {
        Product.Stock s = stock(10, 0, 100);
        assertThrows(BusinessRuleViolation.class, () -> s.increaseBy(-1));
    }

    @Test
    void stock_isBelowThreshold_reportsLowStock() {
        assertTrue(stock(1, 5, 100).isBelowThreshold());
        assertFalse(stock(10, 5, 100).isBelowThreshold());
    }

    @Test
    void product_changePrice_updatesPrice() {
        Product product = Product.builder().price(new BigDecimal("5.00")).build();
        product.changePrice(new BigDecimal("9.99"));
        assertEquals(new BigDecimal("9.99"), product.getPrice());
    }

    @Test
    void product_changePrice_rejectsNegative() {
        Product product = Product.builder().price(new BigDecimal("5.00")).build();
        assertThrows(BusinessRuleViolation.class,
                () -> product.changePrice(new BigDecimal("-1.00")));
    }

    @Test
    void product_changePrice_rejectsNull() {
        Product product = Product.builder().price(new BigDecimal("5.00")).build();
        assertThrows(BusinessRuleViolation.class, () -> product.changePrice(null));
    }

    @Test
    void product_adjustStock_appliesDelta() {
        Product product = Product.builder()
                .price(new BigDecimal("5.00"))
                .stock(stock(10, 2, 100))
                .build();

        product.adjustStock(5);
        assertEquals(15, product.getStock().getQuantity());

        product.adjustStock(-7);
        assertEquals(8, product.getStock().getQuantity());
    }

    @Test
    void product_adjustStock_rejectsBelowZero() {
        Product product = Product.builder()
                .price(new BigDecimal("5.00"))
                .stock(stock(3, 2, 100))
                .build();

        assertThrows(BusinessRuleViolation.class, () -> product.adjustStock(-5));
    }
}
