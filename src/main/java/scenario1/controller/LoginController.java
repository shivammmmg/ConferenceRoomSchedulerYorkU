package scenario1.controller;

import scenario2.viewfx.BookingFX;
import shared.model.User;
import shared.model.UserType;
import shared.model.SystemUser;
import scenario1.controller.UserManager;
import scenario1.controller.NavigationHelper;
import shared.util.GlobalNavigationHelper;
import scenario4.AdminFX;

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

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * LoginController – Scenario 1 (Registration & Account Management)
 *
 * <p>This controller manages the Login workflow for the YorkU Conference Room
 * Scheduler system. It authenticates users, validates credentials, provides UI
 * animations/effects, and redirects the user to the correct scenario based on
 * their role.</p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>Authenticates users via UserManager (Singleton).</li>
 *     <li>Checks password correctness and account existence (Req1).</li>
 *     <li>Provides email auto-complete suggestions.</li>
 *     <li>Handles password visibility toggle in the UI.</li>
 *     <li>Displays fullscreen-safe modal alerts for errors and successes.</li>
 *     <li>Performs animated UI transitions (logo drop, fade-in, blur effects).</li>
 *     <li>Navigates to Forgot Password and Register pages.</li>
 * </ul>
 *
 * <h2>Role-Based Redirection (System Requirement)</h2>
 * <ul>
 *     <li><b>STUDENT / FACULTY / STAFF / PARTNER</b> → Scenario 2 (BookingFX)</li>
 *     <li><b>ADMIN / CHIEF_EVENT_COORDINATOR</b> → Scenario 4 (AdminFX)</li>
 * </ul>
 *
 * <h2>Relevant Requirements</h2>
 * <ul>
 *     <li><b>Req1</b> – Users must authenticate with valid accounts.</li>
 *     <li><b>Req2</b> – Chief Event Coordinator manages Admin accounts (login validation supports this).</li>
 *     <li><b>Req3–10</b> – Determines routing to Booking or Admin dashboards.</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li><b>Singleton</b>: UserManager provides consistent authentication state.</li>
 *     <li><b>Facade</b>: NavigationHelper and GlobalNavigationHelper abstract scene changes.</li>
 * </ul>
 *
 * <h2>Notes</h2>
 * <ul>
 *     <li>Supports disabled admin accounts (Req2 edge case).</li>
 *     <li>Ensures correct scenario navigation based on UserType.</li>
 * </ul>
 */


public class LoginController implements Initializable {

    @FXML private StackPane root;
    @FXML private ImageView backgroundImage;
    @FXML private Rectangle darkOverlay;
    @FXML private ImageView logoImage;

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField visiblePasswordField;

    @FXML private Button showPassBtn;
    @FXML private Button loginBtn;
    @FXML private Button forgotBtn;
    @FXML private Button registerBtn;

    private final ContextMenu emailSuggestions = new ContextMenu();

    /**
     * Initializes the Login page UI.
     *
     * <p>Called automatically after FXML loading. Sets up all animations,
     * email suggestion logic, password visibility toggle, button actions,
     * and a fade-in transition for a polished UX.</p>
     *
     * <h2>Scenario Mapping</h2>
     * Scenario 1 – Registration & Account Management
     *
     * @param url unused
     * @param resourceBundle unused
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setupBackgroundEffects();
        setupLogoAnimation();
        setupEmailSuggestions();
        setupPasswordToggle();
        setupLoginButton();
        setupForgotButton();
        setupRegisterButton();

        root.setOpacity(0);
        javafx.application.Platform.runLater(() -> {
            FadeTransition fade = new FadeTransition(Duration.millis(900), root);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();
        });
    }

    /**
     * Applies blur and overlay effects to the login background image.
     *
     * <p>Creates a modern dimmed login interface and ensures the translucent
     * overlay always matches the current window size.</p>
     */

    private void setupBackgroundEffects() {
        backgroundImage.setEffect(new GaussianBlur(10));
        darkOverlay.widthProperty().bind(backgroundImage.fitWidthProperty());
        darkOverlay.heightProperty().bind(backgroundImage.fitHeightProperty());
    }

    /**
     * Runs the animated drop-down + bounce effect for the YorkU logo.
     *
     * <p>Enhances the Login page with smooth motion and fade-in animations,
     * contributing to an improved first-impression UI experience.</p>
     */

    private void setupLogoAnimation() {
        logoImage.setTranslateY(-500);
        logoImage.setOpacity(0);

        TranslateTransition slide = new TranslateTransition(Duration.millis(2000), logoImage);
        slide.setFromY(-500);
        slide.setToY(0);

        FadeTransition fade = new FadeTransition(Duration.millis(300), logoImage);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition bounce = new TranslateTransition(Duration.millis(250), logoImage);
        bounce.setFromY(0);
        bounce.setToY(-20);
        bounce.setAutoReverse(true);
        bounce.setCycleCount(2);

        slide.setOnFinished(e -> bounce.play());
        fade.play();
        slide.play();
    }

