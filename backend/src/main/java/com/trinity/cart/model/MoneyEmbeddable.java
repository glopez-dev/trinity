package com.trinity.cart.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Mutable persistence twin of the immutable {@link com.trinity.common.domain.vo.Money}
 * record. A Java record cannot be a Hibernate @Embeddable (no no-arg constructor,
 * final components), so this class carries the two columns and the domain
 * invariants are re-applied via Money.of when reconstructing the record.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoneyEmbeddable {

    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(length = 3)
    private String currency;
}
