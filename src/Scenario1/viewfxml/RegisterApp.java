package Scenario1.viewfxml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.input.KeyCombination;

/**
 * ============================================================================
 * CLASS: RegisterApp
 * PURPOSE:
 *     - Standalone launcher for the Registration screen.
 *     - Primarily used for direct testing of register.fxml during development.
 *     - Loads and displays the registration UI in fullscreen mode.
 *
 * WHEN USED:
 *     - Run this class only when you want to test the Register UI independently,
 *       without going through the Login screen.
 *
 * NOTES:
 *     - In the final integrated app, navigation to this screen happens
 *       through the LoginController via NavigationHelper.
 *
 * AUTHOR: Shivam Gupta
 * COURSE: EECS 3311 - Conference Room Scheduler
 * ============================================================================
 */
public class RegisterApp extends Application {

    /**
     * Called automatically when JavaFX starts.
     * Loads register.fxml and applies fullscreen settings.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Load FXML from the Scenario1/viewfxml/fxml directory
        Parent root = FXMLLoader.load(getClass().getResource("fxml/register.fxml"));

        // Create scene based on loaded UI
        Scene scene = new Scene(root);

        // Window title
        primaryStage.setTitle("YorkU Conference Scheduler - Register");
        primaryStage.setScene(scene);

        // ----------------------------------------------------------
        // FULLSCREEN SETTINGS
        // ----------------------------------------------------------
        primaryStage.setFullScreen(true);                     // Enter fullscreen automatically
        primaryStage.setFullScreenExitHint("");               // Remove the ESC hint
        primaryStage.setFullScreenExitKeyCombination(
                KeyCombination.NO_MATCH                       // Disable ESC exiting fullscreen
        );

        // Display the window
        primaryStage.show();
    }

    /**
     * Launches JavaFX runtime and then calls start().
     */
    public static void main(String[] args) {
        launch(args);
    }
}
