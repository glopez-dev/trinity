package com.trinity.user.dto.employee;

import com.trinity.user.constant.EmployeeRole;
import com.trinity.user.constant.UserStatus;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;


public class UpdateEmployeeDTOTest {

    @Test
    void givenAllOptionalValues_whenConstructed_thenGettersReturnValues() {
        // Given
        Optional<String> email = Optional.of("test@example.com");
        Optional<String> firstName = Optional.of("John");
        Optional<String> lastName = Optional.of("Doe");
        Optional<EmployeeRole> role = Optional.of(EmployeeRole.EMPLOYEE);
        Optional<UserStatus> status = Optional.of(UserStatus.ACTIVE);

        // When
        UpdateEmployeeDTO dto = new UpdateEmployeeDTO(email, firstName, lastName, role, status);

        // Then
        assertThat(dto.getEmail()).isEqualTo(email);
        assertThat(dto.getFirstName()).isEqualTo(firstName);
        assertThat(dto.getLastName()).isEqualTo(lastName);
        assertThat(dto.getRole()).isEqualTo(role);
        assertThat(dto.getStatus()).isEqualTo(status);
    }

    @Test
    void givenEmptyOptionals_whenConstructed_thenGettersReturnEmptyOptionals() {
        // Given
        Optional<String> email = Optional.empty();
        Optional<String> firstName = Optional.empty();
        Optional<String> lastName = Optional.empty();
        Optional<EmployeeRole> role = Optional.empty();
        Optional<UserStatus> status = Optional.empty();

        // When
        UpdateEmployeeDTO dto = new UpdateEmployeeDTO(email, firstName, lastName, role, status);

        // Then
        assertThat(dto.getEmail()).isEmpty();
        assertThat(dto.getFirstName()).isEmpty();
        assertThat(dto.getLastName()).isEmpty();
        assertThat(dto.getRole()).isEmpty();
        assertThat(dto.getStatus()).isEmpty();
    }

    @Test
    void givenMixedOptionals_whenConstructed_thenGettersReturnCorrectValues() {
        // Given
        Optional<String> email = Optional.empty();
        Optional<String> firstName = Optional.of("Jane");
        Optional<String> lastName = Optional.empty();
        Optional<EmployeeRole> role = Optional.of(EmployeeRole.MANAGER);
        Optional<UserStatus> status = Optional.empty();

        // When
        UpdateEmployeeDTO dto = new UpdateEmployeeDTO(email, firstName, lastName, role, status);

        // Then
        assertThat(dto.getEmail()).isEmpty();
        assertThat(dto.getFirstName()).contains("Jane");
        assertThat(dto.getLastName()).isEmpty();
        assertThat(dto.getRole()).contains(EmployeeRole.MANAGER);
        assertThat(dto.getStatus()).isEmpty();
    }
}