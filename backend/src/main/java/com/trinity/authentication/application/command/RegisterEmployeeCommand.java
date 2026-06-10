package com.trinity.authentication.application.command;

public record RegisterEmployeeCommand(String email, String rawPassword, String firstName, String lastName) {
}
