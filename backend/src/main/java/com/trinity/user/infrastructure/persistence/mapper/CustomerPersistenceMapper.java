package com.trinity.user.infrastructure.persistence.mapper;

import com.trinity.user.domain.model.Customer;
import com.trinity.user.infrastructure.persistence.entity.CustomerJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Hand-written translation between the {@link Customer} domain POJO and its anemic
 * JPA mirror. Lives on the persistence side of the mapping boundary.
 *
 * <p>Ownership: {@code toEntity} copies {@code id} (null on first insert →
 * {@code @GeneratedValue} fills it) and {@code version}, but never the
 * Hibernate-owned {@code createdAt}/{@code updatedAt}. {@code toDomain} copies the
 * full round-trip (id + version + timestamps — id feeds the JWT, version feeds
 * optimistic locking). {@code updateEntity} syncs mutable fields in-place and
 * never touches {@code id}/{@code version}/{@code createdAt}/{@code updatedAt}.
 */
@Component
public class CustomerPersistenceMapper {

    public CustomerJpaEntity toEntity(Customer d) {
        if (d == null) {
            return null;
        }
        return CustomerJpaEntity.builder()
            .id(d.getId())
            .email(d.getEmail())
            .hashedPassword(d.getHashedPassword())
            .firstName(d.getFirstName())
            .lastName(d.getLastName())
            .type(d.getType())
            .lastLoginAt(d.getLastLoginAt())
            .status(d.getStatus())
            .version(d.getVersion())
            .stripeUserId(d.getStripeUserId())
            .tokenExpiresAt(d.getTokenExpiresAt())
            .stripeAccessToken(d.getStripeAccessToken())
            .stripeRefreshToken(d.getStripeRefreshToken())
            .build();
    }

    public Customer toDomain(CustomerJpaEntity e) {
        if (e == null) {
            return null;
        }
        return Customer.builder()
            .id(e.getId())
            .email(e.getEmail())
            .hashedPassword(e.getHashedPassword())
            .firstName(e.getFirstName())
            .lastName(e.getLastName())
            .type(e.getType())
            .lastLoginAt(e.getLastLoginAt())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .version(e.getVersion())
            .stripeUserId(e.getStripeUserId())
            .tokenExpiresAt(e.getTokenExpiresAt())
            .stripeAccessToken(e.getStripeAccessToken())
            .stripeRefreshToken(e.getStripeRefreshToken())
            .build();
    }

    /** In-place sync of a managed entity (update path); never touches id/version/timestamps. */
    public void updateEntity(CustomerJpaEntity target, Customer source) {
        target.setEmail(source.getEmail());
        target.setHashedPassword(source.getHashedPassword());
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setType(source.getType());
        target.setLastLoginAt(source.getLastLoginAt());
        target.setStatus(source.getStatus());
        target.setStripeUserId(source.getStripeUserId());
        target.setTokenExpiresAt(source.getTokenExpiresAt());
        target.setStripeAccessToken(source.getStripeAccessToken());
        target.setStripeRefreshToken(source.getStripeRefreshToken());
    }
}
