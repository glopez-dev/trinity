package com.trinity.payment.infrastructure.persistence.mapper;

import com.trinity.common.domain.vo.Money;
import com.trinity.payment.domain.model.Payment;
import com.trinity.payment.domain.model.PaymentProvider;
import com.trinity.payment.domain.model.PaymentStatus;
import com.trinity.payment.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.stereotype.Component;

/**
 * Hand-written mapper between the Payment aggregate and its JPA mirror, in the
 * same style as the cart and product persistence mappers.
 */
@Component
public class PaymentPersistenceMapper {

    public PaymentEntity toEntity(Payment payment) {
        return PaymentEntity.builder()
                .id(payment.getId())
                .amount(payment.getAmount().amount())
                .currency(payment.getAmount().currency())
                .provider(payment.getProvider().name())
                .status(payment.getStatus().name())
                .externalRef(payment.getExternalRef())
                .failureReason(payment.getFailureReason())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    public Payment toDomain(PaymentEntity entity) {
        return Payment.rehydrate(
                entity.getId(),
                Money.of(entity.getAmount(), entity.getCurrency()),
                PaymentProvider.valueOf(entity.getProvider()),
                entity.getCreatedAt(),
                PaymentStatus.valueOf(entity.getStatus()),
                entity.getExternalRef(),
                entity.getFailureReason());
    }
}
