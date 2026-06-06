package com.trinity.payment.domain.port;

import com.trinity.payment.interfaces.rest.dto.InvoiceDTO;

import java.util.List;

/**
 * Outbound port for invoicing operations. Implemented by an infrastructure
 * adapter that confines the PayPal SDK; the application service depends only on
 * this contract, never on com.paypal.* types.
 */
public interface InvoicingGateway {

    InvoiceDTO create(InvoiceDTO request);

    void send(String invoiceId);

    InvoiceDTO get(String invoiceId);

    List<InvoiceDTO> getAll();

    InvoiceDTO update(InvoiceDTO request);

    void delete(String invoiceId);

    void cancel(String invoiceId, String reason);
}
