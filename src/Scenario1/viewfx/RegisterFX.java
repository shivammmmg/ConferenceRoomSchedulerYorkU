package Scenario1.viewfx;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration; // === ADDED ===
import Scenario1.controller.UserManager;
import javafx.animation.TranslateTransition;
import javafx.animation.Interpolator;
import javafx.util.Duration;


import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class RegisterFX extends Application {

    @Override
    public void start(Stage stage) {
        // === Window Title ===
        stage.setTitle("YorkU Conference Room Scheduler - Register");

        // ==========================================================
        // =============== 1. LEFT BRANDING PANEL ====================
        // ==========================================================
        Stop[] stops = new Stop[]{
                new Stop(0, Color.web("#AD001D")),   // YorkU Red
                new Stop(1, Color.web("#7A0019"))    // Darker red tone for gradient
        };
        LinearGradient yorkGradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops
        );

        VBox leftPanel = new VBox();
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setPadding(new Insets(40));
        leftPanel.setSpacing(20);
        leftPanel.setPrefWidth(280);
        leftPanel.setBackground(new Background(new BackgroundFill(yorkGradient, CornerRadii.EMPTY, Insets.EMPTY)));

        // === YorkU Logo ===
        ImageView logoView = new ImageView(new Image("images/yorku_logo.png"));
        logoView.setFitHeight(80);
        logoView.setPreserveRatio(true);

        // === App Title & Tagline ===
        Label tagline = new Label("YorkU Conference Room Scheduler");
        tagline.setTextFill(Color.WHITE);
        tagline.setWrapText(true);
        tagline.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        tagline.setAlignment(Pos.CENTER);

        Label subTagline = new Label("Book smarter.\nManage better.");
        subTagline.setTextFill(Color.rgb(255, 255, 255, 0.85));
        subTagline.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        subTagline.setAlignment(Pos.CENTER);

        leftPanel.getChildren().addAll(logoView, tagline, subTagline);


        // ==========================================================
        // =============== 2. REGISTRATION FORM CARD =================
        // ==========================================================
        Label titleLbl = new Label("Create Your Account");
        titleLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        titleLbl.setTextFill(Color.web("#212121"));

        // === Name Field ===
        Label nameLbl = new Label("Full Name");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your full name");

        // === Email Field ===
        Label emailLbl = new Label("Email Address");
        TextField emailField = new TextField();
        emailField.setPromptText("e.g., name@my.yorku.ca");

        // === Email Domain Suggestions (only after '@') ===
        List<String> domains = Arrays.asList(
                "@my.yorku.ca", "@yorku.ca", "@gmail.com", "@yahoo.com", "@hotmail.com", "@outlook.com"
        );
        ContextMenu emailSuggestions = new ContextMenu();

        emailField.textProperty().addListener((obs, oldVal, newVal) -> {
            emailSuggestions.getItems().clear();
            int atIndex = newVal.indexOf('@');
            if (atIndex == -1) {
                emailSuggestions.hide(); // No '@' yet — hide dropdown
                return;
            }

            String afterAt = newVal.substring(atIndex);
            String beforeAt = newVal.substring(0, atIndex);

            if (afterAt.contains(".")) { // User already completed domain
                emailSuggestions.hide();
                return;
            }

            for (String domain : domains) {
                MenuItem item = new MenuItem(beforeAt + domain);
                item.setOnAction(e -> emailField.setText(item.getText()));
                emailSuggestions.getItems().add(item);
            }

            if (!newVal.isEmpty()) emailSuggestions.show(emailField, Side.BOTTOM, 0, 0);
            else emailSuggestions.hide();
        });
        emailField.focusedProperty().addListener((obs, old, focused) -> {
            if (!focused) emailSuggestions.hide();
        });

        // === Password Fields with Show/Hide ===
        Label passLbl = new Label("Password");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter a strong password");

        TextField visiblePassField = new TextField();
        visiblePassField.setPromptText("Enter a strong password");
        visiblePassField.setManaged(false);
        visiblePassField.setVisible(false);

        Button showPassBtn = new Button("👁");
        showPassBtn.setFocusTraversable(false);
        showPassBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 14px; -fx-cursor: hand;");

        StackPane passwordPane = new StackPane(passField, visiblePassField, showPassBtn);
        StackPane.setAlignment(showPassBtn, Pos.CENTER_RIGHT);
        StackPane.setMargin(showPassBtn, new Insets(0, 6, 0, 0));

        showPassBtn.setOnAction(e -> {
            boolean isVisible = visiblePassField.isVisible();
            visiblePassField.setText(passField.getText());
            visiblePassField.setVisible(!isVisible);
            visiblePassField.setManaged(!isVisible);
            passField.setVisible(isVisible);
            passField.setManaged(isVisible);
        });
        passField.textProperty().bindBidirectional(visiblePassField.textProperty());

        ProgressBar strengthBar = new ProgressBar(0);
        strengthBar.setPrefWidth(250);
        strengthBar.setStyle("-fx-accent: #d9534f;");
        Label strengthLabel = new Label("Strength: Weak");
        strengthLabel.setFont(Font.font("Segoe UI", 11));
        strengthLabel.setTextFill(Color.GRAY);

        passField.textProperty().addListener((obs, old, val) -> updatePasswordStrength(val, strengthBar, strengthLabel));
        visiblePassField.textProperty().addListener((obs, old, val) -> updatePasswordStrength(val, strengthBar, strengthLabel));

        Label hintLbl = new Label("Must include uppercase, lowercase, number, and symbol (8+ chars)");
        hintLbl.setTextFill(Color.GRAY);
        hintLbl.setStyle("-fx-font-size: 9.5px;");

        Label typeLbl = new Label("Account Type");
        ComboBox<String> userTypeBox = new ComboBox<>();
        userTypeBox.getItems().addAll("Student", "Faculty", "Staff", "Partner");
        userTypeBox.setPromptText("Select Type");

        Label studentIdLbl = new Label("Student ID");
        TextField studentIdField = new TextField();
        studentIdField.setPromptText("Your YorkU Student Number");
        studentIdLbl.setVisible(false);
        studentIdField.setVisible(false);

        // === ADDED: Role-based field hints ===
        userTypeBox.setOnAction(e -> {
            String type = userTypeBox.getValue();
            boolean isStudent = "Student".equals(type);
            studentIdLbl.setVisible(isStudent);
            studentIdField.setVisible(isStudent);

            if ("Student".equals(type))
                emailField.setPromptText("e.g., name@my.yorku.ca");
            else if ("Faculty".equals(type) || "Staff".equals(type))
                emailField.setPromptText("e.g., name@yorku.ca");
            else if ("Partner".equals(type))
                emailField.setPromptText("e.g., name@domain.com");
            else
                emailField.setPromptText("e.g., name@my.yorku.ca");
        });

        // === Register & Back Buttons ===
        Button registerBtn = new Button("Create Account");
        registerBtn.setPrefWidth(180);
        registerBtn.setStyle("-fx-background-color: #AD001D; -fx-text-fill: white; -fx-font-weight: bold;" +
                "-fx-font-size: 14px; -fx-background-radius: 6; -fx-cursor: hand;");

        Button backBtn = new Button("Back to Login");
        backBtn.setPrefWidth(180);
        backBtn.setStyle("-fx-background-color: #F0F0F0; -fx-text-fill: #333333; -fx-font-size: 14px;" +
                "-fx-background-radius: 6; -fx-cursor: hand;");

        // ==========================================================
        // =============== 3. FORM LAYOUT (RIGHT SIDE) ===============
        // ==========================================================
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(12);
        formGrid.setPadding(new Insets(20));

        // === Account Type First ===
        formGrid.add(typeLbl, 0, 0);
        formGrid.add(userTypeBox, 0, 1);

        // === Full Name Next ===
        formGrid.add(nameLbl, 0, 2);
        formGrid.add(nameField, 0, 3);

        // === Email ===
        formGrid.add(emailLbl, 0, 4);
        formGrid.add(emailField, 0, 5);

        // === Password + Strength ===
        formGrid.add(passLbl, 0, 6);
        formGrid.add(passwordPane, 0, 7);
        formGrid.add(strengthBar, 0, 8);
        formGrid.add(strengthLabel, 0, 9);
        formGrid.add(hintLbl, 0, 10);

        // === Student ID (conditionally visible) ===
        formGrid.add(studentIdLbl, 0, 11);
        formGrid.add(studentIdField, 0, 12);


        VBox buttonBox = new VBox(10, registerBtn, backBtn);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        VBox formCard = new VBox(15, titleLbl, formGrid, buttonBox);
        formCard.setAlignment(Pos.CENTER);
        formCard.setPadding(new Insets(30));
        formCard.setPrefWidth(380);
        formCard.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));
        formCard.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 4);");

        HBox root = new HBox(leftPanel, formCard);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(0);
        root.setBackground(new Background(new BackgroundFill(Color.web("#F5F5F5"), CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(root, 850, 520);
        stage.setScene(scene);

        // === AUTO FULL-SCREEN MODE ===
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(javafx.scene.input.KeyCombination.NO_MATCH);

        // === ADDED: Fade-in animation ===
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), formCard);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        // === ADDED: Focus glow for input fields ===
        addFocusGlow(nameField);
        addFocusGlow(emailField);
        addFocusGlow(passField);
        addFocusGlow(visiblePassField);
        addFocusGlow(studentIdField);

        stage.show();

        // ==========================================================
        // =============== 5. BUTTON ACTIONS =========================
        // ==========================================================
        registerBtn.setOnAction(e -> {
            try {
                String name = nameField.getText();
                String email = emailField.getText();
                String password = passField.isVisible() ? passField.getText() : visiblePassField.getText();
                String type = userTypeBox.getValue();
                String studentId = studentIdField.isVisible() ? studentIdField.getText() : "";

                boolean success = UserManager.getInstance().register(name, email, password, type, studentId);

                if (success) {
                    showToast(root, "✅ Registration Successful!");
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
                showToast(root, "❌ " + ex.getMessage());
            }
        });

        backBtn.setOnAction(e -> {
            stage.close();
            new LoginFX().start(new Stage());
        });
    }

    // === ADDED: Focus glow helper ===
    private void addFocusGlow(Control field) {
        field.focusedProperty().addListener((obs, old, focused) -> {
            if (focused)
                field.setStyle("-fx-effect: dropshadow(gaussian, rgba(173,0,29,0.4), 8, 0, 0, 0);");
            else
                field.setStyle("");
        });
    }

    // === ADDED: Inline toast notification helper ===
    private void showToast(Pane root, String message) {
        Label toast = new Label(message);
        toast.setStyle("-fx-background-color: #333333; -fx-text-fill: white; " +
                "-fx-padding: 10 20 10 20; -fx-background-radius: 8;");
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

    // ==============================================================
    // =============== PASSWORD STRENGTH CHECK LOGIC ================
    // ==============================================================
    private void updatePasswordStrength(String password, ProgressBar bar, Label label) {
        double strength = 0;
        if (password.length() >= 8) strength += 0.25;
        if (Pattern.compile("[A-Z]").matcher(password).find()) strength += 0.25;
        if (Pattern.compile("[0-9]").matcher(password).find()) strength += 0.25;
        if (Pattern.compile("[^A-Za-z0-9]").matcher(password).find()) strength += 0.25;

        bar.setProgress(strength);
        if (strength < 0.4) {
            bar.setStyle("-fx-accent: #d9534f;");
            label.setText("Strength: Weak");
            label.setTextFill(Color.web("#d9534f"));
        } else if (strength < 0.8) {
            bar.setStyle("-fx-accent: #f0ad4e;");
            label.setText("Strength: Fair");
            label.setTextFill(Color.web("#f0ad4e"));
        } else {
            bar.setStyle("-fx-accent: #5cb85c;");
            label.setText("Strength: Strong");
            label.setTextFill(Color.web("#5cb85c"));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
