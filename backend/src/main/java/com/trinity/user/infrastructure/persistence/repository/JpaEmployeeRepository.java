package com.trinity.user.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trinity.user.infrastructure.persistence.entity.EmployeeJpaEntity;

public interface JpaEmployeeRepository extends JpaRepository<EmployeeJpaEntity, UUID> {

    Optional<EmployeeJpaEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
