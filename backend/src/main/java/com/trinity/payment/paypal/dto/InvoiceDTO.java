package com.trinity.payment.paypal.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {
    private String id;
    private String status;
    private CostDTO totalAmount;

    private UserInfoDTO merchantInfo;
    private UserInfoDTO billingInfo;

    @Singular
    private List<InvoiceItemDTO> items;
}