package com.trinity.payment.stripe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @Schema(description = "product amount", example = "100")
    private Long amount;
    @Schema(description = "product quantity", example = "2")
    private Long quantity;
    @Schema(description = "product name", example = "cock")
    private String name;
    @Schema(description = "currency", example = "usd")
    private String currency;
}