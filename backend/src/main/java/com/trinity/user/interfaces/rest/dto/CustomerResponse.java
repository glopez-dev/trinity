package com.trinity.user.interfaces.rest.dto;

import com.trinity.user.domain.model.UserStatus;
import com.trinity.user.domain.model.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for reading customer information")
public class CustomerResponse {
    @Schema(description = "Unique identifier of the customer")
    private UUID id;

    @Schema(description = "Customer's first name")
    private String firstName;

    @Schema(description = "Customer's last name")
    private String lastName;

    @Schema(description = "Customer's email address")
    private String email;

    @Schema(description = "Customer's last login timestamp")
    private Instant lastLoginAt;

    @Schema(description = "Customer's status")
    private UserStatus status;

    @Schema(description = "Customer's role")
    private UserType type;

    @Schema(description = "Timestamp when the customer was created")
    private Instant createdAt;

    @Schema(description = "Timestamp of last customer update")
    private Instant updatedAt;

}
