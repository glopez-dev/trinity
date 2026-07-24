package com.trinity.user.application.command;

public record CreateCustomerCommand(
        String firstName,
        String lastName,
        String email,
        String rawPassword
) {
}
