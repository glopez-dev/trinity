package com.trinity.payment.paypal.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public
class InvoiceItemDTO {
    private String name;

    private int quantity;
    private BigDecimal unitPrice;
}