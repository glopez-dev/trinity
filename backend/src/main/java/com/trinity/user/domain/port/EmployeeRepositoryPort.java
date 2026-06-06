package com.trinity.user.domain.port;

import com.trinity.user.domain.model.Employee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Outbound persistence port for the employee aggregate. Speaks domain types only.
 */
public interface EmployeeRepositoryPort {

    Employee save(Employee employee);

    Optional<Employee> findById(UUID id);

    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Employee> findAll();

    void delete(Employee employee);
}
