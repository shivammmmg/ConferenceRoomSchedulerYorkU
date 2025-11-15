package shared.util;

import shared.model.SystemUser;
import shared.model.User;
import shared.model.UserType;

import java.io.*;
import java.util.ArrayList;

/**
 * CSVHelper
 * -------------------------------------------------------------------------
 * Handles loading and saving of users in CSV format.
 *
 * Works with the NEW SystemUser structure (single subclass + enum UserType).
 * Clean, simple, predictable CSV format:
 *
 *   name,email,passwordHash,userType,orgId,studentId
 *
 * studentId may be empty for non-students.
 * -------------------------------------------------------------------------
 */
public class CSVHelper {

    /**
     * Loads all users from the CSV file.
     */
    public static ArrayList<User> loadUsers(String path) throws Exception {

        ArrayList<User> list = new ArrayList<>();
        File file = new File(path);

        // Ensure directory + file exist
        if (!file.exists()) {
            if (file.getParentFile() != null)
                file.getParentFile().mkdirs();
            file.createNewFile();
            return list; // empty list
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", -1); // keep empty fields

                // Safe indexing
                String name       = parts.length > 0 ? parts[0] : "";
                String email      = parts.length > 1 ? parts[1] : "";
                String password   = parts.length > 2 ? parts[2] : "";
                String typeStr    = parts.length > 3 ? parts[3] : "";
                String orgId      = parts.length > 4 ? parts[4] : "";
                String studentId  = parts.length > 5 ? parts[5] : "";

                UserType type = UserType.valueOf(typeStr.toUpperCase());

                User u = new SystemUser(
                        name,
                        email,
                        password,
                        type,
                        orgId,
                        studentId.isBlank() ? null : studentId
                );

                list.add(u);
            }
        }

        return list;
    }


    /**
     * Saves all users to CSV in a consistent order.
     */
    public static void saveUsers(String path, ArrayList<User> list) throws Exception {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (User u : list) {

                SystemUser su = (SystemUser) u; // safe cast because UserManager only stores SystemUser

                bw.write(String.join(",",
                        su.getName(),
                        su.getEmail(),
                        su.getPasswordHash(),
                        su.getType().name(),
                        su.getOrgId(),
                        su.getStudentId() == null ? "" : su.getStudentId()
                ));

                bw.newLine();
            }
        }
    }
}
