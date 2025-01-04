package eu.epitech.msc2026.user.domain.model.user;

import eu.epitech.msc2026.user.domain.model.common.PersonalInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@DisplayName("User Tests")
class UserTest {
    
    // Concrete implementation for testing the abstract User class
    private static class TestUser extends User {
        public TestUser(UserId id, PersonalInfo personalInfo, String email, UserStatus status) {
            super(id, personalInfo, email, status);
        }
    }

    private UserId userId;
    private PersonalInfo personalInfo;
    private String validEmail;
    private TestUser user;

    @BeforeEach
    void setUp() {
        userId = UserId.generate();
        personalInfo = mock(PersonalInfo.class);
        validEmail = "test@example.com";
        user = new TestUser(userId, personalInfo, validEmail, UserStatus.ACTIVE);
    }

    @Nested
    @DisplayName("Constructor")
    class Constructor {
        @Test
        @DisplayName("should create user with valid parameters")
        void shouldCreateUserWithValidParameters() {
            assertAll(
                () -> assertEquals(userId, user.getId()),
                () -> assertEquals(personalInfo, user.getPersonalInfo()),
                () -> assertEquals(validEmail, user.getEmail()),
                () -> assertEquals(UserStatus.ACTIVE, user.getStatus()),
                () -> assertNotNull(user.getCreatedAt()),
                () -> assertNotNull(user.getLastModifiedAt()),
                () -> assertEquals(user.getCreatedAt(), user.getLastModifiedAt())
            );
        }

        @Test
        @DisplayName("should throw NullPointerException when id is null")
        void shouldThrowExceptionWhenIdIsNull() {
            assertThrows(NullPointerException.class, () ->
                new TestUser(null, personalInfo, validEmail, UserStatus.ACTIVE));
        }

        @Test
        @DisplayName("should throw NullPointerException when personalInfo is null")
        void shouldThrowExceptionWhenPersonalInfoIsNull() {
            assertThrows(NullPointerException.class, () ->
                new TestUser(userId, null, validEmail, UserStatus.ACTIVE));
        }

        @Test
        @DisplayName("should throw IllegalArgumentException when email is invalid")
        void shouldThrowExceptionWhenEmailIsInvalid() {
            assertThrows(IllegalArgumentException.class, () ->
                new TestUser(userId, personalInfo, "invalid-email", UserStatus.ACTIVE));
        }

        @Test
        @DisplayName("should throw NullPointerException when status is null")
        void shouldThrowExceptionWhenStatusIsNull() {
            assertThrows(NullPointerException.class, () ->
                new TestUser(userId, personalInfo, validEmail, null));
        }
    }

    @Nested
    @DisplayName("Email Management")
    class EmailManagement {
        @Test
        @DisplayName("should update email with valid value")
        void shouldUpdateEmailWithValidValue() {
            String newEmail = "new@example.com";
            user.setEmail(newEmail);
            assertEquals(newEmail, user.getEmail());
        }

        @Test
        @DisplayName("should throw exception for null email")
        void shouldThrowExceptionForNullEmail() {
            assertThrows(IllegalArgumentException.class, () -> user.setEmail(null));
        }

        @Test
        @DisplayName("should throw exception for empty email")
        void shouldThrowExceptionForEmptyEmail() {
            assertThrows(IllegalArgumentException.class, () -> user.setEmail("  "));
        }

        @Test
        @DisplayName("should throw exception for invalid email format")
        void shouldThrowExceptionForInvalidEmailFormat() {
            assertThrows(IllegalArgumentException.class, () -> user.setEmail("invalid-email"));
        }

        @Test
        @DisplayName("should normalize email to lowercase")
        void shouldNormalizeEmailToLowercase() {
            user.setEmail("Test@Example.com");
            assertEquals("test@example.com", user.getEmail());
        }
    }

    @Nested
    @DisplayName("Status Management")
    class StatusManagement {
        @Test
        @DisplayName("should update status")
        void shouldUpdateStatus() {
            user.setStatus(UserStatus.INACTIVE);
            assertEquals(UserStatus.INACTIVE, user.getStatus());
        }

