package com.trinity.payment.paypal.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.paypal.api.payments.BillingInfo;
import com.paypal.api.payments.Currency;
import com.paypal.api.payments.Invoice;
import com.paypal.api.payments.InvoiceItem;
import com.paypal.api.payments.MerchantInfo;
import com.trinity.payment.paypal.dto.CostDTO;
import com.trinity.payment.paypal.dto.InvoiceDTO;
import com.trinity.payment.paypal.dto.InvoiceItemDTO;
import com.trinity.payment.paypal.dto.UserInfoDTO;

class PaypalInvoiceAdapterTest {

    private PaypalInvoiceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new PaypalInvoiceAdapter();
    }

    @Test
    void testMapToInvoiceDTO() {
        Invoice invoice = new Invoice();
        invoice.setId("INV2-QXWN-W3VH-Q8H7-XH8J");
        invoice.setStatus("PAID");
        Currency totalAmount = new Currency();
        totalAmount.setValue("100.00");
        totalAmount.setCurrency("USD");
        invoice.setTotalAmount(totalAmount);

        MerchantInfo merchantInfo = new MerchantInfo();
        merchantInfo.setEmail("merchant@example.com");
        merchantInfo.setFirstName("John");
        merchantInfo.setLastName("Doe");
        invoice.setMerchantInfo(merchantInfo);

        BillingInfo billingInfo = new BillingInfo();
        billingInfo.setEmail("customer@example.com");
        billingInfo.setFirstName("Jane");
        billingInfo.setLastName("Doe");
        invoice.setBillingInfo(List.of(billingInfo));

        InvoiceItem item = new InvoiceItem();
        item.setName("Item 1");
        item.setQuantity(2);
        Currency unitPrice = new Currency();
        unitPrice.setValue("50.00");
        unitPrice.setCurrency("USD");
        item.setUnitPrice(unitPrice);
        invoice.setItems(List.of(item));

        InvoiceDTO invoiceDTO = adapter.mapToInvoiceDTO(invoice);

        assertNotNull(invoiceDTO);
        assertEquals("INV2-QXWN-W3VH-Q8H7-XH8J", invoiceDTO.getId());
        assertEquals("PAID", invoiceDTO.getStatus());
        assertEquals(new BigDecimal("100.00"), invoiceDTO.getTotalAmount().getValue());
        assertEquals("USD", invoiceDTO.getTotalAmount().getCurrency());
        assertEquals("merchant@example.com", invoiceDTO.getMerchantInfo().getEmail());
        assertEquals("John", invoiceDTO.getMerchantInfo().getFirstName());
        assertEquals("Doe", invoiceDTO.getMerchantInfo().getLastName());
        assertEquals("customer@example.com", invoiceDTO.getBillingInfo().getEmail());
        assertEquals("Jane", invoiceDTO.getBillingInfo().getFirstName());
        assertEquals("Doe", invoiceDTO.getBillingInfo().getLastName());
        assertEquals(1, invoiceDTO.getItems().size());
        assertEquals("Item 1", invoiceDTO.getItems().get(0).getName());
        assertEquals(2, invoiceDTO.getItems().get(0).getQuantity());
        assertEquals(new BigDecimal("50.00"), invoiceDTO.getItems().get(0).getUnitPrice());
    }

    @Test
    void testMapToInvoice() {
        InvoiceDTO invoiceDTO = InvoiceDTO.builder()
                .id("INV2-QXWN-W3VH-Q8H7-XH8J")
                .status("PAID")
                .totalAmount(CostDTO.builder()
                        .value(new BigDecimal("100.00"))
                        .currency("USD")
                        .build())
                .merchantInfo(UserInfoDTO.builder()
                        .email("merchant@example.com")
                        .firstName("John")
                        .lastName("Doe")
                        .build())
                .billingInfo(UserInfoDTO.builder()
                        .email("customer@example.com")
                        .firstName("Jane")
                        .lastName("Doe")
                        .build())
                .items(List.of(InvoiceItemDTO.builder()
                        .name("Item 1")
                        .quantity(2)
                        .unitPrice(new BigDecimal("50.00"))
                        .build()))
                .build();

        Invoice invoice = adapter.mapToInvoice(invoiceDTO);

        assertNotNull(invoice);
        assertEquals("INV2-QXWN-W3VH-Q8H7-XH8J", invoice.getId());
        assertEquals("PAID", invoice.getStatus());
        assertEquals("100.00", invoice.getTotalAmount().getValue());
        assertEquals("USD", invoice.getTotalAmount().getCurrency());
        assertEquals("merchant@example.com", invoice.getMerchantInfo().getEmail());
        assertEquals("John", invoice.getMerchantInfo().getFirstName());
        assertEquals("Doe", invoice.getMerchantInfo().getLastName());
        assertEquals(1, invoice.getBillingInfo().size());
        assertEquals("customer@example.com", invoice.getBillingInfo().get(0).getEmail());
        assertEquals("Jane", invoice.getBillingInfo().get(0).getFirstName());
        assertEquals("Doe", invoice.getBillingInfo().get(0).getLastName());
        assertEquals(1, invoice.getItems().size());
        assertEquals("Item 1", invoice.getItems().get(0).getName());
        assertEquals(2, invoice.getItems().get(0).getQuantity());
        assertEquals("50.00", invoice.getItems().get(0).getUnitPrice().getValue());
        assertEquals("USD", invoice.getItems().get(0).getUnitPrice().getCurrency());
    }
}