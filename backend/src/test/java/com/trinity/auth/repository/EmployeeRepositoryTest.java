package com.trinity.auth.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.trinity.auth.model.Employee;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        employee = Employee.builder()
            .email("test@example.com")
            .hashedPassword("superSecretPassword")
            .hireDate(Instant.now())
            .build();

        employeeRepository.save(employee);
    }

    @Test
    void testFindByEmail() {
        // Given
        String email = "test@example.com";

        // When
        Optional<Employee> foundEmployee = employeeRepository.findByEmail(email);

        // Then
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getEmail()).isEqualTo(email);
    }
}