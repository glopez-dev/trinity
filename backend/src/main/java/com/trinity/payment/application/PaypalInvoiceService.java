package com.trinity.payment.application;

import com.trinity.payment.domain.model.Invoice;
import com.trinity.payment.domain.port.InvoicingGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Application service for PayPal invoicing. Orchestrates the invoicing use cases
 * through the {@link InvoicingGateway} port; the PayPal SDK lives only in the
 * adapter behind it. Speaks the domain {@link Invoice} — the REST DTO boundary
 * belongs to the controller.
 */
@Service
@RequiredArgsConstructor
public class PaypalInvoiceService {

    private final InvoicingGateway invoicingGateway;

    public Invoice createInvoice(Invoice invoice) {
        return invoicingGateway.create(invoice);
    }

    public void sendInvoice(String invoiceId) {
        invoicingGateway.send(invoiceId);
    }

    public Invoice getInvoice(String invoiceId) {
        return invoicingGateway.get(invoiceId);
    }

    public List<Invoice> getAllInvoices() {
        return invoicingGateway.getAll();
    }

    public Invoice updateInvoice(Invoice invoice) {
        return invoicingGateway.update(invoice);
    }

    public void deleteInvoice(String invoiceId) {
        invoicingGateway.delete(invoiceId);
    }

    public void cancelInvoice(String invoiceId, String reason) {
        invoicingGateway.cancel(invoiceId, reason);
    }
}
