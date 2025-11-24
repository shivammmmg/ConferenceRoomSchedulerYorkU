package scenario1.controller;

import scenario1.controller.NavigationHelper;
import scenario1.controller.UserManager;

import javafx.stage.Stage;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * RegisterController
 * -----------------------------------------------------------
 * Handles ALL UI logic for the Registration Screen.
 *
 * Features:
 *  - Animated background + logo drop/bounce
 *  - Email auto-suggestions (e.g., @my.yorku.ca)
 *  - Password visibility toggle (eye button)
 *  - Password strength indicator with color-coded bar
 *  - Dynamic Student ID visibility based on selected role
 *  - Full validation through UserManager
 *  - Navigation to Login screen
 *
 * This controller directly supports Scenario 1 requirements.
 */
public class RegisterController implements Initializable {

    // -----------------------------
    // UI COMPONENTS (injected from FXML)
    // -----------------------------
    @FXML private ImageView backgroundImage;
    @FXML private Rectangle darkOverlay;
    @FXML private VBox formCard;
    @FXML private ImageView logoImage;

    // User input fields
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField visiblePasswordField;  // used when password is revealed
    @FXML private ProgressBar strengthBar;
    @FXML private Label strengthLabel;

    // User type dropdown
    @FXML private ComboBox<String> typeBox;

    // Student fields (shown only when type = Student)
    @FXML private Label studentIdLbl;
    @FXML private TextField studentIdField;

    // Buttons
    @FXML private Button registerBtn;
    @FXML private Button backBtn;
    @FXML private Button showPassBtn;

    // Root container used for popup ownership
    @FXML private StackPane root;

    // Auto-suggest menu for email domains
    private final ContextMenu emailSuggestions = new ContextMenu();


    /**
     * Initializes the Registration screen.
     *
     * <p>Called automatically when register.fxml loads. This method prepares
     * all UI behaviors including animations, email suggestions, password
     * strength meter, user-type dependent logic, and button actions.</p>
     *
     * <h2>Scenario Mapping</h2>
     * Scenario 1 — Registration & Account Management
     *
     * @param url unused
     * @param resourceBundle unused
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setupBackgroundEffects();
        setupLogoAnimation();
        setupEmailSuggestions();
        setupUserTypeLogic();
        setupPasswordStrengthListener();
        setupPasswordToggle();
        setupRegisterButton();
        setupBackButton();
    }

    /**
     * Configures the blurred background and ensures the dark overlay always matches
     * the screen size. Creates a modern dimmed UI theme for the registration form.
     */
    private void setupBackgroundEffects() {

        // Apply blur to background for a premium UI feel
        GaussianBlur blur = new GaussianBlur(11);
        backgroundImage.setEffect(blur);

        // Ensure overlay always matches the image size
        darkOverlay.widthProperty().bind(backgroundImage.fitWidthProperty());
        darkOverlay.heightProperty().bind(backgroundImage.fitHeightProperty());
    }

    /**
     * Animates the YorkU logo using a slide-down and bounce motion.
     *
     * <p>Provides a polished and engaging entry animation for the Registration UI.</p>
     */

    private void setupLogoAnimation() {

        // Start above screen (hidden)
        logoImage.setTranslateY(-700);
        logoImage.setOpacity(1); // no fade, only movement animation

        // Launcher slide animation
        TranslateTransition slide = new TranslateTransition(Duration.millis(2500), logoImage);
        slide.setFromY(-700);
        slide.setToY(0);
        slide.setInterpolator(javafx.animation.Interpolator.SPLINE(0.25, 0.1, 0.25, 1));

        // Small bounce after landing
        TranslateTransition bounce = new TranslateTransition(Duration.millis(300), logoImage);
        bounce.setFromY(0);
        bounce.setToY(-20);
        bounce.setCycleCount(2);
        bounce.setAutoReverse(true);

        slide.setOnFinished(e -> bounce.play());
        slide.play();
    }

