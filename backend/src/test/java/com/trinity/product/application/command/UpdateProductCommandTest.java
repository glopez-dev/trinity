package com.trinity.product.application.command;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class UpdateProductCommandTest {

    @Test
    void givenAllNull_whenConstructed_thenAllNormalizedToEmptyOptional() {
        UpdateProductCommand command = new UpdateProductCommand(null, null, null);

        assertThat(command.name()).isEmpty();
        assertThat(command.price()).isEmpty();
        assertThat(command.quantity()).isEmpty();
    }

    @Test
    void givenAllPresent_whenConstructed_thenValuesPreserved() {
        UpdateProductCommand command = new UpdateProductCommand(
                Optional.of("Coffee"),
                Optional.of(new BigDecimal("9.99")),
                Optional.of(10));

        assertThat(command.name()).contains("Coffee");
        assertThat(command.price()).contains(new BigDecimal("9.99"));
        assertThat(command.quantity()).contains(10);
    }

    @Test
    void givenMixedNullAndPresent_whenConstructed_thenNormalizedCorrectly() {
        UpdateProductCommand command = new UpdateProductCommand(
                Optional.of("Coffee"),
                null,
                Optional.of(5));

        assertThat(command.name()).contains("Coffee");
        assertThat(command.price()).isEmpty();
        assertThat(command.quantity()).contains(5);
    }
}
