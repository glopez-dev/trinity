package com.trinity.payment.domain.port;

import com.trinity.payment.domain.model.Invoice;

import java.util.List;

/**
 * Outbound port for invoicing operations. Implemented by an infrastructure
 * adapter that confines the PayPal SDK; the application service depends only on
 * this contract, never on com.paypal.* types.
 *
 * <p>The port speaks purely in domain {@link Invoice} types. Conversion to/from
 * the REST DTOs happens in the application service via the API mapper, keeping
 * the domain free of any presentation-layer dependency.
 */
public interface InvoicingGateway {

    Invoice create(Invoice request);

    void send(String invoiceId);

    Invoice get(String invoiceId);

    List<Invoice> getAll();

    Invoice update(Invoice request);

    void delete(String invoiceId);

    void cancel(String invoiceId, String reason);
}
