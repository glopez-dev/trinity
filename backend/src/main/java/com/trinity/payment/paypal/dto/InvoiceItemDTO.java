package com.trinity.payment.paypal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Schema(description = "Invoice Item Data Transfer Object")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemDTO {
    @Schema(description = "Name of the item", example = "Product XYZ")
    private String name;

    @Schema(description = "Quantity of items", example = "1")
    private int quantity;

    @Schema(description = "Unit price of the item", example = "99.99")
    private BigDecimal unitPrice;
}