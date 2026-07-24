package com.trinity.authentication.application.command;

public record RegisterCustomerCommand(String email, String rawPassword, String firstName, String lastName) {
}
