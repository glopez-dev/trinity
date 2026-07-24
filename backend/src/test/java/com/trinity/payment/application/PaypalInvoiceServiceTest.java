package com.trinity.payment.application;

import com.trinity.payment.domain.model.Cost;
import com.trinity.payment.domain.model.Invoice;
import com.trinity.payment.domain.port.InvoicingGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The application service orchestrates the InvoicingGateway port and speaks the
 * domain Invoice only — the DTO boundary moved to the controller. The SDK
 * behaviour is covered by PaypalInvoiceGatewayAdapterTest.
 */
class PaypalInvoiceServiceTest {

    @Mock
    private InvoicingGateway invoicingGateway;

    private PaypalInvoiceService service;

    private static final String INVOICE_ID = "INV2-QXWN-W3VH-Q8H7-XH8J";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new PaypalInvoiceService(invoicingGateway);
    }

    @Test
    void createInvoice_delegatesToGateway() {
        Invoice request = Invoice.builder()
                .id("123").status("DRAFT")
                .totalAmount(Cost.builder().value(new BigDecimal("100.00")).currency("USD").build())
                .build();
        Invoice created = Invoice.builder()
                .id("123").status("CREATED")
                .totalAmount(Cost.builder().value(new BigDecimal("100.00")).currency("USD").build())
                .build();
        when(invoicingGateway.create(any(Invoice.class))).thenReturn(created);

        Invoice result = service.createInvoice(request);

        verify(invoicingGateway).create(request);
        assertThat(result.getStatus()).isEqualTo("CREATED");
        assertThat(result.getTotalAmount().getValue()).isEqualTo(new BigDecimal("100.00"));
    }

    @Test
    void sendInvoice_delegatesToGateway() {
        service.sendInvoice(INVOICE_ID);
        verify(invoicingGateway).send(INVOICE_ID);
    }

    @Test
    void getInvoice_delegatesToGateway() {
        Invoice domain = Invoice.builder().id("456").status("PAID").build();
        when(invoicingGateway.get(INVOICE_ID)).thenReturn(domain);

        Invoice result = service.getInvoice(INVOICE_ID);

        assertThat(result.getId()).isEqualTo("456");
        assertThat(result.getStatus()).isEqualTo("PAID");
        verify(invoicingGateway).get(INVOICE_ID);
    }

    @Test
    void getAllInvoices_delegatesToGateway() {
        Invoice a = Invoice.builder().id("1").status("DRAFT").build();
        Invoice b = Invoice.builder().id("2").status("PAID").build();
        when(invoicingGateway.getAll()).thenReturn(List.of(a, b));

        List<Invoice> result = service.getAllInvoices();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo("1");
        assertThat(result.get(1).getId()).isEqualTo("2");
    }

    @Test
    void updateInvoice_delegatesToGateway() {
        Invoice request = Invoice.builder().id("999").status("UPDATED").build();
        when(invoicingGateway.update(any(Invoice.class))).thenReturn(request);

        Invoice result = service.updateInvoice(request);

        verify(invoicingGateway).update(request);
        assertThat(result.getId()).isEqualTo("999");
    }

    @Test
    void deleteInvoice_delegatesToGateway() {
        service.deleteInvoice(INVOICE_ID);
        verify(invoicingGateway).delete(INVOICE_ID);
    }

    @Test
    void cancelInvoice_delegatesToGateway() {
        service.cancelInvoice(INVOICE_ID, "reason");
        verify(invoicingGateway).cancel(INVOICE_ID, "reason");
    }
}
