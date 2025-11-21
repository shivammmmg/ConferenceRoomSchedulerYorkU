package scenario4.components;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import shared.model.Booking;
import shared.model.BookingRepository;
import scenario3.RoomStatusManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RoomOccupancyPopup {

    private static Timeline sensorTimeline;

    // Nice date formatting: "Nov 21, 2025 09:00 AM"
    private static final DateTimeFormatter NICE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd, yyyy  hh:mm a");

    public static void show(String roomId) {

        // ======================================================
        // CREATE POPUP & ASSIGN OWNER (IMPORTANT!)
        // ======================================================
        Stage popup = new Stage();

        // Get currently focused AdminFX window
        Stage owner = (Stage) Stage.getWindows()
                .stream()
                .filter(Window::isFocused)
                .findFirst()
                .orElse(null);

        if (owner != null) {
            popup.initOwner(owner);
        }

        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Room Occupancy — Room " + roomId);

        // ======================================================
        // BACK BUTTON
        // ======================================================
        Button backBtn = new Button("← Back");
        backBtn.setStyle("-fx-background-color: #8b0000; -fx-text-fill: white;");
        backBtn.setOnAction(e -> {
            popup.close();

            // Bring AdminFX window back to front + fullscreen
            if (owner != null) {
                owner.show();
                owner.toFront();
                owner.requestFocus();
                owner.setFullScreen(true);
            }
        });

        HBox backBox = new HBox(backBtn);
        backBox.setAlignment(Pos.CENTER_LEFT);

        // ======================================================
        // TITLE + SUBTITLE
        // ======================================================
        Label title = new Label("Room Occupancy");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label sub = new Label("Live occupancy + upcoming bookings for Room: " + roomId);
        sub.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");

        // ======================================================
        // BOOKINGS TABLE (FILTERED)
        // ======================================================
        TableView<Booking> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Booking, String> bIdCol = new TableColumn<>("Booking ID");
        bIdCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getBookingId()));

        TableColumn<Booking, String> userCol = new TableColumn<>("Booked By");
        userCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getUserId()));

        TableColumn<Booking, String> startCol = new TableColumn<>("Start Time");
        startCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getStartTime().format(NICE_FORMAT)
                ));

        TableColumn<Booking, String> endCol = new TableColumn<>("End Time");
        endCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getEndTime().format(NICE_FORMAT)
                ));

        table.getColumns().addAll(bIdCol, userCol, startCol, endCol);

        // Filter only ongoing or upcoming bookings
        List<Booking> filtered = BookingRepository.getInstance()
                .getBookingsForRoom(roomId)
                .stream()
                .filter(b -> b.getEndTime().isAfter(LocalDateTime.now()))
                .toList();

        table.getItems().addAll(filtered);

        // ======================================================
        // SENSOR LOG LISTVIEW
        // ======================================================
        ListView<String> logView = new ListView<>();
        logView.setPrefHeight(180);

        // ======================================================
        // START SENSOR SIMULATION
        // ======================================================
        startSensorSimulation(roomId, logView);

        popup.setOnCloseRequest(e -> {
            if (sensorTimeline != null)
                sensorTimeline.stop();
        });

        // ======================================================
        // PAGE LAYOUT
        // ======================================================
        VBox layout = new VBox(15, backBox, title, sub, table, logView);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: white;");

        popup.setScene(new Scene(layout, 720, 520));
        popup.show();
    }

    // =====================================================
    // 30-SECOND SENSOR LOG UPDATER
    // =====================================================
    private static void startSensorSimulation(String roomId, ListView<String> logView) {

        if (sensorTimeline != null)
            sensorTimeline.stop();

        sensorTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> appendSensorTick(roomId, logView)),
                new KeyFrame(Duration.seconds(30))
        );

        sensorTimeline.setCycleCount(Timeline.INDEFINITE);
        sensorTimeline.play();
    }

    private static void appendSensorTick(String roomId, ListView<String> logView) {

        RoomStatusManager statusManager = RoomStatusManager.getInstance();
        LocalDateTime now = LocalDateTime.now();

        // Check if there is an active booking
        Booking active = scenario2.controller.BookingManager.getInstance()
                .getActiveBookingForRoom(roomId, now);

        if (active == null) {
            // No booking → room MUST be AVAILABLE
            statusManager.updateOccupancy(roomId, false);
        } else {
            // Booking exists:
            if (now.isAfter(active.getEndTime())) {
                // Booking ended → room empty now
                statusManager.updateOccupancy(roomId, false);
            } else {
                // Booking has not ended → only IN_USE if user checked in
                boolean inUse = "IN_USE".equals(active.getStatus());
                statusManager.updateOccupancy(roomId, inUse);
            }
        }

        // Now fetch status AFTER updating
        String status = statusManager.getRoomStatus(roomId);

        String line = "[" + now.format(NICE_FORMAT) + "] "
                + "Sensor ping → Room " + roomId + " status = " + status;

        logView.getItems().add(line);
        logView.scrollTo(logView.getItems().size() - 1);
    }

}
