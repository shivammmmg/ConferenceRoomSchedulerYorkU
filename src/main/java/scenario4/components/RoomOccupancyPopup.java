package scenario4.components;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import shared.model.Booking;
import shared.model.BookingRepository;

public class RoomOccupancyPopup {

    public static void show(String roomId) {

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Room Occupancy — Room " + roomId);

        Label title = new Label("Room Occupancy");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label sub = new Label("Bookings for room ID: " + roomId);
        sub.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");

        TableView<Booking> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Booking, String> bIdCol = new TableColumn<>("Booking ID");
        bIdCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getBookingId()));

        TableColumn<Booking, String> userCol = new TableColumn<>("Booked By");
        userCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getBookedBy()));

        TableColumn<Booking, String> startCol = new TableColumn<>("Start Time");
        startCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getStart().toString()));

        TableColumn<Booking, String> endCol = new TableColumn<>("End Time");
        endCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getEnd().toString()));

        table.getColumns().addAll(bIdCol, userCol, startCol, endCol);

        table.getItems().addAll(
                BookingRepository.getInstance().getBookingsForRoom(roomId)
        );

        VBox layout = new VBox(15, title, sub, table);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: white;");

        popup.setScene(new Scene(layout, 650, 420));
        popup.showAndWait();
    }
}
