package com.trinity.user.dto.employee;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.trinity.user.constant.EmployeeRole;
import com.trinity.user.constant.UserStatus;
import com.trinity.user.constant.UserType;
import com.trinity.user.model.Employee;


class ReadEmployeeDTOTest {

    private Employee mockEmployee;

    @BeforeEach
    void setup() {
        mockEmployee = new Employee();
        mockEmployee.setId(UUID.randomUUID());
        mockEmployee.setEmail("john.doe@example.com");
        mockEmployee.setFirstName("John");
        mockEmployee.setLastName("Doe");
        mockEmployee.setRole(EmployeeRole.EMPLOYEE);
        mockEmployee.setType(UserType.EMPLOYEE);
        mockEmployee.setStatus(UserStatus.ACTIVE);
        mockEmployee.setCreatedAt(Instant.parse("2023-01-01T10:00:00Z"));
        mockEmployee.setUpdatedAt(Instant.parse("2023-01-02T10:00:00Z"));
        mockEmployee.setLastLoginAt(Instant.parse("2023-01-03T10:00:00Z"));
        mockEmployee.setHireDate(Instant.parse("2023-01-04T10:00:00Z"));
        mockEmployee.setTerminationDate(null);
    }

    @Test
    void givenEmployee_whenUsingEmployeeConstructor_thenFieldsMatch() {
        // Given (mockEmployee is set)
        // When
        ReadEmployeeDTO dto = new ReadEmployeeDTO(mockEmployee);
        // Then
        assertThat(dto.getId()).isEqualTo(mockEmployee.getId());
        assertThat(dto.getEmail()).isEqualTo(mockEmployee.getEmail());
        assertThat(dto.getFirstName()).isEqualTo(mockEmployee.getFirstName());
        assertThat(dto.getLastName()).isEqualTo(mockEmployee.getLastName());
        assertThat(dto.getRole()).isEqualTo(mockEmployee.getRole());
        assertThat(dto.getType()).isEqualTo(mockEmployee.getType());
        assertThat(dto.getStatus()).isEqualTo(mockEmployee.getStatus());
        assertThat(dto.getCreatedAt()).isEqualTo(mockEmployee.getCreatedAt());
        assertThat(dto.getUpdatedAt()).isEqualTo(mockEmployee.getUpdatedAt());
        assertThat(dto.getLastLoginAt()).isEqualTo(mockEmployee.getLastLoginAt());
        assertThat(dto.getHireDate()).isEqualTo(mockEmployee.getHireDate());
        assertThat(dto.getTerminationDate()).isEqualTo(mockEmployee.getTerminationDate());
    }

    @Test
    void givenAllArgs_whenUsingAllArgsConstructor_thenFieldsMatch() {
        // Given
        UUID id = UUID.randomUUID();
        String email = "jane.doe@example.com";
        String firstName = "Jane";
        String lastName = "Doe";
        EmployeeRole role = EmployeeRole.MANAGER;
        UserType type = UserType.EMPLOYEE;
        UserStatus status = UserStatus.INACTIVE;
        Instant createdAt = Instant.parse("2023-02-01T10:00:00Z");
        Instant updatedAt = Instant.parse("2023-02-02T10:00:00Z");
        Instant lastLoginAt = Instant.parse("2023-02-03T10:00:00Z");
        Instant hireDate = Instant.parse("2023-02-04T10:00:00Z");
        Instant terminationDate = Instant.parse("2023-02-05T10:00:00Z");

        // When
        ReadEmployeeDTO dto = new ReadEmployeeDTO(
            type, status, createdAt, updatedAt, id,
            email, firstName, lastName, role, lastLoginAt, hireDate, terminationDate
        );

        // Then
        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getEmail()).isEqualTo(email);
        assertThat(dto.getFirstName()).isEqualTo(firstName);
        assertThat(dto.getLastName()).isEqualTo(lastName);
        assertThat(dto.getRole()).isEqualTo(role);
        assertThat(dto.getType()).isEqualTo(type);
        assertThat(dto.getStatus()).isEqualTo(status);
        assertThat(dto.getCreatedAt()).isEqualTo(createdAt);
        assertThat(dto.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(dto.getLastLoginAt()).isEqualTo(lastLoginAt);
        assertThat(dto.getHireDate()).isEqualTo(hireDate);
        assertThat(dto.getTerminationDate()).isEqualTo(terminationDate);
    }
}