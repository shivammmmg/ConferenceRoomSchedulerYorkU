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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ManageRoomsController – Scenario 4 (Administration & System Management)
 * ---------------------------------------------------------------------------
 * <p>Controller for the “Manage Rooms” page. Provides full administrative
 * control over room lifecycle actions, including:</p>
 *
 * <ul>
 *     <li>Viewing all rooms in the system</li>
 *     <li>Enabling, disabling, and marking rooms as under maintenance</li>
 *     <li>Editing room details (capacity, location)</li>
 *     <li>Viewing detailed room information in a popup</li>
 * </ul>
 *
 * <h2>Integration Points</h2>
 * <ul>
 *     <li>Loads and persists room data using {@link RoomRepository}</li>
 *     <li>Uses JavaFX TableView for all room information</li>
 *     <li>Uses reusable UI components like {@link RoomDetailsPopup}</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li>MVC Controller (Scenario 4 top-level UI)</li>
 *     <li>Repository Pattern for room persistence (rooms.csv)</li>
 *     <li>Modular popups for consistent admin UI experience</li>
 * </ul>
 */


public class ManageRoomsController {

    @FXML
    private TableView<Room> roomsTable;

    @FXML
    private VBox tableContainer;   // VBox that holds the table + buttons row

    private final RoomRepository repo = RoomRepository.getInstance();

    /**
     * Called automatically when the FXML view loads.
     * Initializes the table structure, loads all room data,
     * and adds the admin action buttons (Enable / Disable / Maintenance / Update / Details).
     */

    @FXML
    public void initialize() {
        setupTable();
        loadRooms();
        addActionButtons();
    }

    /**
     * Builds the column structure for the rooms TableView.
     * Columns displayed:
     * <ul>
     *     <li>Room ID</li>
     *     <li>Name</li>
     *     <li>Capacity</li>
     *     <li>Location</li>
     *     <li>Amenities</li>
     *     <li>Building</li>
     *     <li>Status</li>
     * </ul>
     *
     * Uses {@link PropertyValueFactory} to bind JavaFX table columns to
     * Room model properties.
     */

    private void setupTable() {

        TableColumn<Room, String> idCol = new TableColumn<>("Room ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("roomId"));

        TableColumn<Room, String> nameCol = new TableColumn<>("Room Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("roomName"));

        TableColumn<Room, Integer> capCol = new TableColumn<>("Capacity");
        capCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        TableColumn<Room, String> locCol = new TableColumn<>("Location");
        locCol.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Room, String> amenitiesCol = new TableColumn<>("Amenities");
        amenitiesCol.setCellValueFactory(new PropertyValueFactory<>("amenities"));

        TableColumn<Room, String> buildingCol = new TableColumn<>("Building");
        buildingCol.setCellValueFactory(new PropertyValueFactory<>("building"));

        TableColumn<Room, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        roomsTable.getColumns().addAll(
                idCol, nameCol, capCol, locCol,
                amenitiesCol, buildingCol, statusCol
        );
    }

    /**
     * Loads all rooms from rooms.csv using {@link shared.util.CSVHelper}.
     * <p>Steps:</p>
     * <ol>
     *     <li>Reads the CSV file</li>
     *     <li>Clears the in-memory RoomRepository map</li>
     *     <li>Populates the repository</li>
     *     <li>Displays rooms in the TableView</li>
     * </ol>
     *
     * Shows a warning popup if loading fails.
     */

    private void loadRooms() {
        try {
            var loaded = shared.util.CSVHelper.loadRooms("data/rooms.csv");

            repo.getAllRooms().clear();
            for (Room r : loaded) {
                repo.getAllRooms().put(r.getRoomId(), r);
            }

            roomsTable.getItems().setAll(repo.getAllRooms().values());

        } catch (Exception ex) {
            ex.printStackTrace();
            showWarning("Failed to load room data.");
        }
    }

    /**
     * Creates and styles the action buttons for admin actions:
     * <ul>
     *     <li>Enable Room</li>
     *     <li>Disable Room</li>
     *     <li>Maintenance Mode</li>
     *     <li>View Room Details</li>
     *     <li>Update Room</li>
     * </ul>
     *
     * Adds the button row beneath the table.
     */

    private void addActionButtons() {

        // Remove any OLD button rows (but keep the table and search etc.)
        tableContainer.getChildren().removeIf(node -> node instanceof HBox);

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

    /**
     * Opens a read-only popup showing full room details using
     * {@link RoomDetailsPopup}. Requires a row to be selected.
     */

    private void onViewDetails() {
        Room selected = roomsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Please select a room first.");
            return;
        }
        RoomDetailsPopup.show(selected);
    }

    /**
     * Updates the selected room's operational state.
     * <p>After status update:</p>
     * <ul>
     *     <li>Persists the change to rooms.csv</li>
     *     <li>Refreshes the TableView</li>
     *     <li>Prints an audit log entry to the console</li>
     * </ul>
     *
     * Shows a warning if no room is selected.
     */

    private void enableRoom() {
        Room selected = roomsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Select a room.");
            return;
        }

        selected.setStatus("ENABLED");
        repo.updateRoom(selected);      // persist to CSV
        roomsTable.refresh();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println();
        System.out.println("┌──────────────────────────────────────── ROOM STATUS ───────────────────────────────────────────┐");

        String line = "│ %-12s : %-77s │";
        System.out.println(String.format(line, "Action", "ENABLE"));
        System.out.println(String.format(line, "RoomID", selected.getRoomId()));
        System.out.println(String.format(line, "Status", "ENABLED"));
        System.out.println(String.format(line, "Timestamp", now.format(fmt)));

        System.out.println("└────────────────────────────────────────────────────────────────────────────────────────────────┘");
        System.out.println();

    }

    private void disableRoom() {
        Room selected = roomsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Select a room.");
            return;
        }

        selected.setStatus("DISABLED");
        repo.updateRoom(selected);      // persist to CSV
        roomsTable.refresh();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println();
        System.out.println("┌──────────────────────────────────────── ROOM STATUS ───────────────────────────────────────────┐");

        String line = "│ %-12s : %-77s │";
        System.out.println(String.format(line, "Action", "DISABLE"));
        System.out.println(String.format(line, "RoomID", selected.getRoomId()));
        System.out.println(String.format(line, "Status", "DISABLED"));
        System.out.println(String.format(line, "Timestamp", now.format(fmt)));

        System.out.println("└────────────────────────────────────────────────────────────────────────────────────────────────┘");
        System.out.println();

    }

