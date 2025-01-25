package com.trinity.auth.model;

import static org.junit.jupiter.api.Assertions.*;
import java.time.Instant;
import java.util.Collection;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.trinity.auth.constant.UserRole;
import com.trinity.auth.constant.UserStatus;


class AbstractUserTest {

    private static class ConcreteUser extends AbstractUser {
        @Override
        public UserRole getRole() {
            return UserRole.EMPLOYEE;
        }
    }

    @Test
    void testUpdateLastLogin() {
        // Given
        ConcreteUser user = new ConcreteUser();
        Instant beforeUpdate = user.getLastLoginAt();

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

    @Test
    void testGetAuthorities() {
        // Given
        ConcreteUser user = new ConcreteUser();

        // When
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // Then
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority(UserRole.EMPLOYEE.name())));
    }

    @Test
    void testGetUsername() {
        // Given
        ConcreteUser user = new ConcreteUser();
        user.setEmail("user@example.com");

        // When
        String username = user.getUsername();

        // Then
        assertEquals("user@example.com", username);
    }

    @Test
    void testGetPassword() {
        // Given
        ConcreteUser user = new ConcreteUser();
        user.setHashedPassword("hashedPassword");

        // When
        String password = user.getPassword();

        // Then
        assertEquals("hashedPassword", password);
    }

    @Test
    void testIsAccountNonExpired() {
        // Given
        ConcreteUser user = new ConcreteUser();
        user.setStatus(UserStatus.ACTIVE);

        // When
        boolean isNonExpired = user.isAccountNonExpired();

        // Then
        assertTrue(isNonExpired);
    }

    @Test
    void testIsAccountNonLocked() {
        // Given
        ConcreteUser user = new ConcreteUser();
        user.setStatus(UserStatus.ACTIVE);

        // When
        boolean isNonLocked = user.isAccountNonLocked();

        // Then
        assertTrue(isNonLocked);
    }

    @Test
    void testIsEnabled() {
        // Given
        ConcreteUser user = new ConcreteUser();

        // When
        boolean isEnabled = user.isEnabled();

        // Then
        assertTrue(isEnabled);
    }
}