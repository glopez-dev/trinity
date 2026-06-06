package com.trinity.user.infrastructure.persistence.entity;

import java.time.Instant;

import com.trinity.user.domain.model.EmployeeRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Anemic JPA entity for the {@code employee} table. Adds the role + employment
 * lifecycle columns. {@code role} is mapped ORDINAL (smallint), matching the
 * frozen {@code role smallint check(0..2)} column. No {@code @Builder.Default}.
 */
@Entity
@Table(name = "employee")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EmployeeJpaEntity extends AbstractUserJpaEntity {

    private Instant hireDate;

    private Instant terminationDate;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private EmployeeRole role;
}
