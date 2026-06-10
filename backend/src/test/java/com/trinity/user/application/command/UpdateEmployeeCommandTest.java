package com.trinity.user.application.command;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.trinity.user.domain.model.EmployeeRole;
import com.trinity.user.domain.model.UserStatus;

class UpdateEmployeeCommandTest {

    @Test
    void givenAllNull_whenConstructed_thenAllNormalizedToEmptyOptional() {
        UpdateEmployeeCommand command = new UpdateEmployeeCommand(null, null, null, null, null);

        assertThat(command.email()).isEmpty();
        assertThat(command.firstName()).isEmpty();
        assertThat(command.lastName()).isEmpty();
        assertThat(command.role()).isEmpty();
        assertThat(command.status()).isEmpty();
    }

    @Test
    void givenAllPresent_whenConstructed_thenValuesPreserved() {
        UpdateEmployeeCommand command = new UpdateEmployeeCommand(
                Optional.of("new@company.com"),
                Optional.of("Jane"),
                Optional.of("Doe"),
                Optional.of(EmployeeRole.MANAGER),
                Optional.of(UserStatus.ACTIVE));

        assertThat(command.email()).contains("new@company.com");
        assertThat(command.firstName()).contains("Jane");
        assertThat(command.lastName()).contains("Doe");
        assertThat(command.role()).contains(EmployeeRole.MANAGER);
        assertThat(command.status()).contains(UserStatus.ACTIVE);
    }

    @Test
    void givenMixedNullAndPresent_whenConstructed_thenNormalizedCorrectly() {
        UpdateEmployeeCommand command = new UpdateEmployeeCommand(
                null,
                Optional.of("Jane"),
                null,
                Optional.of(EmployeeRole.ADMIN),
                null);

        assertThat(command.email()).isEmpty();
        assertThat(command.firstName()).contains("Jane");
        assertThat(command.lastName()).isEmpty();
        assertThat(command.role()).contains(EmployeeRole.ADMIN);
        assertThat(command.status()).isEmpty();
    }
}
