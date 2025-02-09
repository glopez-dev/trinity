package com.trinity.user.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.trinity.user.dto.employee.CreateEmployeeDTO;
import com.trinity.user.dto.employee.ReadEmployeeDTO;
import com.trinity.user.dto.employee.UpdateEmployeeDTO;
import com.trinity.user.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/employee")
@AllArgsConstructor
@Tag(name = "Employee", description = "Employee management APIs")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService employeeService;

    @Operation(summary = "Create a new employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping()
    public ResponseEntity<ReadEmployeeDTO> createEmployee(@Valid @RequestBody CreateEmployeeDTO request) {
        logger.info("Creating employee");
        ReadEmployeeDTO body = employeeService.createEmployee(request);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Get all employees")
    @ApiResponse(responseCode = "200", description = "List of all employees retrieved")
    @GetMapping()
    public ResponseEntity<List<ReadEmployeeDTO>> getAllEmployees() {
        logger.info("Fetching all employees");
        List<ReadEmployeeDTO> body = employeeService.getAllEmployees();
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Get employee by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee found"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/{employeeId}")
    public ResponseEntity<ReadEmployeeDTO> getEmployee(@PathVariable UUID employeeId) {
        logger.info("Fetching employee with ID: {}", employeeId);
        ReadEmployeeDTO body = employeeService.getEmployee(employeeId);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Update an employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{employeeId}")
    public ResponseEntity<ReadEmployeeDTO> updateEmployee(
        @PathVariable UUID employeeId,
        @Valid @RequestBody UpdateEmployeeDTO request
    ) {
        logger.info("Updating employee with ID: {}", employeeId);
        ReadEmployeeDTO body = employeeService.updateEmployee(employeeId, request);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Delete an employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID employeeId) {
        logger.info("Deleting employee with ID: {}", employeeId);
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }
}