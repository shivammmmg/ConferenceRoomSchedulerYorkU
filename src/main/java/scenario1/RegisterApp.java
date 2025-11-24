package scenario1;

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
     * Initializes and displays the Registration screen in standalone mode.
     *
     * <p>This method is executed automatically by the JavaFX runtime after
     * {@link Application#launch(String...)} is invoked. It loads the
     * <b>register.fxml</b> layout, creates the Scene, and configures fullscreen
     * behavior to match the UI standards used throughout Scenario 1.</p>
     *
     * <h2>Responsibilities</h2>
     * <ul>
     *     <li>Load and display the registration UI.</li>
     *     <li>Apply fullscreen mode and remove ESC fullscreen-exit hint.</li>
     *     <li>Allow developers to test register.fxml independently.</li>
     * </ul>
     *
     * <h2>Scenario Context</h2>
     * <p>This launcher corresponds to <b>Scenario 1 – Registration &
     * Account Management</b>. In the full system workflow, the user normally
     * navigates here through Login → Register. This class is used only for
     * direct testing.</p>
     *
     * @param primaryStage the main application window provided by JavaFX
     * @throws Exception if register.fxml fails to load
     */

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Load FXML from the Scenario1/viewfxml/fxml directory
        Parent root = FXMLLoader.load(
                getClass().getResource("/scenario1/fxml/register.fxml")
        );


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
     * Standard Java entry point that starts the JavaFX runtime.
     *
     * <p>Once launched, control is handed over to JavaFX, which then calls
     * {@link #start(Stage)} to load the Registration UI.</p>
     *
     * @param args optional command-line arguments
     */

    public static void main(String[] args) {
        launch(args);
    }
}
