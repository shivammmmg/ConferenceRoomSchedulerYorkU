package scenario1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.input.KeyCombination;

/**
 * ============================================================================
 * CLASS: LoginApp
 * PURPOSE:
 *     - Entry point for the entire JavaFX application.
 *     - Loads the Login UI (login.fxml) as the first window.
 *     - Forces fullscreen mode for a clean, modern UI experience.
 *
 * NOTES:
 *     - Only this class is executed directly when testing Scenario 1.
 *     - Navigation to Register / Forgot Password is handled via controllers.
 *
 * AUTHOR: Shivam Gupta
 * COURSE: EECS 3311 - Conference Room Scheduler
 * ============================================================================
 */
public class LoginApp extends Application {

    /**
     * Initializes and displays the Login screen as the first UI window.
     *
     * <p>This method is invoked automatically by the JavaFX runtime after
     * {@link Application#launch(String...)} is called. It loads the login.fxml
     * layout, creates the main scene, and configures fullscreen mode for a
     * clean and immersive UI experience suitable for Scenario 1.</p>
     *
     * <h2>Responsibilities</h2>
     * <ul>
     *     <li>Loads Login UI from /scenario1/fxml/login.fxml</li>
     *     <li>Creates and attaches the main Scene</li>
     *     <li>Enables fullscreen mode and disables ESC exit</li>
     *     <li>Shows the primary stage</li>
     * </ul>
     *
     * <h2>Scenario Mapping</h2>
     * <p>This is the official launcher for <b>Scenario 1 — Registration & Login</b>.
     * From here, navigation proceeds to Register or Forgot Password screens.</p>
     *
     * @param primaryStage the JavaFX application window provided by the runtime
     * @throws Exception if login.fxml fails to load
     */

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Load login.fxml from the same package location
        Parent root = FXMLLoader.load(
                getClass().getResource("/scenario1/fxml/login.fxml")
        );

        // Create the main scene
        Scene scene = new Scene(root);

        // Window title shown in macOS toolbar
        primaryStage.setTitle("YorkU Conference Scheduler - Login");
        primaryStage.setScene(scene);

        // ----------------------------------------------------------
        // FULLSCREEN BEHAVIOR
        // ----------------------------------------------------------
        primaryStage.setFullScreen(true);                     // Enter fullscreen automatically
        primaryStage.setFullScreenExitHint("");               // Remove "Press ESC to exit fullscreen"
        primaryStage.setFullScreenExitKeyCombination(
                KeyCombination.NO_MATCH                       // Disable ESC from exiting fullscreen
        );

        // Display the window
        primaryStage.show();
    }

    /**
     * JVM entry point that launches the JavaFX application.
     *
     * <p>This delegates control to the JavaFX runtime, which then calls the
     * {@link #start(Stage)} method to initialize the UI.</p>
     *
     * @param args optional command-line arguments
     */

    public static void main(String[] args) {
        launch(args);
    }
}
