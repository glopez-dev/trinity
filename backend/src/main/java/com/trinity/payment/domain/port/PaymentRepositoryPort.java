package com.trinity.payment.domain.port;

import com.trinity.payment.domain.model.Payment;

import java.util.Optional;
import java.util.UUID;

/**
 * Persistence port for the Payment aggregate: the local source of truth that
 * makes reconciliation against the provider possible at all.
 */
public interface PaymentRepositoryPort {

    Payment save(Payment payment);

    Optional<Payment> findById(UUID id);
}
