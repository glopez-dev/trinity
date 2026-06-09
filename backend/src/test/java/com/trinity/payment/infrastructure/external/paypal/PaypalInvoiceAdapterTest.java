package com.trinity.payment.infrastructure.external.paypal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.paypal.api.payments.BillingInfo;
import com.paypal.api.payments.Currency;
import com.paypal.api.payments.MerchantInfo;
import com.trinity.payment.domain.model.Cost;
import com.trinity.payment.domain.model.Invoice;
import com.trinity.payment.domain.model.InvoiceItem;
import com.trinity.payment.domain.model.Party;

/**
 * Maps the domain {@link Invoice} aggregate to/from the PayPal SDK. SDK invoice
 * types are fully qualified to disambiguate them from the domain ones. Every
 * mapping quirk asserted here is the exact behaviour of the previous DTO-based
 * mapper (defaults, no-scale BigDecimal, hard-coded USD on item price, billing
 * single&lt;-&gt;list, null-safe items).
 */
class PaypalInvoiceAdapterTest {

    private PaypalInvoiceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new PaypalInvoiceAdapter();
    }

    // ===== SDK -> domain =====

    @Test
    void testMapToInvoice_fromSdk() {
        com.paypal.api.payments.Invoice invoice = new com.paypal.api.payments.Invoice();
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

        com.paypal.api.payments.InvoiceItem item = new com.paypal.api.payments.InvoiceItem();
        item.setName("Item 1");
        item.setQuantity(2);
        Currency unitPrice = new Currency();
        unitPrice.setValue("50.00");
        unitPrice.setCurrency("USD");
        item.setUnitPrice(unitPrice);
        invoice.setItems(List.of(item));

        Invoice domain = adapter.mapToInvoice(invoice);

        assertNotNull(domain);
        assertEquals("INV2-QXWN-W3VH-Q8H7-XH8J", domain.getId());
        assertEquals("PAID", domain.getStatus());
        assertEquals(new BigDecimal("100.00"), domain.getTotalAmount().getValue());
        assertEquals("USD", domain.getTotalAmount().getCurrency());
        assertEquals("merchant@example.com", domain.getMerchantInfo().getEmail());
        assertEquals("John", domain.getMerchantInfo().getFirstName());
        assertEquals("Doe", domain.getMerchantInfo().getLastName());
        // QUIRK: SDK List<BillingInfo> -> single domain Party via findFirst()
        assertEquals("customer@example.com", domain.getBillingInfo().getEmail());
        assertEquals("Jane", domain.getBillingInfo().getFirstName());
        assertEquals("Doe", domain.getBillingInfo().getLastName());
        assertEquals(1, domain.getItems().size());
        assertEquals("Item 1", domain.getItems().get(0).getName());
        assertEquals(2, domain.getItems().get(0).getQuantity());
        // QUIRK: item unit price read as bare BigDecimal
        assertEquals(new BigDecimal("50.00"), domain.getItems().get(0).getUnitPrice());
    }

    @Test
    void testMapToInvoice_nullTotalAmount_defaultsToZeroUsd() {
        // QUIRK: null SDK Currency -> Cost(BigDecimal.ZERO, "USD")
        com.paypal.api.payments.Invoice invoice = new com.paypal.api.payments.Invoice();
        invoice.setTotalAmount(null);

        Invoice domain = adapter.mapToInvoice(invoice);

        assertEquals(BigDecimal.ZERO, domain.getTotalAmount().getValue());
        assertEquals("USD", domain.getTotalAmount().getCurrency());
    }

    @Test
    void testMapToInvoice_nullBillingList_returnsNullParty() {
        // QUIRK: null/empty SDK billing list -> null domain Party
        com.paypal.api.payments.Invoice invoice = new com.paypal.api.payments.Invoice();
        invoice.setBillingInfo(null);

        Invoice domain = adapter.mapToInvoice(invoice);

        assertEquals(null, domain.getBillingInfo());
    }

    // ===== domain -> SDK =====

    @Test
    void testMapToSdkInvoice() {
        Invoice domain = Invoice.builder()
                .id("INV2-QXWN-W3VH-Q8H7-XH8J")
                .status("PAID")
                .totalAmount(Cost.builder().value(new BigDecimal("100.00")).currency("USD").build())
                .merchantInfo(Party.builder()
                        .email("merchant@example.com").firstName("John").lastName("Doe").build())
                .billingInfo(Party.builder()
                        .email("customer@example.com").firstName("Jane").lastName("Doe").build())
                .items(List.of(InvoiceItem.builder()
                        .name("Item 1").quantity(2).unitPrice(new BigDecimal("50.00")).build()))
                .build();

        com.paypal.api.payments.Invoice invoice = adapter.mapToSdkInvoice(domain);

        assertNotNull(invoice);
        assertEquals("INV2-QXWN-W3VH-Q8H7-XH8J", invoice.getId());
        assertEquals("PAID", invoice.getStatus());
        assertEquals("100.00", invoice.getTotalAmount().getValue());
        assertEquals("USD", invoice.getTotalAmount().getCurrency());
        assertEquals("merchant@example.com", invoice.getMerchantInfo().getEmail());
        assertEquals("John", invoice.getMerchantInfo().getFirstName());
        assertEquals("Doe", invoice.getMerchantInfo().getLastName());
        // QUIRK: single domain Party -> SDK List.of(single)
        assertEquals(1, invoice.getBillingInfo().size());
        assertEquals("customer@example.com", invoice.getBillingInfo().get(0).getEmail());
        assertEquals("Jane", invoice.getBillingInfo().get(0).getFirstName());
        assertEquals("Doe", invoice.getBillingInfo().get(0).getLastName());
        assertEquals(1, invoice.getItems().size());
        assertEquals("Item 1", invoice.getItems().get(0).getName());
        assertEquals(2, invoice.getItems().get(0).getQuantity());
        // QUIRK: bare BigDecimal unit price wrapped as Currency with hard-coded "USD"
        assertEquals("50.00", invoice.getItems().get(0).getUnitPrice().getValue());
        assertEquals("USD", invoice.getItems().get(0).getUnitPrice().getCurrency());
    }

    @Test
    void testMapToSdkInvoice_nullTotalAmount_defaultsToZeroUsd() {
        // QUIRK: null domain Cost -> SDK Currency("0", "USD").
        // A billingInfo is supplied because the domain->SDK mapping wraps it in
        // List.of(...), which (by preserved design) rejects a null party.
        Invoice domain = Invoice.builder()
                .id("INV-NULL").status("DRAFT").totalAmount(null)
                .billingInfo(Party.builder().email("b@example.com").build())
                .build();

        com.paypal.api.payments.Invoice invoice = adapter.mapToSdkInvoice(domain);

        assertEquals("0", invoice.getTotalAmount().getValue());
        assertEquals("USD", invoice.getTotalAmount().getCurrency());
    }

    @Test
    void testMapToSdkInvoice_noItems_becomesEmptyList() {
        // The @Singular builder yields an empty (never null) item list when no
        // item is added; the SDK mapping keeps it an empty list, not null.
        Invoice domain = Invoice.builder()
                .id("INV-NOITEMS").status("DRAFT")
                .billingInfo(Party.builder().email("b@example.com").build())
                .build();

        com.paypal.api.payments.Invoice invoice = adapter.mapToSdkInvoice(domain);

        assertNotNull(invoice.getItems());
        assertEquals(0, invoice.getItems().size());
    }

    // ===== full round-trip (domain -> SDK -> domain), preserves every quirk =====

    @Test
    void roundTrip_domainToSdkToDomain_preservesItemUnitPriceAndCurrency() {
        Invoice original = Invoice.builder()
                .id("INV-RT").status("PAID")
                .totalAmount(Cost.builder().value(new BigDecimal("12.34")).currency("EUR").build())
                .merchantInfo(Party.builder().email("m@example.com").firstName("M").lastName("One").build())
                .billingInfo(Party.builder().email("b@example.com").firstName("B").lastName("Two").build())
                .items(List.of(InvoiceItem.builder()
                        .name("Widget").quantity(3).unitPrice(new BigDecimal("50.00")).build()))
                .build();

        Invoice back = adapter.mapToInvoice(adapter.mapToSdkInvoice(original));

        assertEquals(original.getId(), back.getId());
        assertEquals(original.getStatus(), back.getStatus());
        assertEquals(new BigDecimal("12.34"), back.getTotalAmount().getValue());
        assertEquals("EUR", back.getTotalAmount().getCurrency());
        assertEquals("m@example.com", back.getMerchantInfo().getEmail());
        assertEquals("b@example.com", back.getBillingInfo().getEmail());
        assertEquals(1, back.getItems().size());
        assertEquals("Widget", back.getItems().get(0).getName());
        assertEquals(3, back.getItems().get(0).getQuantity());
        // item unit price survives the Cost("USD") wrap/unwrap as a bare BigDecimal
        assertEquals(new BigDecimal("50.00"), back.getItems().get(0).getUnitPrice());
    }
}
