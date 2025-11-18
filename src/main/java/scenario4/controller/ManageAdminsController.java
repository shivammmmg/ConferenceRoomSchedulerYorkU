package scenario4.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import scenario4.shared.model.Admin;
import scenario4.shared.model.AdminRepository;

public class ManageAdminsController {

    @FXML
    private TableView<Admin> adminTable;

    @FXML
    private VBox container;

    private final AdminRepository repo = AdminRepository.getInstance();

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
        TableColumn<Admin, String> nameCol = new TableColumn<>("Username");
        nameCol.setCellValueFactory(e -> e.getValue().usernameProperty());

        TableColumn<Admin, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(e -> e.getValue().statusProperty());

        adminTable.getColumns().addAll(nameCol, statusCol);
    }

    private void loadAdmins() {
        adminTable.getItems().setAll(repo.getAllAdmins().values());
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
    // LOGIC: DELETE ADMIN
    // -------------------------------------------------------------
    private void deleteAdmin() {
        Admin selected = adminTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showWarning("Select an admin."); return; }

        repo.deleteAdmin(selected.getUsername());
        adminTable.getItems().remove(selected);
    }

    // -------------------------------------------------------------
    // LOGIC: DISABLE / ENABLE ADMIN
    // -------------------------------------------------------------
    private void toggleDisable() {
        Admin selected = adminTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showWarning("Select an admin."); return; }

        if (selected.getStatus().equals("ACTIVE")) {
            selected.setStatus("DISABLED");
        } else {
            selected.setStatus("ACTIVE");
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
