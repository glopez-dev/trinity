package com.trinity.user.infrastructure.persistence.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Anemic JPA entity for the {@code customer} table. Inherits the common columns
 * from {@link AbstractUserJpaEntity}; the {@code type} column is owned solely by
 * the superclass (single STRING column), so it is NOT redeclared here.
 */
@Entity
@Table(name = "customer")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CustomerJpaEntity extends AbstractUserJpaEntity {

    @Column(unique = true)
    private String stripeUserId;

    @Column
    private Instant tokenExpiresAt;

    @Column
    private String stripeAccessToken;

    @Column
    private String stripeRefreshToken;
}
