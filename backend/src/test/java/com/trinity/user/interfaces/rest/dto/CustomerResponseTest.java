package com.trinity.user.interfaces.rest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.trinity.user.domain.model.UserStatus;
import com.trinity.user.domain.model.UserType;

class CustomerResponseTest {

    @Test
    void givenAllArgs_whenBuilt_thenGettersMatch() {
        UUID id = UUID.randomUUID();
        Instant lastLoginAt = Instant.parse("2023-01-03T10:00:00Z");
        Instant createdAt = Instant.parse("2023-01-01T10:00:00Z");
        Instant updatedAt = Instant.parse("2023-01-02T10:00:00Z");

        CustomerResponse dto = CustomerResponse.builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .lastLoginAt(lastLoginAt)
                .status(UserStatus.ACTIVE)
                .type(UserType.CUSTOMER)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getFirstName()).isEqualTo("John");
        assertThat(dto.getLastName()).isEqualTo("Doe");
        assertThat(dto.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(dto.getLastLoginAt()).isEqualTo(lastLoginAt);
        assertThat(dto.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(dto.getType()).isEqualTo(UserType.CUSTOMER);
        assertThat(dto.getCreatedAt()).isEqualTo(createdAt);
        assertThat(dto.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void givenNoArgs_whenSettersUsed_thenGettersReflectChanges() {
        CustomerResponse dto = new CustomerResponse();
        UUID id = UUID.randomUUID();
        dto.setId(id);
        dto.setFirstName("Jane");
        dto.setLastName("Smith");
        dto.setEmail("jane@example.com");
        dto.setStatus(UserStatus.INACTIVE);
        dto.setType(UserType.CUSTOMER);

        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getFirstName()).isEqualTo("Jane");
        assertThat(dto.getLastName()).isEqualTo("Smith");
        assertThat(dto.getEmail()).isEqualTo("jane@example.com");
        assertThat(dto.getStatus()).isEqualTo(UserStatus.INACTIVE);
        assertThat(dto.getType()).isEqualTo(UserType.CUSTOMER);
    }
}
