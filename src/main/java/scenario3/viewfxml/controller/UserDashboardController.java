package scenario3.viewfxml.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import scenario3.controller.CheckInManager;
import scenario3.controller.RoomStatusManager;
import shared.model.Booking;
import shared.model.Room;
import shared.model.RoomStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * UserDashboardController
 * Controller for the User Dashboard (Study Room Booking)
 */
public class UserDashboardController {

    @FXML private ListView<String> roomListView;
    @FXML private Label roomNameLabel;
    @FXML private Label roomStatusLabel;
    @FXML private Button checkInBtn;
    @FXML private Label currentBookingLabel;
    @FXML private VBox timerBox; // NEW: Timer container

    private ObservableList<String> rooms;
    private final RoomStatusManager roomManager = RoomStatusManager.getInstance();
    private final CheckInManager checkInManager = new CheckInManager();
    private final Map<String, String> roomIdMap = new HashMap<>();
    private Label timerLabel;
    private AtomicInteger remainingSeconds;

    @FXML
    public void initialize() {
        // Load rooms from RoomStatusManager
        rooms = FXCollections.observableArrayList();
        for (Room r : roomManager.getAllRooms()) {
            rooms.add(r.getName());
            roomIdMap.put(r.getName(), r.getId());
        }

        roomListView.setItems(rooms);
        roomListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        roomListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            showRoomDetails(newVal);
        });

        checkInBtn.setOnAction(e -> checkIn());

        if (!rooms.isEmpty()) roomListView.getSelectionModel().selectFirst();
    }

    private void showRoomDetails(String roomName) {
        if (roomName == null) {
            roomNameLabel.setText("—");
            roomStatusLabel.setText("—");
            currentBookingLabel.setText("—");
        } else {
            roomNameLabel.setText(roomName);
            String roomId = roomIdMap.get(roomName);
            Room r = roomManager.getRoom(roomId);
            RoomStatus status = (r != null) ? r.getStatus() : RoomStatus.AVAILABLE;
            roomStatusLabel.setText(status.name());
            String bookingId = (r != null) ? r.getCurrentBookingId() : null;
            currentBookingLabel.setText(bookingId != null ? bookingId : "None");
        }
    }

    private void createBooking() {
        String selectedRoom = roomListView.getSelectionModel().getSelectedItem();
        if (selectedRoom == null) return;

        String roomId = roomIdMap.get(selectedRoom);
        LocalDateTime now = LocalDateTime.now();
        String bookingId = checkInManager.createBooking(roomId, "User1", now, now.plusHours(1));

        currentBookingLabel.setText(bookingId);
        roomStatusLabel.setText("RESERVED");

        startCountdown(30 * 60); // 30 minute timer

        showAlert("Booking Created", "You have successfully booked " + selectedRoom + ".");
    }

    private void checkIn() {
        String bookingId = currentBookingLabel.getText();
        if (bookingId == null || bookingId.equals("—") || bookingId.equals("None")) {
            showAlert("Check-In Error", "You do not have a booking to check in.");
        } else {
            checkInManager.checkIn(bookingId, "User1");
            roomStatusLabel.setText("IN_USE");
            removeTimer();
            showAlert("Checked In", "You have checked in successfully for booking " + bookingId + ".");
        }
    }

    private void startCountdown(int totalSeconds) {
        removeTimer(); // clear old timer if any

        remainingSeconds = new AtomicInteger(totalSeconds);
        timerLabel = new Label();
        timerBox.getChildren().add(timerLabel);

        Runnable updater = new Runnable() {
            @Override
            public void run() {
                int sec = remainingSeconds.getAndDecrement();

                int minutes = sec / 60;
                int seconds = sec % 60;
                String timeStr = String.format("%02d:%02d", minutes, seconds);

                Platform.runLater(() -> timerLabel.setText("Time to check-in: " + timeStr));

                if (sec <= 0) {
                    Platform.runLater(() -> {
                        removeTimer();
                        // ✅ Only show popup if the booking is NOT checked in
                        String bookingId = currentBookingLabel.getText();
                        Booking b = checkInManager.getRoomManager().getBooking(bookingId);
                        if (b != null && !b.isCheckedIn()) {
                            showAlert("No-Show", "Did not check-in in time. Forfeit Deposited.");
                            checkInManager.getRoomManager().markNoShow(bookingId);
                            roomStatusLabel.setText("NO_SHOW");
                        }
                    });
                }
            }
        };

        Thread timerThread = new Thread(() -> {
            try {
                while (remainingSeconds.get() >= 0) {
                    updater.run();
                    Thread.sleep(1000);
                }
            } catch (InterruptedException ignored) {}
        });
        timerThread.setDaemon(true);
        timerThread.start();
    }



    private void removeTimer() {
        if (timerLabel != null) {
            timerBox.getChildren().remove(timerLabel);
            timerLabel = null;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}