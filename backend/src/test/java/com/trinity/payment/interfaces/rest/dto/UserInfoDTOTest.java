package com.trinity.payment.interfaces.rest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UserInfoDTOTest {

    @Test
    void givenAllArgs_whenBuilt_thenGettersMatch() {
        UserInfoDTO dto = UserInfoDTO.builder()
                .email("user@example.com")
                .firstName("John")
                .lastName("Doe")
                .build();

        assertThat(dto.getEmail()).isEqualTo("user@example.com");
        assertThat(dto.getFirstName()).isEqualTo("John");
        assertThat(dto.getLastName()).isEqualTo("Doe");
    }

    @Test
    void givenNoArgs_whenSettersUsed_thenGettersReflectChanges() {
        UserInfoDTO dto = new UserInfoDTO();
        dto.setEmail("jane@example.com");
        dto.setFirstName("Jane");
        dto.setLastName("Smith");

        assertThat(dto.getEmail()).isEqualTo("jane@example.com");
        assertThat(dto.getFirstName()).isEqualTo("Jane");
        assertThat(dto.getLastName()).isEqualTo("Smith");
    }

    @Test
    void givenTwoEqualDtos_thenEqualsAndHashCodeConsistent() {
        UserInfoDTO a = new UserInfoDTO("user@example.com", "John", "Doe");
        UserInfoDTO b = new UserInfoDTO("user@example.com", "John", "Doe");

        assertThat(a).isEqualTo(b);
        assertThat(a).hasSameHashCodeAs(b);
        assertThat(a.toString()).contains("user@example.com");
    }
}
