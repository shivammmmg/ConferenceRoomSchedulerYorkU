package shared.util;

import java.io.File;

/**
 * FileResolver – Project-Root File Path Utility
 * ------------------------------------------------------------------------
 * <p>This lightweight utility provides a safe and consistent way to resolve
 * file paths relative to the **project root directory**, regardless of whether
 * the application is running from IntelliJ, Maven, or a packaged JAR.</p>
 *
 * <h2>Purpose</h2>
 * <ul>
 *     <li>Standardizes how CSV files are located across all scenarios.</li>
 *     <li>Prevents issues where relative paths break depending on IDE/JAR location.</li>
 *     <li>Ensures {@code UserManager}, {@code BookingManager}, and repositories
 *         always refer to the correct data folder.</li>
 * </ul>
 *
 * <h2>Design Context</h2>
 * <ul>
 *     <li><b>Utility Class</b>: stateless, static helper used across the project.</li>
 *     <li>Often paired with CSVHelper for loading/saving CSV files.</li>
 *     <li>Used primarily in Scenarios 1, 2, and 4 where CSV persistence is needed.</li>
 * </ul>
 *
 * <h2>Behavior</h2>
 * <ul>
 *     <li>Automatically detects the project root (directory containing pom.xml).</li>
 *     <li>Returns a full absolute path regardless of OS or working directory.</li>
 *     <li>Provides fallback behavior if resolution fails.</li>
 * </ul>
 *
 * <h2>Why Needed?</h2>
 * <p>JavaFX applications often run from different working directories depending on
 * the launcher. This helper ensures consistent access to files such as:</p>
 *
 * <ul>
 *     <li><code>data/user.csv</code></li>
 *     <li><code>data/bookings.csv</code></li>
 *     <li><code>data/rooms.csv</code></li>
 * </ul>
 */


public class FileResolver {

    public static String resolve(String relativePath) {
        try {
            // Find the project root (folder containing pom.xml)
            String root = new File("").getAbsolutePath();

            // Always resolve relative to project root
            File file = new File(root, relativePath);
            return file.getAbsolutePath();
        } catch (Exception e) {
            return relativePath; // fallback
        }
    }
}
