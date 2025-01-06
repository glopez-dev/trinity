package eu.epitech.msc2026.user.domain.model.common;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Address")
class AddressTest {

    @Nested
    @DisplayName("Address Creation")
    class AddressCreation {
        
        @Test
        @DisplayName("Given valid address details, when building address, then should create successfully")
        void givenValidDetails_whenBuildingAddress_thenShouldCreateSuccessfully() {
            // Given
            String street = "123 Main St";
            String city = "Springfield";
            String zipCode = "12345";
            String country = "United States";
            String state = "IL";

            // When
            Address address = Address.builder()
                .withStreet(street)
                .withCity(city)
                .withZipCode(zipCode)
                .withCountry(country)
                .withState(state)
                .build();

            // Then
            assertEquals(street, address.getStreet());
            assertEquals(city, address.getCity());
            assertEquals(zipCode, address.getZipCode());
            assertEquals(country, address.getCountry());
            assertEquals(state, address.getState());
        }

        @Test
        @DisplayName("Given address details with whitespace, when building address, then should trim values")
        void givenDetailsWithWhitespace_whenBuildingAddress_thenShouldTrimValues() {
            // Given
            String street = "  123 Main St  ";
            String city = " Springfield ";
            String zipCode = " 12345 ";
            String country = " United States ";
            String state = " IL ";

            // When
            Address address = Address.builder()
                .withStreet(street)
                .withCity(city)
                .withZipCode(zipCode)
                .withCountry(country)
                .withState(state)
                .build();

            // Then
            assertEquals("123 Main St", address.getStreet());
            assertEquals("Springfield", address.getCity());
            assertEquals("12345", address.getZipCode());
            assertEquals("United States", address.getCountry());
            assertEquals("IL", address.getState());
        }
    }

    @Nested
    @DisplayName("Address Validation")
    class AddressValidation {

        @Test
        @DisplayName("Given null street, when building address, then should throw IllegalArgumentException")
        void givenNullStreet_whenBuildingAddress_thenShouldThrowException() {
            // Given
            Address.Builder builder = validBuilder();
            
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> 
                builder.withStreet(null).build());
        }

        @Test
        @DisplayName("Given empty city, when building address, then should throw IllegalArgumentException")
        void givenEmptyCity_whenBuildingAddress_thenShouldThrowException() {
            // Given
            Address.Builder builder = validBuilder();
            
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> 
                builder.withCity("   ").build());
        }

        @Test
        @DisplayName("Given null zip code, when building address, then should throw IllegalArgumentException")
        void givenNullZipCode_whenBuildingAddress_thenShouldThrowException() {
            // Given
            Address.Builder builder = validBuilder();
            
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> 
                builder.withZipCode(null).build());
        }

        @Test
        @DisplayName("Given empty country, when building address, then should throw IllegalArgumentException")
        void givenEmptyCountry_whenBuildingAddress_thenShouldThrowException() {
            // Given
            Address.Builder builder = validBuilder();
            
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> 
                builder.withCountry("").build());
        }

        @Test
        @DisplayName("Given empty state, when building address, then should throw IllegalArgumentException")
        void givenEmptyState_whenBuildingAddress_thenShouldThrowException() {
            // Given
            Address.Builder builder = validBuilder();
            
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> 
                builder.withState("").build());
        }
    }

    @Nested
    @DisplayName("Address Value Equality")
    class AddressEquality {

        @Test
        @DisplayName("Given two addresses with same values, when comparing, then should be equal")
        void givenSameValues_whenComparing_thenShouldBeEqual() {
            // Given
            Address address1 = validAddress();
            Address address2 = validAddress();

            // When & Then
            assertEquals(address1, address2);
            assertEquals(address1.hashCode(), address2.hashCode());
        }

        @Test
        @DisplayName("Given two addresses with different values, when comparing, then should not be equal")
        void givenDifferentValues_whenComparing_thenShouldNotBeEqual() {
            // Given
            Address address1 = validAddress();
            Address address2 = Address.builder()
                .withStreet("456 Other St")
                .withCity("Springfield")
                .withZipCode("12345")
                .withCountry("United States")
                .withState("IL")
                .build();

            // When & Then
            assertNotEquals(address1, address2);
            assertNotEquals(address1.hashCode(), address2.hashCode());
        }
    }

    @Nested
    @DisplayName("Address String Representation")
    class AddressStringRepresentation {

        @Test
        @DisplayName("Given valid address, when converting to string, then should format correctly")
        void givenValidAddress_whenToString_thenShouldFormatCorrectly() {
            // Given
            Address address = validAddress();

            // When
            String result = address.toString();

            // Then
            assertEquals("123 Main St, Springfield, IL 12345, United States", result);
        }
    }

    // Helper methods
    private Address.Builder validBuilder() {
        return Address.builder()
            .withStreet("123 Main St")
            .withCity("Springfield")
            .withZipCode("12345")
            .withCountry("United States")
            .withState("IL");
    }

    private Address validAddress() {
        return validBuilder().build();
    }
}