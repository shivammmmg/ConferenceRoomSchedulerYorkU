package shared.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Admin {

    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty("ACTIVE");

    public Admin(String username, String password) {
        this.username.set(username);
        this.password.set(password);
        this.status.set("ACTIVE");
    }

    // -----------------------
    // GETTERS
    // -----------------------
    public String getUsername() { return username.get(); }
    public String getPassword() { return password.get(); }
    public String getStatus() { return status.get(); }

    // -----------------------
    // SETTERS
    // -----------------------
    public void setStatus(String status) { this.status.set(status); }

    // -----------------------
    // PROPERTIES (For TableView)
    // -----------------------
    public StringProperty usernameProperty() { return username; }
    public StringProperty passwordProperty() { return password; }
    public StringProperty statusProperty() { return status; }
}
