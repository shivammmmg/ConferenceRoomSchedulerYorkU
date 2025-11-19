package scenario3.viewfxml.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import scenario3.controller.RoomStatusManager;
import scenario3.controller.SensorSystem;
import shared.model.Room;
import scenario3.observer.RoomStatusObserver;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable, RoomStatusObserver {

    @FXML private ListView<Room> roomListView;
    @FXML private Label roomNameLabel;
    @FXML private Label roomStatusLabel;
    @FXML private TextArea eventLog;

    private final RoomStatusManager manager = RoomStatusManager.getInstance();
    private final SensorSystem sensors = new SensorSystem();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Observer setup
        manager.registerObserver(this);

        // Configure and immediately start sensors (NO button needed)
        sensors.configure(new ArrayList<>(manager.getAllRooms()), this::appendLog);
        sensors.startSimulation(); // <-- ALWAYS RUNNING

        // List rooms
        roomListView.setItems(FXCollections.observableArrayList(manager.getAllRooms()));
        roomListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Room room, boolean empty) {
                super.updateItem(room, empty);
                if (empty || room == null) { setText(null); return; }
                setText(room.getName() + " — " + room.getStatus());
            }
        });

        // Selection updates room display
        roomListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldV, newV) -> showRoomDetails(newV)
        );

        // Auto-select first room
        Platform.runLater(() -> {
            if (!roomListView.getItems().isEmpty())
                roomListView.getSelectionModel().select(0);
        });
    }

    private void showRoomDetails(Room room) {
        if (room == null) return;
        roomNameLabel.setText(room.getName());
        roomStatusLabel.setText(room.getStatus().name());
    }

    private void appendLog(String msg) {
        eventLog.appendText(msg + "\n");
    }

    @Override
    public void onRoomStatusChanged(Room room) {
        Platform.runLater(() -> {
            roomListView.refresh();
            if (roomListView.getSelectionModel().getSelectedItem() == room) {
                showRoomDetails(room);
            }
        });
    }
}