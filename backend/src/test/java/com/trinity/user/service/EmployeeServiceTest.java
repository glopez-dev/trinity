package com.trinity.user.service;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.trinity.user.model.Employee;
import com.trinity.user.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void findById_ShouldReturnEmployee_WhenEmployeeExists() {
        // Given
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee();
        employee.setId(employeeId);
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // When
        Employee foundEmployee = ReflectionTestUtils.invokeMethod(employeeService, "findById", employeeId);

        // Then
        Assertions.assertNotNull(foundEmployee);
        Assertions.assertEquals(employeeId, foundEmployee.getId());
        Mockito.verify(employeeRepository).findById(employeeId);
    }

    @Test
    void findById_ShouldThrowException_WhenEmployeeDoesNotExist() {
        // Given
        UUID employeeId = UUID.randomUUID();
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
            () -> ReflectionTestUtils.invokeMethod(employeeService, "findById", employeeId)
        );
        Assertions.assertEquals("Employee not found", ex.getMessage());
        Mockito.verify(employeeRepository).findById(employeeId);
    }
}