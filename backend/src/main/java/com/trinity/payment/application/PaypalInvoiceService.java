package com.trinity.payment.application;

import com.trinity.payment.domain.model.Invoice;
import com.trinity.payment.domain.port.InvoicingGateway;
import com.trinity.payment.interfaces.rest.dto.InvoiceDTO;
import com.trinity.payment.interfaces.rest.mapper.InvoiceApiMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Application service for PayPal invoicing. Orchestrates the invoicing use cases
 * through the {@link InvoicingGateway} port; the PayPal SDK lives only in the
 * adapter behind it.
 *
 * <p>It owns the DTO &lt;-&gt; domain boundary: REST {@link InvoiceDTO}s coming
 * from the controller are converted to domain {@link Invoice}s via
 * {@link InvoiceApiMapper} before reaching the port, and back on the way out.
 */
@Service
@RequiredArgsConstructor
public class PaypalInvoiceService {

    private final InvoicingGateway invoicingGateway;
    private final InvoiceApiMapper invoiceMapper;

    public InvoiceDTO createInvoice(InvoiceDTO request) {
        Invoice created = invoicingGateway.create(invoiceMapper.toDomain(request));
        return invoiceMapper.toDTO(created);
    }

    public void sendInvoice(String invoiceId) {
        invoicingGateway.send(invoiceId);
    }

    public InvoiceDTO getInvoiceDTO(String invoiceId) {
        return invoiceMapper.toDTO(invoicingGateway.get(invoiceId));
    }

    public List<InvoiceDTO> getAllInvoices() {
        return invoicingGateway.getAll().stream()
                .map(invoiceMapper::toDTO)
                .toList();
    }

    public InvoiceDTO updateInvoice(InvoiceDTO request) {
        Invoice updated = invoicingGateway.update(invoiceMapper.toDomain(request));
        return invoiceMapper.toDTO(updated);
    }

    public void deleteInvoice(String invoiceId) {
        invoicingGateway.delete(invoiceId);
    }

    public void cancelInvoice(String invoiceId, String reason) {
        invoicingGateway.cancel(invoiceId, reason);
    }
}
