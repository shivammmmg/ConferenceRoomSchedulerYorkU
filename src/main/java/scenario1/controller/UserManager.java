package scenario1.controller;

import shared.model.SystemUser;
import shared.model.User;
import shared.model.UserType;
import shared.util.CSVHelper;
import scenario1.builder.UserBuilder;

import java.util.ArrayList;
import java.util.Optional;

/**
 * =============================================================================
 * CLASS: UserManager
 * PATTERNS: Singleton + Builder (for User creation)
 * SCENARIO: 1 — Registration & Account Management
 * =============================================================================
 *
 * RESPONSIBILITIES:
 *  - Manages all user accounts in memory
 *  - Registration, login, profile update, email update
 *  - Uses CSVHelper for persistence
 *  - Only ONE instance exists across the entire app (Singleton)
 *
 * STORED USER TYPE:
 *   -> All users are stored as SystemUser (unified model)
 *
 * =============================================================================
 */
public class UserManager {

    // SINGLETON instance
    private static UserManager instance;

    // CSV storage location
    private final String CSV_PATH = "src/main/resources/shared/data/user.csv";

    // In-memory list of all users (loaded at startup)
    private ArrayList<User> users = new ArrayList<>();


    // PRIVATE constructor
    private UserManager() {
        try {
            users = CSVHelper.loadUsers(CSV_PATH);
        } catch (Exception e) {
            System.out.println("[INFO] No existing users found. Starting fresh.");
        }
    }

    // Singleton accessor
    public static synchronized UserManager getInstance() {
        if (instance == null)
            instance = new UserManager();
        return instance;
    }


    // =========================================================================
    // EMAIL VALIDATION
    // =========================================================================
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }


    // =========================================================================
    // PASSWORD STRENGTH CHECK
    // =========================================================================
    private boolean isStrongPassword(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");
    }


    // =========================================================================
    // HELPER METHODS
    // =========================================================================
    private boolean emailExists(String email) {
        return users.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    public boolean checkIfEmailRegistered(String email) {
        return emailExists(email);
    }

    public ArrayList<User> getAllUsers() {
        return users;
    }


    // =========================================================================
    // REGISTRATION
    // =========================================================================
    public boolean register(
            String name,
            String email,
            String password,
            String type,
            String studentId
    ) throws Exception {

        // 1. Validate email format
        if (!isValidEmail(email)) {
            throw new Exception("Invalid email format. Please enter a valid email.");
        }

        // 2. Duplicates?
        if (emailExists(email)) {
            throw new Exception("This email is already registered.");
        }

        // 3. Password strength
        if (!isStrongPassword(password)) {
            throw new Exception("Weak password! Must include:\n" +
                    "• 1 uppercase\n• 1 lowercase\n• 1 digit\n• 1 symbol\n• 8+ characters");
        }

        // 4. Convert type String → Enum
        UserType userType;
        try {
            userType = UserType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new Exception("Invalid user type.");
        }

        // 5. Domain rules per user type
        switch (userType) {

            case STUDENT:
                if (!email.endsWith("@my.yorku.ca"))
                    throw new Exception("Students must use @my.yorku.ca email.");
                if (studentId == null || studentId.isBlank())
                    throw new Exception("Student ID is required for student accounts.");
                break;

            case FACULTY:
            case STAFF:
                if (!email.endsWith("@yorku.ca") && !email.endsWith("@my.yorku.ca"))
                    throw new Exception("Faculty/Staff email must be @yorku.ca or @my.yorku.ca");
                break;

            case PARTNER:
                // No domain restriction
                break;
        }

        // 6. Create the user using Builder pattern
        User newUser = new UserBuilder()
                .setName(name)
                .setEmail(email)
                .setPassword(password)
                .setUserType(userType)
                .setOrgId(null)
                .setStudentId(studentId)
                .build();

        // 7. Add + save
        users.add(newUser);
        CSVHelper.saveUsers(CSV_PATH, users);

        System.out.println("[INFO] Registration successful for: " + name);
        return true;
    }


    // =========================================================================
    // LOGIN
    // =========================================================================
    public User login(String email, String password) {

        Optional<User> match = users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();

        if (match.isEmpty())
            return null;

        User u = match.get();

        if (!u.getPasswordHash().equals(password))
            return null;

        return u;
    }


    // =========================================================================
    // PROFILE UPDATE
    // =========================================================================
    public boolean updateProfile(User user, String newName, String newPassword) throws Exception {

        if (newPassword != null && !newPassword.isBlank()) {
            if (!isStrongPassword(newPassword))
                throw new Exception("Password is too weak.");
            user.setPasswordHash(newPassword);
        }

        if (newName != null && !newName.isBlank()) {
            user.setName(newName);
        }

        CSVHelper.saveUsers(CSV_PATH, users);
        return true;
    }


    // =========================================================================
    // EMAIL UPDATE
    // =========================================================================
    public boolean updateEmail(User user, String newEmail) throws Exception {

        if (newEmail.equalsIgnoreCase(user.getEmail()))
            throw new Exception("New email cannot be the same as old email.");

        if (emailExists(newEmail))
            throw new Exception("This email is already taken.");

        if (!isValidEmail(newEmail))
            throw new Exception("Invalid email format.");

        // Faculty / Staff / Student must remain YorkU emails
        UserType type = user.getType();

        if (type != UserType.PARTNER &&
                (!newEmail.endsWith("@yorku.ca") && !newEmail.endsWith("@my.yorku.ca"))) {

            throw new Exception("This account must use a YorkU email address.");
        }

        user.setEmail(newEmail);
        CSVHelper.saveUsers(CSV_PATH, users);

        return true;
    }
}
