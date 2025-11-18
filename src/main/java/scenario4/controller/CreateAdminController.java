package scenario4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import scenario4.shared.model.Admin;
import scenario4.shared.model.AdminRepository;

public class CreateAdminController {

    @FXML private TextField adminNameField;  // DELETE THIS LINE later if unused
    @FXML private TextField adminEmailField;

    @FXML
    public void createAdmin() {
        String name = adminNameField.getText().trim(); // REMOVE later if not needed
        String email = adminEmailField.getText().trim();

        if (email.isEmpty()) {
            showAlert("All fields are required.");
            return;
        }

        // TEMP PASSWORD
        String password = "ADMIN_" + (int)(Math.random() * 9000 + 1000);

        Admin admin = new Admin(email, password);

        AdminRepository.getInstance().addAdmin(admin);

        showAlert("Admin created! Password: " + password);

        adminEmailField.clear();
        adminNameField.clear(); // REMOVE if unused
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setTitle("Info");
        a.setContentText(msg);
        a.showAndWait();
    }
}
