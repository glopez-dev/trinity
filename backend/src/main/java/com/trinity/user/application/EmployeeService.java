package com.trinity.user.application;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trinity.common.domain.exception.NotFoundException;
import com.trinity.user.domain.model.UserStatus;
import com.trinity.user.dto.employee.CreateEmployeeRequest;
import com.trinity.user.dto.employee.EmployeeResponse;
import com.trinity.user.dto.employee.UpdateEmployeeRequest;
import com.trinity.user.interfaces.rest.mapper.EmployeeApiMapper;
import com.trinity.user.domain.model.Employee;
import com.trinity.user.domain.port.EmployeeRepositoryPort;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeService {

    private static final String NOT_FOUND_MESSAGE = "Employee not found";
    private final EmployeeRepositoryPort employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeApiMapper employeeApiMapper;

    private Employee findById(UUID employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE));
    }

    @Transactional
    public EmployeeResponse createEmployee(CreateEmployeeRequest employeeDTO) {

        Employee newEmployee = Employee.builder()
            .email(employeeDTO.getEmail())
            .hashedPassword(passwordEncoder.encode(employeeDTO.getPassword()))
            .firstName(employeeDTO.getFirstName())
            .lastName(employeeDTO.getLastName())
            .hireDate(Instant.now())
            .role(employeeDTO.getRole())
            .build();

        newEmployee = employeeRepository.save(newEmployee);

        return employeeApiMapper.toResponse(newEmployee);
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        if (employees.isEmpty()) {
            return Collections.emptyList();
        }

        return employees.stream()
            .map(employeeApiMapper::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public EmployeeResponse getEmployee(UUID employeeId) {
        Employee employee = findById(employeeId);
        return employeeApiMapper.toResponse(employee);
    }

    @Transactional
    public EmployeeResponse updateEmployee(UUID employeeId, UpdateEmployeeRequest request) {
        Employee employee = findById(employeeId);

        request.getEmail().ifPresent(employee::setEmail);
        employee.rename(
                request.getFirstName().orElse(null),
                request.getLastName().orElse(null));
        request.getRole().ifPresent(employee::changeRole);
        request.getStatus().ifPresent(status -> applyStatus(employee, status));

        Employee updatedEmployee = employeeRepository.save(employee);

        return employeeApiMapper.toResponse(updatedEmployee);
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
