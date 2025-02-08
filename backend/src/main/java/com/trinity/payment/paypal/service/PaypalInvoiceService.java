package com.trinity.payment.paypal.service;

import com.trinity.payment.paypal.adapter.PaypalInvoiceAdapter;
import com.trinity.payment.paypal.config.PaypalConfig;
import com.paypal.api.payments.*;
import com.paypal.base.rest.PayPalRESTException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

import com.trinity.payment.paypal.dto.InvoiceDTO;


@Slf4j
@Service
@RequiredArgsConstructor
public class PaypalInvoiceService {

    private final PaypalConfig paypalConfig;
    private final PaypalInvoiceAdapter invoiceAdapter;


    public InvoiceDTO createInvoice(InvoiceDTO request) {
        try {
            Invoice invoice = invoiceAdapter.mapToInvoice(request);
            invoice = invoice.create(paypalConfig.getAPIContext());
            return invoiceAdapter.mapToInvoiceDTO(invoice);
        } catch (PayPalRESTException e) {
            log.error("Failed to create PayPal invoice", e);
            throw new PayPalInvoiceException("Error creating invoice", e);
        }
    }

    public void sendInvoice(String invoiceId) {
        try {
            Invoice invoice = getInvoice(invoiceId);
            invoice.send(paypalConfig.getAPIContext());
        } catch (PayPalRESTException e) {
            log.error("Failed to send invoice: {}", invoiceId, e);
            throw new PayPalInvoiceException("Error sending invoice", e);
        }
    }

    public Invoice getInvoice(String invoiceId) {
        try {
            return Invoice.get(paypalConfig.getAPIContext(), invoiceId);
        } catch (PayPalRESTException e) {
            log.error("Failed to retrieve invoice: {}", invoiceId, e);
            throw new PayPalInvoiceException("Error retrieving invoice", e);
        }
    }

    public InvoiceDTO getInvoiceDTO(String invoiceId) {
        return invoiceAdapter.mapToInvoiceDTO(getInvoice(invoiceId));
    }

    public List<InvoiceDTO> getAllInvoices() {
        try {
            Invoices invoices =  Invoice.getAll(paypalConfig.getAPIContext());
            return invoices.getInvoices().stream()
                    .map(invoiceAdapter::mapToInvoiceDTO)
                    .toList();
        } catch (PayPalRESTException e) {
            log.error("Failed to get all invoices", e);
            throw new PayPalInvoiceException("Error getting all invoices", e);
        }
    }

    public InvoiceDTO updateInvoice(InvoiceDTO request) {
        try {
            Invoice invoice = invoiceAdapter.mapToInvoice(request);
            invoice = invoice.update(paypalConfig.getAPIContext());
            return invoiceAdapter.mapToInvoiceDTO(invoice);
        } catch (PayPalRESTException e) {
            log.error("Failed to update invoice: {}", request.getId(), e);
            throw new PayPalInvoiceException("Error updating invoice", e);
        }
    }

    public void deleteInvoice(String invoiceId) {
        try {
            Invoice invoice = getInvoice(invoiceId);
            invoice.delete(paypalConfig.getAPIContext());
        } catch (PayPalRESTException e) {
            log.error("Failed to delete invoice: {}", invoiceId, e);
            throw new PayPalInvoiceException("Error deleting invoice", e);
        }
    }

    public void cancelInvoice(String invoiceId, String reason) {
        try {
            Invoice invoice = getInvoice(invoiceId);
            CancelNotification cancelNotification = new CancelNotification()
                    .setSubject("Invoice Cancelled")
                    .setNote(reason);
            invoice.cancel(paypalConfig.getAPIContext(), cancelNotification);
        } catch (PayPalRESTException e) {
            log.error("Failed to cancel invoice: {}", invoiceId, e);
            throw new PayPalInvoiceException("Error cancelling invoice", e);
        }
    }


}

class PayPalInvoiceException extends RuntimeException {
    public PayPalInvoiceException(String message, Throwable cause) {
        super(message, cause);
    }
}