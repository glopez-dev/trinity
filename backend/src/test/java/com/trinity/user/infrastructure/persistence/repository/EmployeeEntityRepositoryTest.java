package com.trinity.user.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.trinity.user.domain.model.EmployeeRole;
import com.trinity.user.domain.model.UserStatus;
import com.trinity.user.domain.model.UserType;
import com.trinity.user.infrastructure.persistence.entity.EmployeeJpaEntity;

/**
 * Boots Hibernate against the frozen V1 schema. The anemic entity carries NO
 * @Builder.Default, so role/type/status must be supplied explicitly — and role
 * being persisted as smallint (ORDINAL) is the schema-identity gate for the
 * {@code role smallint check(0..2)} column.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class EmployeeEntityRepositoryTest {

    @Autowired
    private JpaEmployeeRepository employeeRepository;

    private EmployeeJpaEntity employee;

    @BeforeEach
    public void setUp() {
        employee = EmployeeJpaEntity.builder()
                .email("test@example.com")
                .hashedPassword("superSecretPassword")
                .hireDate(Instant.now())
                .lastLoginAt(Instant.now())
                .status(UserStatus.ACTIVE)
                .role(EmployeeRole.EMPLOYEE)
                .type(UserType.EMPLOYEE)
                .build();

        employeeRepository.save(employee);
    }

    @Test
    void testFindByEmail() {
        // Given
        String email = "test@example.com";

        // When
        Optional<EmployeeJpaEntity> foundEmployee = employeeRepository.findByEmail(email);

        // Then
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getEmail()).isEqualTo(email);
        assertThat(foundEmployee.get().getRole()).isEqualTo(EmployeeRole.EMPLOYEE);
        assertThat(foundEmployee.get().getType()).isEqualTo(UserType.EMPLOYEE);
    }
}
