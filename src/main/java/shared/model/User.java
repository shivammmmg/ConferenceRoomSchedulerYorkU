package shared.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * =============================================================================
 * ABSTRACT CLASS: User
 * =============================================================================
 *
 * Base class for all system users.
 *
 * This class only stores what's common for ALL users:
 *   ✔ name
 *   ✔ email
 *   ✔ passwordHash
 *   ✔ userId (UUID)
 *   ✔ createdAt timestamp
 *   ✔ isActive status
 *
 * SystemUser extends this class and adds:
 *   ✔ UserType type
 *   ✔ orgId
 *   ✔ studentId
 *
 * =============================================================================
 */
public abstract class User {

    protected UUID userId;
    protected String name;
    protected String email;
    protected String passwordHash;

    protected boolean isActive;
    protected LocalDateTime createdAt;

    // -----------------------------------
    // Constructor
    // -----------------------------------
    public User(String name, String email, String passwordHash) {

        this.userId = UUID.randomUUID();

        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;

        this.isActive = true;
        this.createdAt = LocalDateTime.now();
    }

    // -----------------------------------
    // GETTERS
    // -----------------------------------
    public UUID getUserId() {
        return userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    // -----------------------------------
    // SETTERS
    // -----------------------------------
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public void setPasswordHash(String newPasswordHash) {
        this.passwordHash = newPasswordHash;
    }

    /**
     * NEW — REQUIRED BY ManageAdminsController
     */
    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    // -----------------------------------
    // Utility
    // -----------------------------------
    public boolean isYorkUEmail() {
        return email.endsWith("@yorku.ca") || email.endsWith("@my.yorku.ca");
    }

    public boolean validate() {
        // simple validation: non-empty + valid email format
        return name != null && !name.isBlank()
                && email != null && email.contains("@")
                && passwordHash != null && passwordHash.length() >= 8;
    }

    // -----------------------------------
    // Optional — for debugging
    // -----------------------------------
    @Override
    public String toString() {
        return userId + "," +
                name + "," +
                email + "," +
                passwordHash + "," +
                isActive + "," +
                createdAt;
    }
}
