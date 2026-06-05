package com.trinity.cart.model;

import com.trinity.common.domain.vo.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies the lossless round-trip between the immutable Money record and its
 * mutable persistence twin MoneyEmbeddable. The record cannot be a Hibernate
 * @Embeddable, hence this twin.
 */
class MoneyEmbeddableTest {

    @Test
    void roundTrip_preservesAmountAndCurrency() {
        Money original = Money.of(new BigDecimal("20.00"), "USD");

        MoneyEmbeddable embeddable = new MoneyEmbeddable(original.amount(), original.currency());
        Money rebuilt = Money.of(embeddable.getAmount(), embeddable.getCurrency());

        assertThat(rebuilt.amount()).isEqualByComparingTo("20.00");
        assertThat(rebuilt.currency()).isEqualTo("USD");
        assertThat(rebuilt).isEqualTo(original);
    }

    @Test
    void roundTrip_preservesZeroScale() {
        Money zero = Money.zero("USD");

        MoneyEmbeddable embeddable = new MoneyEmbeddable(zero.amount(), zero.currency());
        Money rebuilt = Money.of(embeddable.getAmount(), embeddable.getCurrency());

        assertThat(rebuilt.amount()).isEqualByComparingTo("0.00");
        assertThat(rebuilt).isEqualTo(zero);
    }
}
