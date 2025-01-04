package eu.epitech.msc2026.user.domain.model.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserId Tests")
class UserIdTest {

    @Nested
    @DisplayName("UserId.generate()")
    class Generate {
        @Test
        @DisplayName("should generate unique IDs")
        void shouldGenerateUniqueIds() {
            UserId id1 = UserId.generate();
            UserId id2 = UserId.generate();

            assertNotNull(id1);
            assertNotNull(id2);
            assertNotEquals(id1, id2);
        }
    }

    @Nested
    @DisplayName("UserId.of()")
    class Of {
        @Test
        @DisplayName("should create UserId from UUID")
        void shouldCreateUserIdFromUUID() {
            UUID uuid = UUID.randomUUID();
            UserId userId = UserId.of(uuid);

            assertNotNull(userId);
            assertEquals(uuid, userId.getValue());
        }

        @Test
        @DisplayName("should throw NullPointerException when UUID is null")
        void shouldThrowNullPointerException() {
            assertThrows(NullPointerException.class, () -> UserId.of(null));
        }
    }

    @Nested
    @DisplayName("UserId.fromString()")
    class FromString {
        @Test
        @DisplayName("should create UserId from valid UUID string")
        void shouldCreateUserIdFromValidString() {
            String validUuid = "550e8400-e29b-41d4-a716-446655440000";
            UserId userId = UserId.fromString(validUuid);

            assertNotNull(userId);
            assertEquals(validUuid, userId.toString());
        }

        @Test
        @DisplayName("should throw IllegalArgumentException for invalid UUID string")
        void shouldThrowIllegalArgumentExceptionForInvalidString() {
            assertThrows(IllegalArgumentException.class, () -> UserId.fromString("invalid-uuid"));
        }

        @Test
        @DisplayName("should throw NullPointerException when string is null")
        void shouldThrowNullPointerExceptionForNullString() {
            assertThrows(NullPointerException.class, () -> UserId.fromString(null));
        }
    }

    @Nested
    @DisplayName("equals() and hashCode()")
    class EqualsAndHashCode {
        @Test
        @DisplayName("equal UserIds should be equal and have same hashCode")
        void shouldBeEqualAndHaveSameHashCodeForSameUUID() {
            UUID uuid = UUID.randomUUID();
            UserId id1 = UserId.of(uuid);
            UserId id2 = UserId.of(uuid);

            assertEquals(id1, id2);
            assertEquals(id1.hashCode(), id2.hashCode());
        }

        @Test
        @DisplayName("different UserIds should not be equal")
        void shouldNotBeEqualForDifferentUUIDs() {
            UserId id1 = UserId.of(UUID.randomUUID());
            UserId id2 = UserId.of(UUID.randomUUID());

            assertNotEquals(id1, id2);
        }

        @Test
        @DisplayName("UserId should not be equal to null")
        void shouldNotBeEqualToNull() {
            UserId userId = UserId.generate();
            assertNotEquals(null, userId);
        }

        @Test
        @DisplayName("UserId should not be equal to other types")
        void shouldNotBeEqualToOtherTypes() {
            UserId userId = UserId.generate();
            assertNotEquals(userId, userId.getValue());
        }
    }

    @Nested
    @DisplayName("toString()")
    class ToStringTest {
        @Test
        @DisplayName("should return UUID string representation")
        void shouldReturnUUIDString() {
            UUID uuid = UUID.randomUUID();
            UserId userId = UserId.of(uuid);

            assertEquals(uuid.toString(), userId.toString());
        }
    }
}