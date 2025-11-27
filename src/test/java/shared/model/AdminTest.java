package shared.model;

import javafx.beans.property.StringProperty;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AdminTest — Unit tests for the {@link Admin} domain model.
 * -----------------------------------------------------------------------
 * <p>This class validates the behaviour of the {@code Admin} model used in
 * the Conference Room Scheduler system. The Admin class encapsulates user
 * account information such as username, password, status, and role. It also
 * exposes JavaFX {@link StringProperty} fields for GUI binding.</p>
 *
 * <h2>Test Coverage Goals</h2>
 * <ul>
 *     <li>Verify all constructors correctly initialize fields</li>
 *     <li>Ensure status/role setters update values properly</li>
 *     <li>Validate property bindings for JavaFX UI integration</li>
 *     <li>Confirm {@code toString()} returns valid CSV output</li>
 *     <li>Achieve 100% line and method coverage for the {@link Admin} class</li>
 * </ul>
 */
class AdminTest {

    /**
     * Test 1 — Ensures the 2-argument constructor correctly sets
     * username and password, while applying default:
     * <ul>
     *     <li>status = ACTIVE</li>
     *     <li>role = ADMIN</li>
     * </ul>
     */
    @Test
    void testTwoArgConstructorSetsDefaults() {
        Admin admin = new Admin("alice", "secret");

        assertEquals("alice", admin.getUsername());
        assertEquals("secret", admin.getPassword());
        assertEquals("ACTIVE", admin.getStatus());
        assertEquals("ADMIN", admin.getRole());
    }

    /**
     * Test 2 — Validates that the 3-argument constructor correctly applies
     * the explicitly provided role value.
     */
    @Test
    void testThreeArgConstructorSetsRole() {
        Admin admin = new Admin("bob", "pwd123", "CHIEF");

        assertEquals("bob", admin.getUsername());
        assertEquals("pwd123", admin.getPassword());
        assertEquals("ACTIVE", admin.getStatus());
        assertEquals("CHIEF", admin.getRole());
    }

    /**
     * Test 3 — Confirms the 3-argument constructor defaults the role to
     * {@code ADMIN} when {@code null} is passed.
     */
    @Test
    void testThreeArgConstructorNullRoleDefaultsToAdmin() {
        Admin admin = new Admin("carol", "pass", null);

        assertEquals("carol", admin.getUsername());
        assertEquals("pass", admin.getPassword());
        assertEquals("ACTIVE", admin.getStatus());
        assertEquals("ADMIN", admin.getRole());
    }

    /**
     * Test 4 — Ensures {@link Admin#setStatus(String)} properly updates
     * the status field.
     */
    @Test
    void testSetStatusUpdatesStatus() {
        Admin admin = new Admin("dave", "pw");
        admin.setStatus("DISABLED");

        assertEquals("DISABLED", admin.getStatus());
    }

    /**
     * Test 5 — Ensures {@link Admin#setRole(String)} updates the role.
     */
    @Test
    void testSetRoleUpdatesRole() {
        Admin admin = new Admin("emma", "pw");
        admin.setRole("CHIEF_EVENT_COORDINATOR");

        assertEquals("CHIEF_EVENT_COORDINATOR", admin.getRole());
    }

    /**
     * Test 6 — Verifies that {@link Admin#usernameProperty()} is not null
     * and that the property value matches the username getter.
     */
    @Test
    void testUsernamePropertyReflectsValue() {
        Admin admin = new Admin("frank", "pw");
        StringProperty prop = admin.usernameProperty();

        assertNotNull(prop);
        assertEquals("frank", prop.get());
        assertEquals("frank", admin.getUsername());
    }

    /**
     * Test 7 — Ensures {@link Admin#passwordProperty()} returns a valid
     * property object that reflects the stored password.
     */
    @Test
    void testPasswordPropertyReflectsValue() {
        Admin admin = new Admin("gina", "topsecret");
        StringProperty prop = admin.passwordProperty();

        assertNotNull(prop);
        assertEquals("topsecret", prop.get());
        assertEquals("topsecret", admin.getPassword());
    }

    /**
     * Test 8 — Confirms {@link Admin#statusProperty()} is linked
     * bidirectionally with {@link Admin#getStatus()}.
     */
    @Test
    void testStatusPropertyLinkedToGetter() {
        Admin admin = new Admin("henry", "pw");
        StringProperty statusProp = admin.statusProperty();

        assertNotNull(statusProp);
        assertEquals("ACTIVE", statusProp.get());

        // Modify property → getter should reflect change
        statusProp.set("SUSPENDED");
        assertEquals("SUSPENDED", admin.getStatus());
    }

    /**
     * Test 9 — Ensures {@link Admin#roleProperty()} is linked
     * to the role getter and reflects updates.
     */
    @Test
    void testRolePropertyLinkedToGetter() {
        Admin admin = new Admin("irene", "pw", "ADMIN");
        StringProperty roleProp = admin.roleProperty();

        assertNotNull(roleProp);
        assertEquals("ADMIN", roleProp.get());

        roleProp.set("CHIEF");
        assertEquals("CHIEF", admin.getRole());
    }

    /**
     * Test 10 — Validates the {@link Admin#toString()} method returns
     * the correct CSV representation of the admin object in the form:
     * <pre>
     * username,password,status,role
     * </pre>
     */
    @Test
    void testToStringCsvFormat() {
        Admin admin = new Admin("john", "pw123", "CHIEF");
        admin.setStatus("DISABLED");

        String csv = admin.toString();
        assertEquals("john,pw123,DISABLED,CHIEF", csv);
    }
}
