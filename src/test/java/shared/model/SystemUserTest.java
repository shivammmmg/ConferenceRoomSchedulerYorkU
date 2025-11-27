package shared.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SystemUserTest – High-Coverage Manual Test Suite
 * --------------------------------------------------------------------
 * Validates behavior of the SystemUser class, which extends {@link User}
 * and serves as the concrete user model used across all system scenarios.
 *
 * <h2>Coverage</h2>
 * <ul>
 *     <li>Constructor behavior</li>
 *     <li>Getter/setter correctness</li>
 *     <li>Null-handling for optional fields</li>
 *     <li>Activation / deactivation inherited from User</li>
 *     <li>CSV restoration fields (userId, createdAt)</li>
 *     <li>toString() formatting</li>
 * </ul>
 */
public class SystemUserTest {

    private SystemUser user;

    @BeforeEach
    void setup() {
        user = new SystemUser(
                "Alice",
                "alice@test.com",
                "hashed12345",
                UserType.STUDENT,
                "ORG-1",
                "ST123"
        );
    }

    // -----------------------------------------------------------
    // CONSTRUCTOR TESTS
    // -----------------------------------------------------------

    @Test
    void testConstructorSetsFieldsCorrectly() {
        assertEquals("Alice", user.getName());
        assertEquals("alice@test.com", user.getEmail());
        assertEquals("hashed12345", user.getPasswordHash());
        assertEquals(UserType.STUDENT, user.getType());
        assertEquals("ORG-1", user.getOrgId());
        assertEquals("ST123", user.getStudentId());
        assertTrue(user.isActive());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUserId());
    }

    @Test
    void testConstructorNullOrgIdDefaultsToEmptyString() {
        SystemUser u = new SystemUser(
                "Bob",
                "bob@test.com",
                "pass12345",
                UserType.FACULTY,
                null,
                "ST999"
        );
        assertEquals("", u.getOrgId());
    }

    // -----------------------------------------------------------
    // GETTERS & SETTERS
    // -----------------------------------------------------------

    @Test
    void testSetTypeUpdatesCorrectly() {
        user.setType(UserType.ADMIN);
        assertEquals(UserType.ADMIN, user.getType());
    }

    @Test
    void testSetOrgIdUpdatesCorrectly() {
        user.setOrgId("NEW-ORG");
        assertEquals("NEW-ORG", user.getOrgId());
    }

    @Test
    void testSetStudentIdUpdatesCorrectly() {
        user.setStudentId("NEW-ST-ID");
        assertEquals("NEW-ST-ID", user.getStudentId());
    }

    @Test
    void testSetNameInheritedFromUser() {
        user.setName("NewName");
        assertEquals("NewName", user.getName());
    }

    @Test
    void testSetEmailInheritedFromUser() {
        user.setEmail("newemail@test.com");
        assertEquals("newemail@test.com", user.getEmail());
    }

    @Test
    void testSetPasswordHashInheritedFromUser() {
        user.setPasswordHash("newhash123");
        assertEquals("newhash123", user.getPasswordHash());
    }

    // -----------------------------------------------------------
    // INHERITED USER BEHAVIOR
    // -----------------------------------------------------------

    @Test
    void testDeactivateMakesUserInactive() {
        user.deactivate();
        assertFalse(user.isActive());
    }

    @Test
    void testActivateMakesUserActiveAgain() {
        user.deactivate();
        user.activate();
        assertTrue(user.isActive());
    }

    // -----------------------------------------------------------
    // CSV RESTORATION FIELD TESTS
    // -----------------------------------------------------------

    @Test
    void testSetUserIdOverridesExistingUUID() {
        UUID newId = UUID.randomUUID();
        user.setUserId(newId);
        assertEquals(newId, user.getUserId());
    }

    @Test
    void testSetCreatedAtOverridesTimestamp() {
        LocalDateTime now = LocalDateTime.now().minusDays(3);
        user.setCreatedAt(now);
        assertEquals(now, user.getCreatedAt());
    }

    // -----------------------------------------------------------
    // UTILITY METHODS
    // -----------------------------------------------------------

    @Test
    void testIsYorkUEmailValid() {
        SystemUser u = new SystemUser(
                "York Student",
                "test@my.yorku.ca",
                "pass12345",
                UserType.STUDENT,
                "",
                "ST100"
        );
        assertTrue(u.isYorkUEmail());
    }

    @Test
    void testIsYorkUEmailInvalid() {
        assertFalse(user.isYorkUEmail());  // original email is alice@test.com
    }

    @Test
    void testUserValidationPasses() {
        assertTrue(user.validate());
    }

    @Test
    void testUserValidationFailsForBadEmail() {
        user.setEmail("notanemail");
        assertFalse(user.validate());
    }

    @Test
    void testUserValidationFailsForShortPassword() {
        user.setPasswordHash("short");
        assertFalse(user.validate());
    }

    @Test
    void testUserValidationFailsWhenNameBlank() {
        user.setName("   ");
        assertFalse(user.validate());
    }

    // -----------------------------------------------------------
    // TO STRING TEST
    // -----------------------------------------------------------

    @Test
    void testToStringFormattingContainsAllFields() {
        String output = user.toString();

        assertTrue(output.contains(user.getUserId().toString()));
        assertTrue(output.contains("Alice"));
        assertTrue(output.contains("alice@test.com"));
        assertTrue(output.contains("hashed12345"));
        assertTrue(output.contains("STUDENT"));
        assertTrue(output.contains("ORG-1"));
        assertTrue(output.contains("ST123"));
        assertTrue(output.contains(String.valueOf(user.isActive())));
        assertTrue(output.contains(user.getCreatedAt().toString()));
    }
}
