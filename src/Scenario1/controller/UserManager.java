package Scenario1.controller;

import java.util.*;
import java.io.*;
import shared.model.*;
import shared.util.*;
import Scenario1.factory.UserFactory;

/**
 * EECS 3311 - YorkU Conference Room Scheduler
 * ------------------------------------------------------------
 * Class: UserManager
 * Pattern: Singleton
 * Scenario: 1 - Registration & Account Management
 * ------------------------------------------------------------
 * Description:
 *  - Maintains all user operations: registration, login, update, and persistence.
 *  - Uses the Factory Method to create user objects dynamically.
 *  - Ensures only one global instance manages user data (Singleton).
 *
 * Author: Shivam Gupta
 * Date: November 2025
 */
public class UserManager {

    // =====================================================
    // Singleton Setup
    // =====================================================
    private static UserManager instance;
    private final String CSV_PATH = "src/shared/data/user.csv";
    private ArrayList<User> users = new ArrayList<>();

    // Private constructor to prevent external instantiation
    private UserManager() {
        try {
            users = CSVHelper.loadUsers(CSV_PATH);
        } catch (Exception e) {
            System.out.println("[INFO] No existing users found, starting fresh.");
        }
    }

    /**
     * Provides the single instance of UserManager.
     */
    public static synchronized UserManager getInstance() {
        if (instance == null)
            instance = new UserManager();
        return instance;
    }

    // =====================================================
    // Core Functionalities
    // =====================================================

    /**
     * Registers a new user after validation.
     */
    public boolean register(String name, String email, String password, String type) throws Exception {
        if (emailExists(email)) {
            System.out.println("[ERROR] Email already registered.");
            return false;
        }

        if (!isStrongPassword(password)) {
            System.out.println("[ERROR] Weak password.");
            return false;
        }

        // YorkU identity verification for university accounts
        if (type.equalsIgnoreCase("student") || type.equalsIgnoreCase("faculty") || type.equalsIgnoreCase("staff")) {
            if (!email.endsWith("@yorku.ca") && !email.endsWith("@my.yorku.ca"))
                throw new Exception("Invalid YorkU email address.");
        }

        // Create user via Factory Method
        User user = UserFactory.createUser(type, name, email, password);
        users.add(user);
        CSVHelper.saveUsers(CSV_PATH, users);
        System.out.println("[INFO] Registration successful for " + name);
        return true;
    }

    /**
     * Logs a user into the system.
     */
    public User login(String email, String password) {
        for (User u : users) {
            if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                System.out.println("[INFO] Login successful for " + u.getName());
                return u;
            }
        }
        System.out.println("[ERROR] Invalid credentials.");
        return null;
    }

    /**
     * Updates user profile info (name and password).
     */
    public boolean updateProfile(User user, String newName, String newPass) throws Exception {
        user.setName(newName);
        user.setPassword(newPass);
        CSVHelper.saveUsers(CSV_PATH, users);
        System.out.println("[INFO] Profile updated for " + newName);
        return true;
    }

    /**
     * Updates a user's email address (applies to all user types).
     * Performs uniqueness and domain validation.
     */
    public boolean updateEmail(User user, String newEmail) throws Exception {
        // Check if email is actually changing
        if (user.getEmail().equalsIgnoreCase(newEmail)) {
            throw new Exception("New email cannot be the same as the current one.");
        }

        // Check if email already exists in the system
        if (emailExists(newEmail)) {
            throw new Exception("Email already exists. Please choose another.");
        }

        // Domain verification only for YorkU users
        if (user.getUserType().equalsIgnoreCase("student") ||
                user.getUserType().equalsIgnoreCase("faculty") ||
                user.getUserType().equalsIgnoreCase("staff")) {

            if (!newEmail.endsWith("@yorku.ca") && !newEmail.endsWith("@my.yorku.ca")) {
                throw new Exception("Invalid YorkU email address for this account type.");
            }
        }

        // Update the email
        user.setEmail(newEmail);

        // Save to CSV
        CSVHelper.saveUsers(CSV_PATH, users);
        System.out.println("[INFO] Email updated successfully for " + user.getName());
        return true;
    }


    // =====================================================
    // Helper Methods
    // =====================================================

    private boolean emailExists(String email) {
        return users.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    private boolean isStrongPassword(String password) {
        // Must contain upper, lower, digit, special char, and be 8+ characters
        return password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");
    }

    public ArrayList<User> getAllUsers() {
        return users;
    }
}