    private void setupEmailSuggestions() {
        List<String> domains = Arrays.asList("@my.yorku.ca", "@yorku.ca", "@gmail.com");

        emailField.textProperty().addListener((obs, oldVal, newVal) -> {

            emailSuggestions.getItems().clear();

            int atIndex = newVal.indexOf("@");
            if (atIndex == -1) {
                emailSuggestions.hide();
                return;
            }

            String before = newVal.substring(0, atIndex);

            for (String d : domains) {
                MenuItem item = new MenuItem(before + d);
                item.setOnAction(e -> emailField.setText(item.getText()));
                emailSuggestions.getItems().add(item);
            }

            emailSuggestions.show(emailField, Side.BOTTOM, 0, 0);
        });

        emailField.focusedProperty().addListener((obs, old, focused) -> {
            if (!focused) emailSuggestions.hide();
        });
    }

    /**
     * Enables the show/hide password feature.
     *
     * <p>Binds the password field to a visible text field and toggles between
     * them when the user clicks the eye-icon button.</p>
     */

    private void setupPasswordToggle() {

        passwordField.textProperty().bindBidirectional(visiblePasswordField.textProperty());

        showPassBtn.setOnAction(e -> {

            boolean showing = visiblePasswordField.isVisible();

            visiblePasswordField.setVisible(!showing);
            visiblePasswordField.setManaged(!showing);

            passwordField.setVisible(showing);
            passwordField.setManaged(showing);

            showPassBtn.setText(showing ? "👁" : "🙈");
        });
    }

    /**
     * Handles full login logic including validation, authentication,
     * and role-based redirection.
     *
     * <h2>Validation Steps</h2>
     * <ul>
     *     <li>Checks if email and password fields are not empty.</li>
     *     <li>Verifies email existence using UserManager (Singleton).</li>
     *     <li>Validates password correctness.</li>
     *     <li>Blocks disabled Admin accounts (Req2).</li>
     * </ul>
     *
     * <h2>Scenario Routing</h2>
     * <ul>
     *     <li>Students / Faculty / Staff / Partners → Scenario 2 (BookingFX)</li>
     *     <li>Admin / Chief Event Coordinator → Scenario 4 (AdminFX)</li>
     * </ul>
     *
     * <p>Implements requirement-driven behavior for system role control.</p>
     */

    private void setupLoginButton() {
        loginBtn.setOnAction(e -> {

            String email = emailField.getText().trim();
            String pass = passwordField.getText().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                showError("Please fill in all fields.");
                return;
            }

            if (!UserManager.getInstance().checkIfEmailRegistered(email)) {
                showError("No account found with this email.");
                return;
            }

            User logged = UserManager.getInstance().login(email, pass);

            if (logged == null) {
                showError("Incorrect password.");
                return;
            }

            // NEW: Block disabled accounts
            if (logged instanceof SystemUser) {
                SystemUser su = (SystemUser) logged;

                if (!su.isActive()) {
                    showError("Your admin account has been disabled.\nPlease contact the Chief Event Coordinator.");
                    return;
                }
            }


            SystemUser sysUser = (SystemUser) logged;
            UserType type = sysUser.getType();

            switch (type) {

                // NORMAL USERS → Scenario 2
                case STUDENT:
                case FACULTY:
                case STAFF:
                case PARTNER:
                    BookingFX app = new BookingFX();
                    app.setLoggedInUser(logged.getEmail(), type.name());
                    app.start(new Stage());
                    break;

                // ADMIN + CHIEF → Scenario 4
                case ADMIN:
                case CHIEF_EVENT_COORDINATOR:
                    try {
                        AdminFX adminUI = new AdminFX();
                        adminUI.setLoggedInAdmin(logged.getEmail(), type.name());
                        adminUI.start(new Stage());

                        // Close login window
                        ((Stage) loginBtn.getScene().getWindow()).close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        showError("Failed to load Admin Panel.");
                    }
                    break;

                default:
                    showError("Unknown user type: " + type);
            }
        });
    }

    /**
     * Navigates to the Forgot Password screen.
     *
     * <p>Uses NavigationHelper to maintain consistent fullscreen behavior and
     * screen transition logic.</p>
     */

    private void setupForgotButton() {
        forgotBtn.setOnAction(e ->
                NavigationHelper.navigate((Stage) forgotBtn.getScene().getWindow(), "forgot_password.fxml"));
    }

    /**
     * Navigates to the Registration page.
     *
     * <p>Redirects new users to create an account before attempting to log in.</p>
     */

    private void setupRegisterButton() {
        registerBtn.setOnAction(e ->
                NavigationHelper.navigate((Stage) registerBtn.getScene().getWindow(), "register.fxml"));
    }

    /**
     * Returns the user to the Main Landing Page.
     *
     * <p>Uses GlobalNavigationHelper for cross-scenario navigation.</p>
     */

    @FXML
    private void goBack() {
        GlobalNavigationHelper.navigateTo("/mainpage/MainPage.fxml");
    }

    /**
     * Displays a fullscreen-safe error popup.
     *
     * @param message the error message to show
     *
     * <p>Temporarily exits fullscreen to display the modal alert, then restores it.</p>
     */

    private void showError(String message) {
        Stage stage = (Stage) root.getScene().getWindow();

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Failed");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.initOwner(stage);

        stage.setFullScreen(false);
        alert.showAndWait();
        stage.setFullScreen(true);
    }

    /**
     * Displays a fullscreen-safe success/information popup.
     *
     * @param message the information message to show
     */

    private void showInfo(String message) {
        Stage stage = (Stage) root.getScene().getWindow();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Login Successful");
        alert.setContentText(message);
        alert.initOwner(stage);

        stage.setFullScreen(false);
        alert.showAndWait();
        stage.setFullScreen(true);
    }
}
