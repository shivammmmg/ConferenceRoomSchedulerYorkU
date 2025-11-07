package shared.util;

import java.io.*;
import java.util.*;
import shared.model.*;

/**
 * EECS 3311 - YorkU Conference Room Scheduler
 * ------------------------------------------------------------
 * Class: CSVHelper
 * Purpose:
 *  - Provides helper methods to read and write User data
 *    from/to a CSV file for persistence.
 *  - Used by the UserManager (Singleton) controller.
 *
 */
public class CSVHelper {

    /**
     * Loads all users from the CSV file.
     * Each line format: name,email,password,userType
     */
    public static ArrayList<User> loadUsers(String path) throws Exception {
        ArrayList<User> list = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    // Create an anonymous subclass because User is abstract
                    list.add(new User(parts[0], parts[1], parts[2], parts[3]) {});
                }
            }
        }
        return list;
    }

    /**
     * Saves the current user list back to the CSV file.
     */
    public static void saveUsers(String path, ArrayList<User> list) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (User u : list) {
                bw.write(u.toString());
                bw.newLine();
            }
        }
    }
}
