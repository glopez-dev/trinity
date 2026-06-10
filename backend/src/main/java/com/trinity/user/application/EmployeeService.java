package com.trinity.user.application;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trinity.common.domain.exception.NotFoundException;
import com.trinity.user.application.command.CreateEmployeeCommand;
import com.trinity.user.application.command.UpdateEmployeeCommand;
import com.trinity.user.domain.model.UserStatus;
import com.trinity.user.domain.model.Employee;
import com.trinity.user.domain.port.EmployeeRepositoryPort;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeService {

    private static final String NOT_FOUND_MESSAGE = "Employee not found";
    private final EmployeeRepositoryPort employeeRepository;
    private final PasswordEncoder passwordEncoder;

    private Employee findById(UUID employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
    }

    @Transactional
    public Employee createEmployee(CreateEmployeeCommand command) {

        Employee newEmployee = Employee.builder()
            .email(command.email())
            .hashedPassword(passwordEncoder.encode(command.rawPassword()))
            .firstName(command.firstName())
            .lastName(command.lastName())
            .hireDate(Instant.now())
            .role(command.role())
            .build();

        return employeeRepository.save(newEmployee);
    }

    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Employee getEmployee(UUID employeeId) {
        return findById(employeeId);
    }

    @Transactional
    public Employee updateEmployee(UUID employeeId, UpdateEmployeeCommand command) {
        Employee employee = findById(employeeId);

        command.email().ifPresent(employee::setEmail);
        employee.rename(
                command.firstName().orElse(null),
                command.lastName().orElse(null));
        command.role().ifPresent(employee::changeRole);
        command.status().ifPresent(status -> applyStatus(employee, status));

        return employeeRepository.save(employee);
    }

    @Transactional
    public void deleteEmployee(UUID employeeId) {
        Employee employee = findById(employeeId);

        employeeRepository.delete(employee);
    }

    private void applyStatus(Employee employee, UserStatus status) {
        switch (status) {
            case ACTIVE -> employee.activate();
            case INACTIVE -> employee.deactivate();
            case LOCKED -> employee.lock();
            case EXPIRED -> employee.markExpired();
            case DELETED -> employee.markDeleted();
        }
    }

}
