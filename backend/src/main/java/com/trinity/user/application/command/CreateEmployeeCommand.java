package com.trinity.user.application.command;

import com.trinity.user.domain.model.EmployeeRole;

public record CreateEmployeeCommand(
        String email,
        String rawPassword,
        String firstName,
        String lastName,
        EmployeeRole role
) {
}
