package com.trinity.payment.paypal.dto;

import lombok.*;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Invoice data transfer object")
public class InvoiceDTO {
    @Schema(description = "Invoice identifier")
    private String id;
    
    @Schema(description = "Invoice status", example = "PAID")
    private String status;
    
    @Schema(description = "Total amount of the invoice")
    private CostDTO totalAmount;

    @Schema(description = "Merchant information")
    private UserInfoDTO merchantInfo;
    
    @Schema(description = "Billing information")
    private UserInfoDTO billingInfo;

    @Singular
    @Schema(description = "List of invoice items")
    private List<InvoiceItemDTO> items;
}