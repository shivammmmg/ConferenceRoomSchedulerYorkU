package scenario4.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import shared.model.SystemUser;
import scenario1.controller.UserManager;

import java.util.ArrayList;

public class ManageAdminsController {

    @FXML
    private TableView<SystemUser> adminTable;

    @FXML
    private VBox container;

    private final UserManager userManager = UserManager.getInstance();

    @FXML
    public void initialize() {
        setupTable();
        loadAdmins();
        addButtons();
    }

    // -------------------------------------------------------------
    // TABLE SETUP
    // -------------------------------------------------------------
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

    // -------------------------------------------------------------
    // LOAD ADMINS FROM user.csv
    // -------------------------------------------------------------
    private void loadAdmins() {
        ArrayList<SystemUser> admins = userManager.getAdminAccounts();
        adminTable.getItems().setAll(admins);
    }

    // -------------------------------------------------------------
    // BUTTON ROW
    // -------------------------------------------------------------
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

    // -------------------------------------------------------------
    // DELETE ADMIN (updates user.csv)
    // -------------------------------------------------------------
    private void deleteAdmin() {

        SystemUser selected = adminTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showWarning("Select an admin.");
            return;
        }

        boolean removed = userManager.deleteAdminByEmail(selected.getEmail());

        if (removed) {
            adminTable.getItems().remove(selected);
        }
    }

    // -------------------------------------------------------------
    // ENABLE/DISABLE ADMIN (updates user.csv)
    // -------------------------------------------------------------
    private void toggleDisable() {

        SystemUser selected = adminTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showWarning("Select an admin.");
            return;
        }

        // Flip active flag
        selected.setActive(!selected.isActive());

        try {
            // Save to CSV (reusing updateProfile to persist list)
            userManager.updateProfile(selected, selected.getName(), selected.getPasswordHash());
        } catch (Exception e) {
            e.printStackTrace();
            showWarning("Failed to update admin status.");
        }

        adminTable.refresh();
    }

    // -------------------------------------------------------------
    // UTIL
    // -------------------------------------------------------------
    private void showWarning(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
