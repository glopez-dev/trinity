package com.trinity.payment.application;

import com.trinity.payment.domain.port.InvoicingGateway;
import com.trinity.payment.interfaces.rest.dto.InvoiceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Application service for PayPal invoicing. Orchestrates the invoicing use cases
 * through the {@link InvoicingGateway} port; the PayPal SDK lives only in the
 * adapter behind it.
 */
@Service
@RequiredArgsConstructor
public class PaypalInvoiceService {

    private final InvoicingGateway invoicingGateway;

    public InvoiceDTO createInvoice(InvoiceDTO request) {
        return invoicingGateway.create(request);
    }

    public void sendInvoice(String invoiceId) {
        invoicingGateway.send(invoiceId);
    }

    public InvoiceDTO getInvoiceDTO(String invoiceId) {
        return invoicingGateway.get(invoiceId);
    }

    public List<InvoiceDTO> getAllInvoices() {
        return invoicingGateway.getAll();
    }

    public InvoiceDTO updateInvoice(InvoiceDTO request) {
        return invoicingGateway.update(request);
    }

    public void deleteInvoice(String invoiceId) {
        invoicingGateway.delete(invoiceId);
    }

    public void cancelInvoice(String invoiceId, String reason) {
        invoicingGateway.cancel(invoiceId, reason);
    }
}
