package com.trinity.authentication.dto;

import com.trinity.user.constant.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRegisterRequest {

    @Schema(description = "User's first name", example = "John")
    @NotEmpty(message = "First name is required")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    @NotEmpty(message = "Last name is required")
    private String lastName;

    @Schema(description = "User's email address", example = "user@example.com")
    @NotEmpty(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(description = "User's password", example = "password123")
    @NotEmpty(message = "Password is required")
    private String password;

    @Schema(description = "User's role in the system", example = "EMPLOYEE")
    @NotNull(message = "Role must not be null")
    private UserType role = UserType.CUSTOMER;
}
