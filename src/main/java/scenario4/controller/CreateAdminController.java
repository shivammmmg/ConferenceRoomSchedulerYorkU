package scenario4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import shared.model.Admin;
import shared.model.AdminRepository;

public class CreateAdminController {

    @FXML private TextField adminNameField;  // DELETE THIS LINE later if unused
    @FXML private TextField adminEmailField;

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


    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setTitle("Info");
        a.setContentText(msg);
        a.showAndWait();
    }
}
