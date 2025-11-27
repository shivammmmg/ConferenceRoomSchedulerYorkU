package shared.util;

import org.junit.jupiter.api.Test;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FileResolverTest — Unit tests for {@link FileResolver}.
 * ---------------------------------------------------------
 * <p>This test suite ensures that the FileResolver utility correctly transforms
 * relative and absolute paths into usable absolute filesystem paths, handles
 * invalid inputs gracefully, and preserves expected file-system structure.</p>
 *
 * <h2>Key Behaviors Tested</h2>
 * <ul>
 *     <li>Resolution of relative paths</li>
 *     <li>Resolution of nested directory structures</li>
 *     <li>Handling of edge cases such as ".", "", weird characters, and null</li>
 *     <li>Validation that resolved paths remain absolute</li>
 *     <li>Triggering the fallback exception branch for coverage</li>
 * </ul>
 */
class FileResolverTest {

    /**
     * Test 1 — Verifies that resolving a basic relative path returns
     * a valid absolute path on the machine.
     */
    @Test
    void testResolveReturnsAbsolutePath() {
        String resolved = FileResolver.resolve("data/test.csv");
        assertTrue(new File(resolved).isAbsolute());
    }

    /**
     * Test 2 — Ensures the resolved path correctly ends with the same
     * relative segment provided to the resolver.
     */
    @Test
    void testResolvedPathEndsCorrectly() {
        String relative = "data/sample.txt";
        String resolved = FileResolver.resolve(relative);
        assertTrue(resolved.replace("\\", "/").endsWith("data/sample.txt"));
    }

    /**
     * Test 3 — Resolving "." must return the project root directory as an
     * absolute and existing filesystem path.
     */
    @Test
    void testResolveDotReturnsProjectRoot() {
        String resolved = FileResolver.resolve(".");
        File f = new File(resolved);
        assertTrue(f.isAbsolute());
        assertTrue(f.exists());
    }

    /**
     * Test 4 — Providing an empty string should still return an absolute path
     * pointing to the project root directory.
     */
    @Test
    void testResolveEmptyStringReturnsAbsolute() {
        String resolved = FileResolver.resolve("");
        File f = new File(resolved);
        assertTrue(f.isAbsolute());
    }

    /**
     * Test 5 — Verifies that nested folder structure is preserved inside
     * the resolved absolute path.
     */
    @Test
    void testResolveNestedFolders() {
        String relative = "folderA/folderB/folderC/file.txt";
        String resolved = FileResolver.resolve(relative);

        assertTrue(resolved.replace("\\", "/")
                .endsWith("folderA/folderB/folderC/file.txt"));
    }

    /**
     * Test 6 — Ensures that resolving a non-existent path still produces
     * a valid absolute path (the file does not need to exist).
     */
    @Test
    void testResolveNonExistingFile() {
        String resolved = FileResolver.resolve("this/does/not/exist.xyz");
        assertTrue(new File(resolved).isAbsolute());
    }

    /**
     * Test 7 — Verifies that paths containing strange or symbolic characters
     * do not cause the resolver to throw exceptions.
     */
    @Test
    void testResolveWeirdCharacters() {
        assertDoesNotThrow(() -> FileResolver.resolve("weird!@#$%^&*().txt"));
    }

    /**
     * Test 8 — Ensures that passing an absolute path returns an absolute path
     * that still refers to the same filename.
     */
    @Test
    void testResolveAbsoluteInputRemainsAbsolute() {
        String abs = new File("somefile.txt").getAbsolutePath();
        String resolved = FileResolver.resolve(abs);

        assertTrue(new File(resolved).isAbsolute());
        assertTrue(resolved.endsWith("somefile.txt"));
    }

    /**
     * Test 9 — Verifies that resolve(...) never returns null for valid inputs.
     */
    @Test
    void testResolveNeverReturnsNull() {
        String resolved = FileResolver.resolve("data/rooms.csv");
        assertNotNull(resolved);
    }

    /**
     * Test 10 — Ensures the resolver never returns an empty string,
     * even for paths that do not exist.
     */
    @Test
    void testResolveReturnsNonEmptyString() {
        String resolved = FileResolver.resolve("anything.txt");
        assertFalse(resolved.isEmpty());
    }

    /**
     * Test 11 — Forces FileResolver into its exception-handling fallback branch by
     * passing <code>null</code>. This is required for achieving full line coverage.
     *
     * <p><b>Note:</b> When null is passed, an exception is thrown internally, and
     * FileResolver returns null as the fallback result. Testing this behavior ensures
     * that the catch block is executed during the test suite.</p>
     */
    @Test
    void testResolveTriggersExceptionFallbackWithNull() {
        String resolved = FileResolver.resolve(null);

        // When exception occurs, method returns the original argument (null)
        assertNull(resolved);
    }
}
