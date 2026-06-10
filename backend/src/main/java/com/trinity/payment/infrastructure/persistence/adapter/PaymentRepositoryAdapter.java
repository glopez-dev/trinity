package com.trinity.payment.infrastructure.persistence.adapter;

import com.trinity.payment.domain.model.Payment;
import com.trinity.payment.domain.port.PaymentRepositoryPort;
import com.trinity.payment.infrastructure.persistence.mapper.PaymentPersistenceMapper;
import com.trinity.payment.infrastructure.persistence.repository.JpaPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA implementation of {@link PaymentRepositoryPort}. Each save is its own
 * short transaction: the service deliberately keeps gateway HTTP calls outside
 * any transaction.
 */
@Component
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final JpaPaymentRepository jpaPaymentRepository;
    private final PaymentPersistenceMapper paymentPersistenceMapper;

    @Override
    @Transactional
    public Payment save(Payment payment) {
        jpaPaymentRepository.save(paymentPersistenceMapper.toEntity(payment));
        return payment;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> findById(UUID id) {
        return jpaPaymentRepository.findById(id)
                .map(paymentPersistenceMapper::toDomain);
    }
}
