package com.trinity.user.application.command;

import java.util.Optional;

import com.trinity.user.domain.model.EmployeeRole;
import com.trinity.user.domain.model.UserStatus;

/**
 * Partial update: an empty Optional means "leave the field unchanged". Nulls
 * coming from the REST layer are normalized so the service never null-checks.
 */
public record UpdateEmployeeCommand(
        Optional<String> email,
        Optional<String> firstName,
        Optional<String> lastName,
        Optional<EmployeeRole> role,
        Optional<UserStatus> status
) {
    public UpdateEmployeeCommand {
        email = email == null ? Optional.empty() : email;
        firstName = firstName == null ? Optional.empty() : firstName;
        lastName = lastName == null ? Optional.empty() : lastName;
        role = role == null ? Optional.empty() : role;
        status = status == null ? Optional.empty() : status;
    }
}
