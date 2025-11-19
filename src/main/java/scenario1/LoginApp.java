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
     * Called automatically by JavaFX when the application starts.
     * Loads the login screen and applies fullscreen settings.
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
     * Launcher entrypoint.
     * Launches JavaFX, which then calls start().
     */
    public static void main(String[] args) {
        launch(args);
    }
}
