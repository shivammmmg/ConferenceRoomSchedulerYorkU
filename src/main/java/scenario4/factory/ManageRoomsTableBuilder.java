package scenario4.factory;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import shared.model.Room;
import shared.model.RoomRepository;

/**
 * ManageRoomsTableBuilder – Scenario 4 (Admin → Manage Rooms)
 * -------------------------------------------------------------------------
 * <p>This utility class builds the full <b>Manage Rooms</b> table UI
 * programmatically. It is used when Scenario 4 pages are generated dynamically
 * rather than from static FXML layouts.</p>
 *
 * <h2>Purpose</h2>
 * <ul>
 *     <li>Constructs a complete TableView for managing rooms</li>
 *     <li>Populates the table with RoomRepository data</li>
 *     <li>Creates an inline Delete button column for each row</li>
 *     <li>Injects the finished UI into any provided VBox container</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li><b>Factory / Builder hybrid</b> – encapsulates UI construction logic</li>
 *     <li>Keeps FXML files lighter by generating table structure in Java</li>
 *     <li>Follows Scenario 4 architecture where reusable UI components are
 *         created via small factories/builders</li>
 * </ul>
 *
 * <h2>Used By</h2>
 * <ul>
 *     <li>Scenario 4 Admin pages</li>
 *     <li>Any dynamic room-management view needing an auto-built table</li>
 * </ul>
 */


public class ManageRoomsTableBuilder {

    /**
     * Builds and injects the full "Manage Rooms" table layout into the given VBox.
     *
     * <p>This method constructs:</p>
     * <ul>
     *     <li>Title label ("Manage Rooms")</li>
     *     <li>TableView with columns for Room ID, Capacity, Location, Status</li>
     *     <li>A Delete action column (per-row delete button)</li>
     *     <li>Data loaded from {@link RoomRepository#getAllRoomsList()}</li>
     * </ul>
     *
     * <p>When a row's Delete button is pressed:</p>
     * <ul>
     *     <li>The room is deleted from the RoomRepository</li>
     *     <li>The rooms.csv file is automatically updated</li>
     *     <li>The table row is removed from the UI</li>
     * </ul>
     *
     * @param root the VBox container where the table UI will be rendered
     */

    public static void loadManageRooms(VBox root) {

        Label title = new Label("Manage Rooms");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TableView<Room> table = new TableView<>();

        TableColumn<Room, String> idCol = new TableColumn<>("Room ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("roomId"));

        TableColumn<Room, Integer> capCol = new TableColumn<>("Capacity");
        capCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        TableColumn<Room, String> locCol = new TableColumn<>("Location");
        locCol.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Room, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Room, Void> deleteCol = new TableColumn<>("Delete");

        deleteCol.setCellFactory(param -> new TableCell<>() {
            private final Button deleteBtn = new Button("Delete");

            {
                deleteBtn.setOnAction(e -> {
                    Room r = getTableView().getItems().get(getIndex());
                    RoomRepository repo = RoomRepository.getInstance();
                    repo.deleteRoom(r.getRoomId());
                    repo.saveToCSV();
                    getTableView().getItems().remove(r);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });

        table.getColumns().addAll(idCol, capCol, locCol, statusCol, deleteCol);


        table.getItems().setAll(RoomRepository.getInstance().getAllRoomsList());

        VBox box = new VBox(15, title, table);
        box.setPadding(new Insets(20));

        root.getChildren().add(box);
    }
}
