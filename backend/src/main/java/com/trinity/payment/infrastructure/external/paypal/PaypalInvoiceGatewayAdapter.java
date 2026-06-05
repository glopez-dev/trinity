package com.trinity.payment.infrastructure.external.paypal;

import com.paypal.api.payments.CancelNotification;
import com.paypal.api.payments.Invoice;
import com.paypal.api.payments.Invoices;
import com.paypal.base.rest.PayPalRESTException;
import com.trinity.common.domain.exception.DomainException;
import com.trinity.payment.domain.port.InvoicingGateway;
import com.trinity.payment.paypal.adapter.PaypalInvoiceAdapter;
import com.trinity.payment.paypal.config.PaypalConfig;
import com.trinity.payment.paypal.dto.InvoiceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * PayPal anti-corruption adapter for invoicing — the ONLY place com.paypal.* is
 * allowed to live. Translates between InvoiceDTO and the PayPal SDK, and maps
 * PayPalRESTException to a domain exception.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaypalInvoiceGatewayAdapter implements InvoicingGateway {

    private final PaypalConfig paypalConfig;
    private final PaypalInvoiceAdapter invoiceAdapter;

    @Override
    public InvoiceDTO create(InvoiceDTO request) {
        try {
            Invoice invoice = invoiceAdapter.mapToInvoice(request);
            invoice = invoice.create(paypalConfig.getAPIContext());
            return invoiceAdapter.mapToInvoiceDTO(invoice);
        } catch (PayPalRESTException e) {
            log.error("Failed to create PayPal invoice", e);
            throw new PayPalInvoiceException("Error creating invoice", e);
        }
    }

    @Override
    public void send(String invoiceId) {
        try {
            retrieve(invoiceId).send(paypalConfig.getAPIContext());
        } catch (PayPalRESTException e) {
            log.error("Failed to send invoice: {}", invoiceId, e);
            throw new PayPalInvoiceException("Error sending invoice", e);
        }
    }

    @Override
    public InvoiceDTO get(String invoiceId) {
        return invoiceAdapter.mapToInvoiceDTO(retrieve(invoiceId));
    }

    @Override
    public List<InvoiceDTO> getAll() {
        try {
            Invoices invoices = Invoice.getAll(paypalConfig.getAPIContext());
            if (invoices == null || invoices.getInvoices() == null) {
                return Collections.emptyList();
            }
            return invoices.getInvoices().stream()
                    .map(invoiceAdapter::mapToInvoiceDTO)
                    .toList();
        } catch (PayPalRESTException e) {
            log.error("Failed to get all invoices", e);
            throw new PayPalInvoiceException("Error getting all invoices", e);
        }
    }

    @Override
    public InvoiceDTO update(InvoiceDTO request) {
        try {
            Invoice invoice = invoiceAdapter.mapToInvoice(request);
            invoice = invoice.update(paypalConfig.getAPIContext());
            return invoiceAdapter.mapToInvoiceDTO(invoice);
        } catch (PayPalRESTException e) {
            log.error("Failed to update invoice: {}", request.getId(), e);
            throw new PayPalInvoiceException("Error updating invoice", e);
        }
    }

    @Override
    public void delete(String invoiceId) {
        try {
            retrieve(invoiceId).delete(paypalConfig.getAPIContext());
        } catch (PayPalRESTException e) {
            log.error("Failed to delete invoice: {}", invoiceId, e);
            throw new PayPalInvoiceException("Error deleting invoice", e);
        }
    }

    @Override
    public void cancel(String invoiceId, String reason) {
        try {
            CancelNotification cancelNotification = new CancelNotification()
                    .setSubject("Invoice Cancelled")
                    .setNote(reason);
            retrieve(invoiceId).cancel(paypalConfig.getAPIContext(), cancelNotification);
        } catch (PayPalRESTException e) {
            log.error("Failed to cancel invoice: {}", invoiceId, e);
            throw new PayPalInvoiceException("Error cancelling invoice", e);
        }
    }

    private Invoice retrieve(String invoiceId) {
        try {
            return Invoice.get(paypalConfig.getAPIContext(), invoiceId);
        } catch (PayPalRESTException e) {
            log.error("Failed to retrieve invoice: {}", invoiceId, e);
            throw new PayPalInvoiceException("Error retrieving invoice", e);
        }
    }

    static class PayPalInvoiceException extends DomainException {
        PayPalInvoiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
