package shared.util;

import java.io.File;

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
