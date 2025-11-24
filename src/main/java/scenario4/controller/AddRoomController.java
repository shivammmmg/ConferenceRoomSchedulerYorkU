package scenario4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import shared.model.Room;
import shared.model.RoomRepository;

/**
 * AddRoomController – Scenario 4 (Admin: Room Management)
 * ----------------------------------------------------------------------------
 * <p>This controller handles the “Add New Room” form within the AdminFX
 * dashboard. It allows administrators to manually create new rooms by entering
 * full room details such as:</p>
 *
 * <ul>
 *     <li>Room ID</li>
 *     <li>Name</li>
 *     <li>Capacity</li>
 *     <li>Location</li>
 *     <li>Amenities</li>
 *     <li>Building</li>
 * </ul>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>Validate all user inputs</li>
 *     <li>Prevent duplicate Room IDs</li>
 *     <li>Create a new {@link Room} object</li>
 *     <li>Persist changes through {@link RoomRepository}</li>
 *     <li>Display feedback using JavaFX alerts</li>
 *     <li>Print a detailed creation log for D3 demonstration</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li>Uses Singleton {@link RoomRepository} as the system-wide source of truth</li>
 *     <li>This controller is part of Scenario 4's MVC structure (FXML + Controller)</li>
 * </ul>
 *
 * <h2>Notes</h2>
 * <ul>
 *     <li>All fields are mandatory to ensure clean CSV entries.</li>
 *     <li>Repository auto-saves, but controller explicitly calls saveToCSV() to guarantee persistence.</li>
 * </ul>
 */


public class AddRoomController {

    @FXML private TextField roomIdField;
    @FXML private TextField roomNameField;
    @FXML private TextField capacityField;
    @FXML private TextField locationField;
    @FXML private TextField amenitiesField;
    @FXML private TextField buildingField;

    private RoomRepository repo = RoomRepository.getInstance();

    /**
     * Handles the "Create Room" button action.
     *
     * <p>This method performs:</p>
     * <ul>
     *     <li>Input validation (empty fields, numeric capacity)</li>
     *     <li>Duplicate Room ID checking</li>
     *     <li>Room object creation with all metadata</li>
     *     <li>Saving to {@link RoomRepository} and rooms.csv</li>
     *     <li>Console logging for auditing (used in Deliverable D3)</li>
     *     <li>Resetting all input fields after success</li>
     * </ul>
     *
     * <p>If validation fails, an alert is shown and the operation is aborted.</p>
     */

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

    /**
     * Utility helper for displaying a simple information alert.
     *
     * @param msg message to display to the administrator
     */

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
