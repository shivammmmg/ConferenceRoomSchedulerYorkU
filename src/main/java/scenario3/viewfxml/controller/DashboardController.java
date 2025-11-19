package scenario3.viewfxml.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import scenario3.controller.CheckInManager;
import scenario3.controller.RoomStatusManager;
import scenario3.controller.SensorSystem;
import scenario3.model.Booking;
import scenario3.model.Room;
import scenario3.model.RoomStatus;
import scenario3.observer.RoomStatusObserver;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.*;

/**
 * DashboardController with countdown timer displayed in the ListView.
 * Updated so that when countdown reaches zero, a "Deposit Forfeited"
 * popup appears exactly once — never repeats.
 */
public class DashboardController implements Initializable, RoomStatusObserver {

    @FXML private ListView<Room> roomListView;
    @FXML private Label roomNameLabel;
    @FXML private Label roomStatusLabel;
    @FXML private Button checkInBtn;
    @FXML private Button clearBookingBtn;
    @FXML private Button simulateBtn;
    @FXML private Button createBookingBtn;
    @FXML private TextArea eventLog;

    private final RoomStatusManager manager = RoomStatusManager.getInstance();
    private final SensorSystem sensors = new SensorSystem();
    private final CheckInManager checkInManager = new CheckInManager();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private final Map<String, ScheduledFuture<?>> bookingTimers = new ConcurrentHashMap<>();
    private final Map<String, Long> bookingCountdowns = new ConcurrentHashMap<>();

    private static final DateTimeFormatter LOG_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        manager.registerObserver(this);
        sensors.configure(new ArrayList<>(manager.getAllRooms()), this::appendLog);

        roomListView.setItems(FXCollections.observableArrayList(manager.getAllRooms()));

        // Custom cell factory for showing countdown
        roomListView.setCellFactory(lv -> new ListCell<>() {
            private final HBox hbox = new HBox(10);
            private final Text nameText = new Text();
            private final Text statusText = new Text();
            private final Text countdownText = new Text();

            {
                hbox.getChildren().addAll(nameText, statusText, countdownText);
            }

            @Override
            protected void updateItem(Room room, boolean empty) {
                super.updateItem(room, empty);
                if (empty || room == null) {
                    setGraphic(null);
                    return;
                }
                nameText.setText(room.getName() + " (" + room.getId() + ")");
                statusText.setText("[" + room.getStatus() + "]");

                Long remaining = room.getCurrentBookingId() != null ?
                        bookingCountdowns.get(room.getCurrentBookingId()) : null;

                if (remaining != null && remaining >= 0) {
                    long min = remaining / 60;
                    long sec = remaining % 60;
                    countdownText.setText(String.format("%02d:%02d", min, sec));
                } else {
                    countdownText.setText("");
                }
                setGraphic(hbox);
            }
        });

        roomListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldV, newV) -> showRoomDetails(newV)
        );

        checkInBtn.setOnAction(e -> handleCheckIn());
        clearBookingBtn.setOnAction(e -> handleClearBooking());
        simulateBtn.setOnAction(e -> handleSimulateSensors());
        createBookingBtn.setOnAction(e -> handleCreateBooking());

        Platform.runLater(() -> {
            if (!roomListView.getItems().isEmpty()) {
                roomListView.getSelectionModel().select(0);
            }
        });
    }

    private void handleCheckIn() {
        Room r = roomListView.getSelectionModel().getSelectedItem();
        if (r == null) return;

        String bookingId = r.getCurrentBookingId();
        if (bookingId == null) return;

        boolean ok = checkInManager.checkIn(bookingId, "demo-user");
        appendLog(ok ? "Checked in booking " + bookingId : "Check-in failed");

        stopCountdown(bookingId);
    }

    private void handleClearBooking() {
        Room r = roomListView.getSelectionModel().getSelectedItem();
        if (r == null) return;

        String bookingId = r.getCurrentBookingId();
        manager.clearBookingFromRoom(r.getId());
        appendLog("Cleared booking for " + r.getId());

        stopCountdown(bookingId);
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

    private void handleCreateBooking() {
        Room r = roomListView.getSelectionModel().getSelectedItem();
        if (r == null) {
            r = manager.getAllRooms().iterator().next();
        }

        String bookingId = checkInManager.createBooking(
                r.getId(),
                "demo-user",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1)
        );

        appendLog("Created booking " + bookingId + " for room " + r.getId());

        startBookingCountdown(r, bookingId, 30 * 60); // 30 minute timer
    }

    private void showRoomDetails(Room room) {
        if (room == null) {
            roomNameLabel.setText("—");
            roomStatusLabel.setText("—");
            return;
        }
        roomNameLabel.setText(room.getName() + " (" + room.getId() + ")");
        updateStatusLabel(room.getStatus());
    }

    private void updateStatusLabel(RoomStatus s) {
        roomStatusLabel.setText(s.name());
        roomStatusLabel.getStyleClass().removeIf(c -> c.startsWith("status-"));
        roomStatusLabel.getStyleClass().add("status-" + s.name());
    }

    private void appendLog(String msg) {
        eventLog.appendText(LOG_FORMATTER.format(LocalDateTime.now()) +
                " — " + msg + "\n");
    }

    /**
     * Starts countdown. When it reaches zero:
     * - markNoShow() triggers
     * - popup appears ONLY ONCE
     */
    private void startBookingCountdown(Room room, String bookingId, long seconds) {

        bookingCountdowns.put(bookingId, seconds);

        Runnable countdown = new Runnable() {
            long remaining = seconds;

            @Override
            public void run() {
                Booking b = manager.getBooking(bookingId);
                if (b == null) {
                    stopCountdown(bookingId);
                    return;
                }

                // Stop if user already checked in
                if (b.isCheckedIn()) {
                    stopCountdown(bookingId);
                    return;
                }

                // Update countdown
                bookingCountdowns.put(bookingId, remaining);
                Platform.runLater(roomListView::refresh);
                remaining--;

                // When countdown hits zero — mark no-show immediately
                if (remaining < 0) {
                    stopCountdown(bookingId);

                    // prevent double popup
                    if (!b.isForfeitPopupShown()) {
                        b.setForfeitPopupShown(true);

                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("No Show");
                            alert.setHeaderText(null);
                            alert.setContentText("Check in was not on time! Deposit has been forfeited.");
                            alert.showAndWait();
                        });
                    }

                    manager.markNoShow(bookingId);
                }
            }
        };

        ScheduledFuture<?> future =
                scheduler.scheduleAtFixedRate(countdown, 0, 1, TimeUnit.SECONDS);

        bookingTimers.put(bookingId, future);
    }

    private void stopCountdown(String bookingId) {
        bookingCountdowns.remove(bookingId);
        ScheduledFuture<?> task = bookingTimers.remove(bookingId);
        if (task != null) task.cancel(true);
        Platform.runLater(roomListView::refresh);
    }

    @Override
    public void onRoomStatusChanged(Room room) {
        Platform.runLater(roomListView::refresh);
    }
}
