package Scenario1.viewfxml.controller;

import Scenario1.controller.UserManager;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class RegisterController implements Initializable {

    // FX IDs matching FXML
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passField;
    @FXML private TextField visiblePassField;
    @FXML private Button showPassBtn;
    @FXML private ComboBox<String> userTypeBox;
    @FXML private TextField studentIdField;
    @FXML private Label studentIdLbl;
    @FXML private ProgressBar strengthBar;
    @FXML private Label strengthLabel;
    @FXML private Button registerBtn;
    @FXML private Button backBtn;
    @FXML private Pane formCard;
    @FXML private HBox root;

    private final ContextMenu emailSuggestions = new ContextMenu();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // =====================================================
        // EMAIL DOMAIN SUGGESTIONS
        // =====================================================
        List<String> domains = Arrays.asList(
                "@my.yorku.ca", "@yorku.ca", "@gmail.com", "@yahoo.com", "@hotmail.com", "@outlook.com"
        );

        emailField.textProperty().addListener((obs, oldVal, newVal) -> {
            emailSuggestions.getItems().clear();
            int atIndex = newVal.indexOf('@');
            if (atIndex == -1) {
                emailSuggestions.hide();
                return;
            }

            String beforeAt = newVal.substring(0, atIndex);

            for (String domain : domains) {
                MenuItem item = new MenuItem(beforeAt + domain);
                item.setOnAction(e -> emailField.setText(item.getText()));
                emailSuggestions.getItems().add(item);
            }

            emailSuggestions.show(emailField, Side.BOTTOM, 0, 0);
        });

        emailField.focusedProperty().addListener((obs, old, focused) -> {
            if (!focused) emailSuggestions.hide();
        });

        // =====================================================
        // SHOW/HIDE PASSWORD
        // =====================================================
        showPassBtn.setOnAction(e -> {
            boolean visible = visiblePassField.isVisible();
            visiblePassField.setText(passField.getText());
            visiblePassField.setVisible(!visible);
            visiblePassField.setManaged(!visible);
            passField.setVisible(visible);
            passField.setManaged(visible);
        });

        passField.textProperty().bindBidirectional(visiblePassField.textProperty());
        passField.textProperty().addListener((obs, o, v) -> updatePasswordStrength(v));
        visiblePassField.textProperty().addListener((obs, o, v) -> updatePasswordStrength(v));

        // =====================================================
        // USER TYPE LOGIC
        // =====================================================
        userTypeBox.getItems().addAll("Student", "Faculty", "Staff", "Partner");

        userTypeBox.setOnAction(e -> {
            String type = userTypeBox.getValue();
            boolean isStudent = "Student".equals(type);

            studentIdLbl.setVisible(isStudent);
            studentIdField.setVisible(isStudent);

            if ("Student".equals(type))
                emailField.setPromptText("e.g., name@my.yorku.ca");
            else if ("Faculty".equals(type) || "Staff".equals(type))
                emailField.setPromptText("e.g., name@yorku.ca");
            else
                emailField.setPromptText("e.g., name@domain.com");
        });

        // =====================================================
        // REGISTER BUTTON
        // =====================================================
        registerBtn.setOnAction(e -> {
            try {
                boolean success = UserManager.getInstance().register(
                        nameField.getText(),
                        emailField.getText(),
                        passField.getText(),
                        userTypeBox.getValue(),
                        studentIdField.getText()
                );

                if (success) {
                    showToast("✅ Registration Successful!");
                    nameField.clear();
                    emailField.clear();
                    passField.clear();
                    visiblePassField.clear();
                    userTypeBox.setValue(null);
                    studentIdField.clear();
                    studentIdLbl.setVisible(false);
                    studentIdField.setVisible(false);
                }

            } catch (Exception ex) {
                showToast("❌ " + ex.getMessage());
            }
        });

        // =====================================================
        // BACK BUTTON (same action as before)
        // =====================================================
        backBtn.setOnAction(e -> {
            // You will replace this with FXML navigation
            System.out.println("Back button clicked — implement navigation here");
        });
    }

    // =====================================================
    // PASSWORD STRENGTH LOGIC
    // =====================================================
    private void updatePasswordStrength(String password) {
        double strength = 0;
        if (password.length() >= 8) strength += 0.25;
        if (Pattern.compile("[A-Z]").matcher(password).find()) strength += 0.25;
        if (Pattern.compile("[0-9]").matcher(password).find()) strength += 0.25;
        if (Pattern.compile("[^A-Za-z0-9]").matcher(password).find()) strength += 0.25;

        strengthBar.setProgress(strength);

        if (strength < 0.4) {
            strengthLabel.setText("Strength: Weak");
            strengthLabel.setStyle("-fx-text-fill: #d9534f;");
        } else if (strength < 0.8) {
            strengthLabel.setText("Strength: Fair");
            strengthLabel.setStyle("-fx-text-fill: #f0ad4e;");
        } else {
            strengthLabel.setText("Strength: Strong");
            strengthLabel.setStyle("-fx-text-fill: #5cb85c;");
        }
    }

    // =====================================================
    // TOAST MESSAGE
    // =====================================================
    private void showToast(String message) {
        Label toast = new Label(message);
        toast.setStyle("-fx-background-color: #333; -fx-text-fill: white; "
                + "-fx-padding: 10 20 10 20; -fx-background-radius: 8;");
        toast.setOpacity(0);

        root.getChildren().add(toast);
        HBox.setMargin(toast, new Insets(20, 0, 0, 20));

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), toast);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(0.9);
        fadeIn.play();

        FadeTransition fadeOut = new FadeTransition(Duration.millis(600), toast);
        fadeOut.setDelay(Duration.seconds(2.5));
        fadeOut.setFromValue(0.9);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> root.getChildren().remove(toast));
        fadeOut.play();
    }
}
