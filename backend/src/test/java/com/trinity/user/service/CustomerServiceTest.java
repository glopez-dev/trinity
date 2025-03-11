package com.trinity.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.trinity.user.dto.customer.CreateCustomerDTO;
import com.trinity.user.dto.customer.ReadCustomerDTO;
import com.trinity.user.dto.customer.UpdateCustomerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.trinity.user.constant.UserStatus;
import com.trinity.user.constant.UserType;
import com.trinity.user.model.Customer;
import com.trinity.user.repository.CustomerRepository;

import jakarta.persistence.EntityNotFoundException;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerService customerService;

    private UUID customerId;
    private Customer customer;
    private CreateCustomerDTO createCustomerDTO;
    private UpdateCustomerDTO updateCustomerDTO;

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
                .stripeUserId("stripe123")
                .stripeAccessToken("access123")
                .stripeRefreshToken("refresh123")
                .tokenExpiresAt(Instant.now().plusSeconds(3600))
                .status(UserStatus.ACTIVE)
                .type(UserType.CUSTOMER)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        createCustomerDTO = CreateCustomerDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .build();

        updateCustomerDTO = UpdateCustomerDTO.builder()
                .firstName("John Updated")
                .lastName("Doe Updated")
                .email("updated.john.doe@example.com")
                .password("updatedPassword")
                .build();
    }

    @Test
    void createCustomer_Success() {
        // Given
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // When
        ReadCustomerDTO result = customerService.createCustomer(createCustomerDTO);

        // Then
        assertNotNull(result);
        assertEquals(customerId, result.getId());
        assertEquals(createCustomerDTO.getFirstName(), result.getFirstName());
        assertEquals(createCustomerDTO.getLastName(), result.getLastName());
        assertEquals(createCustomerDTO.getEmail(), result.getEmail());

        verify(customerRepository, times(1)).existsByEmail(createCustomerDTO.getEmail());
        verify(passwordEncoder, times(1)).encode(createCustomerDTO.getPassword());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void createCustomer_EmailAlreadyExists() {
        // Given
        when(customerRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            customerService.createCustomer(createCustomerDTO);
        });

        assertEquals("Email already in use", exception.getMessage());

        verify(customerRepository, times(1)).existsByEmail(createCustomerDTO.getEmail());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void getCustomerById_Success() {
        // Given
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // When
        ReadCustomerDTO result = customerService.getCustomerById(customerId);

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
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            customerService.getCustomerById(customerId);
        });

        assertEquals("Customer not found with id: " + customerId, exception.getMessage());

        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    void updateCustomer_Success() {
        // Given
        Customer updatedCustomer = Customer.builder()
                .id(customerId)
                .firstName(updateCustomerDTO.getFirstName())
                .lastName(updateCustomerDTO.getLastName())
                .email(updateCustomerDTO.getEmail())
                .hashedPassword("updatedHashedPassword")
                .stripeUserId("stripe123")
                .status(UserStatus.ACTIVE)
                .type(UserType.CUSTOMER)
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmail(updateCustomerDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(updateCustomerDTO.getPassword())).thenReturn("updatedHashedPassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        // When
        ReadCustomerDTO result = customerService.updateCustomer(customerId, updateCustomerDTO);

        // Then
        assertNotNull(result);
        assertEquals(customerId, result.getId());
        assertEquals(updateCustomerDTO.getFirstName(), result.getFirstName());
        assertEquals(updateCustomerDTO.getLastName(), result.getLastName());
        assertEquals(updateCustomerDTO.getEmail(), result.getEmail());

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).existsByEmail(updateCustomerDTO.getEmail());
        verify(passwordEncoder, times(1)).encode(updateCustomerDTO.getPassword());
        verify(customerRepository, times(1)).save(any(Customer.class));
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
        ReadCustomerDTO result = customerService.activateCustomer(customerId);

        // Then
        assertNotNull(result);
        assertEquals(UserStatus.ACTIVE, result.getStatus());

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).save(customer);
    }
}