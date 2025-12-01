package shared.model;

import org.junit.jupiter.api.*;

import java.nio.file.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AdminRepositoryTest {

    private AdminRepository repo;

    // 🔒 Backup paths
    private static Path userCsvPath;
    private static Path userCsvBackup;

    // ------------------------------------------------------------
    // BEFORE ALL TESTS → backup data/user.csv
    // ------------------------------------------------------------
    @BeforeAll
    static void backupUserCsv() throws Exception {
        userCsvPath = Paths.get("data", "user.csv");

        if (Files.exists(userCsvPath)) {
            userCsvBackup = Files.createTempFile("user-backup", ".csv");
            Files.copy(userCsvPath, userCsvBackup, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    // ------------------------------------------------------------
    // BEFORE EACH TEST → reset repo state
    // ------------------------------------------------------------
    @BeforeEach
    void setUp() {
        repo = AdminRepository.getInstance();

        // Clear repository
        repo.getAllAdmins().clear();
    }

    // ------------------------------------------------------------
    // AFTER EACH TEST → restore original CSV content
    // ------------------------------------------------------------
    @AfterEach
    void restoreUserCsv() throws Exception {
        if (userCsvBackup != null && Files.exists(userCsvBackup)) {
            Files.copy(userCsvBackup, userCsvPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    // ------------------------------------------------------------
    // AFTER ALL TESTS → remove temp backup
    // ------------------------------------------------------------
    @AfterAll
    static void cleanupBackup() throws Exception {
        if (userCsvBackup != null) {
            Files.deleteIfExists(userCsvBackup);
        }
    }

    // ------------------------------------------------------------
    // 1 – Singleton returns the same instance every time
    // ------------------------------------------------------------
    @Test
    void testSingletonInstanceIsSame() {
        AdminRepository r1 = AdminRepository.getInstance();
        AdminRepository r2 = AdminRepository.getInstance();
        assertSame(r1, r2);
    }

    // 2 – repository is empty after setUp()
    @Test
    void testRepositoryStartsEmptyAfterSetUp() {
        assertEquals(0, repo.getAllAdmins().size());
        assertFalse(repo.adminExists("anyone"));
    }

    // 3 – addAdmin stores admin and adminExists/getAdmin return it
    @Test
    void testAddAdminStoresAdmin() {
        Admin admin = new Admin("alice", "pw");
        repo.addAdmin(admin);

        assertTrue(repo.adminExists("alice"));
        assertEquals("alice", repo.getAdmin("alice").getUsername());
    }

    // 4 – adminExists returns false for unknown usernames
    @Test
    void testAdminExistsReturnsFalseForUnknown() {
        assertFalse(repo.adminExists("nobody"));
    }

    // 5 – deleteAdmin removes admin from repository
    @Test
    void testDeleteAdminRemovesAdmin() {
        Admin admin = new Admin("bob", "pw");
        repo.addAdmin(admin);

        assertTrue(repo.adminExists("bob"));

        repo.deleteAdmin("bob");
        assertFalse(repo.adminExists("bob"));
        assertNull(repo.getAdmin("bob"));
    }

    // 6 – deleting a non-existing admin does not change existing data
    @Test
    void testDeleteNonExistingAdminDoesNotAffectOthers() {
        Admin admin = new Admin("carol", "pw");
        repo.addAdmin(admin);

        int beforeSize = repo.getAllAdmins().size();
        repo.deleteAdmin("no-such-admin");

        assertEquals(beforeSize, repo.getAllAdmins().size());
        assertTrue(repo.adminExists("carol"));
    }

    // 7 – getAdmin returns null when username is missing
    @Test
    void testGetAdminReturnsNullForMissing() {
        assertNull(repo.getAdmin("ghost"));
    }

    // 8 – getAllAdmins returns backing map with all admins
    @Test
    void testGetAllAdminsReturnsBackingMap() {
        Admin a1 = new Admin("c1", "pw1");
        Admin a2 = new Admin("c2", "pw2");
        repo.addAdmin(a1);
        repo.addAdmin(a2);

        Map<String, Admin> map = repo.getAllAdmins();
        assertEquals(2, map.size());
        assertTrue(map.containsKey("c1"));
        assertTrue(map.containsKey("c2"));
    }

    // 9 – backing map modifications are reflected in repository
    @Test
    void testBackingMapModificationReflectsInRepository() {
        Admin a1 = new Admin("d1", "pw1");
        Admin a2 = new Admin("d2", "pw2");
        repo.addAdmin(a1);
        repo.addAdmin(a2);

        Map<String, Admin> map = repo.getAllAdmins();
        map.remove("d1");   // modify map directly

        assertFalse(repo.adminExists("d1"));
        assertTrue(repo.adminExists("d2"));
    }

    // 10 – adding an admin with existing username overwrites previous
    @Test
    void testAddAdminWithExistingUsernameOverwrites() {
        Admin first  = new Admin("eve", "pw1");
        Admin second = new Admin("eve", "pw2");

        repo.addAdmin(first);
        repo.addAdmin(second);

        Admin fromRepo = repo.getAdmin("eve");
        assertSame(second, fromRepo);
        assertEquals("pw2", fromRepo.getPassword());
    }
}
