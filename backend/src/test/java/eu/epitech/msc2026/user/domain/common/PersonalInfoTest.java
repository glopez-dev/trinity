package eu.epitech.msc2026.user.domain.common;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import eu.epitech.msc2026.user.domain.exception.InvalidUserDataException;

@DisplayName("PersonalInfo Tests")
class PersonalInfoTest {

    @Test
    @DisplayName("Should create PersonalInfo with valid data")
    void shouldCreatePersonalInfoWithValidData() {
        PersonalInfo info = PersonalInfo.of("John", "Doe", "+33-612345678");
        
        assertEquals("John", info.getFirstName());
        assertEquals("Doe", info.getLastName());
        assertEquals("+33612345678", info.getPhoneNumber());
        assertEquals("John Doe", info.getFullName());
    }

    @Test
    @DisplayName("Should normalize phone number")
    void shouldNormalizePhoneNumber() {
        PersonalInfo info = PersonalInfo.of("John", "Doe", "+33-6-12-34-56-78");
        assertEquals("+33612345678", info.getPhoneNumber());
    }

    @Test
    @DisplayName("Should create new instance when updating values")
    void shouldCreateNewInstanceWhenUpdating() {
        PersonalInfo original = PersonalInfo.of("John", "Doe", "+33612345678");
        
        PersonalInfo updatedFirstName = original.withFirstName("Jane");
        PersonalInfo updatedLastName = original.withLastName("Smith");
        PersonalInfo updatedPhone = original.withPhoneNumber("+33687654321");
        
        // Verify original object remains unchanged
        assertEquals("John", original.getFirstName());
        assertEquals("Doe", original.getLastName());
        assertEquals("+33612345678", original.getPhoneNumber());
        
        // Verify new instances have updated values
        assertEquals("Jane", updatedFirstName.getFirstName());
        assertEquals("Smith", updatedLastName.getLastName());
        assertEquals("+33687654321", updatedPhone.getPhoneNumber());
    }

    @Test
    @DisplayName("Should test equality")
    void shouldTestEquality() {
        PersonalInfo info1 = PersonalInfo.of("John", "Doe", "+33612345678");
        PersonalInfo info2 = PersonalInfo.of("John", "Doe", "+33612345678");
        PersonalInfo different = PersonalInfo.of("Jane", "Doe", "+33612345678");
        
        // Test equality
        assertEquals(info1, info2);
        assertNotEquals(info1, different);
        
        // Test hashCode
        assertEquals(info1.hashCode(), info2.hashCode());
        assertNotEquals(info1.hashCode(), different.hashCode());
    }

    @Test
    @DisplayName("Should throw exception for null values")
    void shouldThrowExceptionForNullValues() {
        assertThrows(InvalidUserDataException.class, () -> 
            PersonalInfo.of(null, "Doe", "+33612345678"));
            
        assertThrows(InvalidUserDataException.class, () -> 
            PersonalInfo.of("John", null, "+33612345678"));
            
        assertThrows(InvalidUserDataException.class, () -> 
            PersonalInfo.of("John", "Doe", null));
    }

    @Test
    @DisplayName("Should trim whitespace")
    void shouldTrimWhitespace() {
        PersonalInfo info = PersonalInfo.of(" John ", " Doe ", "+33612345678");
        
        assertEquals("John", info.getFirstName());
        assertEquals("Doe", info.getLastName());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "+1-1234567890",
        "+33612345678",
        "+44-7911123456"
    })
    @DisplayName("Should accept valid phone numbers")
    void shouldAcceptValidPhoneNumbers(String phoneNumber) {
        assertDoesNotThrow(() -> 
            PersonalInfo.of("John", "Doe", phoneNumber));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "123",                   // Too short
        "abcdefghijk",           // Not a number
        "+12345678901234556",    // Too long
        "+0123456789",           // Cannot start with 0 after +
        " ",                     // Empty after trim
        "+",                     // Only plus sign
        "+12"                    // Too short after +
    })
    @DisplayName("Should reject invalid phone numbers")
    void shouldRejectInvalidPhoneNumbers(String phoneNumber) {
        assertThrows(InvalidUserDataException.class, () -> 
            PersonalInfo.of("John", "Doe", phoneNumber));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "a",        // Too short
        "123",      // Numbers not allowed
        "$John",    // Special characters not allowed
        "John@",    // Special characters not allowed
        ""         // Empty not allowed
    })
    @DisplayName("Should reject invalid names")
    void shouldRejectInvalidNames(String name) {
        assertThrows(InvalidUserDataException.class, () -> 
            PersonalInfo.of(name, "Doe", "+33612345678"));
        assertThrows(InvalidUserDataException.class, () -> 
            PersonalInfo.of("John", name, "+33612345678"));
    }

    @Test
    @DisplayName("Should test toString format")
    void shouldTestToString() {
        PersonalInfo info = PersonalInfo.of("John", "Doe", "+33612345678");
        String expected = "PersonalInfo{firstName='John', lastName='Doe', phoneNumber='+33612345678'}";
        assertEquals(expected, info.toString());
    }
}