    /**
     * Adds Gmail-style auto-complete suggestions to the email field.
     *
     * <p>As soon as the user types before '@', a list of domain suggestions
     * such as @my.yorku.ca, @yorku.ca, @gmail.com, etc., is shown.</p>
     */

    private void setupEmailSuggestions() {

        // Suggested popular domains
        List<String> domains = Arrays.asList(
                "@my.yorku.ca", "@yorku.ca", "@gmail.com",
                "@yahoo.com", "@hotmail.com", "@outlook.com"
        );

        // Trigger suggestions when user types
        emailField.textProperty().addListener((obs, oldVal, newVal) -> {
            emailSuggestions.getItems().clear();

            int at = newVal.indexOf('@');

            // Only show suggestions when '@' exists
            if (at == -1) {
                emailSuggestions.hide();
                return;
            }

            String before = newVal.substring(0, at);

            // Create menu items
            for (String domain : domains) {
                MenuItem item = new MenuItem(before + domain);
                item.setOnAction(e -> emailField.setText(item.getText()));
                emailSuggestions.getItems().add(item);
            }

            emailSuggestions.show(emailField, Side.BOTTOM, 0, 0);
        });

        // Hide menu when field loses focus
        emailField.focusedProperty().addListener((obs, old, focused) -> {
            if (!focused) emailSuggestions.hide();
        });
    }

    /**
     * Enables the password visibility toggle (eye button).
     *
     * <p>Switches between the hidden PasswordField and the visible TextField
     * while keeping their values synchronized through bidirectional binding.</p>
     */

    private void setupPasswordToggle() {

        // Both fields always share the same text
        passwordField.textProperty().bindBidirectional(visiblePasswordField.textProperty());

        showPassBtn.setOnAction(e -> {
            boolean showing = visiblePasswordField.isVisible();

            if (showing) {
                // Switch back to hidden mode
                passwordField.setVisible(true);
                passwordField.setManaged(true);

                visiblePasswordField.setVisible(false);
                visiblePasswordField.setManaged(false);

                showPassBtn.setText("👁");
            } else {
                // Switch to visible mode
                visiblePasswordField.setVisible(true);
                visiblePasswordField.setManaged(true);

                passwordField.setVisible(false);
                passwordField.setManaged(false);

                showPassBtn.setText("🙈");
            }
        });
    }

    /**
     * Configures dynamic UI behavior based on the selected user type.
     *
     * <p>Shows the Student ID field only for Students. Also adjusts email
     * placeholder hints according to the selected user role.</p>
     */

    private void setupUserTypeLogic() {

        typeBox.getItems().addAll("Student", "Faculty", "Staff", "Partner");

        typeBox.setOnAction(e -> {
            String type = typeBox.getValue();
            boolean isStudent = "Student".equals(type);

            // Only display Student ID for students
            studentIdLbl.setVisible(isStudent);
            studentIdField.setVisible(isStudent);

            // Adjust email hint depending on type
            if (isStudent)
                emailField.setPromptText("e.g., name@my.yorku.ca");
            else if ("Faculty".equals(type) || "Staff".equals(type))
                emailField.setPromptText("e.g., name@yorku.ca");
            else
                emailField.setPromptText("e.g., name@domain.com");
        });
    }

    /**
     * Attaches a listener to update the password strength meter in real time.
     *
     * <p>Delegates to updatePasswordStrength(String) to compute score and update UI.</p>
     */

    private void setupPasswordStrengthListener() {
        passwordField.textProperty().addListener((obs, old, val) -> updatePasswordStrength(val));
    }

    /**
     * Evaluates the strength of the provided password and updates the UI meter.
     *
     * <h2>Scoring Rules</h2>
     * <ul>
     *     <li>+25% if length ≥ 8</li>
     *     <li>+25% if contains uppercase letters</li>
     *     <li>+25% if contains digits</li>
     *     <li>+25% if contains symbols</li>
     * </ul>
     *
     * <p>Updates the progress bar and color-coded feedback label accordingly.</p>
     *
     * @param password the password text to evaluate
     */

