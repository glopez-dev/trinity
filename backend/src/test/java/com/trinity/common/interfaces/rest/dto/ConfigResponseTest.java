package com.trinity.common.interfaces.rest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ConfigResponseTest {

    @Test
    void givenNameAndVersion_whenConstructed_thenGettersMatch() {
        ConfigResponse response = new ConfigResponse("Trinity", "1.0.0");

        assertThat(response.getName()).isEqualTo("Trinity");
        assertThat(response.getVersion()).isEqualTo("1.0.0");
    }

    @Test
    void givenTwoEqualResponses_thenEqualsAndHashCodeConsistent() {
        ConfigResponse a = new ConfigResponse("Trinity", "1.0.0");
        ConfigResponse b = new ConfigResponse("Trinity", "1.0.0");

        assertThat(a).isEqualTo(b);
        assertThat(a).hasSameHashCodeAs(b);
        assertThat(a.toString()).contains("Trinity");
    }
}
