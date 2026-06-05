package com.trinity.user.model;

import static org.junit.jupiter.api.Assertions.*;
import java.time.Instant;
import org.junit.jupiter.api.Test;

import com.trinity.user.constant.UserStatus;
import com.trinity.user.constant.UserType;


class AbstractUserTest {

    private static class ConcreteUser extends AbstractUser {
        @Override
        public UserType getType() {
            return UserType.EMPLOYEE;
        }
    }

    @Test
    void testUpdateLastLogin() {
        // Given
        ConcreteUser user = new ConcreteUser();
        user.setLastLoginAt(Instant.now()); 
        Instant beforeUpdate = user.getLastLoginAt();

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // When
        user.updateLastLogin();
        Instant afterUpdate = user.getLastLoginAt();

        // Then
        assertNotNull(afterUpdate);
        assertTrue(afterUpdate.isAfter(beforeUpdate));
    }

    @Test
    void testSetStatusInactive() {
        // Given
        ConcreteUser user = new ConcreteUser();

        // When
        user.setStatusInactive();

        // Then
        assertEquals(UserStatus.INACTIVE, user.getStatus());
    }

    @Test
    void testSetStatusActive() {
        // Given
        ConcreteUser user = new ConcreteUser();

        // When
        user.setStatusActive();

        // Then
        assertEquals(UserStatus.ACTIVE, user.getStatus());
    }

    @Test
    void testAccountIsLocked() {
        // Given
        ConcreteUser user = new ConcreteUser();
        user.setStatus(UserStatus.LOCKED);

        // When
        boolean isLocked = user.accountIsLocked();

        // Then
        assertTrue(isLocked);
    }

    @Test
    void testAccountIsActive() {
        // Given
        ConcreteUser user = new ConcreteUser();
        user.setStatus(UserStatus.ACTIVE);

        // When
        boolean isActive = user.accountIsActive();

        // Then
        assertTrue(isActive);
    }

    @Test
    void testAccountIsInactive() {
        // Given
        ConcreteUser user = new ConcreteUser();
        user.setStatus(UserStatus.INACTIVE);

        // When
        boolean isInactive = user.accountIsInactive();

        // Then
        assertTrue(isInactive);
    }

    @Test
    void testAccountIsExpired() {
        // Given
        ConcreteUser user = new ConcreteUser();
        user.setStatus(UserStatus.EXPIRED);

        // When
        boolean isExpired = user.accountIsExpired();

        // Then
        assertTrue(isExpired);
    }
}