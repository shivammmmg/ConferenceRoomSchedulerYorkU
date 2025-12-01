package shared.model;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AdminRepositoryTest {

    private AdminRepository repo;

    // 🔒 Backup paths
    private static Path userCsvPath;
    private static Path userCsvBackup;

    // BEFORE ALL TESTS → backup data/user.csv

    @BeforeAll
    static void backupUserCsv() throws Exception {
        userCsvPath = Paths.get("data", "user.csv");

        if (Files.exists(userCsvPath)) {
            userCsvBackup = Files.createTempFile("user-backup", ".csv");
            Files.copy(userCsvPath, userCsvBackup, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    // BEFORE EACH TEST → reset repo state

    @BeforeEach
    void setUp() {
        repo = AdminRepository.getInstance();

        // Clear repository
        repo.getAllAdmins().clear();
    }

    // AFTER EACH TEST → restore original CSV content

    @AfterEach
    void restoreUserCsv() throws Exception {
        if (userCsvBackup != null && Files.exists(userCsvBackup)) {
            Files.copy(userCsvBackup, userCsvPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    // AFTER ALL TESTS → remove temp backup

    @AfterAll
    static void cleanupBackup() throws Exception {
        if (userCsvBackup != null) {
            Files.deleteIfExists(userCsvBackup);
        }
    }

    // 1 – Singleton returns the same instance every time
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

    // 11 – saveAdmins(path) writes CSV containing all admins (custom temp file)
    @Test
    void testSaveAdminsWritesToCustomCsvFile() throws Exception {
        Admin a1 = new Admin("alpha", "p1", "ADMIN");
        a1.setStatus("ACTIVE");
        Admin a2 = new Admin("beta", "p2", "CHIEF");
        a2.setStatus("DISABLED");

        repo.addAdmin(a1);
        repo.addAdmin(a2);

        Path temp = Files.createTempFile("admins-save-test", ".csv");

        // call the path-based save method (does NOT affect data/user.csv)
        repo.saveAdmins(temp.toString());

        String content = Files.readString(temp);
        assertTrue(content.contains(a1.toString()));
        assertTrue(content.contains(a2.toString()));
    }

    // 12 – loadAdmins reads legacy 2-column format (username,password)
    @Test
    void testLoadAdminsLegacyTwoColumnFormat() throws Exception {
        Path temp = Files.createTempFile("admins-legacy-test", ".csv");
        Files.writeString(temp, "legacyUser,legacyPass\n");

        repo.loadAdmins(temp.toString());

        assertTrue(repo.adminExists("legacyUser"));
        Admin loaded = repo.getAdmin("legacyUser");
        assertEquals("legacyUser", loaded.getUsername());
        assertEquals("legacyPass", loaded.getPassword());
        assertEquals("ACTIVE", loaded.getStatus());  // from Admin default
        assertEquals("ADMIN", loaded.getRole());     // default role
    }

    // 13 – loadAdmins reads extended 4-column format (username,password,status,role)
    @Test
    void testLoadAdminsExtendedFourColumnFormat() throws Exception {
        Path temp = Files.createTempFile("admins-extended-test", ".csv");
        Files.writeString(temp, "boss,bossPw,DISABLED,CHIEF\n");

        repo.loadAdmins(temp.toString());

        assertTrue(repo.adminExists("boss"));
        Admin loaded = repo.getAdmin("boss");
        assertEquals("boss", loaded.getUsername());
        assertEquals("bossPw", loaded.getPassword());
        assertEquals("DISABLED", loaded.getStatus());
        assertEquals("CHIEF", loaded.getRole());
    }

    // 14 – loadAdmins ignores blank lines and only loads valid rows
    @Test
    void testLoadAdminsIgnoresBlankLines() throws Exception {
        Path temp = Files.createTempFile("admins-blank-test", ".csv");
        String content =
                "u1,p1,ACTIVE,ADMIN\n" +
                        "\n" +
                        "u2,p2,DISABLED,CHIEF\n";
        Files.writeString(temp, content);

        repo.loadAdmins(temp.toString());

        assertEquals(2, repo.getAllAdmins().size());
        assertTrue(repo.adminExists("u1"));
        assertTrue(repo.adminExists("u2"));
    }

    // 15 – loadAdmins with non-existing file leaves repository unchanged
    @Test
    void testLoadAdminsWithNonExistingFileLeavesStateUnchanged() {
        // Put something in the repo
        repo.addAdmin(new Admin("original", "pw"));

        int beforeSize = repo.getAllAdmins().size();

        // Path that should not exist
        String bogusPath = "this/path/does/not/exist_" + System.nanoTime() + ".csv";
        repo.loadAdmins(bogusPath);

        int afterSize = repo.getAllAdmins().size();
        assertEquals(beforeSize, afterSize);
        assertTrue(repo.adminExists("original"));
    }
}
