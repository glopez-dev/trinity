package com.trinity.payment.infrastructure.external.paypal;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.paypal.api.payments.BillingInfo;
import com.paypal.api.payments.Currency;
import com.paypal.api.payments.MerchantInfo;
import com.trinity.payment.domain.model.Cost;
import com.trinity.payment.domain.model.Invoice;
import com.trinity.payment.domain.model.InvoiceItem;
import com.trinity.payment.domain.model.Party;

import lombok.NoArgsConstructor;

/**
 * PayPal anti-corruption mapper: translates between the domain {@link Invoice}
 * aggregate and the PayPal SDK invoice types. The SDK invoice/item types are
 * fully qualified to disambiguate them from the domain ones.
 *
 * <p>Every PayPal mapping quirk is preserved verbatim from the previous
 * DTO-based mapper:
 * <ul>
 *   <li>null SDK {@code Currency} maps to {@code Cost(BigDecimal.ZERO, "USD")};
 *       null domain {@link Cost} maps back to SDK {@code Currency("0", "USD")};</li>
 *   <li>{@code new BigDecimal(currency.getValue())} with NO scale normalization;</li>
 *   <li>an item's bare {@code unitPrice} is wrapped as a {@code Currency} with a
 *       HARD-CODED {@code "USD"};</li>
 *   <li>the single billing {@link Party} is wrapped as {@code List.of(single)} on
 *       the way to the SDK and read back via {@code findFirst().orElse(null)};</li>
 *   <li>{@code items} are null-safe via {@code Optional.orElse(emptyList())}.</li>
 * </ul>
 */
@NoArgsConstructor
@Component
public class PaypalInvoiceAdapter {

    // ===== SDK -> domain =====

    public Invoice mapToInvoice(com.paypal.api.payments.Invoice invoice) {
        if (invoice == null) return null;

        return Invoice.builder()
                .id(invoice.getId())
                .status(invoice.getStatus())
                .totalAmount(mapToCost(invoice.getTotalAmount()))
                .merchantInfo(mapToParty(invoice.getMerchantInfo()))
                .billingInfo(mapToBillingParty(invoice.getBillingInfo()))
                .items(Optional.ofNullable(invoice.getItems())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(this::mapToInvoiceItem)
                        .toList())
                .build();
    }

    private Cost mapToCost(Currency currency) {
        if (currency == null) return new Cost(BigDecimal.ZERO, "USD");
        return Cost.builder()
                .value(new BigDecimal(currency.getValue()))
                .currency(currency.getCurrency())
                .build();
    }

    public InvoiceItem mapToInvoiceItem(com.paypal.api.payments.InvoiceItem item) {
        if (item == null) return null;

        return InvoiceItem.builder()
                .name(item.getName())
                .quantity((int) item.getQuantity())
                .unitPrice(mapToBigDecimal(item.getUnitPrice()))
                .build();
    }

    private BigDecimal mapToBigDecimal(Currency currency) {
        return currency != null ? new BigDecimal(currency.getValue()) : BigDecimal.ZERO;
    }

    public Party mapToParty(MerchantInfo userInfo) {
        if (userInfo == null) return null;

        return Party.builder()
                .email(userInfo.getEmail())
                .firstName(userInfo.getFirstName())
                .lastName(userInfo.getLastName())
                .build();
    }

    public Party mapToBillingParty(List<BillingInfo> billingInfos) {
        return Optional.ofNullable(billingInfos)
                .orElse(Collections.emptyList())
                .stream()
                .findFirst()
                .map(billingInfo -> Party.builder()
                        .email(billingInfo.getEmail())
                        .firstName(billingInfo.getFirstName())
                        .lastName(billingInfo.getLastName())
                        .build())
                .orElse(null);
    }

    // ===== domain -> SDK =====

    public com.paypal.api.payments.Invoice mapToSdkInvoice(Invoice invoice) {
        if (invoice == null) return null;

        com.paypal.api.payments.Invoice sdkInvoice = new com.paypal.api.payments.Invoice();
        sdkInvoice.setId(invoice.getId());
        sdkInvoice.setStatus(invoice.getStatus());
        sdkInvoice.setTotalAmount(mapToCurrency(invoice.getTotalAmount()));
        sdkInvoice.setMerchantInfo(mapToMerchantInfo(invoice.getMerchantInfo()));
        sdkInvoice.setBillingInfo(List.of(mapToBillingInfo(invoice.getBillingInfo())));
        sdkInvoice.setItems(Optional.ofNullable(invoice.getItems())
                .orElse(Collections.emptyList())
                .stream()
                .map(this::mapToSdkInvoiceItem)
                .toList());
        return sdkInvoice;
    }

    private Currency mapToCurrency(Cost cost) {
        if (cost == null) return new Currency().setValue("0").setCurrency("USD");
        return new Currency()
                .setValue(cost.getValue().toString())
                .setCurrency(cost.getCurrency());
    }

    public com.paypal.api.payments.InvoiceItem mapToSdkInvoiceItem(InvoiceItem item) {
        if (item == null) return null;

        com.paypal.api.payments.InvoiceItem sdkItem = new com.paypal.api.payments.InvoiceItem();
        sdkItem.setName(item.getName());
        sdkItem.setQuantity(item.getQuantity());
        sdkItem.setUnitPrice(mapToCurrency(new Cost(item.getUnitPrice(), "USD")));
        return sdkItem;
    }

    public MerchantInfo mapToMerchantInfo(Party party) {
        if (party == null) return null;

        MerchantInfo merchantInfo = new MerchantInfo();
        merchantInfo.setEmail(party.getEmail());
        merchantInfo.setFirstName(party.getFirstName());
        merchantInfo.setLastName(party.getLastName());
        return merchantInfo;
    }

    public BillingInfo mapToBillingInfo(Party party) {
        if (party == null) return null;

        BillingInfo billingInfo = new BillingInfo();
        billingInfo.setEmail(party.getEmail());
        billingInfo.setFirstName(party.getFirstName());
        billingInfo.setLastName(party.getLastName());
        return billingInfo;
    }
}
