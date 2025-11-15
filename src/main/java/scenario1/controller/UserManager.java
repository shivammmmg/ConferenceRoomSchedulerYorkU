package scenario1.controller;

import java.util.*;

import shared.model.User;
import shared.model.StudentUser;
import shared.model.FacultyUser;
import shared.model.StaffUser;
import shared.model.PartnerUser;

import shared.util.CSVHelper;
import scenario1.builder.UserBuilder;


/**
 * =============================================================================
 * CLASS: UserManager
 * PATTERN: Singleton + Factory (via UserBuilder)
 * SCENARIO: 1 — Registration & Account Management
 * =============================================================================
 *
 * RESPONSIBILITIES:
 *  - Full lifecycle management of all users in the system.
 *  - Handles registration, login, profile updates, and email changes.
 *  - Saves/loads user data from CSV storage using CSVHelper.
 *  - Ensures only ONE instance (Singleton) manages all users across UI screens.
 *
 * WHY SINGLETON?
 *  - Ensures Login, Register, ForgotPassword, etc. all share the same user list.
 *  - Prevents inconsistencies (multiple managers = different user states).
 *
 * WHY BUILDER PATTERN?
 *  - Clean creation of Student / Faculty / Staff / Partner users.
 *  - Avoids long constructors and messy conditionals in this class.
 *
 * DATA SOURCE:
 *  - User records persist in: src/main/resources/shared/data/user.csv
 *
 * AUTHOR: Shivam Gupta
 * DATE: November 2025
 * =============================================================================
 */
public class UserManager {

    // -------------------------------------------------------------------------
    // Singleton Instance + In-Memory User List
    // -------------------------------------------------------------------------
    private static UserManager instance;

    // ❗ FIXED: must use REAL project path (classpath paths won't work in IntelliJ)
    private final String CSV_PATH = "src/main/resources/shared/data/user.csv";

    private ArrayList<User> users = new ArrayList<>();   // in-memory user list


    // -------------------------------------------------------------------------
    // PRIVATE CONSTRUCTOR
    // Reads all existing users into memory at app startup.
    // -------------------------------------------------------------------------
    private UserManager() {
        try {
            users = CSVHelper.loadUsers(CSV_PATH);
        } catch (Exception e) {
            System.out.println("[INFO] No existing users found, starting fresh.");
        }
    }

    /**
     * Returns the single global UserManager instance.
     * Thread-safe due to synchronized.
     */
    public static synchronized UserManager getInstance() {
        if (instance == null)
            instance = new UserManager();
        return instance;
    }



    // -------------------------------------------------------------------------
    // Email Validation (General Format)
    // -------------------------------------------------------------------------
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }



    // =========================================================================
    // CORE FUNCTIONALITY
    // =========================================================================

    /**
     * Registers a new user after all validations.
     */
    public boolean register(String name, String email, String password, String type, String studentId)
            throws Exception {

        // --------------------------------------------------
        // 1. Syntax-based email validation
        // --------------------------------------------------
        if (!isValidEmail(email)) {
            throw new Exception("Invalid email address. Please enter a valid format like name@domain.com");
        }

        // --------------------------------------------------
        // 2. Ensure email is not already used
        // --------------------------------------------------
        if (emailExists(email)) {
            throw new Exception("Email already exists. Please use another email.");
        }

        // --------------------------------------------------
        // 3. Strong password enforcement
        // --------------------------------------------------
        if (!isStrongPassword(password)) {
            throw new Exception("Weak password! Must include:\n" +
                    "• At least 1 uppercase letter\n" +
                    "• At least 1 lowercase letter\n" +
                    "• At least 1 number\n" +
                    "• At least 1 special character (@, #, $, etc.)\n" +
                    "• Minimum 8 characters total");
        }

        // --------------------------------------------------
        // 4. Domain rules for each user type
        // --------------------------------------------------
        if (type.equalsIgnoreCase("student")) {

            if (!email.endsWith("@my.yorku.ca")) {
                throw new Exception("Invalid Student email. Students must use @my.yorku.ca domain.");
            }

            if (studentId == null || studentId.isEmpty()) {
                throw new Exception("Student ID is required for student accounts.");
            }

        } else if (type.equalsIgnoreCase("faculty") || type.equalsIgnoreCase("staff")) {

            if (!email.endsWith("@yorku.ca") && !email.endsWith("@my.yorku.ca")) {
                throw new Exception("Invalid Faculty/Staff email. Must use @yorku.ca or @my.yorku.ca.");
            }
        }

        // --------------------------------------------------
        // 5. Create user using the Builder Pattern
        // --------------------------------------------------
        User newUser = new UserBuilder()
                .setName(name)
                .setEmail(email)
                .setPassword(password)
                .setUserType(type)
                .setStudentId(studentId)
                .build();

        // Add to list and persist to CSV
        users.add(newUser);
        CSVHelper.saveUsers(CSV_PATH, users);

        System.out.println("[INFO] Registration successful for " + name);
        return true;
    }



    /**
     * Logs user into the system.
     * Returns:
     *    null  → email not found OR password wrong
     *    User  → login successful
     */
    public User login(String email, String password) {

        Optional<User> user = users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();

        if (user.isEmpty()) {
            return null; // email does not exist
        }

        User u = user.get();

        if (!u.getPassword().equals(password)) {
            return null; // incorrect password
        }

        System.out.println("[INFO] Login successful for " + u.getName());
        return u;
    }



    // =========================================================================
    // PROFILE UPDATE METHODS
    // =========================================================================

    public boolean updateProfile(User user, String newName, String newPass) throws Exception {
        user.setName(newName);
        user.setPassword(newPass);

        CSVHelper.saveUsers(CSV_PATH, users);

        System.out.println("[INFO] Profile updated for " + newName);
        return true;
    }

    public boolean updateEmail(User user, String newEmail) throws Exception {

        if (user.getEmail().equalsIgnoreCase(newEmail)) {
            throw new Exception("New email cannot be the same as the current one.");
        }

        if (emailExists(newEmail)) {
            throw new Exception("Email already exists. Please choose another.");
        }

        if (user.getUserType().equalsIgnoreCase("student") ||
                user.getUserType().equalsIgnoreCase("faculty") ||
                user.getUserType().equalsIgnoreCase("staff")) {

            if (!newEmail.endsWith("@yorku.ca") && !newEmail.endsWith("@my.yorku.ca")) {
                throw new Exception("Invalid YorkU email address for this account type.");
            }
        }

        user.setEmail(newEmail);
        CSVHelper.saveUsers(CSV_PATH, users);

        System.out.println("[INFO] Email updated successfully for " + user.getName());
        return true;
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

    private boolean isStrongPassword(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");
    }

    public ArrayList<User> getAllUsers() {
        return users;
    }
}
