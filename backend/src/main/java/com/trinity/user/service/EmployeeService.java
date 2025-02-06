package com.trinity.user.service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trinity.user.dto.employee.CreateEmployeeDTO;
import com.trinity.user.dto.employee.ReadEmployeeDTO;
import com.trinity.user.dto.employee.UpdateEmployeeDTO;
import com.trinity.user.model.Employee;
import com.trinity.user.repository.EmployeeRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeService {

    private static final String NOT_FOUND_MESSAGE = "Employee not found";
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    private Employee findById(UUID employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException(NOT_FOUND_MESSAGE));
    }

    public ReadEmployeeDTO createEmployee(CreateEmployeeDTO employeeDTO) {

        Employee newEmployee = Employee.builder()
            .email(employeeDTO.getEmail())
            .hashedPassword(passwordEncoder.encode(employeeDTO.getPassword()))
            .firstName(employeeDTO.getFirstName())
            .lastName(employeeDTO.getLastName())
            .hireDate(Instant.now())
            .role(employeeDTO.getRole())
            .build(); 

        newEmployee = employeeRepository.save(newEmployee);

        return new ReadEmployeeDTO(newEmployee);
    }

    public List<ReadEmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        if (employees.isEmpty()) {
            return Collections.emptyList();
        }

        return employees.stream()
            .map(ReadEmployeeDTO::new)
            .toList();
    }

    public ReadEmployeeDTO getEmployee(UUID employeeId) {
        Employee employee = findById(employeeId);
        return new ReadEmployeeDTO(employee);
    }

    public ReadEmployeeDTO updateEmployee(UUID employeeId, UpdateEmployeeDTO request) {
        Employee employee = findById(employeeId);

        employee.setEmail(request.getEmail().orElse(employee.getEmail()));
        employee.setFirstName(request.getFirstName().orElse(employee.getFirstName()));
        employee.setLastName(request.getLastName().orElse(employee.getLastName()));
        employee.setRole(request.getRole().orElse(employee.getRole()));
        employee.setStatus(request.getStatus().orElse(employee.getStatus()));
        employee.setUpdatedAt(Instant.now());

        Employee updatedEmployee = employeeRepository.save(employee);

        return new ReadEmployeeDTO(updatedEmployee);
    }

    public void deleteEmployee(UUID employeeId) {
        Employee employee = findById(employeeId);

        employeeRepository.delete(employee);
    }

}
