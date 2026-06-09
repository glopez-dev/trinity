package com.trinity.payment.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;

/**
 * Pure domain Invoice aggregate root. Mirrors the REST {@code InvoiceDTO}
 * structure but is owned by the domain and free of framework annotations, so
 * the {@code InvoicingGateway} port can speak in domain terms instead of DTOs.
 *
 * <p>{@code items} is {@code @Singular} (as on the DTO), so the builder never
 * yields a null list — matching the DTO contract the API mapper maps onto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    private String id;
    private String status;
    private Cost totalAmount;
    private Party merchantInfo;
    private Party billingInfo;
    @Singular
    private List<InvoiceItem> items;
}
