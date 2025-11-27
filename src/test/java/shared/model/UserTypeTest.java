package shared.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserTypeTest – Full Coverage Enum Test Suite
 * ------------------------------------------------------------------------
 * Ensures all enum constants for {@link UserType} behave correctly and
 * validates:
 *   • All values are present
 *   • valueOf() works for every constant
 *   • ordinals remain stable
 *   • name() returns expected string
 *   • values() returns a fixed, ordered array
 */
public class UserTypeTest {

    @Test
    void testEnumContainsAllExpectedValues() {
        UserType[] values = UserType.values();

        assertEquals(6, values.length);

        assertArrayEquals(new UserType[]{
                UserType.STUDENT,
                UserType.FACULTY,
                UserType.STAFF,
                UserType.PARTNER,
                UserType.ADMIN,
                UserType.CHIEF_EVENT_COORDINATOR
        }, values);
    }

    @Test
    void testValueOfEachEnumConstant() {
        assertEquals(UserType.STUDENT, UserType.valueOf("STUDENT"));
        assertEquals(UserType.FACULTY, UserType.valueOf("FACULTY"));
        assertEquals(UserType.STAFF, UserType.valueOf("STAFF"));
        assertEquals(UserType.PARTNER, UserType.valueOf("PARTNER"));
        assertEquals(UserType.ADMIN, UserType.valueOf("ADMIN"));
        assertEquals(UserType.CHIEF_EVENT_COORDINATOR, UserType.valueOf("CHIEF_EVENT_COORDINATOR"));
    }

    @Test
    void testValueOfThrowsForInvalidName() {
        assertThrows(IllegalArgumentException.class, () ->
                UserType.valueOf("NOT_A_REAL_ROLE"));
    }

    @Test
    void testOrdinalValuesStayStable() {
        assertEquals(0, UserType.STUDENT.ordinal());
        assertEquals(1, UserType.FACULTY.ordinal());
        assertEquals(2, UserType.STAFF.ordinal());
        assertEquals(3, UserType.PARTNER.ordinal());
        assertEquals(4, UserType.ADMIN.ordinal());
        assertEquals(5, UserType.CHIEF_EVENT_COORDINATOR.ordinal());
    }

    @Test
    void testNameMatchesEnumDeclaration() {
        assertEquals("STUDENT", UserType.STUDENT.name());
        assertEquals("FACULTY", UserType.FACULTY.name());
        assertEquals("STAFF", UserType.STAFF.name());
        assertEquals("PARTNER", UserType.PARTNER.name());
        assertEquals("ADMIN", UserType.ADMIN.name());
        assertEquals("CHIEF_EVENT_COORDINATOR", UserType.CHIEF_EVENT_COORDINATOR.name());
    }

    @Test
    void testEnumIsSingletonForEachConstant() {
        // Enum constants must always reference the same instance.
        assertSame(UserType.STUDENT, UserType.STUDENT);
        assertSame(UserType.FACULTY, UserType.valueOf("FACULTY"));
    }
}
