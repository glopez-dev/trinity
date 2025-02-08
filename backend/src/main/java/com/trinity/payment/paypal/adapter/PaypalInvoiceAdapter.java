package com.trinity.payment.paypal.adapter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.paypal.api.payments.BillingInfo;
import com.paypal.api.payments.Currency;
import com.paypal.api.payments.Invoice;
import com.paypal.api.payments.InvoiceItem;
import com.paypal.api.payments.MerchantInfo;
import com.trinity.payment.paypal.dto.CostDTO;
import com.trinity.payment.paypal.dto.InvoiceDTO;
import com.trinity.payment.paypal.dto.InvoiceItemDTO;
import com.trinity.payment.paypal.dto.UserInfoDTO;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Component
public class PaypalInvoiceAdapter {

    public InvoiceDTO mapToInvoiceDTO(Invoice invoice) {
        if (invoice == null) return null;

        return InvoiceDTO.builder()
                .id(invoice.getId())
                .status(invoice.getStatus())
                .totalAmount(mapToCostDTO(invoice.getTotalAmount()))
                .merchantInfo(mapToUserInfoDTO(invoice.getMerchantInfo()))
                .billingInfo(mapToBillingInfoDTO(invoice.getBillingInfo()))
                .items(Optional.ofNullable(invoice.getItems())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(this::mapToInvoiceItemDTO)
                        .toList())
                .build();
    }

    private CostDTO mapToCostDTO(Currency currency) {
        if (currency == null) return new CostDTO(BigDecimal.ZERO, "USD");
        return CostDTO.builder()
                .value(new BigDecimal(currency.getValue()))
                .currency(currency.getCurrency())
                .build();
    }

    public InvoiceItemDTO mapToInvoiceItemDTO(InvoiceItem item) {
        if (item == null) return null;

        return InvoiceItemDTO.builder()
                .name(item.getName())
                .quantity((int) item.getQuantity())
                .unitPrice(mapToBigDecimal(item.getUnitPrice()))
                .build();
    }

    private BigDecimal mapToBigDecimal(Currency currency) {
        return currency != null ? new BigDecimal(currency.getValue()) : BigDecimal.ZERO;
    }

    public UserInfoDTO mapToUserInfoDTO(MerchantInfo userInfo) {
        if (userInfo == null) return null;

        return UserInfoDTO.builder()
                .email(userInfo.getEmail())
                .firstName(userInfo.getFirstName())
                .lastName(userInfo.getLastName())
                .build();
    }

    public UserInfoDTO mapToBillingInfoDTO(List<BillingInfo> billingInfos) {
        return Optional.ofNullable(billingInfos)
                .orElse(Collections.emptyList())
                .stream()
                .findFirst()
                .map(billingInfo -> UserInfoDTO.builder()
                        .email(billingInfo.getEmail())
                        .firstName(billingInfo.getFirstName())
                        .lastName(billingInfo.getLastName())
                        .build())
                .orElse(null);
    }

    public Invoice mapToInvoice(InvoiceDTO invoiceDTO) {
        if (invoiceDTO == null) return null;

        Invoice invoice = new Invoice();
        invoice.setId(invoiceDTO.getId());
        invoice.setStatus(invoiceDTO.getStatus());
        invoice.setTotalAmount(mapToCurrency(invoiceDTO.getTotalAmount()));
        invoice.setMerchantInfo(mapToMerchantInfo(invoiceDTO.getMerchantInfo()));
        invoice.setBillingInfo(List.of(mapToBillingInfo(invoiceDTO.getBillingInfo())));
        invoice.setItems(Optional.ofNullable(invoiceDTO.getItems())
                .orElse(Collections.emptyList())
                .stream()
                .map(this::mapToInvoiceItem)
                .toList());
        return invoice;
    }

    private Currency mapToCurrency(CostDTO costDTO) {
        if (costDTO == null) return new Currency().setValue("0").setCurrency("USD");
        return new Currency()
                .setValue(costDTO.getValue().toString())
                .setCurrency(costDTO.getCurrency());
    }

    public InvoiceItem mapToInvoiceItem(InvoiceItemDTO itemDTO) {
        if (itemDTO == null) return null;

        InvoiceItem item = new InvoiceItem();
        item.setName(itemDTO.getName());
        item.setQuantity(itemDTO.getQuantity());
        item.setUnitPrice(mapToCurrency(new CostDTO(itemDTO.getUnitPrice(), "USD")));
        return item;
    }

    public MerchantInfo mapToMerchantInfo(UserInfoDTO userInfoDTO) {
        if (userInfoDTO == null) return null;

        MerchantInfo merchantInfo = new MerchantInfo();
        merchantInfo.setEmail(userInfoDTO.getEmail());
        merchantInfo.setFirstName(userInfoDTO.getFirstName());
        merchantInfo.setLastName(userInfoDTO.getLastName());
        return merchantInfo;
    }

    public BillingInfo mapToBillingInfo(UserInfoDTO userInfoDTO) {
        if (userInfoDTO == null) return null;

        BillingInfo billingInfo = new BillingInfo();
        billingInfo.setEmail(userInfoDTO.getEmail());
        billingInfo.setFirstName(userInfoDTO.getFirstName());
        billingInfo.setLastName(userInfoDTO.getLastName());
        return billingInfo;
    }
}
