package com.trinity.product.application;

import com.trinity.cart.domain.event.CartValidatedEvent;
import com.trinity.common.domain.exception.BusinessRuleViolation;
import com.trinity.common.domain.exception.NotFoundException;
import com.trinity.common.domain.vo.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockDeductionListenerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private StockDeductionListener listener;

    private static CartValidatedEvent event(CartValidatedEvent.Line... lines) {
        return new CartValidatedEvent(
                UUID.randomUUID(), List.of(lines), Money.of(new BigDecimal("10.00"), "USD"), Instant.now());
    }

    private static CartValidatedEvent.Line line(UUID productId, int quantity) {
        return new CartValidatedEvent.Line(productId, "Product", quantity, new BigDecimal("5.00"));
    }

    @Test
    void onCartValidated_deductsEveryLine() {
        UUID p1 = UUID.randomUUID();
        UUID p2 = UUID.randomUUID();

        listener.onCartValidated(event(line(p1, 2), line(p2, 5)));

        verify(productService).deductStock(p1, 2);
        verify(productService).deductStock(p2, 5);
    }

    @Test
    void onCartValidated_orphanProduct_isLoggedAndOthersStillProcessed() {
        UUID orphan = UUID.randomUUID();
        UUID valid = UUID.randomUUID();
        when(productService.deductStock(orphan, 1)).thenThrow(new NotFoundException("missing"));

        assertThatCode(() -> listener.onCartValidated(event(line(orphan, 1), line(valid, 3))))
                .doesNotThrowAnyException();

        verify(productService).deductStock(valid, 3);
    }

    @Test
    void onCartValidated_insufficientStock_isLoggedAndOthersStillProcessed() {
        UUID lowStock = UUID.randomUUID();
        UUID valid = UUID.randomUUID();
        when(productService.deductStock(lowStock, 99)).thenThrow(new BusinessRuleViolation("negative"));

        assertThatCode(() -> listener.onCartValidated(event(line(lowStock, 99), line(valid, 1))))
                .doesNotThrowAnyException();

        verify(productService).deductStock(valid, 1);
    }
}
