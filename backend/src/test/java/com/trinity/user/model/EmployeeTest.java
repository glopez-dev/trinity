package com.trinity.user.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.trinity.user.constant.UserType;

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
                .type(UserType.EMPLOYEE)
                .build();

        // Then
        assertNotNull(employee);
        assertEquals("employee@example.com", employee.getEmail());
        assertEquals("hashedPassword", employee.getHashedPassword());
        assertEquals(hireDate, employee.getHireDate());
        assertEquals(terminationDate, employee.getTerminationDate());
        assertEquals(UserType.EMPLOYEE, employee.getType());
    }

    @Test
    void testNoArgsConstructor() {
        // Given & When
        Employee employee = new Employee();

        // Then
        assertNotNull(employee);
        assertEquals(UserType.EMPLOYEE, employee.getType());
    }

    @Test
    void testDefaultType() {
        // Given
        Employee employee = new Employee();

        // When
        UserType role = employee.getType();

        // Then
        assertEquals(UserType.EMPLOYEE, role);
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