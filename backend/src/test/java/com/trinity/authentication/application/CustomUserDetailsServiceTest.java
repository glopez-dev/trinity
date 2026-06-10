package com.trinity.authentication.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.trinity.user.domain.model.Customer;
import com.trinity.user.domain.model.Employee;
import com.trinity.user.domain.port.CustomerRepositoryPort;
import com.trinity.user.domain.port.EmployeeRepositoryPort;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private EmployeeRepositoryPort employeeRepository;

    @Mock
    private CustomerRepositoryPort customerRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsername_EmployeeFound_ReturnsAppUserDetails() {
        String email = "employee@example.com";
        Employee employee = Employee.builder()
                .email(email)
                .hashedPassword("hashed")
                .firstName("John")
                .lastName("Doe")
                .build();
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.of(employee));

        UserDetails result = customUserDetailsService.loadUserByUsername(email);

        assertNotNull(result);
        assertEquals(email, result.getUsername());
        verifyNoInteractions(customerRepository);
    }

    @Test
    void loadUserByUsername_EmployeeAbsentCustomerFound_ReturnsAppUserDetails() {
        String email = "customer@example.com";
        Customer customer = Customer.builder()
                .email(email)
                .hashedPassword("hashed")
                .firstName("Jane")
                .lastName("Doe")
                .build();
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(customer));

        UserDetails result = customUserDetailsService.loadUserByUsername(email);

        assertNotNull(result);
        assertEquals(email, result.getUsername());
    }

    @Test
    void loadUserByUsername_NoUser_ThrowsUsernameNotFoundException() {
        String email = "missing@example.com";
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());

        UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(email));
        assertEquals("User not found with email: " + email, ex.getMessage());
    }
}