    private void updatePasswordStrength(String password) {

        double strength = 0;

        // Scoring rules (25% per rule satisfied)
        if (password.length() >= 8) strength += 0.25;
        if (Pattern.compile("[A-Z]").matcher(password).find()) strength += 0.25;
        if (Pattern.compile("[0-9]").matcher(password).find()) strength += 0.25;
        if (Pattern.compile("[^A-Za-z0-9]").matcher(password).find()) strength += 0.25;

        strengthBar.setProgress(strength);

        // Remove previous style
        strengthBar.getStyleClass().removeAll("weak-bar", "fair-bar", "strong-bar");

        // Color + label update
        if (strength < 0.4) {
            strengthLabel.setText("Strength: Weak");
            strengthLabel.setStyle("-fx-text-fill: #d9534f;");
            strengthBar.getStyleClass().add("weak-bar");
        }
        else if (strength < 0.8) {
            strengthLabel.setText("Strength: Fair");
            strengthLabel.setStyle("-fx-text-fill: #f0ad4e;");
            strengthBar.getStyleClass().add("fair-bar");
        }
        else {
            strengthLabel.setText("Strength: Strong");
            strengthLabel.setStyle("-fx-text-fill: #5cb85c;");
            strengthBar.getStyleClass().add("strong-bar");
        }
    }

    /**
     * Registers a new user after performing full validation.
     *
     * <h2>Validation Logic</h2>
     * <ul>
     *     <li>Student → must have a 9-digit Student ID</li>
     *     <li>Email, name, password validated via UserManager</li>
     * </ul>
     *
     * <h2>Workflow</h2>
     * <ol>
     *     <li>Validate form</li>
     *     <li>Call UserManager.register()</li>
     *     <li>Show success/error dialog</li>
     *     <li>Clear form on success</li>
     * </ol>
     *
     * <p>Implements Requirement 1 (Account Creation & Verification).</p>
     */

    private void setupRegisterButton() {

        registerBtn.setOnAction(e -> {
            try {
                String type = typeBox.getValue();
                String studentId = studentIdField.getText().trim();

                // ---- STUDENT NUMBER VALIDATION ----
                if ("Student".equals(type)) {
                    if (!studentId.matches("\\d{9}")) {
                        showErrorDialog("Student ID must be EXACTLY 9 digits.");
                        return;
                    }
                }

                // Attempt registration via UserManager
                boolean success = UserManager.getInstance().register(
                        nameField.getText(),
                        emailField.getText(),
                        passwordField.getText(),
                        type,
                        studentId
                );

                if (success) {
                    showInfoDialog("Registration Successful!");
                    clearFields();
                }

            } catch (Exception ex) {
                showErrorDialog(ex.getMessage());
            }
        });
    }

    /**
     * Resets all input fields and UI components after successful registration.
     *
     * <p>Clears text inputs, resets password strength meter, and hides
     * student-specific fields.</p>
     */

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        passwordField.clear();
        strengthBar.setProgress(0);
        strengthLabel.setText("Strength: Weak");
        typeBox.setValue(null);
        studentIdField.clear();
        studentIdLbl.setVisible(false);
        studentIdField.setVisible(false);
    }

    /**
     * Navigates the user back to the Login screen.
     *
     * <p>Uses NavigationHelper to maintain consistent fullscreen transitions.</p>
     */

    private void setupBackButton() {
        backBtn.setOnAction(e ->
                NavigationHelper.navigate((Stage) backBtn.getScene().getWindow(), "login.fxml")
        );
    }

    /**
     * Displays an error dialog with the given message.
     *
     * @param message detailed error text to show to the user
     */

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Registration Failed");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.initOwner(root.getScene().getWindow());
        alert.showAndWait();
    }

    /**
     * Displays a success/information dialog.
     *
     * @param message message to show after successful registration
     */

    private void showInfoDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Registration Completed");
        alert.setContentText(message);
        alert.initOwner(root.getScene().getWindow());
        alert.showAndWait();
    }
}
