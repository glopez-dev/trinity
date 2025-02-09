package com.trinity.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login request payload")
public class LoginRequest {
    @Schema(description = "User email address", example = "user@example.com", required = true)
    private String email;

    @Schema(description = "User password", example = "password123", required = true)
    private String password;
}
