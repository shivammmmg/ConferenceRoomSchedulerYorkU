package scenario4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import scenario4.factory.RoomFactory;
import shared.model.Room;
import shared.model.RoomRepository;

public class AddRoomController {

    @FXML
    private TextField roomIdField;

    @FXML
    private TextField capacityField;

    @FXML
    private TextField locationField;

    private RoomRepository repo = RoomRepository.getInstance();
    private RoomFactory factory = new RoomFactory();

    @FXML
    public void createRoom() {

        String roomId = roomIdField.getText().trim();
        String capacityTxt = capacityField.getText().trim();
        String location = locationField.getText().trim();

        // VALIDATION
        if (roomId.isEmpty() || capacityTxt.isEmpty() || location.isEmpty()) {
            showAlert("All fields are required.");
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityTxt);
        } catch (NumberFormatException ex) {
            showAlert("Capacity must be a number.");
            return;
        }

        // CHECK DUPLICATE
        if (repo.roomExists(roomId)) {
            showAlert("Room already exists!");
            return;
        }

        // CREATE ROOM USING FACTORY
        Room room = factory.createRoom(roomId, capacity, location);

        // SAVE TO REPOSITORY
        repo.addRoom(room);

        showSuccess("Room created successfully!");

        // CLEAR FIELDS
        roomIdField.clear();
        capacityField.clear();
        locationField.clear();
    }

    // --------------------------------------------
    // ALERT HELPERS
    // --------------------------------------------

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showSuccess(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
