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
    private TextField roomNameField;

    @FXML
    private TextField capacityField;

    @FXML
    private TextField locationField;

    @FXML
    private TextField amenitiesField;

    @FXML
    private TextField buildingField;

    private final RoomRepository repo = RoomRepository.getInstance();

    @FXML
    public void createRoom() {

        String roomId = roomIdField.getText().trim();
        String roomName = roomNameField.getText().trim();
        String capacityTxt = capacityField.getText().trim();
        String location = locationField.getText().trim();
        String amenities = amenitiesField.getText().trim();
        String building = buildingField.getText().trim();

        // VALIDATION
        if (roomId.isEmpty() || roomName.isEmpty() || capacityTxt.isEmpty()
                || location.isEmpty()) {
            showAlert("Room ID, Room Name, Capacity, and Location are required.");
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityTxt);
        } catch (NumberFormatException ex) {
            showAlert("Capacity must be a number.");
            return;
        }

        if (repo.roomExists(roomId)) {
            showAlert("Room already exists!");
            return;
        }

        // CREATE ROOM WITH FULL DETAILS
        Room room = new Room(
                roomId,
                roomName,
                capacity,
                location,
                amenities,
                building
        );

        repo.addRoom(room);
        repo.saveToCSV();
        showSuccess("Room created successfully!");

        // CLEAR FIELDS
        roomIdField.clear();
        roomNameField.clear();
        capacityField.clear();
        locationField.clear();
        amenitiesField.clear();
        buildingField.clear();
    }

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
