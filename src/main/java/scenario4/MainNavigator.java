package scenario4;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * MainNavigator – Scenario 4 (Admin Module Navigation Helper)
 * -------------------------------------------------------------------------
 * <p>Centralized navigation utility for Scenario 4. Provides a clean,
 * unified API for switching between Admin UI pages inside the same
 * primary Stage.</p>
 *
 * <h2>Purpose</h2>
 * <ul>
 *     <li>Stores a reference to the main application Stage</li>
 *     <li>Loads FXML pages on demand and replaces the scene</li>
 *     <li>Keeps all navigation logic in one place (reducing duplication)</li>
 * </ul>
 *
 * <h2>Design Context</h2>
 * <ul>
 *     <li>Utility-class role: acts as a simple navigation Facade</li>
 *     <li>Used exclusively by controllers inside Scenario 4</li>
 *     <li>Makes admin page transitions consistent and maintainable</li>
 * </ul>
 *
 * <h2>Used By</h2>
 * <ul>
 *     <li>{@code AdminDashboardController}</li>
 *     <li>{@code AddRoomController}</li>
 *     <li>{@code ManageRoomsController}</li>
 *     <li>{@code CreateAdminController}</li>
 * </ul>
 */


public class MainNavigator {

    private static Stage mainStage;
    /**
     * Registers the primary Stage for Scenario 4 navigation.
     * Must be called once by the launcher before any page loads.
     *
     * @param stage the main application window
     */
    public static void setStage(Stage stage) {
        mainStage = stage;
    }

    /**
     * Loads a new FXML page into the admin panel.
     *
     * @param fxmlPath absolute FXML resource path (e.g. "/scenario4/manage-rooms.fxml")
     * @throws Exception if the FXML file cannot be loaded
     */

    public static void loadPage(String fxmlPath) throws Exception {
        Parent root = FXMLLoader.load(MainNavigator.class.getResource(fxmlPath));
        mainStage.setScene(new Scene(root, 900, 600));
        mainStage.show();
    }
}
