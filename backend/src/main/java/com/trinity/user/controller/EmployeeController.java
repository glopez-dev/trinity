package com.trinity.user.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trinity.user.dto.employee.CreateEmployeeDTO;
import com.trinity.user.dto.employee.ReadEmployeeDTO;
import com.trinity.user.dto.employee.UpdateEmployeeDTO;
import com.trinity.user.service.EmployeeService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/employee")
@AllArgsConstructor
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService employeeService;

    @PostMapping()
    public ResponseEntity<ReadEmployeeDTO> createEmployee(@Valid @RequestBody CreateEmployeeDTO request) {
        logger.info("Creating employee");
        ReadEmployeeDTO body = employeeService.createEmployee(request);
        return ResponseEntity.ok(body);
    }

    @GetMapping()
    public ResponseEntity<List<ReadEmployeeDTO>> getAllEmployees() {
        logger.info("Fetching all employees");
        List<ReadEmployeeDTO> body = employeeService.getAllEmployees();
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<ReadEmployeeDTO> getEmployee(@PathVariable UUID employeeId) {
        logger.info("Fetching employee with ID: {}", employeeId);
        ReadEmployeeDTO body = employeeService.getEmployee(employeeId);
        return ResponseEntity.ok(body);
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<ReadEmployeeDTO> updateEmployee(
        @PathVariable UUID employeeId,
        @Valid @RequestBody UpdateEmployeeDTO request
    ) {
        logger.info("Updating employee with ID: {}", employeeId);
        ReadEmployeeDTO body = employeeService.updateEmployee(employeeId, request);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID employeeId) {
        logger.info("Deleting employee with ID: {}", employeeId);
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }
}