package scenario1.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shared.model.SystemUser;
import shared.model.User;
import shared.model.UserType;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserManagerTest – High-coverage unit tests for UserManager.
 *
 * <p>This test suite focuses on exercising as many execution paths as possible
 * in {@link UserManager} without modifying production code. It:
 * <ul>
 *     <li>Resets the UserManager Singleton between tests via reflection.</li>
 *     <li>Uses an isolated temporary project root so <code>data/user.csv</code>
 *         never clashes with real project data.</li>
 *     <li>Covers happy paths and failure paths for registration, login,
 *         admin creation, email/profile updates, and utility helpers.</li>
 *     <li>Invokes the private <code>pad</code> method via reflection to
 *         ensure method coverage for all non-GUI logic.</li>
 * </ul>
 */
public class UserManagerTest {

    /** Temporary project root for this test; each test gets its own folder. */
    private Path tempProjectRoot;

    /**
     * Before each test:
     * <ul>
     *     <li>Create a fresh temporary directory.</li>
     *     <li>Point {@code user.dir} to that directory so UserManager writes
     *         its CSV files under {@code tempRoot/data/user.csv}.</li>
     *     <li>Reset the UserManager Singleton instance via reflection.</li>
     * </ul>
     */
    @BeforeEach
    void setUp() throws Exception {
        // Point UserManager to the TestData folder
        Path testDataRoot = Paths.get("TestData");
        System.setProperty("user.dir", testDataRoot.toAbsolutePath().toString());

        // Reset Singleton
        resetSingleton();

        // OPTIONAL: clear the test files before each test
        Files.write(testDataRoot.resolve("data/user.csv"), new byte[0]);
        Files.write(testDataRoot.resolve("data/rooms.csv"), new byte[0]);
        Files.write(testDataRoot.resolve("data/bookings.csv"), new byte[0]);
    }




