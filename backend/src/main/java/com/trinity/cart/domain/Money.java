package com.trinity.cart.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Money {
    private BigDecimal amount = BigDecimal.ZERO;
    private String currency = "USD";
}
