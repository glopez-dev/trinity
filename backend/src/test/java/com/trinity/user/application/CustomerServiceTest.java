package com.trinity.user.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.trinity.user.dto.customer.CreateCustomerRequest;
import com.trinity.user.dto.customer.CustomerResponse;
import com.trinity.user.dto.customer.UpdateCustomerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.trinity.user.domain.model.UserStatus;
import com.trinity.user.domain.model.UserType;
import com.trinity.user.interfaces.rest.mapper.CustomerApiMapper;
import com.trinity.user.domain.model.Customer;
import com.trinity.user.domain.port.CustomerRepositoryPort;

import jakarta.persistence.EntityNotFoundException;

class CustomerServiceTest {

    @Mock
    private CustomerRepositoryPort customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    private CustomerApiMapper customerApiMapper = Mappers.getMapper(CustomerApiMapper.class);

    @InjectMocks
    private CustomerService customerService;

    private UUID customerId;
    private Customer customer;
    private CreateCustomerRequest createCustomerDTO;
    private UpdateCustomerRequest updateCustomerDTO;

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

        createCustomerDTO = CreateCustomerRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .build();

        updateCustomerDTO = UpdateCustomerRequest.builder()
                .firstName(Optional.of("John Updated"))
                .lastName(Optional.of("Doe Updated"))
                .email(Optional.of("updated.john.doe@example.com"))
                .password(Optional.of("updatedPassword"))
                .build();
    }

    @Test
    void createCustomer_Success() {
        // Given
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // When
        CustomerResponse result = customerService.createCustomer(createCustomerDTO);

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
        CustomerResponse result = customerService.getCustomerById(customerId);

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
                .firstName(updateCustomerDTO.getFirstName().get())
                .lastName(updateCustomerDTO.getLastName().get())
                .email(updateCustomerDTO.getEmail().get())
                .hashedPassword("updatedHashedPassword")
                .stripeUserId("stripe123")
                .status(UserStatus.ACTIVE)
                .type(UserType.CUSTOMER)
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmail(updateCustomerDTO.getEmail().get())).thenReturn(false);
        when(passwordEncoder.encode(updateCustomerDTO.getPassword().get())).thenReturn("updatedHashedPassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        // When
        CustomerResponse result = customerService.updateCustomer(customerId, updateCustomerDTO);

        // Then
        assertNotNull(result);
        assertEquals(customerId, result.getId());
        assertEquals(updateCustomerDTO.getFirstName().get(), result.getFirstName());
        assertEquals(updateCustomerDTO.getLastName().get(), result.getLastName());
        assertEquals(updateCustomerDTO.getEmail().get(), result.getEmail());

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).existsByEmail(updateCustomerDTO.getEmail().get());
        verify(passwordEncoder, times(1)).encode(updateCustomerDTO.getPassword().get());
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
        CustomerResponse result = customerService.activateCustomer(customerId);

        // Then
        assertNotNull(result);
        assertEquals(UserStatus.ACTIVE, result.getStatus());

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).save(customer);
    }
}