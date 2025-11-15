package scenario1.viewfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * ============================================================================
 * CLASS: LoginFX
 * PURPOSE:
 *     - This is a placeholder / example JavaFX class.
 *     - It was originally created as a simple test window to ensure
 *       JavaFX is running correctly.
 *
 * CURRENT USAGE:
 *     - This class does NOT participate in the main application flow.
 *     - Kept only for debugging, testing, or future experiments.
 *
 * WHY THIS EXISTS:
 *     - Early in development, minimal JavaFX apps like this are created to
 *       verify setup, SDK paths, configurations, etc.
 *
 * SAFE TO DELETE?
 *     - Yes. This class can be removed if no longer used.
 *     - It does NOT affect login.fxml, LoginController, or LoginApp.
 *
 * AUTHOR: Shivam Gupta
 * COURSE: EECS 3311 - Conference Room Scheduler
 * ============================================================================
 */
public class LoginFX extends Application {

    /**
     * Creates a very simple window with a label.
     * Used only as a JavaFX test page.
     */
    @Override
    public void start(Stage stage) {

        // Title at the top of the window
        stage.setTitle("Login Screen");

        // Simple UI element for testing
        Label lbl = new Label("This will be the LoginFX screen.");

        // Create scene (300x200 px)
        Scene scene = new Scene(lbl, 300, 200);

        stage.setScene(scene);   // Attach scene to window
        stage.show();            // Display window
    }

    /**
     * Launches JavaFX runtime.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
