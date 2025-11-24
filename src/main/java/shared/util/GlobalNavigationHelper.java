package shared.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.input.KeyCombination;

/**
 * GlobalNavigationHelper – Unified Navigation Utility (All Scenarios)
 * ------------------------------------------------------------------------
 * <p>This class provides a **centralized navigation system** for the entire
 * Conference Room Scheduler application. Unlike the Scenario-specific
 * NavigationHelper classes, this utility works across **all modules**:
 * Main Page, Scenario 1, Scenario 2, Scenario 3, and Scenario 4.</p>
 *
 * <h2>Purpose</h2>
 * <ul>
 *     <li>Stores a global reference to the primary JavaFX Stage.</li>
 *     <li>Allows any controller to navigate to any FXML screen using a
 *         consistent method.</li>
 *     <li>Ensures unified fullscreen behavior across all scenarios.</li>
 * </ul>
 *
 * <h2>Why This Exists?</h2>
 * <p>When switching between scenarios (e.g., Login → BookingFX → AdminFX),
 * the standard JavaFX navigation becomes fragmented across many controllers.
 * This helper acts as a <b>Facade</b>, providing one shared API:</p>
 *
 * <pre>
 * GlobalNavigationHelper.navigateTo("/scenario2/fxml/BookingMain.fxml");
 * </pre>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li><b>Facade</b>: Simplifies cross-scenario scene transitions.</li>
 *     <li><b>Singleton-like Behavior</b>: Stores the main Stage globally.</li>
 *     <li>Enhances modularity by removing navigation logic from controllers.</li>
 * </ul>
 *
 * <h2>System Integration</h2>
 * <ul>
 *     <li>Registered once from Launcher.java (application startup).</li>
 *     <li>Used by controllers in all four project scenarios.</li>
 *     <li>Ensures fullscreen settings remain consistent after every navigation.</li>
 * </ul>
 *
 * <h2>Notes</h2>
 * <ul>
 *     <li>Only stores the Stage reference; it does not manage state.</li>
 *     <li>FXML paths must be absolute (starting with "/").</li>
 *     <li>Works flawlessly with Login, BookingFX, AdminFX, and sensor screens.</li>
 * </ul>
 */


public class GlobalNavigationHelper {

    private static Stage primaryStage;

    // Register the Stage from Launcher.java
    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    // Navigate to ANY FXML in ANY scenario
    public static void navigateTo(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    GlobalNavigationHelper.class.getResource(fxmlPath)
            );

            Parent root = loader.load();
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);

            // ---------------------------------------
            // FULLSCREEN settings for ALL pages
            // ---------------------------------------
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitHint("");
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

            primaryStage.show();

        } catch (Exception e) {
            System.err.println("[Navigation Error] Cannot load: " + fxmlPath);
            e.printStackTrace();
        }
    }
}
