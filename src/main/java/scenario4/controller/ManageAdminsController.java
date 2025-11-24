package scenario4.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import shared.model.SystemUser;
import scenario1.controller.UserManager;

import java.util.ArrayList;

/**
 * ManageAdminsController – Scenario 4 (Administration & System Management)
 * ---------------------------------------------------------------------------
 * <p>Controller for the “Manage Admins” page. Displays all admin accounts
 * stored in the user.csv file (via {@link UserManager}). Administrators can:</p>
 *
 * <ul>
 *     <li>View all system admin accounts</li>
 *     <li>Delete an admin</li>
 *     <li>Disable / Enable an admin</li>
 *     <li>See formatted audit logs printed to the console</li>
 * </ul>
 *
 * <h2>Key Integration Points</h2>
 * <ul>
 *     <li>Uses a JavaFX TableView to show admins</li>
 *     <li>Communicates with {@link UserManager} for CRUD operations</li>
 *     <li>Automatically updates user.csv after any change</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li>Repository pattern (UserManager → user.csv)</li>
 *     <li>MVC controller for Scenario 4</li>
 *     <li>Encapsulates all admin-management business rules</li>
 * </ul>
 */


public class ManageAdminsController {

    @FXML
    private TableView<SystemUser> adminTable;

    @FXML
    private VBox container;

    private final UserManager userManager = UserManager.getInstance();

    /**
     * Called automatically by JavaFX when the view loads.
     * Initializes the admin table, loads all admin accounts,
     * and adds action buttons below the table.
     */

    @FXML
    public void initialize() {
        setupTable();
        loadAdmins();
        addButtons();
    }

    /**
     * Configures all columns of the admin TableView:
     * <ul>
     *     <li>Username (email)</li>
     *     <li>Status (derived from {@link SystemUser#isActive()})</li>
     * </ul>
     *
     * Clears any previous column definitions and rebuilds the table structure.
     */

    private void setupTable() {

        // Username column (email)
        TableColumn<SystemUser, String> nameCol = new TableColumn<>("Username");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Status column (ACTIVE / DISABLED derived from isActive)
        TableColumn<SystemUser, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> {
            SystemUser u = cellData.getValue();
            String status = u.isActive() ? "ACTIVE" : "DISABLED";
            return new SimpleStringProperty(status);
        });

        adminTable.getColumns().clear();
        adminTable.getColumns().addAll(nameCol, statusCol);
    }

    /**
     * Loads all admin accounts from {@link UserManager#getAdminAccounts()}
     * and populates the TableView.
     */

    private void loadAdmins() {
        ArrayList<SystemUser> admins = userManager.getAdminAccounts();
        adminTable.getItems().setAll(admins);
    }

    /**
     * Creates and styles the “Delete” and “Disable / Enable” buttons,
     * then adds them as a control row beneath the admin table.
     */

    private void addButtons() {

        Button deleteBtn = new Button("Delete");
        deleteBtn.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> deleteAdmin());

        Button disableBtn = new Button("Disable / Enable");
        disableBtn.setStyle("-fx-background-color: #f0ad4e; -fx-text-fill: white;");
        disableBtn.setOnAction(e -> toggleDisable());

        HBox row = new HBox(12, deleteBtn, disableBtn);
        row.setAlignment(Pos.CENTER);
        row.setPadding(new Insets(10));

        container.getChildren().add(row);
    }

    /**
     * Deletes the admin selected in the TableView.
     *
     * <p>Process:</p>
     * <ol>
     *     <li>Ensures an admin is selected</li>
     *     <li>Removes them through {@link UserManager#deleteAdminByEmail(String)}</li>
     *     <li>Updates user.csv</li>
     *     <li>Removes them from the table UI</li>
     *     <li>Prints a formatted deletion audit log</li>
     * </ol>
     */

    private void deleteAdmin() {

        SystemUser selected = adminTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showWarning("Select an admin.");
            return;
        }

        boolean removed = userManager.deleteAdminByEmail(selected.getEmail());

        if (removed) {

            // =============== ADMIN DELETED LOG ====================
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            System.out.println();
            System.out.println("┌──────────────────────────────────────── ADMIN DELETED ─────────────────────────────────────────┐");

            String borderLine = "│ %-12s : %-77s │";

            System.out.println(String.format(borderLine, "Email", selected.getEmail()));
            System.out.println(String.format(borderLine, "Status", "DELETED"));
            System.out.println(String.format(borderLine, "Timestamp", now.format(fmt)));

            System.out.println("└────────────────────────────────────────────────────────────────────────────────────────────────┘");
            System.out.println();
            // ======================================================

            adminTable.getItems().remove(selected);
        }
    }


    /**
     * Enables or disables the selected admin account.
     *
     * <p>Steps:</p>
     * <ol>
     *     <li>Validates a table selection</li>
     *     <li>Toggles the {@code isActive} flag</li>
     *     <li>Saves updated status via {@link UserManager#updateProfile}</li>
     *     <li>Refreshes the table display</li>
     *     <li>Prints a status-change audit log</li>
     * </ol>
     */
    private void toggleDisable() {

        SystemUser selected = adminTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showWarning("Select an admin.");
            return;
        }

        boolean oldState = selected.isActive();
        selected.setActive(!oldState);

        try {
            userManager.updateProfile(selected, selected.getName(), selected.getPasswordHash());
        } catch (Exception e) {
            e.printStackTrace();
            showWarning("Failed to update admin status.");
            return;
        }

        adminTable.refresh();

        // =============== ADMIN STATUS TOGGLE LOG ====================
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println();
        System.out.println("┌──────────────────────────────────────── ADMIN STATUS ──────────────────────────────────────────┐");

        String borderLine = "│ %-12s : %-77s │";

        System.out.println(String.format(borderLine, "Email", selected.getEmail()));
        System.out.println(String.format(borderLine, "Change",
                (oldState ? "ACTIVE → DISABLED" : "DISABLED → ACTIVE")));
        System.out.println(String.format(borderLine, "Timestamp", now.format(fmt)));

        System.out.println("└────────────────────────────────────────────────────────────────────────────────────────────────┘");
        System.out.println();
        // ============================================================
    }


    /**
     * Displays a simple warning alert with the given message.
     *
     * @param msg message to show in the alert dialog
     */

    private void showWarning(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
