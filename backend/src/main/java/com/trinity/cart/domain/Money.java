package com.trinity.cart.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class Money {
    @Builder.Default
    private BigDecimal amount = BigDecimal.ZERO;

    @Builder.Default
    private String currency = "USD";
}
