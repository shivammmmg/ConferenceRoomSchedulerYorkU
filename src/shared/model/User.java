package shared.model;

/**
 * EECS 3311 - YorkU Conference Room Scheduler
 * ------------------------------------------------------------
 * Class: User
 * ------------------------------------------------------------
 * Description:
 *  - Base abstract class for all user types (e.g., Student, Faculty, Staff, Partner).
 *  - Stores common user attributes and shared behaviors.
 *  - Designed to be reused across multiple project scenarios.
 */

public abstract class User {

    // ==============================
    // Attributes
    // ==============================
    protected String name;
    protected String email;
    protected String password;
    protected String userType;

    // ==============================
    // Constructor
    // ==============================
    public User(String name, String email, String password, String userType) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    // ==============================
    // Getters
    // ==============================
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getUserType() { return userType; }

    // ==============================
    // Setters
    // ==============================
    public void setName(String name) { this.name = name; }
    public void setPassword(String password) { this.password = password; }

    // ==============================
    // Common Methods
    // ==============================
    @Override
    public String toString() {
        return name + "," + email + "," + password + "," + userType;
    }

    /**
     * Optional: subclasses can override this for role-specific actions.
     */
    public void displayInfo() {
        System.out.println("Name: " + name + " | Type: " + userType + " | Email: " + email);
    }
}
