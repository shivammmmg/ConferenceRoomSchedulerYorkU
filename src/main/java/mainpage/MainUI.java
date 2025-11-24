package mainpage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * MainUI – Application Entry Point
 *
 * <p>This class launches the JavaFX application for the
 * YorkU Conference Room Scheduler system. It initializes the main
 * window, loads the primary FXML (MainPage.fxml), and prepares global
 * UI resources such as custom fonts.</p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>Bootstraps the JavaFX runtime (Application.start).</li>
 *     <li>Loads the Main Page (landing screen before Scenario 1).</li>
 *     <li>Sets up initial window title and scene.</li>
 * </ul>
 *
 * <h2>Related Scenarios</h2>
 * <ul>
 *     <li><b>Scenario 1</b> – Registration & Account Management
 *         (Login is accessed from this main page)</li>
 *     <li><b>Scenario 4</b> – Admin & Room Management
 *         (Admins also enter through Login)</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <p>This class does not implement patterns directly, but it initializes
 * UI screens that use Singleton, Builder, Factory, Observer, Strategy,
 * and Facade patterns across the four scenarios.</p>
 */


public class MainUI extends Application {


    /**
     * Starts the JavaFX application and loads the main landing page.
     *
     * <p>This method is invoked automatically by the JavaFX runtime when the
     * application launches. It prepares global UI resources such as the Inter
     * font, loads the MainPage.fxml layout, initializes the scene, and displays
     * the primary application window.</p>
     *
     * <h2>Workflow</h2>
     * <ol>
     *     <li>Loads custom fonts for consistent UI styling.</li>
     *     <li>Loads the Main Page (MainPage.fxml) via FXMLLoader.</li>
     *     <li>Constructs a new Scene with the loaded UI.</li>
     *     <li>Applies the stage title and attaches the scene.</li>
     *     <li>Displays the window to the user.</li>
     * </ol>
     *
     * <h2>Scenario Mapping</h2>
     * <ul>
     *     <li><b>Scenario 1</b> – Starting point before Login and Registration flows.</li>
     *     <li><b>Scenario 4</b> – Admin Dashboard becomes accessible after login.</li>
     * </ul>
     *
     * @param stage the main JavaFX window created by the runtime
     * @throws Exception if the FXML or font resource cannot be loaded
     */

    @Override
    public void start(Stage stage) throws Exception {

        // Load Inter font properly
        Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-Regular.ttf"), 14);

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/viewfx/MainPage.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setTitle("YorkU Room Scheduler");
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Launches the JavaFX application.
     *
     * <p>This is the actual JVM entry point. Internally, it delegates to
     * {@link Application#launch(String...)} which triggers the JavaFX startup
     * lifecycle and invokes the {@code start(Stage)} method.</p>
     *
     * @param args command-line arguments (not used in this application)
     */

    public static void main(String[] args) { launch(args); }
}
