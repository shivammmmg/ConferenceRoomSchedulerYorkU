package shared.model;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

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
