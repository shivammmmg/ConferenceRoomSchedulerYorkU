package shared.model;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * AdminRepository – Scenario 4 (Admin & System Management)
 * ============================================================================
 * <p>A lightweight repository responsible for storing and managing Admin
 * accounts separately from the main {@code UserManager}. This class was kept
 * for backward compatibility with earlier versions of Scenario 4 Deliverable
 * code where Admins were handled in a dedicated CSV file.</p>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li><b>Singleton</b> – Only one repository instance exists.</li>
 *     <li><b>Repository Pattern</b> – Encapsulates CSV persistence and
 *         lookup logic for Admin accounts.</li>
 *     <li>Backward-compatible loader supports both legacy (2-column) and
 *         extended (4-column) admin CSV formats.</li>
 * </ul>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>Maintain a lookup map of all Admin accounts (username → Admin).</li>
 *     <li>Provide methods to add, retrieve, delete, and list all admins.</li>
 *     <li>Persist admin data to CSV automatically after each change.</li>
 *     <li>Load Admins from CSV using old or new format:
 *         <ul>
 *             <li><b>Legacy:</b> username,password</li>
 *             <li><b>Extended:</b> username,password,status,role</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * <h2>Where It's Used</h2>
 * <ul>
 *     <li><b>Scenario 4</b> – Admin Management Panel (Create, Delete, Disable)</li>
 *     <li>Early project versions where Admins were not part of UserManager</li>
 * </ul>
 *
 * <h2>Notes</h2>
 * <ul>
 *     <li>Modern implementation stores Admin users inside UserManager instead.</li>
 *     <li>This repository remains for compatibility with older CSV structures
 *         and specific Scenario 4 UI components.</li>
 * </ul>
 * ============================================================================
 */


public class AdminRepository {

    private static AdminRepository instance;
    private final Map<String, Admin> admins = new HashMap<>();

    // -----------------------------------------------------
    // SINGLETON
    // -----------------------------------------------------
    public static AdminRepository getInstance() {
        if (instance == null) {
            instance = new AdminRepository();
        }
        return instance;
    }

    private AdminRepository() {}

    // -----------------------------------------------------
    // ADD ADMIN
    // -----------------------------------------------------
    public void addAdmin(Admin admin) {
        admins.put(admin.getUsername(), admin);
        saveAdmins();  // auto-save
    }

    // -----------------------------------------------------
    // CHECK IF EXISTS
    // -----------------------------------------------------
    public boolean adminExists(String username) {
        return admins.containsKey(username);
    }

    // -----------------------------------------------------
    // GET ADMIN
    // -----------------------------------------------------
    public Admin getAdmin(String username) {
        return admins.get(username);
    }

    // -----------------------------------------------------
    // GET ALL ADMINS
    // -----------------------------------------------------
    public Map<String, Admin> getAllAdmins() {
        return admins;
    }

    // -----------------------------------------------------
    // DELETE ADMIN
    // -----------------------------------------------------
    public void deleteAdmin(String username) {
        admins.remove(username);
        saveAdmins(); // auto-save
    }


    // =====================================================
    // CSV LOADING (ROLE ENABLED + BACKWARD COMPATIBLE)
    // =====================================================
    public void loadAdmins(String path) {
        File file = new File(path);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            admins.clear();

            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] parts = line.split(",");

                // Legacy format: username,password
                if (parts.length == 2) {
                    String user = parts[0];
                    String pass = parts[1];
                    Admin admin = new Admin(user, pass, "ADMIN"); // default role
                    admins.put(user, admin);
                }

                // New format: username,password,status,role
                else if (parts.length >= 4) {
                    String user = parts[0];
                    String pass = parts[1];
                    String status = parts[2];
                    String role = parts[3];

                    Admin admin = new Admin(user, pass, role);
                    admin.setStatus(status);

                    admins.put(user, admin);
                }
            }

        } catch (Exception e) {
            System.out.println("[AdminRepository] Failed to load admin CSV");
            e.printStackTrace();
        }
    }


    // =====================================================
    // CSV SAVING (ROLE ENABLED)
    // =====================================================
    public void saveAdmins() {
        saveAdmins("data/user.csv");  // default CSV path
    }

    public void saveAdmins(String path) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {

            for (Admin a : admins.values()) {
                pw.println(a.toString()); // Admin.java now writes username,password,status,role
            }

        } catch (Exception e) {
            System.out.println("[AdminRepository] Failed to save admin CSV");
            e.printStackTrace();
        }
    }
}