        @Test
        @DisplayName("should throw exception for null status")
        void shouldThrowExceptionForNullStatus() {
            assertThrows(NullPointerException.class, () -> user.setStatus(null));
        }

        @Test
        @DisplayName("should deactivate user")
        void shouldDeactivateUser() {
            user.deactivate();
            assertEquals(UserStatus.INACTIVE, user.getStatus());
        }
    }

    @Nested
    @DisplayName("Login Management")
    class LoginManagement {
        @Test
        @DisplayName("should record login time")
        void shouldRecordLoginTime() {
            LocalDateTime beforeLogin = LocalDateTime.now();
            user.recordLogin();
            LocalDateTime afterLogin = LocalDateTime.now();
            
            LocalDateTime loginTime = user.getLastLoginAt();
            assertNotNull(loginTime);
            assertTrue(
                !loginTime.isBefore(beforeLogin) && 
                !loginTime.isAfter(afterLogin),
                "Login time should be between before and after timestamps"
            );
        }
    }

    @Nested
    @DisplayName("Personal Info Management")
    class PersonalInfoManagement {
        @Test
        @DisplayName("should update personal info")
        void shouldUpdatePersonalInfo() {
            PersonalInfo newInfo = mock(PersonalInfo.class);
            user.updatePersonalInfo(newInfo);
            assertEquals(newInfo, user.getPersonalInfo());
        }

        @Test
        @DisplayName("should throw exception for null personal info")
        void shouldThrowExceptionForNullPersonalInfo() {
            assertThrows(NullPointerException.class, () -> user.updatePersonalInfo(null));
        }

        @Test
        @DisplayName("should update last modified time when updating personal info")
        void shouldUpdateLastModifiedTime() {
            LocalDateTime before = user.getLastModifiedAt();
            user.updatePersonalInfo(mock(PersonalInfo.class));
            assertTrue(user.getLastModifiedAt().isAfter(before));
        }
    }

    @Nested
    @DisplayName("Equality and HashCode")
    class EqualityAndHashCode {
        @Test
        @DisplayName("should be equal for same ID")
        void shouldBeEqualForSameId() {
            TestUser user2 = new TestUser(userId, mock(PersonalInfo.class), "other@example.com", UserStatus.INACTIVE);
            assertEquals(user, user2);
            assertEquals(user.hashCode(), user2.hashCode());
        }

        @Test
        @DisplayName("should not be equal for different IDs")
        void shouldNotBeEqualForDifferentIds() {
            TestUser user2 = new TestUser(UserId.generate(), personalInfo, validEmail, UserStatus.ACTIVE);
            assertNotEquals(user, user2);
        }

        @Test
        @DisplayName("should not be equal to null")
        void shouldNotBeEqualToNull() {
            assertNotEquals(null, user);
        }

        @Test
        @DisplayName("should not be equal to other types")
        void shouldNotBeEqualToOtherTypes() {
            assertNotEquals(user, "not a user");
        }
    }

    @Nested
    @DisplayName("Timestamps")
    class TimestampManagement {
        @Test
        @DisplayName("should update last modified time on relevant changes")
        void shouldUpdateLastModifiedTime() {
            LocalDateTime initialModified = user.getLastModifiedAt();
            
            // Wait a small amount to ensure timestamp difference
            try { Thread.sleep(10); } catch (InterruptedException e) {}
            
            user.setStatus(UserStatus.INACTIVE);
            assertTrue(user.getLastModifiedAt().isAfter(initialModified),
                "LastModified should update after status change");
            
            LocalDateTime afterStatus = user.getLastModifiedAt();
            try { Thread.sleep(10); } catch (InterruptedException e) {}
            
            user.setEmail("new@example.com");
            assertTrue(user.getLastModifiedAt().isAfter(afterStatus),
                "LastModified should update after email change");
        }

        @Test
        @DisplayName("should preserve creation time")
        void shouldPreserveCreationTime() {
            LocalDateTime creationTime = user.getCreatedAt();
            
            user.setStatus(UserStatus.INACTIVE);
            user.setEmail("new@example.com");
            user.updatePersonalInfo(mock(PersonalInfo.class));
            
            assertEquals(creationTime, user.getCreatedAt(),
                "Creation time should remain unchanged");
        }
    }
}