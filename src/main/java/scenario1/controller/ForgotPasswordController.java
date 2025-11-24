package scenario1.controller;

import scenario1.controller.UserManager;
import scenario1.controller.NavigationHelper;
import shared.model.User;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


/**
 * ForgotPasswordController – Scenario 1 (Registration & Account Management)
 *
 * <p>This controller manages the "Forgot Password" workflow in Scenario 1.
 * It handles UI effects, email validation, autocomplete suggestions, and
 * navigation back to the Login page.</p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>Validates whether a given email exists in the system (Req1).</li>
 *     <li>Provides email domain auto-complete suggestions.</li>
 *     <li>Handles password-reset request feedback (success/failure).</li>
 *     <li>Supports modern UI animations (logo drop, fade-in, blur effects).</li>
 *     <li>Shows fullscreen-safe popups for alerts and errors.</li>
 *     <li>Navigates back to Login.fxml via NavigationHelper.</li>
 * </ul>
 *
 * <h2>Related Requirements</h2>
 * <ul>
 *     <li><b>Req 1</b> – Account Creation & Verification: Includes email validation and login recovery.</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li>Uses UserManager (Singleton) to check email existence.</li>
 *     <li>Uses NavigationHelper (Facade-style utility) for screen transitions.</li>
 * </ul>
 *
 * <h2>UI/UX Features</h2>
 * <ul>
 *     <li>Blurred background overlay</li>
 *     <li>Drop-bounce logo animation</li>
 *     <li>Smooth fade-in transitions</li>
 *     <li>Auto-complete domain suggestions</li>
 * </ul>
 */

public class ForgotPasswordController implements Initializable {

    // ------------------------------
    // FXML UI COMPONENTS
    // ------------------------------
    @FXML private StackPane root;               // Root container for scene transitions + alerts
    @FXML private ImageView backgroundImage;    // Blurred background image
    @FXML private Rectangle darkOverlay;        // Semi-transparent overlay
    @FXML private ImageView logoImage;          // YorkU logo with animation
    @FXML private TextField emailField;         // User’s email input
    @FXML private Button resetBtn;              // Reset password action button
    @FXML private Button backBtn;               // Navigation back to Login

    // Context menu for domain suggestions as user types
    private final ContextMenu emailSuggestions = new ContextMenu();


    /**
     * Initializes the Forgot Password page.
     *
     * <p>Loads all UI components, animations, autocomplete behavior,
     * validation logic, and applies a smooth fade-in transition.</p>
     *
     * <h2>Scenario Mapping</h2>
     * <ul>
     *     <li>Scenario 1 — Registration & Account Management</li>
     * </ul>
     *
     * @param url   unused resource locator
     * @param resourceBundle unused resources bundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setupBackgroundEffects();      // Blur + overlay binding
        setupLogoAnimation();          // Slide + bounce animation
        setupEmailSuggestions();       // Domain autocomplete
        setupResetButton();            // Reset password validation logic
        setupBackButton();             // Back to Login navigation

        // Smooth fade-in transition when screen loads
        root.setOpacity(0);
        javafx.application.Platform.runLater(() -> {
            FadeTransition fade = new FadeTransition(Duration.millis(900), root);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();
        });
    }


    // ----------------------------------------------------------
    // POPUP HANDLER (FULLSCREEN SAFE)
    // ----------------------------------------------------------

    /**
     * Displays a fullscreen-safe popup alert.
     *
     * <p>Since JavaFX modal dialogs cannot appear above a fullscreen
     * window, this method temporarily disables fullscreen, shows the
     * alert, and restores fullscreen afterward.</p>
     *
     * @param type   the type of alert (ERROR, INFORMATION, etc.)
     * @param title  title of the dialog
     * @param header header text
     * @param content detailed message content
     */

    private void showPopup(Alert.AlertType type, String title, String header, String content) {

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        // Link popup to current window
        Stage stage = (Stage) root.getScene().getWindow();
        alert.initOwner(stage);

        // JavaFX Fix: must exit fullscreen before showing modal alert
        stage.setFullScreen(false);
        alert.showAndWait();
        stage.setFullScreen(true);  // Restore fullscreen automatically
    }


    /**
     * Navigates to another FXML file using NavigationHelper.
     *
     * <p>Keeps the Forgot Password controller free of navigation logic
     * while maintaining consistent fullscreen behavior.</p>
     *
     * @param fxml name of the FXML file (e.g., "login.fxml")
     */
    private void navigate(String fxml) {
        NavigationHelper.navigate((Stage) root.getScene().getWindow(), fxml);
    }


    // ----------------------------------------------------------
    // BACKGROUND SETUP
    // ----------------------------------------------------------

    /**
     * Applies blur to the background and binds the overlay dimensions.
     *
     * <p>This creates a dimmed background aesthetic and ensures the
     * overlay always matches the window size, even when resized.</p>
     */

    private void setupBackgroundEffects() {
        backgroundImage.setEffect(new GaussianBlur(10));

        // Bind overlay size to background image so it covers the entire window
        darkOverlay.widthProperty().bind(backgroundImage.fitWidthProperty());
        darkOverlay.heightProperty().bind(backgroundImage.fitHeightProperty());
    }


    // ----------------------------------------------------------
    // LOGO ANIMATION
    // ----------------------------------------------------------

    /**
     * Applies drop-down + bounce animation to the YorkU logo.
     *
     * <p>Creates a polished visual entry animation for the Forgot
     * Password screen.</p>
     */

