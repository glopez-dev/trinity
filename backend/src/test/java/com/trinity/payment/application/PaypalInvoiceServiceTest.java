package com.trinity.payment.application;

import com.trinity.payment.domain.model.Cost;
import com.trinity.payment.domain.model.Invoice;
import com.trinity.payment.domain.port.InvoicingGateway;
import com.trinity.payment.interfaces.rest.dto.CostDTO;
import com.trinity.payment.interfaces.rest.dto.InvoiceDTO;
import com.trinity.payment.interfaces.rest.mapper.InvoiceApiMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The application service orchestrates the InvoicingGateway port and owns the
 * DTO<->domain boundary. It uses the REAL {@link InvoiceApiMapper} (MapStruct)
 * so the conversion is exercised end to end; only the gateway is mocked. The
 * SDK behaviour is covered by PaypalInvoiceGatewayAdapterTest.
 */
class PaypalInvoiceServiceTest {

    @Mock
    private InvoicingGateway invoicingGateway;

    private final InvoiceApiMapper invoiceMapper = Mappers.getMapper(InvoiceApiMapper.class);

    private PaypalInvoiceService service;

    private static final String INVOICE_ID = "INV2-QXWN-W3VH-Q8H7-XH8J";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new PaypalInvoiceService(invoicingGateway, invoiceMapper);
    }

    @Test
    void createInvoice_convertsDtoToDomain_delegates_andConvertsBack() {
        InvoiceDTO request = InvoiceDTO.builder()
                .id("123").status("DRAFT")
                .totalAmount(CostDTO.builder().value(new BigDecimal("100.00")).currency("USD").build())
                .build();
        Invoice created = Invoice.builder()
                .id("123").status("CREATED")
                .totalAmount(Cost.builder().value(new BigDecimal("100.00")).currency("USD").build())
                .build();
        when(invoicingGateway.create(any(Invoice.class))).thenReturn(created);

        InvoiceDTO result = service.createInvoice(request);

        // the gateway was called with the domain conversion of the request
        ArgumentCaptor<Invoice> captor = ArgumentCaptor.forClass(Invoice.class);
        verify(invoicingGateway).create(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo("123");
        assertThat(captor.getValue().getStatus()).isEqualTo("DRAFT");
        assertThat(captor.getValue().getTotalAmount().getValue()).isEqualTo(new BigDecimal("100.00"));
        // the gateway result was converted back to a DTO
        assertThat(result.getId()).isEqualTo("123");
        assertThat(result.getStatus()).isEqualTo("CREATED");
        assertThat(result.getTotalAmount().getValue()).isEqualTo(new BigDecimal("100.00"));
        assertThat(result.getTotalAmount().getCurrency()).isEqualTo("USD");
    }

    @Test
    void sendInvoice_delegatesToGateway() {
        service.sendInvoice(INVOICE_ID);
        verify(invoicingGateway).send(INVOICE_ID);
    }

    @Test
    void getInvoiceDTO_delegates_andConvertsToDto() {
        Invoice domain = Invoice.builder().id("456").status("PAID").build();
        when(invoicingGateway.get(INVOICE_ID)).thenReturn(domain);

        InvoiceDTO result = service.getInvoiceDTO(INVOICE_ID);

        assertThat(result.getId()).isEqualTo("456");
        assertThat(result.getStatus()).isEqualTo("PAID");
        verify(invoicingGateway).get(INVOICE_ID);
    }

    @Test
    void getAllInvoices_delegates_andConvertsEach() {
        Invoice a = Invoice.builder().id("1").status("DRAFT").build();
        Invoice b = Invoice.builder().id("2").status("PAID").build();
        when(invoicingGateway.getAll()).thenReturn(List.of(a, b));

        List<InvoiceDTO> result = service.getAllInvoices();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo("1");
        assertThat(result.get(1).getId()).isEqualTo("2");
    }

    @Test
    void updateInvoice_convertsDtoToDomain_delegates_andConvertsBack() {
        InvoiceDTO request = InvoiceDTO.builder().id("999").status("UPDATED").build();
        Invoice updated = Invoice.builder().id("999").status("UPDATED").build();
        when(invoicingGateway.update(any(Invoice.class))).thenReturn(updated);

        InvoiceDTO result = service.updateInvoice(request);

        ArgumentCaptor<Invoice> captor = ArgumentCaptor.forClass(Invoice.class);
        verify(invoicingGateway).update(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo("999");
        assertThat(result.getId()).isEqualTo("999");
        assertThat(result.getStatus()).isEqualTo("UPDATED");
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
