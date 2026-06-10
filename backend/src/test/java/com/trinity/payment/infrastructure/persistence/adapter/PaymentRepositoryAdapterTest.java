package com.trinity.payment.infrastructure.persistence.adapter;

import com.trinity.common.domain.vo.Money;
import com.trinity.payment.domain.model.Payment;
import com.trinity.payment.domain.model.PaymentProvider;
import com.trinity.payment.domain.model.PaymentStatus;
import com.trinity.payment.infrastructure.persistence.mapper.PaymentPersistenceMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Round-trips the Payment aggregate through the real schema (Flyway V2 on H2):
 * proves the mapper restores every field and that the text-stored enums satisfy
 * the CHECK constraints for each status.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import({PaymentRepositoryAdapter.class, PaymentPersistenceMapper.class})
class PaymentRepositoryAdapterTest {

    @Autowired
    private PaymentRepositoryAdapter adapter;

    @Test
    void saveAndReload_restoresThePendingAggregate() {
        Payment payment = Payment.initiate(Money.of(new BigDecimal("25.00"), "USD"), PaymentProvider.STRIPE);

        adapter.save(payment);
        Optional<Payment> reloaded = adapter.findById(payment.getId());

        assertThat(reloaded).isPresent();
        assertThat(reloaded.get().getAmount()).isEqualTo(Money.of(new BigDecimal("25.00"), "USD"));
        assertThat(reloaded.get().getProvider()).isEqualTo(PaymentProvider.STRIPE);
        assertThat(reloaded.get().getStatus()).isEqualTo(PaymentStatus.PENDING);
        assertThat(reloaded.get().getCreatedAt()).isEqualTo(payment.getCreatedAt());
    }

    @Test
    void saveAndReload_coversEveryStatusAcceptedByTheCheckConstraint() {
        for (PaymentStatus status : PaymentStatus.values()) {
            Payment payment = Payment.rehydrate(
                    java.util.UUID.randomUUID(),
                    Money.of(new BigDecimal("9.99"), "EUR"),
                    PaymentProvider.PAYPAL,
                    java.time.Instant.now(),
                    status,
                    status == PaymentStatus.PENDING ? null : "ref-" + status,
                    status == PaymentStatus.FAILED ? "declined" : null);

            adapter.save(payment);
            Optional<Payment> reloaded = adapter.findById(payment.getId());

            assertThat(reloaded).isPresent();
            assertThat(reloaded.get().getStatus()).isEqualTo(status);
            assertThat(reloaded.get().getExternalRef()).isEqualTo(payment.getExternalRef());
            assertThat(reloaded.get().getFailureReason()).isEqualTo(payment.getFailureReason());
        }
    }

    @Test
    void save_updatesAnExistingRow() {
        Payment payment = Payment.initiate(Money.of(new BigDecimal("30.00"), "USD"), PaymentProvider.STRIPE);
        adapter.save(payment);

        payment.markAuthorized("pi_42");
        payment.markSucceeded();
        adapter.save(payment);

        Optional<Payment> reloaded = adapter.findById(payment.getId());
        assertThat(reloaded).isPresent();
        assertThat(reloaded.get().getStatus()).isEqualTo(PaymentStatus.SUCCEEDED);
        assertThat(reloaded.get().getExternalRef()).isEqualTo("pi_42");
    }
}