    private void setupLogoAnimation() {

        logoImage.setTranslateY(-500);
        logoImage.setOpacity(0);

        // Slide down
        TranslateTransition slide = new TranslateTransition(Duration.millis(2200), logoImage);
        slide.setFromY(-500);
        slide.setToY(0);

        // Fade-in
        FadeTransition fade = new FadeTransition(Duration.millis(300), logoImage);
        fade.setFromValue(0);
        fade.setToValue(1);

        // Small bounce at the end
        TranslateTransition bounce = new TranslateTransition(Duration.millis(250), logoImage);
        bounce.setFromY(0);
        bounce.setToY(-15);
        bounce.setAutoReverse(true);
        bounce.setCycleCount(2);

        slide.setOnFinished(e -> bounce.play());

        fade.play();
        slide.play();
    }


    // ----------------------------------------------------------
    // EMAIL AUTOCOMPLETE
    // ----------------------------------------------------------

    /**
     * Configures intelligent email auto-complete suggestions.
     *
     * <p>As the user types before the '@', domain suggestions such as
     * @my.yorku.ca, @yorku.ca, and @gmail.com appear automatically.</p>
     */

    private void setupEmailSuggestions() {

        List<String> domains = Arrays.asList("@my.yorku.ca", "@yorku.ca", "@gmail.com");

        emailField.textProperty().addListener((obs, oldVal, newVal) -> {
            emailSuggestions.getItems().clear();

            int at = newVal.indexOf("@");
            if (at == -1) {
                emailSuggestions.hide();
                return;
            }

            String before = newVal.substring(0, at);

            // Add menu items for each domain option
            for (String d : domains) {
                MenuItem item = new MenuItem(before + d);
                item.setOnAction(e -> emailField.setText(item.getText()));
                emailSuggestions.getItems().add(item);
            }

            emailSuggestions.show(emailField, Side.BOTTOM, 0, 0);
        });

        // Hide suggestions when focus is lost
        emailField.focusedProperty().addListener((obs, old, focused) -> {
            if (!focused) emailSuggestions.hide();
        });
    }


    // ----------------------------------------------------------
    // RESET PASSWORD BUTTON
    // ----------------------------------------------------------

    /**
     * Handles the logic for the "Reset Password" button.
     *
     * <p>Validates input, checks if the email is registered, prints
     * formatted logs to the console for debugging, and shows
     * fullscreen-safe success/error alerts.</p>
     *
     * <h2>Validation Steps</h2>
     * <ol>
     *     <li>Email must not be empty</li>
     *     <li>Email must exist in UserManager</li>
     *     <li>Simulates sending reset instructions</li>
     * </ol>
     */

    private void setupResetButton() {
        resetBtn.setOnAction(e -> {

            String email = emailField.getText().trim();
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


            // EMPTY EMAIL
            if (email.isEmpty()) {

                System.out.println("");
                System.out.println("┌──────────────────────────────────── FORGOT PASSWORD ─────────────────────────────────────────┐");

                String border = "│ %-12s : %-77s │";

                System.out.println(String.format(border, "Email", "(empty)"));
                System.out.println(String.format(border, "Status", "FAILED"));
                System.out.println(String.format(border, "Reason", "No email entered"));
                System.out.println(String.format(border, "Timestamp", now.format(fmt)));

                System.out.println("└──────────────────────────────────────────────────────────────────────────────────────────────┘");
                System.out.println("");

                showError("Please enter your email.");
                return;
            }

            // EMAIL NOT REGISTERED
            if (!UserManager.getInstance().checkIfEmailRegistered(email)) {

                System.out.println("");
                System.out.println("┌──────────────────────────────────── FORGOT PASSWORD ─────────────────────────────────────────┐");

                String border = "│ %-12s : %-77s │";

                System.out.println(String.format(border, "Email", email));
                System.out.println(String.format(border, "Status", "FAILED"));
                System.out.println(String.format(border, "Reason", "Email not registered"));
                System.out.println(String.format(border, "Timestamp", now.format(fmt)));

                System.out.println("└──────────────────────────────────────────────────────────────────────────────────────────────┘");
                System.out.println("");

                showError("No account found with this email.");
                return;
            }

            // SUCCESS
            System.out.println("");
            System.out.println("┌──────────────────────────────────── FORGOT PASSWORD ─────────────────────────────────────────┐");

            String border = "│ %-12s : %-77s │";

            System.out.println(String.format(border, "Email", email));
            System.out.println(String.format(border, "Status", "SUCCESS"));
            System.out.println(String.format(border, "Message", "Reset instructions sent"));
            System.out.println(String.format(border, "Timestamp", now.format(fmt)));

            System.out.println("└──────────────────────────────────────────────────────────────────────────────────────────────┘");
            System.out.println("");

            showInfo("Password reset instructions sent (simulated).");
            emailField.clear();

        });
    }



    // ----------------------------------------------------------
    // BACK BUTTON (RETURN TO LOGIN)
    // ----------------------------------------------------------

    /**
     * Configures the Back button to return the user to Login.fxml.
     *
     * <p>This uses the shared NavigationHelper to maintain consistent
     * UI transitions across Scenario 1 pages.</p>
     */

    private void setupBackButton() {
        backBtn.setOnAction(e -> navigate("login.fxml"));
    }


    // ----------------------------------------------------------
    // POPUP SHORTCUTS
    // ----------------------------------------------------------

    private void showError(String msg) {
        showPopup(
                Alert.AlertType.ERROR,
                "Password Reset Failed",
                "Error",
                msg
        );
    }

    private void showInfo(String msg) {
        showPopup(
                Alert.AlertType.INFORMATION,
                "Request Received",
                "Success",
                msg
        );
    }
}
