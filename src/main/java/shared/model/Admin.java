package shared.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Admin model with role-based access (ADMIN / CHIEF).
 */
public class Admin {

    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty status   = new SimpleStringProperty("ACTIVE");
    private final StringProperty role     = new SimpleStringProperty("ADMIN"); // default role

    // -----------------------
    // CONSTRUCTORS
    // -----------------------

    // Existing constructor → keeps backward compatibility
    public Admin(String username, String password) {
        this(username, password, "ADMIN");   // default to ADMIN
    }

    // New full constructor with role
    public Admin(String username, String password, String role) {
        this.username.set(username);
        this.password.set(password);
        this.status.set("ACTIVE");
        this.role.set(role != null ? role : "ADMIN");
    }

    // -----------------------
    // GETTERS
    // -----------------------
    public String getUsername() { return username.get(); }
    public String getPassword() { return password.get(); }
    public String getStatus()   { return status.get(); }
    public String getRole()     { return role.get(); }

    // -----------------------
    // SETTERS
    // -----------------------
    public void setStatus(String status) { this.status.set(status); }
    public void setRole(String role)     { this.role.set(role); }

    // -----------------------
    // PROPERTIES (For TableView)
    // -----------------------
    public StringProperty usernameProperty() { return username; }
    public StringProperty passwordProperty() { return password; }
    public StringProperty statusProperty()   { return status; }
    public StringProperty roleProperty()     { return role; }

    // -----------------------
    // CSV SAVE FORMAT
    // -----------------------
    @Override
    public String toString() {
        return getUsername() + "," + getPassword() + "," + getStatus() + "," + getRole();
    }
}
