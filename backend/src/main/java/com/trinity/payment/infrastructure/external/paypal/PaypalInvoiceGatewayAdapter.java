package com.trinity.payment.infrastructure.external.paypal;

import com.paypal.api.payments.CancelNotification;
import com.paypal.api.payments.Invoice;
import com.paypal.api.payments.Invoices;
import com.paypal.base.rest.PayPalRESTException;
import com.trinity.common.domain.exception.DomainException;
import com.trinity.payment.domain.port.InvoicingGateway;
import com.trinity.payment.infrastructure.config.PaypalConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * PayPal anti-corruption adapter for invoicing — the ONLY place com.paypal.* is
 * allowed to live. Translates between the domain {@link com.trinity.payment.domain.model.Invoice}
 * and the PayPal SDK (via {@link PaypalInvoiceAdapter}), and maps
 * {@link PayPalRESTException} to a domain exception.
 *
 * <p>The SDK invoice type ({@link Invoice}) is imported; the domain invoice type
 * is fully qualified to disambiguate the two.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaypalInvoiceGatewayAdapter implements InvoicingGateway {

    private final PaypalConfig paypalConfig;
    private final PaypalInvoiceAdapter invoiceAdapter;

    @Override
    public com.trinity.payment.domain.model.Invoice create(com.trinity.payment.domain.model.Invoice request) {
        try {
            Invoice invoice = invoiceAdapter.mapToSdkInvoice(request);
            invoice = invoice.create(paypalConfig.getAPIContext());
            return invoiceAdapter.mapToInvoice(invoice);
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
    public com.trinity.payment.domain.model.Invoice get(String invoiceId) {
        return invoiceAdapter.mapToInvoice(retrieve(invoiceId));
    }

    @Override
    public List<com.trinity.payment.domain.model.Invoice> getAll() {
        try {
            Invoices invoices = Invoice.getAll(paypalConfig.getAPIContext());
            if (invoices == null || invoices.getInvoices() == null) {
                return Collections.emptyList();
            }
            return invoices.getInvoices().stream()
                    .map(invoiceAdapter::mapToInvoice)
                    .toList();
        } catch (PayPalRESTException e) {
            log.error("Failed to get all invoices", e);
            throw new PayPalInvoiceException("Error getting all invoices", e);
        }
    }

    @Override
    public com.trinity.payment.domain.model.Invoice update(com.trinity.payment.domain.model.Invoice request) {
        try {
            Invoice invoice = invoiceAdapter.mapToSdkInvoice(request);
            invoice = invoice.update(paypalConfig.getAPIContext());
            return invoiceAdapter.mapToInvoice(invoice);
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
