package shared.model;

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
        // Use USERNAME as the unique key
        admins.put(admin.getUsername(), admin);
    }

    // -----------------------------------------------------
    // CHECK IF EXISTS
    // -----------------------------------------------------
    public boolean adminExists(String username) {
        return admins.containsKey(username);
    }

    // -----------------------------------------------------
    // GET ONE ADMIN
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
    }
}
