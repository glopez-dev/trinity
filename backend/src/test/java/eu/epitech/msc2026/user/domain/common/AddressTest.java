package eu.epitech.msc2026.user.domain.common;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class AddressTest {

    @Test
    @DisplayName("Should create valid address with all fields")
    void shouldCreateValidAddress() {
        Address address = Address.builder()
                .withStreet("123 Main St")
                .withCity("Paris")
                .withState("Île-de-France")
                .withZipCode("75001")
                .withCountry("France")
                .build();

        assertAll(
                () -> assertEquals("123 Main St", address.getStreet()),
                () -> assertEquals("Paris", address.getCity()),
                () -> assertEquals("Île-de-France", address.getState()),
                () -> assertEquals("75001", address.getZipCode()),
                () -> assertEquals("France", address.getCountry())
        );
    }

    @Test
    @DisplayName("Should trim whitespace from all fields")
    void shouldTrimWhitespace() {
        Address address = Address.builder()
                .withStreet("  123 Main St  ")
                .withCity(" Paris ")
                .withState(" Île-de-France ")
                .withZipCode(" 75001 ")
                .withCountry(" France ")
                .build();

        assertAll(
                () -> assertEquals("123 Main St", address.getStreet()),
                () -> assertEquals("Paris", address.getCity()),
                () -> assertEquals("Île-de-France", address.getState()),
                () -> assertEquals("75001", address.getZipCode()),
                () -> assertEquals("France", address.getCountry())
        );
    }

    @ParameterizedTest
    @DisplayName("Should throw exception for null or empty street")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void shouldThrowExceptionForInvalidStreet(String street) {
        Address.Builder builder = Address.builder()
                .withStreet(street)
                .withCity("Paris")
                .withState("Île-de-France")
                .withZipCode("75001")
                .withCountry("France");

        assertThrows(IllegalArgumentException.class, builder::build,
                "Should throw IllegalArgumentException for invalid street");
    }

    @ParameterizedTest
    @DisplayName("Should throw exception for null or empty city")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void shouldThrowExceptionForInvalidCity(String city) {
        Address.Builder builder = Address.builder()
                .withStreet("123 Main St")
                .withCity(city)
                .withState("Île-de-France")
                .withZipCode("75001")
                .withCountry("France");

        assertThrows(IllegalArgumentException.class, builder::build,
                "Should throw IllegalArgumentException for invalid city");
    }

    @ParameterizedTest
    @DisplayName("Should throw exception for null or empty state")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void shouldThrowExceptionForInvalidState(String state) {
        Address.Builder builder = Address.builder()
                .withStreet("123 Main St")
                .withCity("Paris")
                .withState(state)
                .withZipCode("75001")
                .withCountry("France");

        assertThrows(IllegalArgumentException.class, builder::build,
                "Should throw IllegalArgumentException for invalid state");
    }

    @ParameterizedTest
    @DisplayName("Should throw exception for null or empty zip code")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void shouldThrowExceptionForInvalidZipCode(String zipCode) {
        Address.Builder builder = Address.builder()
                .withStreet("123 Main St")
                .withCity("Paris")
                .withState("Île-de-France")
                .withZipCode(zipCode)
                .withCountry("France");

        assertThrows(IllegalArgumentException.class, builder::build,
                "Should throw IllegalArgumentException for invalid zip code");
    }

    @ParameterizedTest
    @DisplayName("Should throw exception for null or empty country")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void shouldThrowExceptionForInvalidCountry(String country) {
        Address.Builder builder = Address.builder()
                .withStreet("123 Main St")
                .withCity("Paris")
                .withState("Île-de-France")
                .withZipCode("75001")
                .withCountry(country);

        assertThrows(IllegalArgumentException.class, builder::build,
                "Should throw IllegalArgumentException for invalid country");
    }

    @Test
    @DisplayName("Should implement value equality")
    void shouldImplementValueEquality() {
        Address address1 = Address.builder()
                .withStreet("123 Main St")
                .withCity("Paris")
                .withState("Île-de-France")
                .withZipCode("75001")
                .withCountry("France")
                .build();

        Address address2 = Address.builder()
                .withStreet("123 Main St")
                .withCity("Paris")
                .withState("Île-de-France")
                .withZipCode("75001")
                .withCountry("France")
                .build();

        Address differentAddress = Address.builder()
                .withStreet("456 Other St")
                .withCity("Lyon")
                .withState("Auvergne-Rhône-Alpes")
                .withZipCode("69001")
                .withCountry("France")
                .build();

        assertAll(
                () -> assertEquals(address1, address2, "Same values should be equal"),
                () -> assertEquals(address1.hashCode(), address2.hashCode(), "Same values should have same hash"),
                () -> assertNotEquals(address1, differentAddress, "Different values should not be equal"),
                () -> assertNotEquals(address1.hashCode(), differentAddress.hashCode(), "Different values should have different hash")
        );
    }

    @Test
    @DisplayName("Should format toString correctly")
    void shouldFormatToStringCorrectly() {
        Address address = Address.builder()
                .withStreet("123 Main St")
                .withCity("Paris")
                .withState("Île-de-France")
                .withZipCode("75001")
                .withCountry("France")
                .build();

        String expected = "123 Main St, Paris, Île-de-France 75001, France";
        assertEquals(expected, address.toString(), "toString should format address correctly");
    }

    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
        Address address = Address.builder()
                .withStreet("123 Main St")
                .withCity("Paris")
                .withState("Île-de-France")
                .withZipCode("75001")
                .withCountry("France")
                .build();

        assertNotEquals(null, address, "Address should not be equal to null");
    }

    @Test
    @DisplayName("Should not be equal to different type")
    void shouldNotBeEqualToDifferentType() {
        Address address = Address.builder()
                .withStreet("123 Main St")
                .withCity("Paris")
                .withState("Île-de-France")
                .withZipCode("75001")
                .withCountry("France")
                .build();

        assertNotEquals(address, "Some String", "Address should not be equal to different type");
    }
}