package scenario4.factory;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import shared.model.Room;
import shared.model.RoomRepository;

public class ManageRoomsTableBuilder {

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
