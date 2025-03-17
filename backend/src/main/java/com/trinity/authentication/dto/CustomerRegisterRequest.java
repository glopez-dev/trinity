package com.trinity.authentication.dto;

import com.trinity.user.constant.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(example = """
    {
      "firstName": "Jahn",
      "lastName": "Smith",
      "email": "customer@example.com",
      "password": "customer123"
    }
    """)
public class CustomerRegisterRequest {

    @Schema(description = "User's first name")
    @NotEmpty(message = "First name is required")
    private String firstName;

    @Schema(description = "User's last name")
    @NotEmpty(message = "Last name is required")
    private String lastName;

    @Schema(description = "User's email address")
    @NotEmpty(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(description = "User's password")
    @NotEmpty(message = "Password is required")
    private String password;

    @Schema(hidden = true)
    private UserType type = UserType.CUSTOMER;

}
