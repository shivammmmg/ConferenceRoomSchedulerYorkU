package scenario1.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

/**
 * NavigationHelper – Scenario 1 (Registration & Account Management)
 *
 * <p>This utility class centralizes scene navigation for all Scenario 1 controllers.
 * It ensures that all FXML transitions use a consistent process and that fullscreen
 * mode is restored after each navigation event.</p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>Loads and switches JavaFX scenes uniformly across Scenario 1.</li>
 *     <li>Re-applies fullscreen mode after navigation (required because alerts
 *         temporarily disable fullscreen).</li>
 *     <li>Removes the default ESC fullscreen-exit hint and key binding.</li>
 *     <li>Reduces code duplication inside Login, Register, and ForgotPassword controllers.</li>
 * </ul>
 *
 * <h2>Relevant Requirements</h2>
 * <ul>
 *     <li><b>Req1</b> – Supports the registration/login flow by enabling smooth
 *         transitions between Login, Register, and Forgot Password screens.</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li><b>Facade-style Utility</b>: Provides a single access point for scene
 *         transitions, simplifying controller code.</li>
 *     <li>Supports the Singleton-based UserManager by keeping screens consistent.</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * <pre>
 * NavigationHelper.navigate(stage, "login.fxml");
 * NavigationHelper.navigate(stage, "register.fxml");
 * NavigationHelper.navigate(stage, "forgot_password.fxml");
 * </pre>
 */

public class NavigationHelper {

    /**
     * Navigates to another FXML view within Scenario 1.
     *
     * <p>This method loads the specified FXML file, replaces the current scene,
     * and re-applies fullscreen settings. JavaFX modal alerts temporarily break
     * fullscreen mode, so restoring fullscreen ensures all Scenario 1 pages
     * behave consistently.</p>
     *
     * <h2>Behavior</h2>
     * <ul>
     *     <li>Loads the new FXML layout from /scenario1/fxml/</li>
     *     <li>Replaces the scene on the provided Stage</li>
     *     <li>Re-enables fullscreen mode</li>
     *     <li>Hides the default “Press ESC to exit fullscreen” hint</li>
     *     <li>Disables ESC from exiting fullscreen (NO_MATCH)</li>
     * </ul>
     *
     * <h2>Scenario Mapping</h2>
     * Scenario 1 – Login, Registration, Forgot Password screens
     *
     * @param stage    the JavaFX Stage whose scene will be replaced
     * @param fxmlPath the FXML filename (e.g., "login.fxml")
     */

    public static void navigate(Stage stage, String fxmlPath) {
        try {
            // Load resource using absolute path
            FXMLLoader loader = new FXMLLoader(
                    NavigationHelper.class.getResource("/scenario1/fxml/" + fxmlPath)
            );

            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Replace the current scene with the new one
            stage.setScene(scene);

            // Re-apply fullscreen settings on every screen change
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");                         // Hide "press ESC to exit fullscreen"
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // Disable exit key

        } catch (Exception e) {
            System.err.println("[Navigation Error] Failed to load FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }
}
