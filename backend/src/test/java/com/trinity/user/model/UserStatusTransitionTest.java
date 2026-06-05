package com.trinity.user.model;

import com.trinity.common.domain.exception.BusinessRuleViolation;
import com.trinity.user.constant.UserStatus;
import com.trinity.user.constant.UserType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Validates the UserStatus state machine enforced by AbstractUser.
 */
class UserStatusTransitionTest {

    private static class ConcreteUser extends AbstractUser {
        @Override
        public UserType getType() {
            return UserType.EMPLOYEE;
        }
    }

    private ConcreteUser userWithStatus(UserStatus status) {
        ConcreteUser user = new ConcreteUser();
        user.setStatus(status);
        return user;
    }

    @Test
    void activate_setsStatusActive() {
        ConcreteUser user = userWithStatus(UserStatus.INACTIVE);
        user.activate();
        assertEquals(UserStatus.ACTIVE, user.getStatus());
    }

    @Test
    void activate_fromLocked_isAllowed() {
        ConcreteUser user = userWithStatus(UserStatus.LOCKED);
        user.activate();
        assertEquals(UserStatus.ACTIVE, user.getStatus());
    }

    @Test
    void activate_fromDeleted_isRejected() {
        ConcreteUser user = userWithStatus(UserStatus.DELETED);
        assertThrows(BusinessRuleViolation.class, user::activate);
    }

    @Test
    void deactivate_setsStatusInactive() {
        ConcreteUser user = userWithStatus(UserStatus.ACTIVE);
        user.deactivate();
        assertEquals(UserStatus.INACTIVE, user.getStatus());
    }

    @Test
    void deactivate_fromDeleted_isRejected() {
        ConcreteUser user = userWithStatus(UserStatus.DELETED);
        assertThrows(BusinessRuleViolation.class, user::deactivate);
    }

    @Test
    void lock_setsStatusLocked() {
        ConcreteUser user = userWithStatus(UserStatus.ACTIVE);
        user.lock();
        assertEquals(UserStatus.LOCKED, user.getStatus());
    }

    @Test
    void lock_fromDeleted_isRejected() {
        ConcreteUser user = userWithStatus(UserStatus.DELETED);
        assertThrows(BusinessRuleViolation.class, user::lock);
    }

    @Test
    void markDeleted_isAlwaysAllowed_andTerminal() {
        ConcreteUser user = userWithStatus(UserStatus.ACTIVE);
        user.markDeleted();
        assertEquals(UserStatus.DELETED, user.getStatus());
    }

    @Test
    void markDeleted_fromAnyNonDeleted_isAllowed() {
        ConcreteUser user = userWithStatus(UserStatus.LOCKED);
        user.markDeleted();
        assertEquals(UserStatus.DELETED, user.getStatus());
    }
}
