package scenario4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import shared.model.Admin;
import shared.model.AdminRepository;

/**
 * CreateAdminController – Scenario 4 (Administration & System Management)
 * -------------------------------------------------------------------------
 * <p>Controller for the "Create Admin" page. Allows the Admin or Chief Event
 * Coordinator to create new system administrators.</p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>Validate admin input (email field)</li>
 *     <li>Generate a temporary password for new admins</li>
 *     <li>Save the new admin to {@link AdminRepository}</li>
 *     <li>Display success confirmation + auto-generated password</li>
 *     <li>Print creation log to console for audit purposes</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li>Part of Scenario 4 MVC architecture</li>
 *     <li>Uses Repository pattern via {@link AdminRepository}</li>
 *     <li>Decouples UI (FXML) from business logic</li>
 * </ul>
 */


public class CreateAdminController {

    @FXML private TextField adminNameField;  // DELETE THIS LINE later if unused
    @FXML private TextField adminEmailField;


    /**
     * Handles the "Create Admin" button click.
     *
     * <p>Process:</p>
     * <ol>
     *     <li>Reads and validates input fields</li>
     *     <li>Generates a temporary password (random 4-digit code)</li>
     *     <li>Creates a new {@link Admin} object</li>
     *     <li>Saves it using {@link AdminRepository}</li>
     *     <li>Logs formatted audit information to the console</li>
     *     <li>Displays a success message including the generated password</li>
     * </ol>
     *
     * <p>This method does not handle roles yet — all created admins default to
     * the "ADMIN" role unless changed later.</p>
     */

    @FXML
    public void createAdmin() {
        String name = adminNameField.getText().trim(); // may remove later
        String email = adminEmailField.getText().trim();

        if (email.isEmpty()) {
            showAlert("All fields are required.");
            return;
        }

        // TEMP PASSWORD
        String password = "ADMIN_" + (int)(Math.random() * 9000 + 1000);

        Admin admin = new Admin(email, password);

        AdminRepository.getInstance().addAdmin(admin);

        // =============== ADMIN CREATED LOG ====================
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println();
        System.out.println("┌──────────────────────────────────────── ADMIN CREATED ─────────────────────────────────────────┐");

        String borderLine = "│ %-12s : %-77s │";

        System.out.println(String.format(borderLine, "Email", email));
        System.out.println(String.format(borderLine, "Password", password));
        System.out.println(String.format(borderLine, "Timestamp", now.format(fmt)));

        System.out.println("└─────────────────────────────────────────────────────────────────────────────────────────────────┘");
        System.out.println();
        // ======================================================

        showAlert("Admin created! Password: " + password);

        adminEmailField.clear();
        adminNameField.clear();
    }


    /**
     * Utility method for showing a simple information dialog.
     *
     * @param msg Text to display inside the alert popup.
     */

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setTitle("Info");
        a.setContentText(msg);
        a.showAndWait();
    }
}
