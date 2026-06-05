package com.trinity.payment.paypal.service;

import com.trinity.payment.domain.port.InvoicingGateway;
import com.trinity.payment.paypal.dto.InvoiceDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The application service is a thin orchestration over the InvoicingGateway port.
 * SDK behavior is covered by PaypalInvoiceGatewayAdapterTest.
 */
@ExtendWith(MockitoExtension.class)
class PaypalInvoiceServiceTest {

    @Mock
    private InvoicingGateway invoicingGateway;

    @InjectMocks
    private PaypalInvoiceService service;

    private static final String INVOICE_ID = "INV2-QXWN-W3VH-Q8H7-XH8J";

    @Test
    void createInvoice_delegatesToGateway() {
        InvoiceDTO request = new InvoiceDTO();
        InvoiceDTO created = new InvoiceDTO();
        when(invoicingGateway.create(request)).thenReturn(created);

        assertThat(service.createInvoice(request)).isSameAs(created);
        verify(invoicingGateway).create(request);
    }

    @Test
    void sendInvoice_delegatesToGateway() {
        service.sendInvoice(INVOICE_ID);
        verify(invoicingGateway).send(INVOICE_ID);
    }

    @Test
    void getInvoiceDTO_delegatesToGateway() {
        InvoiceDTO dto = new InvoiceDTO();
        when(invoicingGateway.get(INVOICE_ID)).thenReturn(dto);

        assertThat(service.getInvoiceDTO(INVOICE_ID)).isSameAs(dto);
    }

    @Test
    void getAllInvoices_delegatesToGateway() {
        List<InvoiceDTO> all = List.of(new InvoiceDTO());
        when(invoicingGateway.getAll()).thenReturn(all);

        assertThat(service.getAllInvoices()).isEqualTo(all);
    }

    @Test
    void updateInvoice_delegatesToGateway() {
        InvoiceDTO request = new InvoiceDTO();
        InvoiceDTO updated = new InvoiceDTO();
        when(invoicingGateway.update(request)).thenReturn(updated);

        assertThat(service.updateInvoice(request)).isSameAs(updated);
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
