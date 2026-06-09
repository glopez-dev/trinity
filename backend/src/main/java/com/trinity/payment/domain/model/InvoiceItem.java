package com.trinity.payment.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Line item within an {@link Invoice} aggregate. Mirrors {@code InvoiceItemDTO}.
 *
 * <p>{@code unitPrice} is a bare {@link BigDecimal} (not wrapped in {@link Cost}).
 * When mapped to the PayPal SDK it is wrapped as a {@code Currency} with a
 * HARD-CODED {@code "USD"}; when mapped back it is read as
 * {@code new BigDecimal(currency.getValue())} with NO scale normalization.
 * These are PayPal SDK quirks the adapter preserves verbatim.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceItem {
    private String name;
    private int quantity;
    private BigDecimal unitPrice;
}
