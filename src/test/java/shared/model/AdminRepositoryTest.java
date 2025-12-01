package shared.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AdminRepositoryTest — Unit test suite for {@link AdminRepository}.
 * ----------------------------------------------------------------------
 * <p>This test class verifies all core behaviours of the AdminRepository,
 * which implements both the Singleton pattern and the Repository pattern
 * for storing and managing administrator accounts.</p>
 *
 * <h2>Test Coverage Objectives</h2>
 * <ul>
 *     <li>Validate Singleton instance behaviour</li>
 *     <li>Ensure persistence methods correctly save and load CSV files</li>
 *     <li>Confirm repository correctly handles CRUD operations</li>
 *     <li>Verify backward compatibility (legacy 2-column admin format)</li>
 *     <li>Ensure extended CSV format (4-column) loads correctly</li>
 *     <li>Check robustness against blank lines and missing files</li>
 *     <li>Achieve 100% method and line coverage for {@code AdminRepository}</li>
 * </ul>
 */

class AdminRepositoryTest {

    private AdminRepository repo;

    @BeforeEach
    void setUp() {
        repo = AdminRepository.getInstance();
        // Clear singleton state before each test
        repo.getAllAdmins().clear();
    }

    // 1 – Singleton returns the same instance every time
    @Test
    void testSingletonInstanceIsSame() {
        AdminRepository r1 = AdminRepository.getInstance();
        AdminRepository r2 = AdminRepository.getInstance();
        assertSame(r1, r2);
    }

    // 2 – addAdmin stores admin and adminExists/getAdmin return it
    @Test
    void testAddAdminStoresAdmin() {
        Admin admin = new Admin("alice", "pw");
        repo.addAdmin(admin);

        assertTrue(repo.adminExists("alice"));
        assertEquals("alice", repo.getAdmin("alice").getUsername());
    }

    // 3 – adminExists returns false for unknown usernames
    @Test
    void testAdminExistsReturnsFalseForUnknown() {
        assertFalse(repo.adminExists("nobody"));
    }

    // 4 – deleteAdmin removes admin from repository
    @Test
    void testDeleteAdminRemovesAdmin() {
        Admin admin = new Admin("bob", "pw");
        repo.addAdmin(admin);

        assertTrue(repo.adminExists("bob"));

        repo.deleteAdmin("bob");
        assertFalse(repo.adminExists("bob"));
        assertNull(repo.getAdmin("bob"));
    }

    // 5 – getAllAdmins returns backing map that reflects changes
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

        // modify map directly and verify repository sees it
        map.remove("c1");
        assertFalse(repo.adminExists("c1"));
    }

    // 6 – saveAdmins(path) writes CSV lines in Admin.toString() format
    @Test
    void testSaveAdminsWritesCsvFile() throws Exception {
        Admin a1 = new Admin("dave", "pw1", "ADMIN");
        a1.setStatus("ACTIVE");
        Admin a2 = new Admin("eva", "pw2", "CHIEF");
        a2.setStatus("DISABLED");

        repo.addAdmin(a1);
        repo.addAdmin(a2);

        Path temp = Files.createTempFile("admins-save", ".csv");
        repo.saveAdmins(temp.toString());

        // Read file back
        try (BufferedReader br = Files.newBufferedReader(temp)) {
            String line1 = br.readLine();
            String line2 = br.readLine();
            assertNotNull(line1);
            assertNotNull(line2);

            // Because HashMap order is not guaranteed, just check that
            // both string representations appear in the file content
            String all = line1 + "\n" + line2;
            assertTrue(all.contains(a1.toString()));
            assertTrue(all.contains(a2.toString()));
        }
    }

    // 7 – loadAdmins can read legacy 2-column format (username,password)
    @Test
    void testLoadAdminsLegacyFormatTwoColumns() throws Exception {
        Path temp = Files.createTempFile("admins-legacy", ".csv");
        Files.writeString(temp, "legacyUser,legacyPass\n");

        repo.loadAdmins(temp.toString());

        assertTrue(repo.adminExists("legacyUser"));
        Admin loaded = repo.getAdmin("legacyUser");
        assertEquals("legacyUser", loaded.getUsername());
        assertEquals("legacyPass", loaded.getPassword());
        assertEquals("ACTIVE", loaded.getStatus());  // from constructor
        assertEquals("ADMIN", loaded.getRole());     // default role
    }

    // 8 – loadAdmins can read extended 4-column format (username,password,status,role)
    @Test
    void testLoadAdminsExtendedFormatFourColumns() throws Exception {
        Path temp = Files.createTempFile("admins-extended", ".csv");
        Files.writeString(temp, "boss,bossPw,DISABLED,CHIEF\n");

        repo.loadAdmins(temp.toString());

        assertTrue(repo.adminExists("boss"));
        Admin loaded = repo.getAdmin("boss");
        assertEquals("boss", loaded.getUsername());
        assertEquals("bossPw", loaded.getPassword());
        assertEquals("DISABLED", loaded.getStatus());
        assertEquals("CHIEF", loaded.getRole());
    }

    // 9 – loadAdmins ignores blank lines
    @Test
    void testLoadAdminsIgnoresBlankLines() throws Exception {
        Path temp = Files.createTempFile("admins-blank", ".csv");
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

    // 10 – loadAdmins with non-existing file leaves repository unchanged
    @Test
    void testLoadAdminsWithNonExistingFileLeavesState() throws Exception {
        // Put something in repo
        repo.addAdmin(new Admin("original", "pw"));

        int beforeSize = repo.getAllAdmins().size();

        // Use a random path that should not exist
        repo.loadAdmins("this/path/does/not/exist_" + System.nanoTime() + ".csv");

        int afterSize = repo.getAllAdmins().size();
        assertEquals(beforeSize, afterSize);
        assertTrue(repo.adminExists("original"));
    }

}