    private void markMaintenance() {
        Room selected = roomsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Select a room.");
            return;
        }

        selected.setStatus("MAINTENANCE");
        repo.updateRoom(selected);      // persist to CSV
        roomsTable.refresh();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println();
        System.out.println("┌──────────────────────────────────────── ROOM STATUS ───────────────────────────────────────────┐");

        String line = "│ %-12s : %-77s │";
        System.out.println(String.format(line, "Action", "MAINTENANCE"));
        System.out.println(String.format(line, "RoomID", selected.getRoomId()));
        System.out.println(String.format(line, "Status", "MAINTENANCE"));
        System.out.println(String.format(line, "Timestamp", now.format(fmt)));

        System.out.println("└────────────────────────────────────────────────────────────────────────────────────────────────┘");
        System.out.println();

    }

    /**
     * Opens a modal popup allowing the admin to edit the selected room’s
     * capacity and location.
     *
     * <p>Process:</p>
     * <ol>
     *     <li>Validate that a room is selected</li>
     *     <li>Display editable fields</li>
     *     <li>Persist new values to {@link RoomRepository}</li>
     *     <li>Refresh the TableView</li>
     *     <li>Print an audit log entry for the update</li>
     * </ol>
     *
     * Uses an undecorated JavaFX modal window for a clean admin look.
     */

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

                selected.setCapacity(newCap);
                selected.setLocation(newLoc);

                repo.updateRoom(selected);
                roomsTable.refresh();

                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                System.out.println();
                System.out.println("┌──────────────────────────────────────── ROOM UPDATED ───────────────────────────────────────────┐");

                String line = "│ %-12s : %-77s │";
                System.out.println(String.format(line, "RoomID", selected.getRoomId()));
                System.out.println(String.format(line, "Capacity", String.valueOf(newCap)));
                System.out.println(String.format(line, "Location", newLoc));
                System.out.println(String.format(line, "Timestamp", now.format(fmt)));

                System.out.println("└──────────────────────────────────────────────────────────────────────────────────────────────────┘");
                System.out.println();

                stage.close();

            } catch (Exception ex) {
                showWarning("Enter valid values.");
            }
        });

        VBox root = new VBox(15, title, capField, locField, saveBtn);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: white; -fx-background-radius: 15;");

        stage.setScene(new Scene(root, 350, 300));
        stage.show();
    }

    /**
     * Displays a simple warning dialog with the provided message.
     *
     * @param msg text to display in the alert dialog
     */

    private void showWarning(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
