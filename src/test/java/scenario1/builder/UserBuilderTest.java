package scenario1.builder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shared.model.SystemUser;
import shared.model.User;
import shared.model.UserType;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserBuilderTest – Manual JUnit Test Cases for Scenario 1
 * --------------------------------------------------------------------
 * <p>This test suite validates the behaviour of the {@link UserBuilder}
 * class, which implements the Builder Pattern to construct
 * {@link SystemUser} objects during the registration workflow.</p>
 *
 * <h2>What This Test Class Covers</h2>
 * <ul>
 *     <li>All fluent setter methods (name, email, password, userType)</li>
 *     <li>Validation behaviour when required fields are missing</li>
 *     <li>Default behaviour for optional fields (orgId, studentId)</li>
 *     <li>Correct mapping of builder state into a final SystemUser object</li>
 *     <li>Exception handling when build() is invoked with incomplete data</li>
 * </ul>
 *
 * <p>These tests are part of the deliverable requirements ensuring:</p>
 * <ul>
 *     <li>≥10 test cases per non-GUI class</li>
 *     <li>≥80% code coverage on manual test suite</li>
 * </ul>
 */
public class UserBuilderTest {

    private UserBuilder builder;

    /**
     * Initializes a fresh UserBuilder object before each test.
     */
    @BeforeEach
    void setup() {
        builder = new UserBuilder();
    }

    /**
     * Ensures that calling setName() correctly updates the builder state.
     */
    @Test
    void testSetNameCorrectly() {
        builder.setName("John Doe");
        User user = builder
                .setEmail("john@test.com")
                .setPassword("abc12345")
                .setUserType(UserType.STUDENT)
                .build();

        assertEquals("John Doe", user.getName());
    }

    /**
     * Verifies that setEmail() correctly stores the user email.
     */
    @Test
    void testSetEmailCorrectly() {
        builder.setName("John")
                .setEmail("john@test.com")
                .setPassword("abc12345")
                .setUserType(UserType.FACULTY);

        User user = builder.build();
        assertEquals("john@test.com", user.getEmail());
    }

    /**
     * Tests that setPassword() properly records the password hash.
     */
    @Test
    void testSetPasswordCorrectly() {
        builder.setName("John")
                .setEmail("john@test.com")
                .setPassword("hashed123")
                .setUserType(UserType.PARTNER);

        User user = builder.build();
        assertEquals("hashed123", user.getPasswordHash());
    }

    /**
     * Confirms that UserType defaults to PARTNER if not explicitly set.
     */
    @Test
    void testDefaultUserTypeIsPartner() {
        builder.setName("Alex")
                .setEmail("alex@test.com")
                .setPassword("pass987");

        User user = builder.build();
        assertEquals(UserType.PARTNER, ((SystemUser) user).getType());
    }

    /**
     * Ensures studentId defaults to empty string when null is provided.
     */
    @Test
    void testStudentIdDefaultsToEmptyStringWhenNull() {
        builder.setName("Alex")
                .setEmail("alex@test.com")
                .setPassword("pass987")
                .setUserType(UserType.STUDENT)
                .setStudentId(null);

        User user = builder.build();
        assertEquals("", ((SystemUser) user).getStudentId());
    }

    /**
     * Ensures orgId defaults to empty string when null is provided.
     */
    @Test
    void testOrgIdDefaultsToEmptyStringWhenNull() {
        builder.setName("Alex")
                .setEmail("alex@test.com")
                .setPassword("pass987")
                .setUserType(UserType.STAFF)
                .setOrgId(null);

        User user = builder.build();
        assertEquals("", ((SystemUser) user).getOrgId());
    }

    /**
     * Verifies that calling setUserType() properly stores the user role.
     */
    @Test
    void testSetUserTypeCorrectly() {
        builder.setName("Chris")
                .setEmail("chris@test.com")
                .setPassword("123abc456")
                .setUserType(UserType.ADMIN);

        User user = builder.build();
        assertEquals(UserType.ADMIN, ((SystemUser) user).getType());
    }

    /**
     * Tests that build() throws an exception when name is missing.
     */
    @Test
    void testMissingName_ThrowsException() {
        builder.setEmail("a@test.com")
                .setPassword("12345678")
                .setUserType(UserType.STUDENT);

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    /**
     * Tests that build() throws an exception when email is missing.
     */
    @Test
    void testMissingEmail_ThrowsException() {
        builder.setName("Test User")
                .setPassword("12345678")
                .setUserType(UserType.STAFF);

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    /**
     * Tests that build() throws an exception when password is missing.
     */
    @Test
    void testMissingPassword_ThrowsException() {
        builder.setName("Test User")
                .setEmail("test@test.com")
                .setUserType(UserType.PARTNER);

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    /**
     * Tests that build() throws an exception when UserType is missing.
     */
    @Test
    void testMissingUserType_ThrowsException() {
        builder.setName("Test User")
                .setEmail("test@test.com")
                .setPassword("pass1234");

        // Explicitly set type to null to trigger exception
        builder.setUserType(null);

        assertThrows(IllegalStateException.class, () -> builder.build());
    }
}
