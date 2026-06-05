package com.trinity.common.domain.vo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoneyTest {

    @Test
    void of_createsMoney_withNormalizedScale() {
        Money money = Money.of(new BigDecimal("10.5"), "EUR");

        assertEquals(new BigDecimal("10.50"), money.amount());
        assertEquals("EUR", money.currency());
    }

    @Test
    void of_roundsHalfUp_toTwoDecimals() {
        assertEquals(new BigDecimal("10.13"), Money.of(new BigDecimal("10.125"), "EUR").amount());
        assertEquals(new BigDecimal("10.12"), Money.of(new BigDecimal("10.124"), "EUR").amount());
    }

    @Test
    void of_rejectsNegativeAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> Money.of(new BigDecimal("-1"), "EUR"));
    }

    @Test
    void of_rejectsNullAmount() {
        assertThrows(IllegalArgumentException.class, () -> Money.of(null, "EUR"));
    }

    @Test
    void of_rejectsNullOrBlankCurrency() {
        assertThrows(IllegalArgumentException.class, () -> Money.of(BigDecimal.TEN, null));
        assertThrows(IllegalArgumentException.class, () -> Money.of(BigDecimal.TEN, ""));
        assertThrows(IllegalArgumentException.class, () -> Money.of(BigDecimal.TEN, "  "));
    }

    @Test
    void of_rejectsInvalidCurrencyCode() {
        assertThrows(IllegalArgumentException.class, () -> Money.of(BigDecimal.TEN, "EU"));
        assertThrows(IllegalArgumentException.class, () -> Money.of(BigDecimal.TEN, "EURO"));
    }

    @Test
    void of_normalizesCurrencyToUpperCase() {
        assertEquals("EUR", Money.of(BigDecimal.TEN, "eur").currency());
    }

    @Test
    void zero_createsZeroAmount() {
        Money zero = Money.zero("USD");

        assertEquals(0, zero.amount().compareTo(BigDecimal.ZERO));
        assertEquals("USD", zero.currency());
    }

    @Test
    void add_sumsAmounts_sameCurrency() {
        Money result = Money.of(new BigDecimal("10.00"), "EUR")
                .add(Money.of(new BigDecimal("5.50"), "EUR"));

        assertEquals(new BigDecimal("15.50"), result.amount());
        assertEquals("EUR", result.currency());
    }

    @Test
    void add_rejectsDifferentCurrencies() {
        Money eur = Money.of(BigDecimal.TEN, "EUR");
        Money usd = Money.of(BigDecimal.TEN, "USD");

        assertThrows(IllegalArgumentException.class, () -> eur.add(usd));
    }

    @Test
    void multiply_scalesAmount() {
        Money result = Money.of(new BigDecimal("4.25"), "EUR").multiply(3);

        assertEquals(new BigDecimal("12.75"), result.amount());
        assertEquals("EUR", result.currency());
    }

    @Test
    void multiply_rejectsNegativeFactor() {
        Money money = Money.of(BigDecimal.TEN, "EUR");

        assertThrows(IllegalArgumentException.class, () -> money.multiply(-1));
    }

    @Test
    void isImmutable_operationsReturnNewInstances() {
        Money original = Money.of(new BigDecimal("10.00"), "EUR");
        Money added = original.add(Money.of(new BigDecimal("1.00"), "EUR"));

        assertNotSame(original, added);
        assertEquals(new BigDecimal("10.00"), original.amount());
    }

    @Test
    void equality_isByValue() {
        assertEquals(Money.of(new BigDecimal("10.0"), "EUR"), Money.of(new BigDecimal("10.00"), "EUR"));
        assertNotEquals(Money.of(BigDecimal.TEN, "EUR"), Money.of(BigDecimal.TEN, "USD"));
    }

    @Test
    void isZero_reportsZeroAmount() {
        assertTrue(Money.zero("EUR").isZero());
    }
}
