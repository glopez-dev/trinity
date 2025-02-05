package com.trinity.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trinity.user.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Optional<Employee> findByEmail(String email);

}
