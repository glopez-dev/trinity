package com.trinity.authentication.dto;

import com.trinity.user.constant.EmployeeRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for user registration")
public class RegisterRequest {

    @Schema(description = "User's email address", example = "user@example.com")
    @NotEmpty(message = "Email cannot be empty")
    @Email
    private String email;

    @Schema(description = "User's password", example = "password123")
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @Schema(description = "User's first name", example = "John")
    @NotEmpty(message = "First name cannot be empty")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    @Schema(description = "User's role in the system", example = "EMPLOYEE")
    @NotNull(message = "Role cannot be null")
    private EmployeeRole role;
}
