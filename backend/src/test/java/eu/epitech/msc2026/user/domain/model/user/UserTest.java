package eu.epitech.msc2026.user.domain.model.user;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import eu.epitech.msc2026.user.domain.model.common.PersonalInfo;
import java.time.LocalDateTime;
import java.util.UUID;

@DisplayName("User")
class UserTest {
    
    // Concrete implementation for testing the abstract User class
    private static class TestUser extends User {
        public TestUser(UUID id, String email, String hashedPassword,
                       PersonalInfo personalInfo, UserStatus status) {
            super(id, email, hashedPassword, personalInfo, status);
        }
    }
    
    private UUID userId;
    private String validEmail;
    private String validHashedPassword;
    private PersonalInfo validPersonalInfo;
    private TestUser user;
    
    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        validEmail = "test@example.com";
        validHashedPassword = "hashedPassword123";
        validPersonalInfo = PersonalInfo.of("John", "Doe", "+33612345678");
    }
    
    @Nested
    @DisplayName("User Creation")
    class UserCreation {
        
        @Test
        @DisplayName("Given valid user data, when creating user, then user should be created successfully")
        void givenValidUserData_whenCreatingUser_thenUserShouldBeCreated() {
            // Given - valid data prepared in setUp()
            
            // When
            user = new TestUser(userId, validEmail, validHashedPassword, validPersonalInfo, UserStatus.ACTIVE);
            
            // Then
            assertEquals(userId, user.getId());
            assertEquals(validEmail, user.getEmail());
            assertEquals(validHashedPassword, user.getHashedPassword());
            assertEquals(validPersonalInfo, user.getPersonalInfo());
            assertEquals(UserStatus.ACTIVE, user.getStatus());
            assertNotNull(user.getCreatedAt());
            assertNotNull(user.getLastModifiedAt());
            assertNull(user.getLastLoginAt());
        }
        
        @Test
        @DisplayName("Given null id, when creating user, then should throw NullPointerException")
        void givenNullId_whenCreatingUser_thenShouldThrowException() {
            // Given
            UUID nullId = null;
            
            // When & Then
            assertThrows(NullPointerException.class, () -> 
                new TestUser(nullId, validEmail, validHashedPassword, validPersonalInfo, UserStatus.ACTIVE));
        }
    }
    
    @Nested
    @DisplayName("Email Management")
    class EmailManagement {
        
        @BeforeEach
        void setUp() {
            user = new TestUser(userId, validEmail, validHashedPassword, validPersonalInfo, UserStatus.ACTIVE);
        }
        
        @Test
        @DisplayName("Given valid email, when updating email, then email should be updated")
        void givenValidEmail_whenUpdatingEmail_thenEmailShouldBeUpdated() {
            // Given
            String newEmail = "new@example.com";
            LocalDateTime originalModifiedAt = user.getLastModifiedAt();
            
            // When
            user.setEmail(newEmail);
            
            // Then
            assertEquals(newEmail, user.getEmail());
            assertTrue(user.getLastModifiedAt().isAfter(originalModifiedAt));
        }
        
        @Test
        @DisplayName("Given invalid email format, when updating email, then should throw IllegalArgumentException")
        void givenInvalidEmailFormat_whenUpdatingEmail_thenShouldThrowException() {
            // Given
            String invalidEmail = "invalid-email";
            
            // When & Then
            assertThrows(IllegalArgumentException.class, () -> 
                user.setEmail(invalidEmail));
        }
    }
    
    @Nested
    @DisplayName("Status Management")
    class StatusManagement {
        
        @BeforeEach
        void setUp() {
            user = new TestUser(userId, validEmail, validHashedPassword, validPersonalInfo, UserStatus.ACTIVE);
        }
        
        @Test
        @DisplayName("Given active user, when deactivating, then status should be INACTIVE")
        void givenActiveUser_whenDeactivating_thenStatusShouldBeInactive() {
            // Given
            assertTrue(user.getStatus() == UserStatus.ACTIVE);
            LocalDateTime originalModifiedAt = user.getLastModifiedAt();
            
            // When
            user.deactivate();
            
            // Then
            assertEquals(UserStatus.INACTIVE, user.getStatus());
            assertTrue(user.getLastModifiedAt().isAfter(originalModifiedAt));
        }
        
        @Test
        @DisplayName("Given inactive user, when activating, then status should be ACTIVE")
        void givenInactiveUser_whenActivating_thenStatusShouldBeActive() {
            // Given
            user.deactivate();
            assertTrue(user.getStatus() == UserStatus.INACTIVE);
            LocalDateTime originalModifiedAt = user.getLastModifiedAt();
            
            // When
            user.activate();
            
            // Then
            assertEquals(UserStatus.ACTIVE, user.getStatus());
            assertTrue(user.getLastModifiedAt().isAfter(originalModifiedAt));
        }
    }
    
    @Nested
    @DisplayName("Personal Info Management")
    class PersonalInfoManagement {
        
        @BeforeEach
        void setUp() {
            user = new TestUser(userId, validEmail, validHashedPassword, validPersonalInfo, UserStatus.ACTIVE);
        }
        
        @Test
        @DisplayName("Given new valid personal info, when updating, then personal info should be updated")
        void givenNewValidPersonalInfo_whenUpdating_thenPersonalInfoShouldBeUpdated() {
            // Given
            PersonalInfo newPersonalInfo = PersonalInfo.of("Jane", "Smith", "+33687654321");
            LocalDateTime originalModifiedAt = user.getLastModifiedAt();
            
            // When
            user.setPersonalInfo(newPersonalInfo);
            
            // Then
            assertEquals(newPersonalInfo, user.getPersonalInfo());
            assertTrue(user.getLastModifiedAt().isAfter(originalModifiedAt));
        }
        
        @Test
        @DisplayName("Given null personal info, when updating, then should throw NullPointerException")
        void givenNullPersonalInfo_whenUpdating_thenShouldThrowException() {
            // When & Then
            assertThrows(NullPointerException.class, () -> 
                user.setPersonalInfo(null));
        }
    }
    
    @Nested
    @DisplayName("Password Management")
    class PasswordManagement {
        
        @BeforeEach
        void setUp() {
            user = new TestUser(userId, validEmail, validHashedPassword, validPersonalInfo, UserStatus.ACTIVE);
        }
        
        @Test
        @DisplayName("Given new hashed password, when updating, then password should be updated")
        void givenNewHashedPassword_whenUpdating_thenPasswordShouldBeUpdated() {
            // Given
            String newHashedPassword = "newHashedPassword456";
            LocalDateTime originalModifiedAt = user.getLastModifiedAt();
            
            // When
            user.setHashedPassword(newHashedPassword);
            
            // Then
            assertEquals(newHashedPassword, user.getHashedPassword());
            assertTrue(user.getLastModifiedAt().isAfter(originalModifiedAt));
        }
    }
}