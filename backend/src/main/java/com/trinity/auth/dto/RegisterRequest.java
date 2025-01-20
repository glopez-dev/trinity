package com.trinity.auth.dto;

import com.trinity.auth.constant.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotEmpty(message = "Email cannot be empty")
    @Email
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @NotEmpty(message = "First name cannot be empty")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    @NotNull(message = "Role cannot be null")
    private UserRole role;
}
