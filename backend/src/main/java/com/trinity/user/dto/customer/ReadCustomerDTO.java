package com.trinity.user.dto.customer;

import com.trinity.user.constant.UserStatus;
import com.trinity.user.constant.UserType;
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
public class ReadCustomerDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Instant tokenExpiresAt;
    private Instant lastLoginAt;
    private UserStatus status;
    private UserType type;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean tokenExpired;
}
