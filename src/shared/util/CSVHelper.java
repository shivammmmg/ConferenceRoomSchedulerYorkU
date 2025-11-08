package shared.util;

import java.io.*;
import java.util.*;
import shared.model.*;
import Scenario1.builder.UserBuilder;


/**
 * EECS 3311 - YorkU Conference Room Scheduler
 * ------------------------------------------------------------
 * Class: CSVHelper
 * Purpose:
 *  - Reads and writes user data (with optional studentId).
 *  - Supports Builder pattern and StudentUser extension.
 *  - Used by the UserManager (Singleton) controller.
 *
 */
public class CSVHelper {

    /**
     * Loads all users from the CSV file.
     * Each line format: name,email,password,userType,studentId(optional)
     */
    public static ArrayList<User> loadUsers(String path) throws Exception {
        ArrayList<User> list = new ArrayList<>();
        File file = new File(path);

        // Create the file if missing (so app never crashes on first run)
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            return list;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");

                // Handle legacy users (without studentId)
                String name = parts.length > 0 ? parts[0].trim() : "";
                String email = parts.length > 1 ? parts[1].trim() : "";
                String password = parts.length > 2 ? parts[2].trim() : "";
                String userType = parts.length > 3 ? parts[3].trim() : "";
                String studentId = parts.length > 4 ? parts[4].trim() : "";

                // Use Builder pattern to construct user objects
                User user = new UserBuilder()
                        .setName(name)
                        .setEmail(email)
                        .setPassword(password)
                        .setUserType(userType)
                        .setStudentId(studentId)
                        .build();

                list.add(user);
            }
        }
        return list;
    }

    /**
     * Saves the current user list back to the CSV file.
     * Each line format: name,email,password,userType,studentId(optional)
     */
    public static void saveUsers(String path, ArrayList<User> list) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (User u : list) {
                String studentId = "";
                try {
                    // Only StudentUser has getStudentId()
                    if (u.getUserType().equalsIgnoreCase("Student")) {
                        studentId = (String) u.getClass().getMethod("getStudentId").invoke(u);
                    }
                } catch (Exception ignored) {}

                bw.write(String.join(",", Arrays.asList(
                        u.getName(),
                        u.getEmail(),
                        u.getPassword(),
                        u.getUserType(),
                        studentId
                )));
                bw.newLine();
            }
        }
    }
}
