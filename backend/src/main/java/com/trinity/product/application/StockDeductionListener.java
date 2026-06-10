package com.trinity.product.application;

import com.trinity.cart.domain.event.CartValidatedEvent;
import com.trinity.common.domain.exception.BusinessRuleViolation;
import com.trinity.common.domain.exception.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;

/**
 * Deducts sold quantities when a cart is validated. AFTER_COMMIT keeps the
 * deduction out of the cart transaction; REQUIRES_NEW is mandatory there
 * because the original transaction is already over — without it the writes
 * would silently never commit. Failures are logged per line, never propagated:
 * exceptions thrown after commit are swallowed by Spring anyway, and one bad
 * line must not block the others (cart lines carry no FK, so an orphan
 * productId is possible).
 */
@Component
@RequiredArgsConstructor
public class StockDeductionListener {

    private static final Logger logger = LoggerFactory.getLogger(StockDeductionListener.class);

    private final ProductService productService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onCartValidated(CartValidatedEvent event) {
        for (CartValidatedEvent.Line line : event.lines()) {
            try {
                productService.deductStock(line.productId(), line.quantity());
            } catch (NotFoundException e) {
                logger.warn("Stock deduction skipped: product {} not found (cart lines carry no FK)",
                        line.productId());
            } catch (BusinessRuleViolation e) {
                logger.warn("Stock deduction rejected for product {}: {}", line.productId(), e.getMessage());
            }
        }
    }
}
