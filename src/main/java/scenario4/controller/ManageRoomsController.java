package scenario4.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import shared.model.Room;
import shared.model.RoomRepository;
import scenario4.components.RoomDetailsPopup;

public class ManageRoomsController {

    @FXML
    private TableView<Room> roomsTable;

    @FXML
    private VBox tableContainer;

    private final RoomRepository repo = RoomRepository.getInstance();

    @FXML
    public void initialize() {
        setupTable();
        loadRooms();
        addActionButtons();
    }

    // -------------------------------------------------------------
    // SETUP TABLE
    // -------------------------------------------------------------
    private void setupTable() {

        TableColumn<Room, String> idCol = new TableColumn<>("Room ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("roomId"));

        TableColumn<Room, Integer> capCol = new TableColumn<>("Capacity");
        capCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        TableColumn<Room, String> locCol = new TableColumn<>("Location");
        locCol.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Room, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        roomsTable.getColumns().addAll(idCol, capCol, locCol, statusCol);
    }

    private void loadRooms() {
        roomsTable.getItems().setAll(repo.getAllRooms().values());
    }

    // -------------------------------------------------------------
    // ACTION BUTTON ROW
    // -------------------------------------------------------------
    private void addActionButtons() {

        Button enableBtn = new Button("Enable");
        enableBtn.setOnAction(e -> enableRoom());

        Button disableBtn = new Button("Disable");
        disableBtn.setOnAction(e -> disableRoom());

        Button maintenanceBtn = new Button("Maintenance");
        maintenanceBtn.setOnAction(e -> markMaintenance());

        Button viewDetailsBtn = new Button("View Details");
        viewDetailsBtn.setOnAction(e -> onViewDetails());

        Button updateBtn = new Button("Update Room");
        updateBtn.setOnAction(e -> openUpdateRoomPopup());

        HBox buttonRow = new HBox(10, enableBtn, disableBtn, maintenanceBtn, viewDetailsBtn, updateBtn);
        buttonRow.setPadding(new Insets(10));
        buttonRow.setAlignment(Pos.CENTER);

        tableContainer.getChildren().add(buttonRow);
    }

    // -------------------------------------------------------------
    // LOGIC: VIEW DETAILS
    // -------------------------------------------------------------
    private void onViewDetails() {
        Room selected = roomsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showWarning("Please select a room first.");
            return;
        }

        RoomDetailsPopup.show(selected);
    }

    // -------------------------------------------------------------
    // LOGIC: ENABLE / DISABLE / MAINTENANCE
    // -------------------------------------------------------------
    private void enableRoom() {
        Room selected = roomsTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showWarning("Select a room."); return; }

        selected.setStatus("ENABLED");
        roomsTable.refresh();
    }

    private void disableRoom() {
        Room selected = roomsTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showWarning("Select a room."); return; }

        selected.setStatus("DISABLED");
        roomsTable.refresh();
    }

    private void markMaintenance() {
        Room selected = roomsTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showWarning("Select a room."); return; }

        selected.setStatus("MAINTENANCE");
        roomsTable.refresh();
    }

    // -------------------------------------------------------------
    // LOGIC: UPDATE ROOM POPUP
    // -------------------------------------------------------------
    private void openUpdateRoomPopup() {

        Room selected = roomsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Select a room to update.");
            return;
        }

        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);

        Label title = new Label("Update Room");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        TextField capField = new TextField(String.valueOf(selected.getCapacity()));
        capField.setPromptText("Capacity");

        TextField locField = new TextField(selected.getLocation());
        locField.setPromptText("Location");

        Button saveBtn = new Button("Save");
        saveBtn.setStyle("-fx-background-color: #BA0C2F; -fx-text-fill: white;");

        saveBtn.setOnAction(e -> {
            try {
                int newCap = Integer.parseInt(capField.getText());
                String newLoc = locField.getText();

                selected.capacityProperty().set(newCap);
                selected.locationProperty().set(newLoc);

                roomsTable.refresh();
                stage.close();

            } catch (Exception ex) {
                showWarning("Enter valid values.");
            }
        });

        VBox root = new VBox(15, title, capField, locField, saveBtn);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: white; -fx-background-radius: 15;");

        Scene scene = new Scene(root, 350, 300);
        stage.setScene(scene);
        stage.show();
    }

    // -------------------------------------------------------------
    // UTILITY POPUP
    // -------------------------------------------------------------
    private void showWarning(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
