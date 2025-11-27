package shared.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserTest – High-Coverage Manual Test Suite
 * --------------------------------------------------------------------
 * Tests the abstract {@link User} base class by using a minimal
 * concrete subclass (TestUser). Covers:
 *
 *  • Constructor initialization
 *  • All setters & getters
 *  • Activation / deactivation logic
 *  • isYorkUEmail() behaviour
 *  • validate() rules for multiple cases
 *  • toString() format sanity check
 */
public class UserTest {

    /**
     * Minimal concrete subclass for testing.
     */
    static class TestUser extends User {
        public TestUser(String name, String email, String passwordHash) {
            super(name, email, passwordHash);
        }
    }

    private TestUser user;

    @BeforeEach
    void setup() {
        user = new TestUser("Alice", "alice@test.com", "password123");
    }

    // -----------------------------------------------------------
    // CONSTRUCTOR TESTS
    // -----------------------------------------------------------

    @Test
    void testConstructorInitializesFields() {
        assertNotNull(user.getUserId());
        assertEquals("Alice", user.getName());
        assertEquals("alice@test.com", user.getEmail());
        assertEquals("password123", user.getPasswordHash());
        assertTrue(user.isActive());
        assertNotNull(user.getCreatedAt());
    }

    @Test
    void testUniqueUserId() {
        TestUser u2 = new TestUser("Bob", "b@test.com", "pass12345");
        assertNotEquals(user.getUserId(), u2.getUserId());
    }

    // -----------------------------------------------------------
    // GETTERS/SETTERS
    // -----------------------------------------------------------

    @Test
    void testSetNameUpdatesName() {
        user.setName("NewName");
        assertEquals("NewName", user.getName());
    }

    @Test
    void testSetEmailUpdatesEmail() {
        user.setEmail("new@test.com");
        assertEquals("new@test.com", user.getEmail());
    }

    @Test
    void testSetPasswordHashUpdatesPassword() {
        user.setPasswordHash("newPassword999");
        assertEquals("newPassword999", user.getPasswordHash());
    }

    // -----------------------------------------------------------
    // ACTIVATION / DEACTIVATION
    // -----------------------------------------------------------

    @Test
    void testDeactivateUser() {
        user.deactivate();
        assertFalse(user.isActive());
    }

    @Test
    void testActivateUser() {
        user.deactivate();
        user.activate();
        assertTrue(user.isActive());
    }

    @Test
    void testSetActiveDirectly() {
        user.setActive(false);
        assertFalse(user.isActive());

        user.setActive(true);
        assertTrue(user.isActive());
    }

    // -----------------------------------------------------------
    // EMAIL VALIDATION (YorkU)
    // -----------------------------------------------------------

    @Test
    void testYorkUEmail_TrueForYorku() {
        TestUser u = new TestUser("A", "student@yorku.ca", "password123");
        assertTrue(u.isYorkUEmail());
    }

    @Test
    void testYorkUEmail_TrueForMyYorku() {
        TestUser u = new TestUser("A", "abc@my.yorku.ca", "password123");
        assertTrue(u.isYorkUEmail());
    }

    @Test
    void testYorkUEmail_FalseForOtherDomains() {
        assertFalse(user.isYorkUEmail());
    }

    // -----------------------------------------------------------
    // VALIDATE() METHOD
    // -----------------------------------------------------------

    @Test
    void testValidateReturnsTrueForValidUser() {
        assertTrue(user.validate());
    }

    @Test
    void testValidateFailsWhenNameMissing() {
        user.setName("");
        assertFalse(user.validate());
    }

    @Test
    void testValidateFailsWhenEmailMissing() {
        user.setEmail(null);
        assertFalse(user.validate());
    }

    @Test
    void testValidateFailsWhenEmailInvalid() {
        user.setEmail("not-an-email");
        assertFalse(user.validate());
    }

    @Test
    void testValidateFailsWhenPasswordTooShort() {
        user.setPasswordHash("short");
        assertFalse(user.validate());
    }

    // -----------------------------------------------------------
    // toString() FORMAT
    // -----------------------------------------------------------

    @Test
    void testToStringContainsUserIdNameEmail() {
        String output = user.toString();
        assertTrue(output.contains(user.getUserId().toString()));
        assertTrue(output.contains("Alice"));
        assertTrue(output.contains("alice@test.com"));
    }

    @Test
    void testCreatedAtIsRecent() {
        LocalDateTime created = user.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();

        assertTrue(created.isBefore(now.plusSeconds(1)));
    }
}
