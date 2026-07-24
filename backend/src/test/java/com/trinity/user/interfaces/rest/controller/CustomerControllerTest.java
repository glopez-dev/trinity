package com.trinity.user.interfaces.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.trinity.user.application.CustomerService;
import com.trinity.user.application.command.CreateCustomerCommand;
import com.trinity.user.application.command.UpdateCustomerCommand;
import com.trinity.user.domain.model.Customer;
import com.trinity.user.domain.model.UserStatus;
import com.trinity.user.interfaces.rest.dto.CreateCustomerRequest;
import com.trinity.user.interfaces.rest.dto.CustomerResponse;
import com.trinity.user.interfaces.rest.dto.UpdateCustomerRequest;
import com.trinity.user.interfaces.rest.mapper.CustomerApiMapper;

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Spy
    private CustomerApiMapper customerApiMapper = Mappers.getMapper(CustomerApiMapper.class);

    @InjectMocks
    private CustomerController customerController;

    private UUID customerId;
    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerId = UUID.randomUUID();
        customer = Customer.builder()
                .id(customerId)
                .email("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .status(UserStatus.ACTIVE)
                .build();
    }

    @Test
    void createCustomer_ReturnsCreated() {
        CreateCustomerRequest request = CreateCustomerRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .build();
        when(customerService.createCustomer(any(CreateCustomerCommand.class))).thenReturn(customer);

        ResponseEntity<CustomerResponse> response = customerController.createCustomer(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customerId, response.getBody().getId());
        assertEquals("john.doe@example.com", response.getBody().getEmail());
        verify(customerService).createCustomer(any(CreateCustomerCommand.class));
    }

    @Test
    void getCustomerById_ReturnsOk() {
        when(customerService.getCustomerById(customerId)).thenReturn(customer);

        ResponseEntity<CustomerResponse> response = customerController.getCustomerById(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customerId, response.getBody().getId());
        verify(customerService).getCustomerById(customerId);
    }

    @Test
    void getCustomerByEmail_ReturnsOk() {
        String email = "john.doe@example.com";
        when(customerService.getCustomerByEmail(email)).thenReturn(customer);

        ResponseEntity<CustomerResponse> response = customerController.getCustomerByEmail(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(email, response.getBody().getEmail());
        verify(customerService).getCustomerByEmail(email);
    }

    @Test
    void getAllCustomers_ReturnsOkWithList() {
        when(customerService.getAllCustomers()).thenReturn(List.of(customer));

        ResponseEntity<List<CustomerResponse>> response = customerController.getAllCustomers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(customerService).getAllCustomers();
    }

    @Test
    void updateCustomer_ReturnsOk() {
        UpdateCustomerRequest request = UpdateCustomerRequest.builder()
                .firstName(Optional.of("NewFirst"))
                .lastName(Optional.of("NewLast"))
                .email(Optional.of("new@example.com"))
                .password(Optional.of("newPassword"))
                .build();
        when(customerService.updateCustomer(eq(customerId), any(UpdateCustomerCommand.class)))
                .thenReturn(customer);

        ResponseEntity<CustomerResponse> response = customerController.updateCustomer(customerId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(customerService).updateCustomer(eq(customerId), any(UpdateCustomerCommand.class));
    }

    @Test
    void deleteCustomer_ReturnsNoContent() {
        doNothing().when(customerService).deleteCustomer(customerId);

        ResponseEntity<Void> response = customerController.deleteCustomer(customerId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(customerService).deleteCustomer(customerId);
    }

    @Test
    void activateCustomer_ReturnsOk() {
        when(customerService.activateCustomer(customerId)).thenReturn(customer);

        ResponseEntity<CustomerResponse> response = customerController.activateCustomer(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customerId, response.getBody().getId());
        verify(customerService).activateCustomer(customerId);
    }
}
