package com.devops.backend.user.infrastructure.persistence;

import com.devops.backend.user.domain.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeJpaRepository extends JpaRepository<Employee, UUID> {
    Optional<Employee> findByEmail(String email);
}
