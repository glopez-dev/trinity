package com.trinity.payment.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Plain, non-validating holder of a monetary value (amount + currency).
 *
 * <p>Deliberately NOT {@code common.domain.vo.Money}: {@code Money} validates
 * (forces scale 2, rejects negatives, enforces a 3-letter upper-case currency),
 * which would change behaviour and throw on inputs the PayPal mapping currently
 * accepts. The PayPal anti-corruption layer relies on these quirks:
 * <ul>
 *   <li>a null SDK currency maps to {@code Cost(BigDecimal.ZERO, "USD")};</li>
 *   <li>a null {@code Cost} maps back to SDK {@code Currency("0", "USD")};</li>
 *   <li>{@code new BigDecimal(currency.getValue())} with NO scale normalization.</li>
 * </ul>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cost {
    private BigDecimal value;
    private String currency;
}
