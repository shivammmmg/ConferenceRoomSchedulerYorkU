package scenario4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import shared.model.Room;
import shared.model.RoomRepository;

public class AddRoomController {

    @FXML private TextField roomIdField;
    @FXML private TextField roomNameField;
    @FXML private TextField capacityField;
    @FXML private TextField locationField;
    @FXML private TextField amenitiesField;
    @FXML private TextField buildingField;

    private RoomRepository repo = RoomRepository.getInstance();

    @FXML
    public void createRoom() {

        String id       = roomIdField.getText().trim();
        String name     = roomNameField.getText().trim();
        String capText  = capacityField.getText().trim();
        String location = locationField.getText().trim();
        String amenities = amenitiesField.getText().trim();
        String building  = buildingField.getText().trim();

        // Validate
        if (id.isEmpty() || name.isEmpty() || capText.isEmpty() ||
                location.isEmpty() || amenities.isEmpty() || building.isEmpty()) {

            showAlert("All fields are required.");
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capText);
        } catch (NumberFormatException ex) {
            showAlert("Capacity must be a number.");
            return;
        }

        if (repo.roomExists(id)) {
            showAlert("Room ID already exists!");
            return;
        }

        // CREATE fully detailed room
        Room room = new Room(id, name, capacity, location, amenities, building);

        // SAVE to repository + CSV
        repo.addRoom(room);
        repo.saveToCSV();

        // ========== ROOM CREATED LOG =========

// After repo.saveToCSV();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println();
        System.out.println("┌──────────────────────────────────────── ROOM CREATED ─────────────────────────────────────────┐");

        String line = "│ %-12s : %-77s │";
        System.out.println(String.format(line, "RoomID", id));
        System.out.println(String.format(line, "Name", name));
        System.out.println(String.format(line, "Capacity", capText));
        System.out.println(String.format(line, "Location", location));
        System.out.println(String.format(line, "Amenities", amenities));
        System.out.println(String.format(line, "Building", building));
        System.out.println(String.format(line, "Timestamp", now.format(fmt)));

        System.out.println("└────────────────────────────────────────────────────────────────────────────────────────────────┘");
        System.out.println();


        showAlert("Room added successfully!");

        roomIdField.clear();
        roomNameField.clear();
        capacityField.clear();
        locationField.clear();
        amenitiesField.clear();
        buildingField.clear();
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
