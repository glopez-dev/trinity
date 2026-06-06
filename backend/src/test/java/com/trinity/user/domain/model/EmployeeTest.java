package com.trinity.user.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.trinity.common.domain.exception.BusinessRuleViolation;

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

    @Test
    void rename_updatesNames() {
        Employee employee = new Employee();

        employee.rename("Jane", "Doe");

        assertEquals("Jane", employee.getFirstName());
        assertEquals("Doe", employee.getLastName());
    }

    @Test
    void rename_ignoresNullValues() {
        Employee employee = Employee.builder().firstName("John").lastName("Smith").build();

        employee.rename(null, "Doe");

        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getLastName());
    }

    @Test
    void changeRole_updatesRole() {
        Employee employee = new Employee();

        employee.changeRole(EmployeeRole.MANAGER);

        assertEquals(EmployeeRole.MANAGER, employee.getRole());
    }

    @Test
    void changeRole_rejectsNull() {
        Employee employee = new Employee();

        assertThrows(BusinessRuleViolation.class, () -> employee.changeRole(null));
    }

    @Test
    void terminate_setsTerminationDate() {
        Instant hireDate = Instant.now().minusSeconds(3600);
        Employee employee = Employee.builder().hireDate(hireDate).build();
        Instant termination = Instant.now();

        employee.terminate(termination);

        assertEquals(termination, employee.getTerminationDate());
    }

    @Test
    void terminate_rejectsDateBeforeHire() {
        Instant hireDate = Instant.now();
        Employee employee = Employee.builder().hireDate(hireDate).build();

        assertThrows(BusinessRuleViolation.class,
                () -> employee.terminate(hireDate.minusSeconds(3600)));
    }
}