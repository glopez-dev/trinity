package com.trinity.user.controller;

import java.util.List;
import java.util.UUID;

import com.trinity.user.dto.customer.CreateCustomerRequest;
import com.trinity.user.dto.customer.CustomerResponse;
import com.trinity.user.dto.customer.UpdateCustomerRequest;
import com.trinity.user.application.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Customer management APIs")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerService customerService;

    @Operation(summary = "Create a new customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Customer created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CreateCustomerRequest customerCreateDTO) {
        logger.info("Creating customer");
        return new ResponseEntity<>(customerService.createCustomer(customerCreateDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Get customer by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer found"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable UUID customerId) {
        logger.info("Fetching customer with ID: {}", customerId);
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }

    @Operation(summary = "Get customer by email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer found"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerResponse> getCustomerByEmail(@PathVariable String email) {
        logger.info("Fetching customer with email: {}", email);
        return ResponseEntity.ok(customerService.getCustomerByEmail(email));
    }

    @Operation(summary = "Get all customers")
    @ApiResponse(responseCode = "200", description = "List of all customers retrieved")
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        logger.info("Fetching all customers");
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @Operation(summary = "Update a customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
        @ApiResponse(responseCode = "404", description = "Customer not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCustomerRequest customerUpdateDTO) {
        logger.info("Updating customer with ID: {}", id);
        return ResponseEntity.ok(customerService.updateCustomer(id, customerUpdateDTO));
    }

    @Operation(summary = "Delete a customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        logger.info("Deleting customer with ID: {}", id);
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Activate a customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer activated successfully"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PostMapping("/{id}/activate")
    public ResponseEntity<CustomerResponse> activateCustomer(@PathVariable UUID id) {
        return ResponseEntity.ok(customerService.activateCustomer(id));
    }
}
