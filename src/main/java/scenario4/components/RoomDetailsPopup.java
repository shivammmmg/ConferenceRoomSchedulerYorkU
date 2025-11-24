package scenario4.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import shared.model.Room;

import java.util.Random;

/**
 * RoomDetailsPopup – Scenario 4 (Admin Dashboard: Live Room View)
 * ----------------------------------------------------------------------------
 * <p>A lightweight, reusable JavaFX popup that displays detailed information
 * for a selected room. This component is used in Scenario 4 (Admin/System
 * Management) to help administrators quickly inspect room metadata,
 * occupancy, and operational status.</p>
 *
 * <h2>Purpose</h2>
 * <ul>
 *     <li>Show room attributes in a clean, modal popup</li>
 *     <li>Support Scenario 4 “click to inspect room” functionality</li>
 *     <li>Simulate current occupancy (placeholder until real sensor link)</li>
 * </ul>
 *
 * <h2>Displayed Information</h2>
 * <ul>
 *     <li>Room ID</li>
 *     <li>Name & Location</li>
 *     <li>Capacity</li>
 *     <li>Status (AVAILABLE, DISABLED, MAINTENANCE, etc.)</li>
 *     <li>Simulated current occupancy (random value)</li>
 * </ul>
 *
 * <h2>UI Characteristics</h2>
 * <ul>
 *     <li>Modal window (blocks background UI until closed)</li>
 *     <li>Soft-shadow rounded card layout</li>
 *     <li>Consistent Scenario 4 typography and color palette</li>
 * </ul>
 *
 * <h2>Notes</h2>
 * <ul>
 *     <li>The occupancy value is currently simulated and not tied to real sensors.</li>
 *     <li>Used primarily inside the AdminFX dashboard when clicking on a room card.</li>
 * </ul>
 */


public class RoomDetailsPopup {

    public static void show(Room room) {

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Room Details");
        popup.setMinWidth(420);

        // ---------- TITLE ----------
        Label title = new Label("Room Details");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#1f2937"));

        // ---------- DETAILS ----------
        Label idLabel = info("Room ID: ", room.getRoomId());
        Label capLabel = info("Capacity: ", String.valueOf(room.getCapacity()));
        Label locLabel = info("Location: ", room.getLocation());
        Label statusLabel = info("Status: ", room.getStatus());

        // Fake occupancy (sensor simulation)
        Random random = new Random();
        int occupancy = random.nextInt(room.getCapacity() + 1);
        Label occLabel = info("Current Occupancy: ", occupancy + " people");

        // ---------- CLOSE BUTTON ----------
        Button closeBtn = new Button("Close");
        closeBtn.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
        closeBtn.setOnAction(e -> popup.close());

        HBox btnBox = new HBox(closeBtn);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setPadding(new Insets(10, 0, 0, 0));

        // ---------- CONTAINER ----------
        VBox layout = new VBox(12,
                title,
                idLabel, capLabel, locLabel, statusLabel,
                occLabel,
                btnBox
        );

        layout.setPadding(new Insets(25));
        layout.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 4);"
        );

        Scene scene = new Scene(layout);
        popup.setScene(scene);
        popup.showAndWait();
    }

    /**
     * Utility method for generating a styled label row inside the popup.
     * Each row combines a bold prefix (e.g., "Room ID:") with the provided value.
     *
     * @param label the human-readable field name
     * @param value the field value to display
     * @return a JavaFX Label styled for Scenario 4 popups
     */

    private static Label info(String label, String value) {
        Label l = new Label(label + value);
        l.setFont(Font.font("Segoe UI", 16));
        l.setTextFill(Color.web("#374151"));
        return l;
    }
}
