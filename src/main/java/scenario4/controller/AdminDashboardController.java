package scenario4.controller;

import javafx.fxml.FXML;
import scenario4.MainNavigator;


/**
 * AdminDashboardController – Scenario 4 (Administration & System Management)
 * ----------------------------------------------------------------------------
 * <p>This controller handles navigation inside the Admin Dashboard. Each button
 * on the dashboard routes the administrator to a different management module:</p>
 *
 * <ul>
 *     <li><b>Add Room</b> – create new rooms</li>
 *     <li><b>Manage Rooms</b> – edit, delete, enable/disable rooms</li>
 *     <li><b>Create Admin</b> – add system administrators or chief coordinators</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li>Works with {@link MainNavigator} to load FXML screens.</li>
 *     <li>Part of Scenario 4’s MVC structure (FXML view + controller layer).</li>
 *     <li>Keeps navigation separated from business logic for clean architecture.</li>
 * </ul>
 */

public class AdminDashboardController {

    /**
     * Navigates to the "Add Room" page inside the Admin panel.
     * Triggered when the admin clicks the “Add Room” tile/button.
     */

    @FXML
    public void openAddRoom() throws Exception {
        System.out.println("Add Room clicked"); // DEBUG
        MainNavigator.loadPage("/scenario4/add-room.fxml");
    }

    /**
     * Opens the Manage Rooms module where administrators can modify,
     * disable, or delete existing rooms.
     */

    @FXML
    public void openManageRooms() throws Exception {
        System.out.println("Manage Rooms clicked"); // DEBUG
        MainNavigator.loadPage("/scenario4/manage-rooms.fxml");
    }

    /**
     * Navigates to the "Create Admin" screen for adding new system
     * administrators or chief event coordinators.
     */

    @FXML
    public void openCreateAdmin() throws Exception {
        System.out.println("Create Admin clicked"); // DEBUG
        MainNavigator.loadPage("/scenario4/create-admin.fxml");
    }
}
