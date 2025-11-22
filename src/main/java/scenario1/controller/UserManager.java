package scenario1.controller;

import shared.model.SystemUser;
import shared.model.User;
import shared.model.UserType;
import shared.util.CSVHelper;
import scenario1.builder.UserBuilder;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

/**
 * ============================================================================
 * UNIFIED UserManager — Supports ALL Scenarios
 *  - Scenario 1: Registration, Login, Profile Update, Email Update, Validation
 *  - Scenario 4: Admin Account Management (create, list, delete)
 *  - Always ensures Chief Event Coordinator exists
 *  - Strong Student/Faculty domain rules
 *  - Full logging (Version 1)
 * ============================================================================
 * Patterns: Singleton + CSV Persistence
 * ============================================================================
 */
public class UserManager {

    private static UserManager instance;

    // ============================================================
    // Resolve correct project directory for CSV
    // ============================================================
    private String getProjectRootPath() {
        String base = System.getProperty("user.dir");

        if (base.endsWith("target/classes")) {
            File f = new File(base);
            return f.getParentFile().getParent();
        }
        return base;
    }

    private final String CSV_PATH = getProjectRootPath() + "/data/user.csv";

    // In-memory list
    private ArrayList<User> users = new ArrayList<>();


    // PRIVATE constructor
    private UserManager() {
        try {
            users = CSVHelper.loadUsers(CSV_PATH);
        } catch (Exception ignored) {}

        // Version 2 feature
        ensureDefaultChiefAccount();
    }


    // Singleton accessor
    public static synchronized UserManager getInstance() {
        if (instance == null) instance = new UserManager();
        return instance;
    }

    // ============================================================================
    // Ensure 1 Chief Event Coordinator Exists
    // ============================================================================
    private void ensureDefaultChiefAccount() {

        boolean hasChief = users.stream().anyMatch(u ->
                (u instanceof SystemUser) &&
                        ((SystemUser) u).getType() == UserType.CHIEF_EVENT_COORDINATOR
        );

        if (!hasChief) {
            User chief = new UserBuilder()
                    .setName("Chief Event Coordinator")
                    .setEmail("chief@yorku.ca")
                    .setPassword("Chief123@")
                    .setUserType(UserType.CHIEF_EVENT_COORDINATOR)
                    .setOrgId("")
                    .setStudentId("")
                    .build();

            users.add(chief);

            try { CSVHelper.saveUsers(CSV_PATH, users); }
            catch (Exception ignored) {}

            System.out.println("[INFO] Chief Event Coordinator account created automatically.");
        }
    }


    // ============================================================================
    // Validation Helpers
    // ============================================================================
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private boolean isStrongPassword(String pass) {
        return pass.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");
    }

