package shared.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * User – Abstract Base Class (All Scenarios)
 * =============================================================================
 * <p>This abstract class defines the <b>core identity + authentication fields</b>
 * common to every user in the Conference Room Scheduler system.</p>
 *
 * <h2>Purpose</h2>
 * <ul>
 *     <li>Provides a unified foundation for all account types.</li>
 *     <li>Encapsulates fields that ALL users share (name, email, password).</li>
 *     <li>Supports lifecycle management (activation/deactivation).</li>
 *     <li>Ensures consistent storage structure for CSV persistence.</li>
 * </ul>
 *
 * <h2>Used In</h2>
 * <ul>
 *     <li><b>Scenario 1</b> – Registration, Login, Profile Management</li>
 *     <li><b>Scenario 2</b> – Booking attribution (booking stored with userId)</li>
 *     <li><b>Scenario 3</b> – Check-in logic (validate booking → user)</li>
 *     <li><b>Scenario 4</b> – Admin: create/delete accounts, deactivate users</li>
 * </ul>
 *
 * <h2>Extended By</h2>
 * <ul>
 *     <li>{@link SystemUser} – Adds role-specific fields:
 *         <ul>
 *             <li>UserType type</li>
 *             <li>orgId</li>
 *             <li>studentId</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * <h2>Design Notes</h2>
 * <ul>
 *     <li>The class is intentionally abstract — instances are always concrete
 *         {@code SystemUser} objects.</li>
 *     <li>UUID-based userId ensures uniqueness across CSV loads.</li>
 *     <li>createdAt and isActive support audit/logging and admin control.</li>
 *     <li>validate() provides a lightweight pre-persistence integrity check.</li>
 * </ul>
 *
 * <h2>Persistence Mapping (CSV)</h2>
 * <p>This class corresponds to the following columns in users.csv:</p>
 * <pre>
 * userId, name, email, passwordHash, isActive, createdAt
 * </pre>
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