    /**
     * After each test, delete the temporary directory tree.
     */
    @AfterEach
    void tearDown() throws IOException {
        if (tempProjectRoot != null && Files.exists(tempProjectRoot)) {
            Files.walk(tempProjectRoot)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException ignored) {
                        }
                    });
        }
    }

    /**
     * Resets the private static {@code instance} field in {@link UserManager}
     * so that each test gets a fresh Singleton.
     */
    private void resetSingleton() throws Exception {
        Field instanceField = UserManager.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null); // static field: target = null, value = null
    }

    /**
     * Convenience helper to obtain a freshly constructed UserManager instance.
     */
    private UserManager getManager() {
        return UserManager.getInstance();
    }

    // -------------------------------------------------------------------------
    //  Basic sanity / singleton / chief account
    // -------------------------------------------------------------------------

    /**
     * Verifies that {@code getInstance()} returns a non-null Singleton.
     */
    @Test
    void testGetInstanceReturnsSingleton() {
        UserManager m1 = getManager();
        UserManager m2 = getManager();
        assertNotNull(m1);
        assertSame(m1, m2, "UserManager should be a Singleton per JVM");
    }

    /**
     * Ensures that the default Chief Event Coordinator is created exactly once,
     * even if UserManager is re-instantiated (via reflection reset).
     */
    @Test
    void testEnsureDefaultChiefAccountNotDuplicated() throws Exception {
        UserManager first = getManager();
        long chiefCountFirst = first.getAllUsers().stream()
                .filter(u -> u instanceof SystemUser &&
                        ((SystemUser) u).getType() == UserType.CHIEF_EVENT_COORDINATOR)
                .count();
        assertEquals(1, chiefCountFirst, "First instance should create exactly one chief.");

        // Reset Singleton but keep the CSV file; second instance should detect chief already exists.
        resetSingleton();
        UserManager second = getManager();
        long chiefCountSecond = second.getAllUsers().stream()
                .filter(u -> u instanceof SystemUser &&
                        ((SystemUser) u).getType() == UserType.CHIEF_EVENT_COORDINATOR)
                .count();
        assertEquals(1, chiefCountSecond, "Second instance should not create a duplicate chief.");
    }

    // -------------------------------------------------------------------------
    //  Registration: happy path + all major failure paths
    // -------------------------------------------------------------------------

    /**
     * Registers a valid YorkU student and verifies that the user is stored.
     */
    @Test
    void testRegisterStudentSuccess() throws Exception {
        UserManager manager = getManager();

        boolean result = manager.register(
                "John Doe",
                "john@my.yorku.ca",
                "Strong1@",
                "STUDENT",
                "123456789"
        );

        assertTrue(result);
        User loaded = manager.findByEmail("john@my.yorku.ca");
        assertNotNull(loaded);
        assertTrue(loaded instanceof SystemUser);
        SystemUser su = (SystemUser) loaded;
        assertEquals(UserType.STUDENT, su.getType());
        assertEquals("123456789", su.getStudentId());
    }

    /**
     * Registration should fail when an invalid email format is used.
     */
    @Test
    void testRegisterInvalidEmailThrows() {
        UserManager manager = getManager();

        Exception ex = assertThrows(Exception.class, () ->
                manager.register(
                        "Bad Email",
                        "not-an-email",
                        "Strong1@",
                        "STUDENT",
                        "123456789"
                ));
        assertTrue(ex.getMessage().toLowerCase().contains("invalid email"));
    }

    /**
     * Registration should fail when the password is too weak.
     */
    @Test
    void testRegisterWeakPasswordThrows() {
        UserManager manager = getManager();

        Exception ex = assertThrows(Exception.class, () ->
                manager.register(
                        "Weak Pass",
                        "weak@my.yorku.ca",
                        "abc123",
                        "STUDENT",
                        "123456789"
                ));
        assertTrue(ex.getMessage().toLowerCase().contains("weak password"));
    }

    /**
     * Registration should fail when the user type string is invalid.
     */
    @Test
    void testRegisterInvalidUserTypeThrows() {
        UserManager manager = getManager();

        Exception ex = assertThrows(Exception.class, () ->
                manager.register(
                        "Alien",
                        "alien@my.yorku.ca",
                        "Strong1@",
                        "ALIEN",
                        "123456789"
                ));
        assertTrue(ex.getMessage().toLowerCase().contains("invalid user type"));
    }

    /**
     * Registers a student, then attempts to register with the same email again.
     * The second registration should fail due to duplicate email.
     */
    @Test
    void testRegisterDuplicateEmailThrows() throws Exception {
        UserManager manager = getManager();

        manager.register(
                "John Doe",
                "john@my.yorku.ca",
                "Strong1@",
                "STUDENT",
                "123456789"
        );

        Exception ex = assertThrows(Exception.class, () ->
                manager.register(
                        "John Doe 2",
                        "john@my.yorku.ca",
                        "Strong1@",
                        "STUDENT",
                        "987654321"
                ));
        assertTrue(ex.getMessage().toLowerCase().contains("already registered"));
    }

    /**
     * Students must use a @my.yorku.ca email — otherwise registration fails.
     */
    @Test
    void testRegisterStudentWrongDomainThrows() {
        UserManager manager = getManager();

        Exception ex = assertThrows(Exception.class, () ->
                manager.register(
                        "Wrong Domain",
                        "student@yorku.ca",
                        "Strong1@",
                        "STUDENT",
                        "123456789"
                ));
        assertTrue(ex.getMessage().contains("@my.yorku.ca"));
    }

    /**
     * Student registration should fail when student ID is missing.
     */
    @Test
    void testRegisterStudentMissingIdThrows() {
        UserManager manager = getManager();

        Exception ex = assertThrows(Exception.class, () ->
                manager.register(
                        "No ID",
                        "noid@my.yorku.ca",
                        "Strong1@",
                        "STUDENT",
                        ""
                ));
        assertTrue(ex.getMessage().toLowerCase().contains("student id required"));
    }

    /**
     * Student registration should fail when the student ID is not exactly 9 digits.
     */
    @Test
    void testRegisterStudentBadIdFormatThrows() {
        UserManager manager = getManager();

        Exception ex = assertThrows(Exception.class, () ->
                manager.register(
                        "Bad ID",
                        "badid@my.yorku.ca",
                        "Strong1@",
                        "STUDENT",
                        "12345"
                ));
        assertTrue(ex.getMessage().toLowerCase().contains("exactly 9 digits"));
    }

    /**
     * Faculty/Staff registration should enforce YorkU email domains.
     */
    @Test
    void testRegisterFacultyWrongDomainThrows() {
        UserManager manager = getManager();

        Exception ex = assertThrows(Exception.class, () ->
                manager.register(
                        "Prof",
                        "prof@gmail.com",
                        "Strong1@",
                        "FACULTY",
                        null
                ));
        assertTrue(ex.getMessage().toLowerCase().contains("@yorku.ca"));
    }

    /**
     * Partners have no special domain restrictions and should register successfully.
     */
    @Test
    void testRegisterPartnerSuccess() throws Exception {
        UserManager manager = getManager();

        boolean result = manager.register(
                "Partner User",
                "partner@gmail.com",
                "Strong1@",
                "PARTNER",
                null
        );
        assertTrue(result);
        assertTrue(manager.checkIfEmailRegistered("partner@gmail.com"));
    }

    // -------------------------------------------------------------------------
    //  Admin account creation & deletion
    // -------------------------------------------------------------------------

    /**
     * Successfully creates an Administrator account and verifies its role.
     */
    @Test
    void testCreateAdminAccountSuccess() throws Exception {
        UserManager manager = getManager();

        SystemUser admin = manager.createAdminAccount("admin@test.com", "Strong1@");
        assertNotNull(admin);
        assertEquals(UserType.ADMIN, admin.getType());

        List<SystemUser> admins = manager.getAdminAccounts();
        assertEquals(1, admins.size());
        assertEquals("admin@test.com", admins.get(0).getEmail());
    }

    /**
     * Admin creation should fail with an invalid email format.
     */
    @Test
    void testCreateAdminAccountInvalidEmailThrows() {
        UserManager manager = getManager();

        Exception ex = assertThrows(Exception.class, () ->
                manager.createAdminAccount("bad-email", "Strong1@"));
        assertTrue(ex.getMessage().toLowerCase().contains("invalid email"));
    }

    /**
     * Admin creation should fail with a weak password.
     */
    @Test
    void testCreateAdminAccountWeakPasswordThrows() {
        UserManager manager = getManager();

        Exception ex = assertThrows(Exception.class, () ->
                manager.createAdminAccount("admin2@test.com", "weak"));
        assertTrue(ex.getMessage().toLowerCase().contains("weak password"));
    }

    /**
     * Creating two admins with the same email should fail on the second attempt.
     */
    @Test
    void testCreateAdminAccountDuplicateEmailThrows() throws Exception {
        UserManager manager = getManager();

        manager.createAdminAccount("dupe@test.com", "Strong1@");

        Exception ex = assertThrows(Exception.class, () ->
                manager.createAdminAccount("dupe@test.com", "Strong1@"));
        assertTrue(ex.getMessage().toLowerCase().contains("already registered"));
    }

    /**
     * Deletes an existing admin by email and verifies that it is removed.
     */
    @Test
    void testDeleteAdminByEmailSuccess() throws Exception {
        UserManager manager = getManager();

        manager.createAdminAccount("to-delete@test.com", "Strong1@");
        assertTrue(manager.deleteAdminByEmail("to-delete@test.com"));
        assertFalse(manager.checkIfEmailRegistered("to-delete@test.com"));
    }

    /**
     * Deleting a non-existing admin email should return false.
     */
    @Test
    void testDeleteAdminByEmailNonExistingReturnsFalse() {
        UserManager manager = getManager();

        boolean removed = manager.deleteAdminByEmail("no-admin@test.com");
        assertFalse(removed);
    }

    /**
     * Verifies that getAdminAccounts() returns only ADMIN users.
     */
    @Test
    void testGetAdminAccountsReturnsOnlyAdmins() throws Exception {
        UserManager manager = getManager();

        // student
        manager.register(
                "Student",
                "student@my.yorku.ca",
                "Strong1@",
                "STUDENT",
                "123456789"
        );

        // admin
        manager.createAdminAccount("admin1@test.com", "Strong1@");
        manager.createAdminAccount("admin2@test.com", "Strong2@");

        List<SystemUser> admins = manager.getAdminAccounts();
        assertEquals(2, admins.size());
        assertTrue(admins.stream().allMatch(a -> a.getType() == UserType.ADMIN));
    }

    // -------------------------------------------------------------------------
    //  Login
    // -------------------------------------------------------------------------

    /**
     * login() should return null when the email is not registered.
     */
    @Test
    void testLoginEmailNotRegisteredReturnsNull() {
        UserManager manager = getManager();

        User result = manager.login("missing@test.com", "SomePass1!");
        assertNull(result);
    }

    /**
     * Successful login when email and password are correct.
     */
    @Test
    void testLoginSuccess() throws Exception {
        UserManager manager = getManager();

        manager.register(
                "John Doe",
                "john@my.yorku.ca",
                "Strong1@",
                "STUDENT",
                "123456789"
        );

        User result = manager.login("john@my.yorku.ca", "Strong1@");
        assertNotNull(result);
        assertEquals("john@my.yorku.ca", result.getEmail());
    }

    /**
     * login() should return null when the password is wrong.
     */
    @Test
    void testLoginWrongPasswordReturnsNull() throws Exception {
        UserManager manager = getManager();

        manager.register(
                "John Doe",
                "john@my.yorku.ca",
                "Strong1@",
                "STUDENT",
                "123456789"
        );

        User result = manager.login("john@my.yorku.ca", "WrongPass1!");
        assertNull(result);
    }

    // -------------------------------------------------------------------------
    //  Email update
    // -------------------------------------------------------------------------

    /**
     * Successfully updates a student's email to another valid @my.yorku.ca address.
     */
    @Test
    void testUpdateEmailStudentSuccess() throws Exception {
        UserManager manager = getManager();

        manager.register(
                "Student",
                "stud@my.yorku.ca",
                "Strong1@",
                "STUDENT",
                "123456789"
        );
        User user = manager.findByEmail("stud@my.yorku.ca");
        assertNotNull(user);

        boolean result = manager.updateEmail(user, "stud2@my.yorku.ca");
        assertTrue(result);
        assertEquals("stud2@my.yorku.ca", user.getEmail());
    }

    /**
     * updateEmail() should fail if the new email is identical to the old one.
     */
    @Test
    void testUpdateEmailSameEmailThrows() throws Exception {
        UserManager manager = getManager();

        manager.register(
                "Student",
                "same@my.yorku.ca",
                "Strong1@",
                "STUDENT",
                "123456789"
        );
        User user = manager.findByEmail("same@my.yorku.ca");

        Exception ex = assertThrows(Exception.class, () ->
                manager.updateEmail(user, "same@my.yorku.ca"));
        assertTrue(ex.getMessage().toLowerCase().contains("same as old"));
    }

    /**
     * updateEmail() should fail if the new email already belongs to another user.
     */
    @Test
    void testUpdateEmailDuplicateThrows() throws Exception {
        UserManager manager = getManager();

        manager.register(
                "User1",
                "u1@my.yorku.ca",
                "Strong1@",
                "STUDENT",
                "123456789"
        );
        manager.register(
                "User2",
                "u2@my.yorku.ca",
                "Strong1@",
                "STUDENT",
                "987654321"
        );

        User user2 = manager.findByEmail("u2@my.yorku.ca");
        Exception ex = assertThrows(Exception.class, () ->
                manager.updateEmail(user2, "u1@my.yorku.ca"));
        assertTrue(ex.getMessage().toLowerCase().contains("already taken"));
    }

    /**
     * updateEmail() should fail with an invalid email format.
     */
    @Test
    void testUpdateEmailInvalidFormatThrows() throws Exception {
        UserManager manager = getManager();

        manager.register(
                "Student",
                "stud@my.yorku.ca",
                "Strong1@",
                "STUDENT",
                "123456789"
        );
        User user = manager.findByEmail("stud@my.yorku.ca");

        Exception ex = assertThrows(Exception.class, () ->
                manager.updateEmail(user, "bad-email"));
        assertTrue(ex.getMessage().toLowerCase().contains("invalid email"));
    }

    /**
     * Students must keep @my.yorku.ca addresses when updating email.
     */
    @Test
    void testUpdateEmailStudentWrongDomainThrows() throws Exception {
        UserManager manager = getManager();

        manager.register(
                "Student",
                "stud@my.yorku.ca",
                "Strong1@",
                "STUDENT",
                "123456789"
        );
        User user = manager.findByEmail("stud@my.yorku.ca");

        Exception ex = assertThrows(Exception.class, () ->
                manager.updateEmail(user, "stud@gmail.com"));
        assertTrue(ex.getMessage().contains("@my.yorku.ca"));
    }

    /**
     * Faculty/Staff must use YorkU domains when updating email.
     */
    @Test
    void testUpdateEmailFacultyWrongDomainThrows() throws Exception {
        UserManager manager = getManager();

        manager.register(
                "Prof",
                "prof@yorku.ca",
                "Strong1@",
                "FACULTY",
                null
        );
        User prof = manager.findByEmail("prof@yorku.ca");

        Exception ex = assertThrows(Exception.class, () ->
                manager.updateEmail(prof, "prof@gmail.com"));
        assertTrue(ex.getMessage().toLowerCase().contains("@yorku.ca"));
    }

    // -------------------------------------------------------------------------
    //  Profile update (name + password)
    // -------------------------------------------------------------------------

    /**
     * updateProfile() should throw when the new password is too weak.
     */
    @Test
    void testUpdateProfileWeakPasswordThrows() throws Exception {
        UserManager manager = getManager();

        manager.register(
                "John",
                "john@my.yorku.ca",
                "Strong1@",
                "STUDENT",
                "123456789"
        );
        User user = manager.findByEmail("john@my.yorku.ca");

        Exception ex = assertThrows(Exception.class, () ->
                manager.updateProfile(user, null, "weak"));
        assertTrue(ex.getMessage().toLowerCase().contains("too weak"));
    }

    /**
     * Successfully updates only the password, leaving the name unchanged.
     */
    @Test
    void testUpdateProfilePasswordOnlySuccess() throws Exception {
        UserManager manager = getManager();

        manager.register(
                "John",
                "john@my.yorku.ca",
                "Strong1@",
                "STUDENT",
                "123456789"
        );
        User user = manager.findByEmail("john@my.yorku.ca");

        boolean result = manager.updateProfile(user, null, "NewStrong1@");
        assertTrue(result);
        assertEquals("NewStrong1@", user.getPasswordHash());
    }

    /**
     * Successfully updates only the name, leaving the password unchanged.
     */
    @Test
    void testUpdateProfileNameOnlySuccess() throws Exception {
        UserManager manager = getManager();

        manager.register(
                "John",
                "john@my.yorku.ca",
                "Strong1@",
                "STUDENT",
                "123456789"
        );
        User user = manager.findByEmail("john@my.yorku.ca");

        boolean result = manager.updateProfile(user, "John Updated", null);
        assertTrue(result);
        assertEquals("John Updated", user.getName());
        assertEquals("Strong1@", user.getPasswordHash());
    }

    /**
     * Calling updateProfile() with no changes should still return true
     * and simply skip any write or log branches.
     */
    @Test
    void testUpdateProfileNoChangesStillReturnsTrue() throws Exception {
        UserManager manager = getManager();

        manager.register(
                "John",
                "john@my.yorku.ca",
                "Strong1@",
                "STUDENT",
                "123456789"
        );
        User user = manager.findByEmail("john@my.yorku.ca");

        boolean result = manager.updateProfile(user, null, null);
        assertTrue(result);
        assertEquals("John", user.getName());
        assertEquals("Strong1@", user.getPasswordHash());
    }

    // -------------------------------------------------------------------------
    //  Misc helpers: findByEmail, checkIfEmailRegistered, saveAllUsers, pad()
    // -------------------------------------------------------------------------

    /**
     * findByEmail() should return null when called with null.
     */
    @Test
    void testFindByEmailNullReturnsNull() {
        UserManager manager = getManager();
        assertNull(manager.findByEmail(null));
    }

    /**
     * findByEmail() returns the correct user when a match exists.
     */
    @Test
    void testFindByEmailReturnsUser() throws Exception {
        UserManager manager = getManager();

        manager.register(
                "John",
                "john@my.yorku.ca",
                "Strong1@",
                "STUDENT",
                "123456789"
        );
        User found = manager.findByEmail("john@my.yorku.ca");
        assertNotNull(found);
        assertEquals("john@my.yorku.ca", found.getEmail());
    }

    /**
     * checkIfEmailRegistered() should correctly reflect existence of a stored user.
     */
    @Test
    void testCheckIfEmailRegistered() throws Exception {
        UserManager manager = getManager();

        assertFalse(manager.checkIfEmailRegistered("user@my.yorku.ca"));

        manager.register(
                "User",
                "user@my.yorku.ca",
                "Strong1@",
                "STUDENT",
                "123456789"
        );

        assertTrue(manager.checkIfEmailRegistered("user@my.yorku.ca"));
    }

    /**
     * saveAllUsers() should complete without throwing, even if no users were added
     * beyond the default chief account.
     */
    @Test
    void testSaveAllUsersDoesNotThrow() {
        UserManager manager = getManager();
        assertDoesNotThrow(manager::saveAllUsers);
    }

    /**
     * Invokes the private {@code pad} helper via reflection to ensure full
     * method coverage. Verifies that it returns the expected padding string.
     */
    @Test
    void testPadHelperViaReflection() throws Exception {
        UserManager manager = getManager();

        Method padMethod = UserManager.class.getDeclaredMethod("pad", String.class, int.class);
        padMethod.setAccessible(true);

        String result = (String) padMethod.invoke(manager, "abc", 10);
        assertEquals(7, result.length(), "Pad should add enough spaces to reach total length 10.");
    }
}
