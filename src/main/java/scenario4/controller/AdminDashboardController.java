package scenario4.controller;

import javafx.fxml.FXML;
import scenario4.MainNavigator;

public class AdminDashboardController {

    @FXML
    public void openAddRoom() throws Exception {
        System.out.println("Add Room clicked"); // DEBUG
        MainNavigator.loadPage("/scenario4/add-room.fxml");
    }

    @FXML
    public void openManageRooms() throws Exception {
        System.out.println("Manage Rooms clicked"); // DEBUG
        MainNavigator.loadPage("/scenario4/manage-rooms.fxml");
    }

    @FXML
    public void openCreateAdmin() throws Exception {
        System.out.println("Create Admin clicked"); // DEBUG
        MainNavigator.loadPage("/scenario4/create-admin.fxml");
    }
}
