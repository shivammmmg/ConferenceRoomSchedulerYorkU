package scenario3.viewfxml.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import scenario3.controller.RoomStatusManager;
import scenario3.controller.SensorSystem;
import scenario3.model.Room;
import scenario3.observer.RoomStatusObserver;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable, RoomStatusObserver {

    @FXML private ListView<Room> roomListView;
    @FXML private Label roomNameLabel;
    @FXML private Label roomStatusLabel;
    @FXML private Button simulateBtn;
    @FXML private TextArea eventLog;

    private final RoomStatusManager manager = RoomStatusManager.getInstance();
    private final SensorSystem sensors = new SensorSystem();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        manager.registerObserver(this);
        sensors.configure(new ArrayList<>(manager.getAllRooms()), this::appendLog);

        roomListView.setItems(FXCollections.observableArrayList(manager.getAllRooms()));

        roomListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Room room, boolean empty) {
                super.updateItem(room, empty);
                if (empty || room == null) { setText(null); return; }
                setText(room.getName() + " — " + room.getStatus());
            }
        });

        roomListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldV, newV) -> showRoomDetails(newV)
        );

        simulateBtn.setOnAction(e -> handleSimulateSensors());

        // select first room by default
        Platform.runLater(() -> {
            if (!roomListView.getItems().isEmpty())
                roomListView.getSelectionModel().select(0);
        });
    }

    private void handleSimulateSensors() {
        if (simulateBtn.getText().startsWith("Start")) {
            sensors.startSimulation();
            simulateBtn.setText("Stop Sensors");
        } else {
            sensors.stopSimulation();
            simulateBtn.setText("Start Sensors");
        }
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
        Platform.runLater(roomListView::refresh);
    }
}
