package shared.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Admin – Scenario 4 (Admin & System Management)
 * ============================================================================
 * <p>Represents an administrative account used exclusively in Scenario 4 for
 * managing the Conference Room Scheduler system. This model is intentionally
 * lightweight and UI-friendly, using JavaFX {@code StringProperty} fields for
 * seamless binding inside TableView components.</p>
 *
 * <h2>Purpose</h2>
 * <ul>
 *     <li>Stores core admin credentials (username, password).</li>
 *     <li>Tracks activation status (ACTIVE / DISABLED).</li>
 *     <li>Supports role-based access (ADMIN / CHIEF).</li>
 *     <li>Provides JavaFX properties for live UI updates.</li>
 * </ul>
 *
 * <h2>Design Context</h2>
 * <ul>
 *     <li><b>Scenario 4</b>: Used by ManageAdminsController + AdminRepository.</li>
 *     <li><b>Backward Compatible</b>: Supports both legacy 2-field CSV and new
 *         4-field CSV format (username,password,status,role).</li>
 *     <li>Acts as a simple data holder; business rules are enforced elsewhere
 *         (UserManager / AdminRepository).</li>
 * </ul>
 *
 * <h2>Key Features</h2>
 * <ul>
 *     <li>Role-based hierarchy (ADMIN vs CHIEF_EVENT_COORDINATOR).</li>
 *     <li>JavaFX-friendly model for real-time TableView rendering.</li>
 *     <li>Serializable via {@link #toString()} for CSV persistence.</li>
 * </ul>
 *
 * <h2>Notes</h2>
 * <ul>
 *     <li>Passwords are stored in plain text for academic simplicity (D3 scope).
 *         In production this must be hashed.</li>
 *     <li>Admin accounts exist separately from SystemUser objects.</li>
 * </ul>
 * ============================================================================
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
