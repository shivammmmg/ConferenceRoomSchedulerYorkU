package Scenario1.viewfx;

import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import Scenario1.controller.UserManager;

public class RegisterFX extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("YorkU Conference Scheduler - Register");

        // === Background Gradient ===
        Stop[] stops = new Stop[] {
                new Stop(0, Color.web("#00416A")),
                new Stop(1, Color.web("#E4E5E6"))
        };
        LinearGradient gradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops
        );
        Background gradientBg = new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY));

        // === Main Container ===
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(60, 40, 60, 40));
        root.setBackground(gradientBg);

        // === White Card ===
        VBox card = new VBox(20);
        card.setPadding(new Insets(40, 50, 40, 50));
        card.setAlignment(Pos.CENTER);
        card.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 15, 0.1, 0, 5);"
        );

        // === Title ===
        Label title = new Label("Create New Account");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#222"));

        // === Form Layout ===
        GridPane form = new GridPane();
        form.setVgap(12);
        form.setHgap(12);
        form.setAlignment(Pos.CENTER);

        Label nameLbl = new Label("Full Name:");
        TextField nameTxt = new TextField();
        nameTxt.setPromptText("Full Name");
        nameTxt.setPrefWidth(260);

        Label emailLbl = new Label("Email:");
        TextField emailTxt = new TextField();
        emailTxt.setPromptText("Email (e.g., name@my.yorku.ca)");

        Label passLbl = new Label("Password:");
        PasswordField passTxt = new PasswordField();
        passTxt.setPromptText("Enter your password");

        Label hintLbl = new Label("Password must contain uppercase, lowercase, number, special char, and 8+ chars.");
        hintLbl.setStyle("-fx-font-size: 11px; -fx-text-fill: gray;");

        Label typeLbl = new Label("User Type:");
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Student", "Faculty", "Staff", "Partner");
        typeBox.setPrefWidth(260);

        form.add(nameLbl, 0, 0);
        form.add(nameTxt, 1, 0);
        form.add(emailLbl, 0, 1);
        form.add(emailTxt, 1, 1);
        form.add(passLbl, 0, 2);
        form.add(passTxt, 1, 2);
        form.add(hintLbl, 1, 3);
        form.add(typeLbl, 0, 4);
        form.add(typeBox, 1, 4);

        form.getColumnConstraints().addAll(
                new ColumnConstraints(100),
                new ColumnConstraints(260)
        );

        // === Buttons ===
        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);

        Button registerBtn = new Button("Register");
        Button backBtn = new Button("Back to Login");

        registerBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #007bff, #00c6ff); " +
                        "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; " +
                        "-fx-padding: 8 25 8 25;"
        );

        backBtn.setStyle(
                "-fx-background-color: #f1f1f1; -fx-text-fill: #333; -fx-font-weight: normal; " +
                        "-fx-background-radius: 20; -fx-padding: 8 20 8 20;"
        );

        registerBtn.setOnAction(e -> {
            try {
                boolean success = UserManager.getInstance().register(
                        nameTxt.getText(),
                        emailTxt.getText(),
                        passTxt.getText(),
                        typeBox.getValue()
                );

                if (success) {
                    new Alert(Alert.AlertType.INFORMATION, "Registration successful!").showAndWait();
                    nameTxt.clear();
                    emailTxt.clear();
                    passTxt.clear();
                    typeBox.setValue(null);
                }

            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
        });

        backBtn.setOnAction(e -> {
            try {
                new LoginFX().start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        buttons.getChildren().addAll(registerBtn, backBtn);

        // === Assemble Card ===
        card.getChildren().addAll(title, form, buttons);
        root.getChildren().add(card);

        // === Scene ===
        Scene scene = new Scene(root, 500, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