    private boolean emailExists(String email) {
        return users.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    public boolean checkIfEmailRegistered(String email) {
        return emailExists(email);
    }

    public ArrayList<User> getAllUsers() {
        return users;
    }

    public User findByEmail(String email) {
        if (email == null) return null;

        return users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }


    // ============================================================================
    // Scenario 4: Admin Account Management
    // ============================================================================
    public SystemUser createAdminAccount(String email, String password) throws Exception {

        if (!isValidEmail(email))
            throw new Exception("Invalid email format.");

        if (emailExists(email))
            throw new Exception("This email is already registered.");

        if (!isStrongPassword(password))
            throw new Exception("Weak password! Must include uppercase, lowercase, digit, symbol, 8+ chars.");

        User newAdmin = new UserBuilder()
                .setName(email)
                .setEmail(email)
                .setPassword(password)
                .setUserType(UserType.ADMIN)
                .setOrgId("")
                .setStudentId("")
                .build();

        users.add(newAdmin);
        CSVHelper.saveUsers(CSV_PATH, users);

        System.out.println("[ADMIN CREATED] " + email);
        return (SystemUser) newAdmin;
    }


    public ArrayList<SystemUser> getAdminAccounts() {
        ArrayList<SystemUser> list = new ArrayList<>();

        for (User u : users) {
            if (u instanceof SystemUser) {
                SystemUser su = (SystemUser) u;
                if (su.getType() == UserType.ADMIN) list.add(su);
            }
        }
        return list;
    }


    public boolean deleteAdminByEmail(String email) {

        boolean removed = users.removeIf(u ->
                u instanceof SystemUser &&
                        ((SystemUser) u).getType() == UserType.ADMIN &&
                        u.getEmail().equalsIgnoreCase(email)
        );

        if (removed) {
            try { CSVHelper.saveUsers(CSV_PATH, users); }
            catch (Exception ignored) {}

            System.out.println("[ADMIN DELETED] " + email);
        }

        return removed;
    }


    // ============================================================================
    // Registration (Scenario 1 + all Version 1 rules)
    // ============================================================================
    public boolean register(
            String name,
            String email,
            String password,
            String type,
            String studentId
    ) throws Exception {

        // Format
        if (!isValidEmail(email))
            throw new Exception("Invalid email format. Enter a valid email.");

        // Duplicate
        if (emailExists(email))
            throw new Exception("This email is already registered.");

        // Strength
        if (!isStrongPassword(password))
            throw new Exception("Weak password! Must include:\n" +
                    "• 1 uppercase\n• 1 lowercase\n• 1 digit\n• 1 symbol\n• 8+ characters");

        // UserType
        UserType userType;
        try {
            userType = UserType.valueOf(type.toUpperCase());
        } catch (Exception e) {
            throw new Exception("Invalid user type.");
        }

        // Domain rules
        switch (userType) {

            case STUDENT:
                if (!email.endsWith("@my.yorku.ca"))
                    throw new Exception("Students must use @my.yorku.ca email.");

                if (studentId == null || studentId.isBlank())
                    throw new Exception("Student ID required.");

                if (!studentId.matches("\\d{9}"))
                    throw new Exception("Student ID must be EXACTLY 9 digits.");
                break;

            case FACULTY:
            case STAFF:
                if (!email.endsWith("@yorku.ca") && !email.endsWith("@my.yorku.ca"))
                    throw new Exception("Faculty/Staff must use @yorku.ca or @my.yorku.ca");
                break;

            case PARTNER:
            case ADMIN:
            case CHIEF_EVENT_COORDINATOR:
                break;
        }

        // Build user
        User newUser = new UserBuilder()
                .setName(name)
                .setEmail(email)
                .setPassword(password)
                .setUserType(userType)
                .setOrgId("")
                .setStudentId(studentId)
                .build();

        users.add(newUser);
        CSVHelper.saveUsers(CSV_PATH, users);

        // === Version 1 Registration Log ===
        System.out.println("");
        System.out.println("┌──────────────────────────────────── USER REGISTRATION ───────────────────────────────────────┐");

        String borderLine = "│ %-12s : %-77s │";

        System.out.println(String.format(borderLine, "Name", name));
        System.out.println(String.format(borderLine, "Email", email));
        System.out.println(String.format(borderLine, "Type", userType.name()));

        if (userType == UserType.STUDENT) {
            System.out.println(String.format(borderLine, "StudentID", studentId));
        }

        System.out.println(String.format(borderLine, "Status", "CREATED"));
        System.out.println(String.format(borderLine, "Timestamp",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

        System.out.println("└──────────────────────────────────────────────────────────────────────────────────────────────┘");
        System.out.println("");


        return true;
    }


    // ============================================================================
    // LOGIN — FULL Version 1 Logging
    // ============================================================================
    public User login(String email, String password) {

        Optional<User> match = users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // ---------------------------------------------
        // ✘ Case 1: Email does NOT exist
        // ---------------------------------------------
        if (match.isEmpty()) {

            System.out.println("");
            System.out.println("┌──────────────────────────────────────── LOGIN EVENT ─────────────────────────────────────────┐");

            String borderLine = "│ %-12s : %-77s │";

            System.out.println(String.format(borderLine, "Email", email));
            System.out.println(String.format(borderLine, "Status", "FAILED"));
            System.out.println(String.format(borderLine, "Reason", "Email not registered"));
            System.out.println(String.format(borderLine, "Timestamp", now.format(fmt)));

            System.out.println("└──────────────────────────────────────────────────────────────────────────────────────────────┘");
            System.out.println("");


            return null;
        }

        User u = match.get();

        // ---------------------------------------------
        // ✘ Case 2: Incorrect password
        // ---------------------------------------------
        if (!u.getPasswordHash().equals(password)) {

            System.out.println("");
            System.out.println("┌──────────────────────────────────────── LOGIN EVENT ─────────────────────────────────────────┐");

            String borderLine = "│ %-12s : %-77s │";

            System.out.println(String.format(borderLine, "Email", email));
            System.out.println(String.format(borderLine, "Status", "FAILED"));
            System.out.println(String.format(borderLine, "Reason", "Incorrect password"));
            System.out.println(String.format(borderLine, "Timestamp", now.format(fmt)));

            System.out.println("└──────────────────────────────────────────────────────────────────────────────────────────────┘");
            System.out.println("");


            return null;
        }

        // ---------------------------------------------
        // ✔ Case 3: SUCCESSFUL LOGIN
        // ---------------------------------------------
        System.out.println("");
        System.out.println("┌──────────────────────────────────────── LOGIN EVENT ─────────────────────────────────────────┐");

        String borderLine = "│ %-12s : %-77s │";

        System.out.println(String.format(borderLine, "Email", email));
        System.out.println(String.format(borderLine, "Role", ((SystemUser) u).getType().name()));
        System.out.println(String.format(borderLine, "Status", "SUCCESS"));
        System.out.println(String.format(borderLine, "Timestamp", now.format(fmt)));

        System.out.println("└──────────────────────────────────────────────────────────────────────────────────────────────┘");
        System.out.println("");



        return u;
    }



    // ============================================================================
    // Profile Update (Version 1)
    // ============================================================================
    public boolean updateProfile(User user, String newName, String newPassword) throws Exception {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        boolean changed = false;

        // --- PASSWORD UPDATE ---
        if (newPassword != null && !newPassword.isBlank()) {

            if (!isStrongPassword(newPassword))
                throw new Exception("Password is too weak.");

            user.setPasswordHash(newPassword);
            CSVHelper.saveUsers(CSV_PATH, users);


            System.out.println();
            System.out.println("┌──────────────────────────────────────── PROFILE EVENT ─────────────────────────────────────────┐");

            String borderLine = "│ %-12s : %-79s │";
            System.out.println(String.format(borderLine, "Action", "PASSWORD_UPDATE"));
            System.out.println(String.format(borderLine, "Change", "(hidden)"));
            System.out.println(String.format(borderLine, "Timestamp", now.format(fmt)));

            System.out.println("└────────────────────────────────────────────────────────────────────────────────────────────────┘");
            System.out.println();
        }


        // --- NAME UPDATE ---
        if (newName != null && !newName.isBlank() && !newName.equals(user.getName())) {

            String oldName = user.getName();
            user.setName(newName);
            CSVHelper.saveUsers(CSV_PATH, users);


            System.out.println();
            System.out.println("┌──────────────────────────────────────── PROFILE EVENT ─────────────────────────────────────────┐");

            String borderLine = "│ %-12s : %-79s │";
            System.out.println(String.format(borderLine, "Action", "NAME_UPDATE"));
            System.out.println(String.format(borderLine, "Change", oldName + " → " + newName));
            System.out.println(String.format(borderLine, "Timestamp", now.format(fmt)));

            System.out.println("└────────────────────────────────────────────────────────────────────────────────────────────────┘");
            System.out.println();
        }


        if (changed) {
            CSVHelper.saveUsers(CSV_PATH, users);
        }

        return true;
    }



    // ============================================================================
    // Email Update — Version 1 Fully Restored
    // ============================================================================
    public boolean updateEmail(User user, String newEmail) throws Exception {

        if (newEmail.equalsIgnoreCase(user.getEmail()))
            throw new Exception("New email cannot be same as old.");

        if (emailExists(newEmail))
            throw new Exception("This email is already taken.");

        if (!isValidEmail(newEmail))
            throw new Exception("Invalid email format.");

        UserType type = ((SystemUser) user).getType();

        switch (type) {
            case STUDENT:
                if (!newEmail.endsWith("@my.yorku.ca"))
                    throw new Exception("Students must use @my.yorku.ca email.");
                break;

            case FACULTY:
            case STAFF:
                if (!newEmail.endsWith("@yorku.ca") && !newEmail.endsWith("@my.yorku.ca"))
                    throw new Exception("Faculty/Staff must use @yorku.ca or @my.yorku.ca");
                break;

            case PARTNER:
            case ADMIN:
            case CHIEF_EVENT_COORDINATOR:
                break;
        }

        // Store old email before changing
        String oldEmail = user.getEmail();

        // Update email
        user.setEmail(newEmail);
        CSVHelper.saveUsers(CSV_PATH, users);

        // ===================== EMAIL UPDATE LOG (BOX FORMAT) =====================
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println();
        System.out.println("┌──────────────────────────────────────── PROFILE EVENT ─────────────────────────────────────────┐");

        String borderLine = "│ %-12s : %-79s │";
        System.out.println(String.format(borderLine, "Action", "EMAIL_UPDATE"));
        System.out.println(String.format(borderLine, "Change", oldEmail + " → " + newEmail));
        System.out.println(String.format(borderLine, "Timestamp", now.format(fmt)));

        System.out.println("└────────────────────────────────────────────────────────────────────────────────────────────────┘");
        System.out.println();
        // ==========================================================================

        return true;
    }


    // Utility method to pad text for box formatting
    private String pad(String text, int totalLength) {
        if (text == null) text = "";
        int len = text.length();
        if (len >= totalLength) return "";

        return " ".repeat(totalLength - len);
    }

}