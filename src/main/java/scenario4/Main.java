package scenario4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * Main – Scenario 4 (Admin Module Launcher)
 * -------------------------------------------------------------------------
 * <p>Entry point for launching the <b>Scenario 4 Admin Dashboard</b>.</p>
 *
 * <h2>Purpose</h2>
 * <ul>
 *     <li>Initializes JavaFX for the admin module</li>
 *     <li>Loads the Admin Dashboard FXML as the first screen</li>
 *     <li>Registers the primary Stage with {@code MainNavigator}</li>
 * </ul>
 *
 * <h2>Design Context</h2>
 * <ul>
 *     <li>Acts as the dedicated launcher only for Scenario 4</li>
 *     <li>Uses {@link MainNavigator} to manage navigation between admin pages</li>
 *     <li>Clean separation from Scenario 1/2/3 launchers</li>
 * </ul>
 *
 * <h2>Used By</h2>
 * <ul>
 *     <li>TA/Instructor when testing Scenario 4 independently</li>
 *     <li>Developers during admin UI debugging and feature testing</li>
 * </ul>
 */


public class Main extends Application {

    /**
     * JavaFX lifecycle entry point.
     * Loads the admin dashboard UI and displays the Scenario 4 panel.
     *
     * @param stage primary application stage provided by JavaFX
     * @throws Exception if FXML loading fails
     */

    @Override
    public void start(Stage stage) throws Exception {

        MainNavigator.setStage(stage);

        Parent root = FXMLLoader.load(getClass().getResource("/scenario4/admin-dashboard.fxml"));
        stage.setScene(new Scene(root, 900, 600));
        stage.setTitle("Scenario 4 - Admin Panel");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
