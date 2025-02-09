package com.trinity.payment.paypal.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing a cost with value and currency")
public class CostDTO {
    @Schema(description = "The monetary value", example = "99.99")
    private BigDecimal value;

    @Schema(description = "The currency code", example = "USD")
    private String currency;
}
