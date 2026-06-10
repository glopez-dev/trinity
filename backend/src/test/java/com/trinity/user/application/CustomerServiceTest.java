package com.trinity.user.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.trinity.user.application.command.CreateCustomerCommand;
import com.trinity.user.application.command.UpdateCustomerCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.trinity.user.domain.model.UserStatus;
import com.trinity.user.domain.model.UserType;
import com.trinity.user.domain.model.Customer;
import com.trinity.user.domain.port.CustomerRepositoryPort;

import com.trinity.common.domain.exception.NotFoundException;

class CustomerServiceTest {

    @Mock
    private CustomerRepositoryPort customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerService customerService;

    private UUID customerId;
    private Customer customer;
    private CreateCustomerCommand createCommand;
    private UpdateCustomerCommand updateCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerId = UUID.randomUUID();

        customer = Customer.builder()
                .id(customerId)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .hashedPassword("hashedPassword")
                .status(UserStatus.ACTIVE)
                .type(UserType.CUSTOMER)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        createCommand = new CreateCustomerCommand(
                "John", "Doe", "john.doe@example.com", "password123");

        updateCommand = new UpdateCustomerCommand(
                Optional.of("John Updated"),
                Optional.of("Doe Updated"),
                Optional.of("updated.john.doe@example.com"),
                Optional.of("updatedPassword"));
    }

    @Test
    void createCustomer_Success() {
        // Given
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // When
        Customer result = customerService.createCustomer(createCommand);

        // Then
        assertNotNull(result);
        assertEquals(customerId, result.getId());
        assertEquals(createCommand.firstName(), result.getFirstName());
        assertEquals(createCommand.lastName(), result.getLastName());
        assertEquals(createCommand.email(), result.getEmail());

        verify(customerRepository, times(1)).existsByEmail(createCommand.email());
        verify(passwordEncoder, times(1)).encode(createCommand.rawPassword());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void createCustomer_EmailAlreadyExists() {
        // Given
        when(customerRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            customerService.createCustomer(createCommand);
        });

        assertEquals("Email already in use", exception.getMessage());

        verify(customerRepository, times(1)).existsByEmail(createCommand.email());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void getCustomerById_Success() {
        // Given
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // When
        Customer result = customerService.getCustomerById(customerId);

        // Then
        assertNotNull(result);
        assertEquals(customerId, result.getId());
        assertEquals(customer.getFirstName(), result.getFirstName());

        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    void getCustomerById_NotFound() {
        // Given
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            customerService.getCustomerById(customerId);
        });

        assertEquals("Customer not found with id: " + customerId, exception.getMessage());

        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    void updateCustomer_Success() {
        // Given
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmail(updateCommand.email().get())).thenReturn(false);
        when(passwordEncoder.encode(updateCommand.rawPassword().get())).thenReturn("updatedHashedPassword");
        when(customerRepository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Customer result = customerService.updateCustomer(customerId, updateCommand);

        // Then
        assertNotNull(result);
        assertEquals(customerId, result.getId());
        assertEquals(updateCommand.firstName().get(), result.getFirstName());
        assertEquals(updateCommand.lastName().get(), result.getLastName());
        assertEquals(updateCommand.email().get(), result.getEmail());
        assertEquals("updatedHashedPassword", result.getHashedPassword());

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).existsByEmail(updateCommand.email().get());
        verify(passwordEncoder, times(1)).encode(updateCommand.rawPassword().get());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void updateCustomer_BlankFieldsLeaveCustomerUnchanged() {
        // Given — null Optionals are normalized to empty by the command
        UpdateCustomerCommand emptyCommand = new UpdateCustomerCommand(null, null, null, null);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Customer result = customerService.updateCustomer(customerId, emptyCommand);

        // Then
        assertEquals("John", result.getFirstName());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(customerRepository, never()).existsByEmail(anyString());
    }

    @Test
    void deleteCustomer_Success() {
        // Given
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // When
        customerService.deleteCustomer(customerId);

        // Then
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).save(customer);
        assertEquals(UserStatus.INACTIVE, customer.getStatus());
    }

    @Test
    void activateCustomer_Success() {
        // Given
        customer.setStatus(UserStatus.INACTIVE);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);

        // When
        Customer result = customerService.activateCustomer(customerId);

        // Then
        assertNotNull(result);
        assertEquals(UserStatus.ACTIVE, result.getStatus());

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).save(customer);
    }
}
