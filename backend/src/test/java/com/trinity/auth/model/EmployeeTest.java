package com.trinity.auth.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import com.trinity.auth.constant.UserRole;

class EmployeeTest {

    @Test
    void testEmployeeBuilder() {
        // Given
        String email = "employee@example.com";
        String hashedPassword = "hashedPassword";
        Instant hireDate = Instant.now();
        Instant terminationDate = Instant.now().plusSeconds(3600);

        // When
        Employee employee = Employee.builder()
                .email(email)
                .hashedPassword(hashedPassword)
                .hireDate(hireDate)
                .terminationDate(terminationDate)
                .role(UserRole.EMPLOYEE)
                .build();

        // Then
        assertNotNull(employee);
        assertEquals("employee@example.com", employee.getEmail());
        assertEquals("hashedPassword", employee.getHashedPassword());
        assertEquals(hireDate, employee.getHireDate());
        assertEquals(terminationDate, employee.getTerminationDate());
        assertEquals(UserRole.EMPLOYEE, employee.getRole());
    }

    @Test
    void testNoArgsConstructor() {
        // Given & When
        Employee employee = new Employee();

        // Then
        assertNotNull(employee);
        assertEquals(UserRole.EMPLOYEE, employee.getRole());
    }

    @Test
    void testDefaultRole() {
        // Given
        Employee employee = new Employee();

        // When
        UserRole role = employee.getRole();

        // Then
        assertEquals(UserRole.EMPLOYEE, role);
    }

    @Test
    void testSetHireDate() {
        // Given
        Employee employee = new Employee();
        Instant hireDate = Instant.now();

        // When
        employee.setHireDate(hireDate);

        // Then
        assertEquals(hireDate, employee.getHireDate());
    }

    @Test
    void testSetTerminationDate() {
        // Given
        Employee employee = new Employee();
        Instant terminationDate = Instant.now().plusSeconds(3600);

        // When
        employee.setTerminationDate(terminationDate);

        // Then
        assertEquals(terminationDate, employee.getTerminationDate());
    }
}