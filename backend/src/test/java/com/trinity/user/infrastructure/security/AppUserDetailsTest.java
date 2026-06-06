package com.trinity.user.infrastructure.security;

import com.trinity.user.domain.model.UserStatus;
import com.trinity.user.domain.model.UserType;
import com.trinity.user.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppUserDetailsTest {

    private static class ConcreteUser extends User {
        @Override
        public UserType getType() {
            return UserType.EMPLOYEE;
        }
    }

    private AppUserDetails wrap(UserStatus status) {
        ConcreteUser user = new ConcreteUser();
        user.setEmail("user@example.com");
        user.setHashedPassword("hashedPassword");
        user.setStatus(status);
        return new AppUserDetails(user);
    }

    @Test
    void getUsername_returnsEmail() {
        assertEquals("user@example.com", wrap(UserStatus.ACTIVE).getUsername());
    }

    @Test
    void getPassword_returnsHashedPassword() {
        assertEquals("hashedPassword", wrap(UserStatus.ACTIVE).getPassword());
    }

    @Test
    void getAuthorities_containsUserType() {
        var authorities = wrap(UserStatus.ACTIVE).getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority(UserType.EMPLOYEE.name())));
    }

    @Test
    void isEnabled_trueOnlyWhenActive() {
        assertTrue(wrap(UserStatus.ACTIVE).isEnabled());
        assertFalse(wrap(UserStatus.INACTIVE).isEnabled());
        assertFalse(wrap(UserStatus.LOCKED).isEnabled());
        assertFalse(wrap(UserStatus.EXPIRED).isEnabled());
        assertFalse(wrap(UserStatus.DELETED).isEnabled());
    }

    @Test
    void isAccountNonLocked_falseWhenLocked() {
        assertTrue(wrap(UserStatus.ACTIVE).isAccountNonLocked());
        assertFalse(wrap(UserStatus.LOCKED).isAccountNonLocked());
    }

    @Test
    void isAccountNonExpired_falseWhenExpired() {
        assertTrue(wrap(UserStatus.ACTIVE).isAccountNonExpired());
        assertFalse(wrap(UserStatus.EXPIRED).isAccountNonExpired());
    }

    @Test
    void getDomainUser_exposesWrappedEntity() {
        ConcreteUser user = new ConcreteUser();
        user.setEmail("x@y.z");
        AppUserDetails details = new AppUserDetails(user);
        assertSame(user, details.getDomainUser());
    }
}
