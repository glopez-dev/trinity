package com.trinity.stock.model;

import com.trinity.stock.exception.NegativeStockException;
import com.trinity.stock.exception.NotEnoughStockException;
import com.trinity.stock.exception.TooMuchStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class StockTest {

    private Stock stock;

    @BeforeEach
    void setUp() {
        stock = Stock.builder()
                .id(UUID.randomUUID())
                .productId(UUID.randomUUID())
                .currentQuantity(50)
                .minTreshold(10)
                .maxTreshold(100)
                .build();
    }

    @Test
    void testSetCurrentQuantityThrowsNegativeStockException() {
        // Given
        int negativeQuantity = -1;

        // When & Then
        assertThatThrownBy(() -> stock.setCurrentQuantity(negativeQuantity))
                .isInstanceOf(NegativeStockException.class)
                .hasMessage("Current quantity cannot be negative");
    }

    @Test
    void testCanQuantityBeDeducted() {
        // Given
        int desiredQuantity = 20;

        // When
        boolean result = stock.canQuantityBeDeducted(desiredQuantity);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void testDeductFromStockThrowsNotEnoughStockException() {
        // Given
        int quantity = 60;

        // When & Then
        assertThatThrownBy(() -> stock.deductFromStock(quantity))
                .isInstanceOf(NotEnoughStockException.class)
                .hasMessage("Requested quantity is not available in stock");
    }

    @Test
    void testDeductFromStock() throws NotEnoughStockException, NegativeStockException {
        // Given
        int quantity = 20;

        // When
        stock.deductFromStock(quantity);

        // Then
        assertThat(stock.getCurrentQuantity()).isEqualTo(30);
    }

    @Test
    void testCanQuantityBeAdded() {
        // Given
        int quantity = 30;

        // When
        boolean result = stock.canQuantityBeAdded(quantity);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void testAddToStockThrowsTooMuchStockException() {
        // Given
        int quantity = 60;

        // When & Then
        assertThatThrownBy(() -> stock.addToStock(quantity))
                .isInstanceOf(TooMuchStockException.class)
                .hasMessage("Quantity exceeds the maximum treshold");
    }

    @Test
    void testAddToStock() throws TooMuchStockException, NegativeStockException {
        // Given
        int quantity = 30;

        // When
        stock.addToStock(quantity);

        // Then
        assertThat(stock.getCurrentQuantity()).isEqualTo(80);
    }

    @Test
    void testSetMinTresholdThrowsIllegalArgumentException() {
        // Given
        int negativeMinTreshold = -1;

        // When & Then
        assertThatThrownBy(() -> stock.setMinTreshold(negativeMinTreshold))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Minimum treshold cannot be negative");
    }

    @Test
    void testSetMaxTresholdThrowsIllegalArgumentException() {
        // Given
        int negativeMaxTreshold = -1;

        // When & Then
        assertThatThrownBy(() -> stock.setMaxTreshold(negativeMaxTreshold))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Maximum treshold cannot be negative");
    }
}