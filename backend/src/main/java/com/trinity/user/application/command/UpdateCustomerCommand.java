package com.trinity.user.application.command;

import java.util.Optional;

/**
 * Partial update: an empty Optional means "leave the field unchanged". Nulls
 * coming from the REST layer are normalized so the service never null-checks.
 */
public record UpdateCustomerCommand(
        Optional<String> firstName,
        Optional<String> lastName,
        Optional<String> email,
        Optional<String> rawPassword
) {
    public UpdateCustomerCommand {
        firstName = firstName == null ? Optional.empty() : firstName;
        lastName = lastName == null ? Optional.empty() : lastName;
        email = email == null ? Optional.empty() : email;
        rawPassword = rawPassword == null ? Optional.empty() : rawPassword;
    }
}
