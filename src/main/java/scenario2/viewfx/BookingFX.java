package scenario2.viewfx;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Control;
import java.io.InputStream;
import javafx.scene.shape.Rectangle;


import scenario2.controller.BookingManager;
import shared.model.Booking;
import shared.model.Room;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class BookingFX extends Application {

    private BookingManager bookingManager;
    private String currentUserEmail;
    private String currentUserType;

    private VBox mainContent;
    private StackPane overlayPane;
    private VBox paymentModal;
    private VBox confirmationModal;

    // nav buttons so we can toggle active state
    private Button bookRoomBtn;
    private Button myBookingsBtn;
    private Button backToLoginBtn;

    @Override
    public void start(Stage stage) {
        bookingManager = BookingManager.getInstance();
        currentUserEmail = "test@yorku.ca";
        currentUserType = "student";

        // ==========================================================
// =============== 1. LEFT NAVIGATION PANEL =================
// ==========================================================
        Stop[] stops = new Stop[]{
                new Stop(0, Color.web("#AD001D")),
                new Stop(1, Color.web("#7A0019"))
        };
        LinearGradient yorkGradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);

        VBox leftPanel = new VBox();
        leftPanel.setAlignment(Pos.TOP_CENTER);
        leftPanel.setPadding(new Insets(28, 22, 24, 22));
        leftPanel.setSpacing(24);
        leftPanel.setPrefWidth(260);
        leftPanel.setBackground(
                new Background(new BackgroundFill(yorkGradient, CornerRadii.EMPTY, Insets.EMPTY)));

// --------- brand card (logo + app title + subtitle) ---------
        ImageView logoView = new ImageView(new Image("images/yorku_logo.png", true));
        logoView.setFitHeight(44);
        logoView.setPreserveRatio(true);

        Label appTitle = new Label("Room Scheduler");
        appTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        appTitle.setTextFill(Color.WHITE);
        appTitle.setWrapText(true);

        Label appSubtitle = new Label("Conference Rooms");
        appSubtitle.setStyle("-fx-text-fill: rgba(255,255,255,0.75); -fx-font-size: 11;");
        appSubtitle.setWrapText(true);

        VBox titleBox = new VBox(3, appTitle, appSubtitle);
        titleBox.setAlignment(Pos.CENTER_LEFT);
// width cap so labels wrap instead of cutting off with "..."
        titleBox.setMaxWidth(150);

        HBox brandRow = new HBox(10, logoView, titleBox);
        brandRow.setAlignment(Pos.CENTER_LEFT);

        StackPane brandCard = new StackPane(brandRow);
        brandCard.setPadding(new Insets(14, 16, 14, 16));
        brandCard.setMaxWidth(Double.MAX_VALUE);
        brandCard.setBackground(new Background(
                new BackgroundFill(Color.rgb(0, 0, 0, 0.18), new CornerRadii(20), Insets.EMPTY)));
        brandCard.setBorder(new Border(new BorderStroke(
                Color.rgb(255, 255, 255, 0.18),
                BorderStrokeStyle.SOLID,
                new CornerRadii(20),
                new BorderWidths(1)
        )));

// --------- user info: email + small role badge -------------
        Label userEmailLabel = new Label(currentUserEmail);
        userEmailLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.9); -fx-font-size: 11;");

        Label userRoleChip = new Label(
                currentUserType.equalsIgnoreCase("student") ? "Student" : "Staff");
        userRoleChip.setStyle(
                "-fx-background-color: rgba(0,0,0,0.30); " +
                        "-fx-text-fill: #f8fafc; " +
                        "-fx-padding: 3 10; " +
                        "-fx-background-radius: 999; " +
                        "-fx-font-size: 10;");
        userRoleChip.setMouseTransparent(true); // makes it feel like a label, not a button

        Tooltip.install(userRoleChip, new Tooltip("Current role used for pricing and permissions."));

        VBox accountBox = new VBox(2, userEmailLabel, userRoleChip);
        accountBox.setAlignment(Pos.CENTER_LEFT);
        accountBox.setMaxWidth(Double.MAX_VALUE);

        VBox headerBox = new VBox(10, brandCard, accountBox);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setMaxWidth(Double.MAX_VALUE);

// --------- nav buttons inside a soft glassy card ----------
        bookRoomBtn = createNavButton("📅  Book Room", true);
        myBookingsBtn = createNavButton("📋  My Bookings", false);
        backToLoginBtn = createNavButton("🚪  Logout", false);

        VBox navButtons = new VBox(6, bookRoomBtn, myBookingsBtn, backToLoginBtn);
        navButtons.setAlignment(Pos.CENTER_LEFT);
        navButtons.setPadding(new Insets(10, 8, 10, 8));
        navButtons.setMaxWidth(Double.MAX_VALUE);
        navButtons.setBackground(new Background(
                new BackgroundFill(Color.rgb(0, 0, 0, 0.18), new CornerRadii(18), Insets.EMPTY)));

// --------- spacer & tip at bottom -------------------------
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label tipTitle = new Label("Booking tips");
        tipTitle.setStyle("-fx-text-fill: rgba(255,255,255,0.9); -fx-font-size: 11; -fx-font-weight: bold;");

        Label tipLabel = new Label("⏰ Off-peak times (8–10am, 4–8pm)\nare usually easier to book.");
        tipLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.8); -fx-font-size: 11;");
        tipLabel.setWrapText(true);

        VBox tipBox = new VBox(4, tipTitle, tipLabel);
        tipBox.setAlignment(Pos.BOTTOM_LEFT);

        leftPanel.getChildren().addAll(headerBox, navButtons, spacer, tipBox);



        // ==========================================================
        // =============== 2. MAIN CONTENT AREA =====================
        // ==========================================================
        mainContent = new VBox();
        mainContent.setSpacing(20);
        mainContent.setAlignment(Pos.TOP_LEFT);
        mainContent.getStyleClass().add("main-content");

        // ==========================================================
        // =============== 3. OVERLAY SYSTEM ========================
        // ==========================================================
        overlayPane = new StackPane();
        overlayPane.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
        overlayPane.setVisible(false);

        paymentModal = createPaymentModal();
        confirmationModal = createConfirmationModal();
        overlayPane.getChildren().addAll(paymentModal, confirmationModal);

        // ==========================================================
        // =============== 4. ROOT LAYOUT ===========================
        // ==========================================================
        VBox contentWrapper = new VBox(mainContent);
        contentWrapper.setAlignment(Pos.TOP_CENTER);
        contentWrapper.setPadding(new Insets(30));

        ScrollPane contentScroll = new ScrollPane(contentWrapper);
        contentScroll.setFitToWidth(true);
        contentScroll.setFitToHeight(true);
        contentScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        contentScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        contentScroll.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        BorderPane layout = new BorderPane();
        layout.setLeft(leftPanel);
        layout.setCenter(contentScroll);

        StackPane rootContainer = new StackPane(layout, overlayPane);
        rootContainer.getStyleClass().add("app-root");

        // Default view
        showBookingView();
        setActiveNav(bookRoomBtn);

        // ==========================================================
        // =============== 5. NAVIGATION HANDLERS ===================
        // ==========================================================
        bookRoomBtn.setOnAction(e -> {
            setActiveNav(bookRoomBtn);
            showBookingView();
        });

        myBookingsBtn.setOnAction(e -> {
            setActiveNav(myBookingsBtn);
            showMyBookingsView();
        });

        backToLoginBtn.setOnAction(e -> {
            stage.close();
            try {
                new Scenario1.viewfx.LoginFX().start(new Stage());
            } catch (Exception ex) {
                System.out.println("Could not open login screen: " + ex.getMessage());
            }
        });

        Scene scene = new Scene(rootContainer, 1200, 720);

        // Attach CSS (safe even if file not found)
        URL css = getClass().getResource("/styles/booking.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        stage.setTitle("YorkU Conference Room Scheduler");
        stage.setScene(scene);
        stage.show();
    }

    // ==============================================================
    // ========================  NAV HELPERS  ========================
    // ==============================================================
    private ImageView getRoomImageViewForSearch(Room room) {
        String path;

        switch (room.getRoomName()) {
            case "York Room":
                path = "/images/rooms/york-room.png";
                break;
            case "Lassonde Room":
                path = "/images/rooms/lassonde-room.png";
                break;
            case "Bergeron Room":
                path = "/images/rooms/bergeron-room.png";
                break;
            case "Scott Library Room":
                path = "/images/rooms/scott-library-room.png";
                break;
            case "Accolade Room":
                path = "/images/rooms/accolade-room.png";
                break;
            case "Student Center Room":
                path = "/images/rooms/student-center-room.png";
                break;
            default:
                path = "/images/rooms/default-room.png";
                break;
        }

        InputStream is = getClass().getResourceAsStream(path);
        if (is == null) {
            System.out.println("Search card image not found for path: " + path);
            return null;
        }

        Image img = new Image(is);
        ImageView view = new ImageView(img);
        view.setFitWidth(150);
        view.setFitHeight(95);
        view.setPreserveRatio(false);
        view.setSmooth(true);
        view.setCache(true);

        Rectangle clip = new Rectangle(150, 95);
        clip.setArcWidth(16);
        clip.setArcHeight(16);
        view.setClip(clip);

        return view;
    }

    private ImageView getRoomImageView(Room room) {
        String path;

        // map room names -> resource paths
        switch (room.getRoomName()) {
            case "York Room":
                path = "/images/york-room.png";
                break;
            case "Lassonde Room":
                path = "/images/lassonde-room.png";
                break;
            case "Bergeron Room":
                path = "/images/bergeron-room.png";
                break;
            case "Scott Library Room":
                path = "/images/scott-library-room.png";
                break;
            case "Accolade Room":
                path = "/images/accolade-room.png";
                break;
            case "Student Center Room":
                path = "/images/student-center-room.png";
                break;
            default:
                path = "/images/default-room.png";
                break;
        }

        InputStream is = getClass().getResourceAsStream(path);
        if (is == null) {
            System.out.println("Room image not found for path: " + path);
            return null;
        }

        Image img = new Image(is);
        ImageView view = new ImageView(img);
        view.setFitWidth(220);
        view.setFitHeight(120);
        view.setPreserveRatio(false);
        view.setSmooth(true);
        view.setCache(true);

        // rounded corners on the image itself
        Rectangle clip = new Rectangle(220, 120);
        clip.setArcWidth(22);
        clip.setArcHeight(22);
        view.setClip(clip);

        return view;
    }

    private Label createSmallTag(String text) {
        Label tag = new Label(text);
        tag.setStyle(
                "-fx-background-color: #f3f4f6;" +
                        "-fx-text-fill: #4b5563;" +
                        "-fx-font-size: 10;" +
                        "-fx-padding: 3 8;" +
                        "-fx-background-radius: 999;"
        );
        return tag;
    }

    private void applyModernFieldStyle(Control control) {
        String base =
                "-fx-background-color: #f3f4f6;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: transparent;" +
                        "-fx-padding: 6 10;";
        String focused =
                "-fx-background-color: #ffffff;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: #AD001D;" +
                        "-fx-border-width: 1.3;" +
                        "-fx-padding: 6 10;";

        control.setStyle(base);
        control.focusedProperty().addListener((obs, oldVal, nowFocused) -> {
            control.setStyle(nowFocused ? focused : base);
        });
    }
    // helper style strings for nav buttons
    private static final String NAV_BASE_STYLE =
            "-fx-background-radius: 999;" +
                    "-fx-padding: 10 16;" +
                    "-fx-font-size: 13;" +
                    "-fx-font-weight: 600;" +
                    "-fx-text-fill: #f8fafc;" +
                    "-fx-background-insets: 0;" +
                    "-fx-border-color: transparent;";

    private static final String NAV_BG_INACTIVE =
            "-fx-background-color: transparent;";
    private static final String NAV_BG_ACTIVE =
            "-fx-background-color: rgba(248,249,250,0.28);";
    private static final String NAV_BG_HOVER =
            "-fx-background-color: rgba(248,249,250,0.16);";

    private Button createNavButton(String text, boolean isActive) {
        Button button = new Button(text);
        button.setPrefWidth(Double.MAX_VALUE);
        button.setMinHeight(40);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        button.setBackground(Background.EMPTY);
        button.setBorder(Border.EMPTY);
        button.setFocusTraversable(false);
        button.setStyle(NAV_BASE_STYLE + (isActive ? NAV_BG_ACTIVE : NAV_BG_INACTIVE));

        // store active state in properties
        button.getProperties().put("active", isActive);

        button.setOnMouseEntered(e ->
                applyNavStyle(button,
                        (boolean) button.getProperties().getOrDefault("active", false),
                        true));

        button.setOnMouseExited(e ->
                applyNavStyle(button,
                        (boolean) button.getProperties().getOrDefault("active", false),
                        false));

        return button;
    }


    private void applyNavStyle(Button button, boolean active, boolean hover) {
        String bg;
        if (active) {
            bg = NAV_BG_ACTIVE;
        } else if (hover) {
            bg = NAV_BG_HOVER;
        } else {
            bg = NAV_BG_INACTIVE;
        }
        button.setStyle(NAV_BASE_STYLE + bg);
    }

    private void setActiveNav(Button activeButton) {
        for (Button b : new Button[]{bookRoomBtn, myBookingsBtn, backToLoginBtn}) {
            if (b == null) continue;
            boolean active = (b == activeButton);
            b.getProperties().put("active", active);
            applyNavStyle(b, active, false);
        }
    }


    // ==============================================================
    // ======================  MODAL OVERLAYS  ======================
    // ==============================================================

    private VBox createPaymentModal() {
        VBox modal = new VBox(18);
        modal.setPadding(new Insets(24));
        modal.setAlignment(Pos.TOP_LEFT);
        modal.setMaxWidth(480);

        // card look
        modal.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-effect: dropshadow(gaussian, rgba(15,23,42,0.18), 26, 0, 0, 8);"
        );

        // --- title + subtitle ---
        Label title = new Label("Complete Payment");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#AD001D"));

        Label subtitle = new Label("Review your booking details and confirm payment.");
        subtitle.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 11;");

        // --- summary box ---
        VBox summaryBox = new VBox(6);
        summaryBox.setPadding(new Insets(10, 12, 10, 12));
        summaryBox.setStyle(
                "-fx-background-color: #f9fafb;" +
                        "-fx-background-radius: 12;"
        );

        Label summaryTitle = new Label("Booking Summary");
        summaryTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 12; -fx-text-fill: #111827;");

        Label roomLabel = new Label();
        roomLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #4b5563;");

        Label timeLabel = new Label();
        timeLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #4b5563;");

        Label amountLabel = new Label();
        amountLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: #AD001D;");

        summaryBox.getChildren().addAll(summaryTitle, roomLabel, timeLabel, amountLabel);

        // --- separator ---
        Separator sep = new Separator();
        sep.setOpacity(0.25);

        // --- payment form ---
        VBox paymentForm = new VBox(10);

        Label paymentTitle = new Label("Payment Details");
        paymentTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 12; -fx-text-fill: #111827;");

        Label cardLabel = new Label("Card Number");
        cardLabel.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 11;");

        TextField cardField = new TextField();
        cardField.setPromptText("1234 5678 9012 3456");
        applyModernFieldStyle(cardField);

        HBox expiryCvvRow = new HBox(10);

        VBox expiryBox = new VBox(4);
        Label expiryLabel = new Label("Expiry (MM/YY)");
        expiryLabel.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 11;");
        TextField expiryField = new TextField();
        expiryField.setPromptText("MM/YY");
        applyModernFieldStyle(expiryField);
        expiryBox.getChildren().addAll(expiryLabel, expiryField);

        VBox cvvBox = new VBox(4);
        Label cvvLabel = new Label("CVV");
        cvvLabel.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 11;");
        TextField cvvField = new TextField();
        cvvField.setPromptText("123");
        applyModernFieldStyle(cvvField);
        cvvBox.getChildren().addAll(cvvLabel, cvvField);

        expiryCvvRow.getChildren().addAll(expiryBox, cvvBox);

        VBox nameBox = new VBox(4);
        Label nameLabel = new Label("Name on Card");
        nameLabel.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 11;");
        TextField nameField = new TextField();
        nameField.setPromptText("Name as it appears on card");
        applyModernFieldStyle(nameField);
        nameBox.getChildren().addAll(nameLabel, nameField);

        Label disclaimer = new Label(
                "We do not store card details. This is a simulated payment for demo purposes.");
        disclaimer.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 10;");

        paymentForm.getChildren().addAll(
                paymentTitle,
                cardLabel,
                cardField,
                expiryCvvRow,
                nameBox,
                disclaimer
        );

        // --- buttons row ---
        HBox buttonRow = new HBox(10);
        buttonRow.setAlignment(Pos.CENTER_RIGHT);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle(
                "-fx-background-color: #e5e7eb;" +
                        "-fx-text-fill: #374151;" +
                        "-fx-background-radius: 999;" +
                        "-fx-padding: 6 16;"
        );
        cancelBtn.setPrefWidth(90);

        Button payBtn = new Button("Pay Now");
        payBtn.setStyle(
                "-fx-background-color: #AD001D;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 999;" +
                        "-fx-padding: 6 22;"
        );
        payBtn.setPrefWidth(100);

        buttonRow.getChildren().addAll(cancelBtn, payBtn);

        modal.getChildren().addAll(
                title,
                subtitle,
                summaryBox,
                sep,
                paymentForm,
                buttonRow
        );

        // store references for showPaymentModal()
        modal.setUserData(new Object[]{
                roomLabel, timeLabel, amountLabel,
                cardField, expiryField, cvvField, nameField,
                payBtn, cancelBtn
        });

        modal.setVisible(false);
        return modal;
    }


    private VBox createConfirmationModal() {
        VBox modal = new VBox(20);
        modal.setPadding(new Insets(25));
        modal.setMaxWidth(400);
        modal.setVisible(false);
        modal.setAlignment(Pos.CENTER);
        modal.getStyleClass().add("card-elevated");

        Label successIcon = new Label("✓");
        successIcon.setStyle("-fx-text-fill: #5cb85c; -fx-font-size: 40; -fx-font-weight: bold;");

        Label title = new Label("Booking Confirmed!");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));

        VBox detailsBox = new VBox(8);
        detailsBox.setAlignment(Pos.CENTER_LEFT);
        detailsBox.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 15; -fx-background-radius: 8;");

        Label bookingIdLabel = new Label();
        bookingIdLabel.setStyle("-fx-font-size: 12;");
        Label nameLabel = new Label();
        nameLabel.setStyle("-fx-font-size: 12;");
        Label timeLabel = new Label();
        timeLabel.setStyle("-fx-font-size: 12;");
        Label amountLabel = new Label();
        amountLabel.setStyle("-fx-font-size: 12;");
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");

        detailsBox.getChildren().addAll(
                bookingIdLabel, nameLabel, timeLabel, amountLabel, statusLabel);

        Label followUpHint = new Label(
                "You can view or cancel this booking anytime from the \"My Bookings\" tab.");
        followUpHint.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 11;");

        Button doneBtn = new Button("Done");
        doneBtn.getStyleClass().add("primary-button");
        doneBtn.setPrefWidth(100);
        Tooltip.install(doneBtn, new Tooltip("Close and go back to your bookings"));

        modal.getChildren().addAll(successIcon, title, detailsBox, followUpHint, doneBtn);

        modal.setUserData(new Object[]{
                bookingIdLabel, nameLabel, timeLabel, amountLabel, statusLabel, doneBtn
        });

        return modal;
    }

    private void showOverlay() {
        overlayPane.setVisible(true);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(250), overlayPane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    private void hideOverlay() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), overlayPane);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> overlayPane.setVisible(false));
        fadeOut.play();
    }

    private void showPaymentModal(Room room,
                                  LocalDateTime startTime,
                                  LocalDateTime endTime,
                                  String purpose,
                                  long durationHours,
                                  double depositAmount) {

        Object[] refs = (Object[]) paymentModal.getUserData();
        Label roomLabel = (Label) refs[0];
        Label timeLabel = (Label) refs[1];
        Label amountLabel = (Label) refs[2];
        TextField cardField = (TextField) refs[3];
        TextField expiryField = (TextField) refs[4];
        TextField cvvField = (TextField) refs[5];
        TextField nameField = (TextField) refs[6];
        Button payBtn = (Button) refs[7];
        Button cancelBtn = (Button) refs[8];

        roomLabel.setText("Room: " + room.getRoomName());
        timeLabel.setText("Time: " + startTime.toLocalDate() + " "
                + startTime.toLocalTime() + " - " + endTime.toLocalTime());
        amountLabel.setText("Amount: $" + depositAmount);

        cardField.clear();
        expiryField.clear();
        cvvField.clear();
        nameField.clear();

        payBtn.setOnAction(null);
        cancelBtn.setOnAction(null);

        payBtn.setOnAction(event -> {
            if (cardField.getText().trim().isEmpty()
                    || expiryField.getText().trim().isEmpty()
                    || cvvField.getText().trim().isEmpty()
                    || nameField.getText().trim().isEmpty()) {
                showAlert("Payment Error", "Please fill in all payment details.");
                return;
            }

            processPaymentAndConfirm(
                    room, startTime, endTime, purpose, currentUserType, depositAmount);
        });

        cancelBtn.setOnAction(event -> hideOverlay());

        paymentModal.setVisible(true);
        confirmationModal.setVisible(false);
        showOverlay();
    }

    private void processPaymentAndConfirm(Room room,
                                          LocalDateTime startTime,
                                          LocalDateTime endTime,
                                          String purpose,
                                          String userType,
                                          double depositAmount) {
        try {
            Booking booking = bookingManager.bookRoom(
                    room.getRoomId(),
                    currentUserEmail,
                    startTime,
                    endTime,
                    purpose,
                    userType
            );

            showConfirmationModal(booking, room, startTime, endTime);

        } catch (Exception ex) {
            showAlert("Booking Failed", ex.getMessage());
            hideOverlay();
        }
    }

    private void showConfirmationModal(Booking booking,
                                       Room room,
                                       LocalDateTime startTime,
                                       LocalDateTime endTime) {

        Object[] refs = (Object[]) confirmationModal.getUserData();
        Label bookingIdLabel = (Label) refs[0];
        Label nameLabel = (Label) refs[1];
        Label timeLabel = (Label) refs[2];
        Label amountLabel = (Label) refs[3];
        Label statusLabel = (Label) refs[4];
        Button doneBtn = (Button) refs[5];

        bookingIdLabel.setText("Booking ID: " + booking.getBookingId());
        nameLabel.setText("Room: " + room.getRoomName());
        timeLabel.setText("Time: " + startTime.toLocalDate() + " "
                + startTime.toLocalTime() + " - " + endTime.toLocalTime());
        amountLabel.setText("Amount Paid: $" + booking.getDepositAmount());

        String statusText = "Status: " + booking.getPaymentStatus();
        statusLabel.setText(statusText);
        statusLabel.setStyle(
                "-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: "
                        + ("APPROVED".equals(booking.getPaymentStatus()) ? "#5cb85c" : "#f0ad4e")
                        + ";");

        doneBtn.setOnAction(event -> {
            hideOverlay();
            if ("APPROVED".equals(booking.getPaymentStatus())) {
                showMyBookingsView();
            }
        });

        paymentModal.setVisible(false);
        confirmationModal.setVisible(true);
    }

    // ==============================================================
    // =======================  PAGE HEADER  ========================
    // ==============================================================

    private HBox createPageHeader(String title, String subtitle) {
        Label titleLbl = new Label(title);
        titleLbl.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        titleLbl.setTextFill(Color.web("#2C3E50"));

        Label subtitleLbl = new Label(subtitle);
        subtitleLbl.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 12;");

        VBox left = new VBox(4, titleLbl, subtitleLbl);

        Label todayChip = new Label("Today: " + LocalDate.now());
        todayChip.getStyleClass().add("chip");

        Label userHint = new Label(
                "💡 Pro tip: Reserve popular rooms a few days in advance.");
        userHint.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 11;");

        VBox right = new VBox(4, todayChip, userHint);
        right.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox header = new HBox(20, left, spacer, right);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        return header;
    }

    private void playMainContentFadeIn() {
        mainContent.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.millis(220), mainContent);
        fade.setToValue(1);
        fade.play();
    }

    // ==============================================================
    // ======================  BOOKING VIEW  ========================
    // ==============================================================

    private void showBookingView() {
        mainContent.getChildren().clear();

        HBox header = createPageHeader(
                "Book a Conference Room",
                "Search, filter, and instantly secure a space that fits your meeting."
        );

        VBox formCard = createBookingForm();

        Label availableTitle = new Label("Quick glance at all rooms");
        availableTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        availableTitle.setTextFill(Color.web("#2C3E50"));

        FlowPane roomsPreview = createRoomsPreview();

        mainContent.getChildren().addAll(header, formCard, availableTitle, roomsPreview);
        playMainContentFadeIn();
    }

    private VBox createBookingForm() {
        VBox formCard = new VBox(18);
        formCard.setPadding(new Insets(26));
        formCard.setSpacing(18);
        formCard.setMaxWidth(950);
        // big modern card
        formCard.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 24;" +
                        "-fx-effect: dropshadow(gaussian, rgba(15,23,42,0.12), 22, 0, 0, 6);"
        );

        Label formTitle = new Label("New Booking Request");
        formTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        formTitle.setTextFill(Color.web("#AD001D"));

        Label formSubtitle = new Label(
                "Tell us when, where, and how many people – we’ll find the best room.");
        formSubtitle.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 12;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(16);
        formGrid.setVgap(12);
        formGrid.setPadding(new Insets(10, 0, 0, 0));

        String labelStyle =
                "-fx-text-fill: #6b7280;" +
                        "-fx-font-size: 11;" +
                        "-fx-font-weight: bold;";


        // --- Date ---
        Label dateLbl = new Label("Booking Date *");
        dateLbl.setStyle(labelStyle);
        DatePicker datePicker = new DatePicker(LocalDate.now().plusDays(1));
        datePicker.setEditable(false);
        applyModernFieldStyle(datePicker);
        Tooltip.install(datePicker, new Tooltip("Rooms can be booked from the next day onward."));

        // --- Time ---
        Label startTimeLbl = new Label("Start Time *");
        startTimeLbl.setStyle(labelStyle);
        ComboBox<String> startTimeBox = new ComboBox<>();
        for (int hour = 8; hour <= 19; hour++) {
            startTimeBox.getItems().add(String.format("%02d:00", hour));
            startTimeBox.getItems().add(String.format("%02d:30", hour));
        }
        startTimeBox.setValue("09:00");
        applyModernFieldStyle(startTimeBox);

        Label endTimeLbl = new Label("End Time *");
        endTimeLbl.setStyle(labelStyle);
        ComboBox<String> endTimeBox = new ComboBox<>();
        for (int hour = 9; hour <= 20; hour++) {
            endTimeBox.getItems().add(String.format("%02d:00", hour));
            endTimeBox.getItems().add(String.format("%02d:30", hour));
        }
        endTimeBox.setValue("10:00");
        applyModernFieldStyle(endTimeBox);

        Tooltip.install(startTimeBox, new Tooltip("When your meeting starts."));
        Tooltip.install(endTimeBox, new Tooltip("When your meeting ends."));

        // --- Capacity ---
        Label capacityLbl = new Label("Number of People *");
        capacityLbl.setStyle(labelStyle);
        Spinner<Integer> capacitySpinner = new Spinner<>(1, 30, 5);
        capacitySpinner.setEditable(false);
        capacitySpinner.setPrefWidth(120);
        applyModernFieldStyle(capacitySpinner);
        Tooltip.install(capacitySpinner, new Tooltip("Approximate number of attendees (max 30)."));

        // --- Building ---
        Label buildingLbl = new Label("Building");
        buildingLbl.setStyle(labelStyle);
        ComboBox<String> buildingBox = new ComboBox<>();
        buildingBox.getItems().add("All Buildings");
        buildingBox.getItems().addAll(bookingManager.getAvailableBuildings());
        buildingBox.setValue("All Buildings");
        applyModernFieldStyle(buildingBox);
        Tooltip.install(buildingBox, new Tooltip("Optional: limit search to a specific building."));

        // --- Equipment ---
        Label equipmentLbl = new Label("Equipment");
        equipmentLbl.setStyle(labelStyle);
        ComboBox<String> equipmentBox = new ComboBox<>();
        equipmentBox.getItems().add("All Equipment");
        equipmentBox.getItems().addAll(bookingManager.getAvailableEquipment());
        equipmentBox.setValue("All Equipment");
        applyModernFieldStyle(equipmentBox);
        Tooltip.install(equipmentBox, new Tooltip("Optional: filter by projector, whiteboard, etc."));

        // --- Purpose ---
        Label purposeLbl = new Label("Meeting Purpose *");
        purposeLbl.setStyle(labelStyle);
        TextArea purposeArea = new TextArea();
        purposeArea.setPromptText("e.g., Study group, client meeting, project presentation, interview...");
        purposeArea.setPrefRowCount(3);
        applyModernFieldStyle(purposeArea);
        Label purposeErrorLabel = new Label("Please enter a meeting purpose.");
        purposeErrorLabel.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11;");
        purposeErrorLabel.setVisible(false);
        purposeErrorLabel.setManaged(false); // so it doesn't take space when hidden

// clear error as soon as user starts typing
        purposeArea.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.trim().isEmpty()) {
                purposeErrorLabel.setVisible(false);
                purposeErrorLabel.setManaged(false);
                // restore normal style
                applyModernFieldStyle(purposeArea);
            }
        });



        // Layout in grid
        formGrid.add(dateLbl, 0, 0);
        formGrid.add(datePicker, 0, 1);

        formGrid.add(startTimeLbl, 1, 0);
        formGrid.add(startTimeBox, 1, 1);

        formGrid.add(endTimeLbl, 2, 0);
        formGrid.add(endTimeBox, 2, 1);

        formGrid.add(capacityLbl, 0, 2);
        formGrid.add(capacitySpinner, 0, 3);

        formGrid.add(buildingLbl, 1, 2);
        formGrid.add(buildingBox, 1, 3);

        formGrid.add(equipmentLbl, 2, 2);
        formGrid.add(equipmentBox, 2, 3);

        formGrid.add(purposeLbl, 0, 4, 3, 1);
        formGrid.add(purposeArea, 0, 5, 3, 1);

        // --- Quick presets row -----------------------------------
        Label quickLbl = new Label("Quick presets");
        quickLbl.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 11;");

        HBox quickBox = new HBox(8);
        quickBox.setAlignment(Pos.CENTER_LEFT);

        Button oneHourBtn = createPresetChip("1-hour slot",
                () -> applyQuickDuration(startTimeBox, endTimeBox, 1));
        Button morningBtn = createPresetChip("Morning (9–11)", () -> {
            startTimeBox.setValue("09:00");
            endTimeBox.setValue("11:00");
        });
        Button smallGroupBtn = createPresetChip("Study group", () -> {
            capacitySpinner.getValueFactory().setValue(6);
            if (purposeArea.getText() == null || purposeArea.getText().isBlank()) {
                purposeArea.setText("Study group / project work session");
            }
        });
        Button presentationBtn = createPresetChip("Presentation", () -> {
            capacitySpinner.getValueFactory().setValue(
                    Math.max(capacitySpinner.getValue(), 10));
            if (purposeArea.getText() == null || purposeArea.getText().isBlank()) {
                purposeArea.setText("Presentation / talk with slides");
            }
        });

        quickBox.getChildren().addAll(oneHourBtn, morningBtn, smallGroupBtn, presentationBtn);
        VBox quickPresetsRow = new VBox(4, quickLbl, quickBox);

        Label requiredNote = new Label("* Required fields");
        requiredNote.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 11;");

        Button checkAvailabilityBtn = new Button("🔍 Search Available Rooms");
        checkAvailabilityBtn.setPrefWidth(230);
        checkAvailabilityBtn.setStyle(
                "-fx-background-color: #AD001D;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 999;" +
                        "-fx-padding: 10 22;" +
                        "-fx-cursor: hand;");
        Tooltip.install(checkAvailabilityBtn, new Tooltip("Find rooms that match your filters."));

        VBox resultArea = new VBox();
        resultArea.setPadding(new Insets(10, 0, 0, 0));

        checkAvailabilityBtn.setOnAction(e -> {
            try {
                LocalDate date = datePicker.getValue();
                if (date == null) {
                    showAlert("Error", "Please select a date.");
                    return;
                }

                LocalTime start = LocalTime.parse(startTimeBox.getValue());
                LocalTime end = LocalTime.parse(endTimeBox.getValue());

                if (!end.isAfter(start)) {
                    showAlert("Error", "End time must be after start time.");
                    return;
                }

                int capacity = capacitySpinner.getValue();
                String building = buildingBox.getValue().equals("All Buildings")
                        ? null : buildingBox.getValue();
                String equipment = equipmentBox.getValue().equals("All Equipment")
                        ? null : equipmentBox.getValue();
                String purpose = purposeArea.getText();

                if (purpose == null || purpose.trim().isEmpty()) {
                    showAlert("Error", "Please enter a meeting purpose.");
                    return;
                }

                LocalDateTime startDateTime = LocalDateTime.of(date, start);
                LocalDateTime endDateTime = LocalDateTime.of(date, end);

                List<Room> availableRooms = bookingManager.searchAvailableRooms(
                        startDateTime, endDateTime, capacity, building, equipment);

                displayAvailableRooms(availableRooms, startDateTime, endDateTime, purpose, resultArea);

            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        formCard.getChildren().addAll(
                formTitle,
                formSubtitle,
                formGrid,
                quickPresetsRow,
                requiredNote,
                checkAvailabilityBtn,
                resultArea
        );

        return formCard;
    }


    private Button createPresetChip(String text, Runnable action) {
        Button chip = new Button(text);
        chip.setFocusTraversable(false);

        String base =
                "-fx-background-color: #f3f4f6;" +
                        "-fx-text-fill: #374151;" +
                        "-fx-background-radius: 999;" +
                        "-fx-padding: 4 12;" +
                        "-fx-font-size: 11;" +
                        "-fx-cursor: hand;";
        String hover =
                "-fx-background-color: #e5e7eb;" +
                        "-fx-text-fill: #111827;" +
                        "-fx-background-radius: 999;" +
                        "-fx-padding: 4 12;" +
                        "-fx-font-size: 11;";

        chip.setStyle(base);
        chip.setOnMouseEntered(e -> chip.setStyle(hover));
        chip.setOnMouseExited(e -> chip.setStyle(base));
        chip.setOnAction(e -> action.run());

        return chip;
    }


    private void applyQuickDuration(ComboBox<String> startTimeBox,
                                    ComboBox<String> endTimeBox,
                                    int hours) {
        try {
            String startVal = startTimeBox.getValue();
            if (startVal == null) return;

            LocalTime start = LocalTime.parse(startVal);
            LocalTime end = start.plusHours(hours);
            String endStr = String.format("%02d:%02d", end.getHour(), end.getMinute());

            if (endTimeBox.getItems().contains(endStr)) {
                endTimeBox.setValue(endStr);
            }
        } catch (Exception ignored) {
        }
    }

    private void displayAvailableRooms(List<Room> rooms,
                                       LocalDateTime startTime,
                                       LocalDateTime endTime,
                                       String purpose,
                                       VBox resultArea) {
        resultArea.getChildren().clear();

        if (rooms.isEmpty()) {
            Label noRoomsLbl = new Label(
                    "❌ No rooms available for the selected criteria.");
            noRoomsLbl.setStyle("-fx-text-fill: #d9534f; -fx-font-size: 14;");

            Label suggestionLbl = new Label(
                    "Try adjusting the time window, lowering capacity slightly, or removing some filters.");
            suggestionLbl.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 11;");
            suggestionLbl.setWrapText(true);

            resultArea.getChildren().addAll(noRoomsLbl, suggestionLbl);
            return;
        }

        Label availableLbl = new Label("✅ " + rooms.size() + " rooms available:");
        availableLbl.setStyle("-fx-text-fill: #5cb85c; -fx-font-size: 14; -fx-font-weight: bold;");

        Label tipLbl = new Label(
                "Tip: Choose a room that slightly exceeds your capacity so people aren’t cramped.");
        tipLbl.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 11;");

        resultArea.getChildren().addAll(availableLbl, tipLbl);

        for (Room room : rooms) {
            HBox roomCard = createRoomCard(room, startTime, endTime, purpose);
            resultArea.getChildren().add(roomCard);
            animateCardIn(roomCard);
        }
    }

    private void animateCardIn(Node card) {
        card.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.millis(180), card);
        fade.setToValue(1);
        fade.play();
    }

    private HBox createRoomCard(Room room,
                                LocalDateTime startTime,
                                LocalDateTime endTime,
                                String purpose) {

        HBox card = new HBox(16);
        card.setPadding(new Insets(12));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPrefWidth(780);

        String baseStyle =
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(15,23,42,0.06), 14, 0, 0, 3);";
        String hoverStyle =
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(15,23,42,0.16), 20, 0, 0, 8);";

        card.setStyle(baseStyle);
        card.setOnMouseEntered(e -> {
            card.setStyle(hoverStyle);
            card.setScaleX(1.01);
            card.setScaleY(1.01);
        });
        card.setOnMouseExited(e -> {
            card.setStyle(baseStyle);
            card.setScaleX(1.0);
            card.setScaleY(1.0);
        });

        // ---------- thumbnail on the left (reuse same images as quick glance) ----------
        ImageView preview = getRoomImageView(room);   // <- SAME helper you use for quick glance
        if (preview != null) {
            // shrink it for list view
            preview.setFitWidth(150);
            preview.setFitHeight(95);

            Rectangle clip = new Rectangle(150, 95);
            clip.setArcWidth(16);
            clip.setArcHeight(16);
            preview.setClip(clip);

            card.getChildren().add(preview);
        }

        // ---------- center info ----------
        VBox infoBox = new VBox(4);
        infoBox.setPrefWidth(520);

        Label nameLabel = new Label(room.getRoomName() + " (" + room.getRoomId() + ")");
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13; -fx-text-fill: #111827;");

        Label locationLabel = new Label(
                "📍 " + room.getBuilding() +
                        (room.getLocation() != null && !room.getLocation().isBlank()
                                ? " | " + room.getLocation() : "") +
                        " | 👥 Capacity: " + room.getCapacity());
        locationLabel.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 11;");

        Label amenitiesLabel = new Label("🛠 " + room.getAmenities());
        amenitiesLabel.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 11;");

        long durationHours = java.time.Duration.between(startTime, endTime).toHours();
        double depositRate = currentUserType.equalsIgnoreCase("student") ? 5.0 : 10.0;
        double depositAmount = depositRate * durationHours;

        Label depositLabel = new Label(
                "💰 Deposit: $" + depositAmount +
                        "  (" + durationHours + " hours × $" + depositRate + "/hour)");
        depositLabel.setStyle("-fx-text-fill: #AD001D; -fx-font-size: 11; -fx-font-weight: bold;");

        Label tipLabel = new Label("Ideal for " + getRoomVibeText(room));
        tipLabel.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 11;");

        infoBox.getChildren().addAll(nameLabel, locationLabel, amenitiesLabel, depositLabel, tipLabel);

        // ---------- right side: Book button ----------
        VBox actionBox = new VBox();
        actionBox.setAlignment(Pos.CENTER_RIGHT);
        actionBox.setPrefWidth(120);

        Button bookBtn = new Button("Book Now");
        bookBtn.setPrefWidth(100);
        bookBtn.setStyle(
                "-fx-background-color: #AD001D;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 999;" +
                        "-fx-padding: 6 18;"
        );

        bookBtn.setOnAction(e -> {
            try {
                showPaymentModal(room, startTime, endTime, purpose, durationHours, depositAmount);
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        actionBox.getChildren().add(bookBtn);

        card.getChildren().addAll(infoBox, actionBox);
        return card;
    }


    private String getRoomVibeText(Room room) {
        int cap = room.getCapacity();
        if (cap <= 4) {
            return "Perfect for 1:1s, interviews, or quiet focus sessions.";
        } else if (cap <= 8) {
            return "Great for project groups and team huddles.";
        } else if (cap <= 15) {
            return "Ideal for seminars, workshops, or larger study groups.";
        } else {
            return "Best suited for events, presentations, or club meetings.";
        }
    }

    private FlowPane createRoomsPreview() {
        FlowPane preview = new FlowPane();
        preview.setHgap(24);
        preview.setVgap(24);
        preview.setAlignment(Pos.TOP_LEFT);

        // let it wrap nicely as the center area resizes
        preview.prefWrapLengthProperty().bind(
                mainContent.widthProperty().subtract(80)
        );

        List<Room> allRooms = bookingManager.getAllRooms();
        for (Room room : allRooms) {
            VBox roomCard = createRoomPreviewCard(room);
            preview.getChildren().add(roomCard);
        }

        return preview;
    }


    private VBox createRoomPreviewCard(Room room) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(14));
        card.setAlignment(Pos.TOP_LEFT);
        card.setPrefWidth(220);

        String baseStyle =
                "-fx-background-color: white;" +
                        "-fx-background-radius: 18;" +
                        "-fx-effect: dropshadow(gaussian, rgba(15,23,42,0.10), 18, 0, 0, 4);";
        String hoverStyle =
                "-fx-background-color: white;" +
                        "-fx-background-radius: 18;" +
                        "-fx-effect: dropshadow(gaussian, rgba(15,23,42,0.18), 24, 0, 0, 10);";

        card.setStyle(baseStyle);
        card.setOnMouseEntered(e -> {
            card.setStyle(hoverStyle);
            card.setScaleX(1.03);
            card.setScaleY(1.03);
        });
        card.setOnMouseExited(e -> {
            card.setStyle(baseStyle);
            card.setScaleX(1.0);
            card.setScaleY(1.0);
        });

        // ---------- image preview ----------
        ImageView preview = getRoomImageView(room);
        if (preview != null) {
            StackPane imageWrapper = new StackPane(preview);
            imageWrapper.setPadding(new Insets(0, 0, 6, 0));
            card.getChildren().add(imageWrapper);
        }

        // ---------- header row: icon chip + name ----------
        StackPane iconCircle = new StackPane();
        iconCircle.setPadding(new Insets(4));
        iconCircle.setStyle(
                "-fx-background-color: #f3f4f6;" +
                        "-fx-background-radius: 999;"
        );

        Label icon = new Label(room.getAmenitiesIcon());
        icon.setStyle("-fx-font-size: 13;");
        iconCircle.getChildren().add(icon);

        Label name = new Label(room.getRoomName());
        name.setStyle("-fx-font-weight: bold; -fx-font-size: 13; -fx-text-fill: #111827;");
        name.setWrapText(true);

        HBox headerRow = new HBox(8, iconCircle, name);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        // ---------- details ----------
        Label building = new Label("🏢 " + room.getBuilding());
        building.setStyle("-fx-font-size: 11; -fx-text-fill: #6b7280;");

        Label capacity = new Label("👥 Up to " + room.getCapacity() + " people");
        capacity.setStyle("-fx-font-size: 11; -fx-text-fill: #6b7280;");

        Label vibe = new Label(getRoomVibeText(room));
        vibe.setStyle("-fx-font-size: 11; -fx-text-fill: #9ca3af;");
        vibe.setWrapText(true);

        // ---------- tags ----------
        HBox tagRow = new HBox(6);
        tagRow.setAlignment(Pos.CENTER_LEFT);

        Label buildingTag = createSmallTag(room.getBuilding());

        String amenitySummary = room.getAmenities();
        if (amenitySummary == null || amenitySummary.isBlank()) {
            amenitySummary = "General use";
        } else if (amenitySummary.length() > 18) {
            amenitySummary = amenitySummary.substring(0, 18) + "…";
        }
        Label amenityTag = createSmallTag(amenitySummary);

        tagRow.getChildren().addAll(buildingTag, amenityTag);

        card.getChildren().addAll(headerRow, building, capacity, vibe, tagRow);
        return card;
    }




    // ==============================================================
    // ======================  MY BOOKINGS VIEW  ====================
    // ==============================================================

    private void showMyBookingsView() {
        mainContent.getChildren().clear();

        HBox header = createPageHeader(
                "My Bookings",
                "Review your upcoming reservations, check payment status, or cancel if plans change."
        );

        List<Booking> userBookings = bookingManager.getUserBookings(currentUserEmail);

        if (userBookings.isEmpty()) {
            Label noBookings = new Label("You don't have any upcoming bookings yet.");
            noBookings.setStyle("-fx-text-fill: #666; -fx-font-size: 16;");

            Label hint = new Label("Head over to \"Book Room\" to make your first reservation.");
            hint.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 11;");

            VBox emptyState = new VBox(6, noBookings, hint);
            emptyState.setAlignment(Pos.CENTER_LEFT);

            mainContent.getChildren().addAll(header, emptyState);
            playMainContentFadeIn();
            return;
        }

        VBox bookingsList = new VBox(10);
        for (Booking booking : userBookings) {
            HBox bookingCard = createBookingCard(booking);
            bookingsList.getChildren().add(bookingCard);
            animateCardIn(bookingCard);
        }

        ScrollPane scrollPane = new ScrollPane(bookingsList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        mainContent.getChildren().addAll(header, scrollPane);
        playMainContentFadeIn();
    }

    private HBox createBookingCard(Booking booking) {
        HBox card = new HBox(15);
        card.setPadding(new Insets(15));
        card.setPrefWidth(750);
        card.getStyleClass().add("card-small");

        Room room = bookingManager.getRoomById(booking.getRoomId());

        VBox bookingInfo = new VBox(5);
        bookingInfo.setPrefWidth(550);

        Label roomName = new Label(room != null ? room.getRoomName() : booking.getRoomId());
        roomName.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        Label time = new Label("🕒 " + booking.getFormattedDate()
                + " | " + booking.getFormattedTime());
        time.setStyle("-fx-text-fill: #666; -fx-font-size: 12;");

        Label purpose = new Label("📝 " + booking.getPurpose());
        purpose.setStyle("-fx-text-fill: #666; -fx-font-size: 12;");

        String paymentStatus = booking.getPaymentStatus();
        String paymentColor = "APPROVED".equals(paymentStatus) ? "#5cb85c"
                : "PENDING".equals(paymentStatus) ? "#f0ad4e" : "#d9534f";

        Label payment = new Label(
                "💰 Payment: " + paymentStatus
                        + " | Deposit: $" + booking.getDepositAmount());
        payment.setStyle("-fx-text-fill: " + paymentColor
                + "; -fx-font-size: 11; -fx-font-weight: bold;");

        Label bookingId = new Label(
                "ID: " + booking.getBookingId()
                        + " | Status: " + booking.getStatus());
        bookingId.setStyle("-fx-text-fill: #999; -fx-font-size: 10;");

        bookingInfo.getChildren().addAll(roomName, time, purpose, payment, bookingId);

        HBox buttonBox = new HBox(10);

        if ("CONFIRMED".equals(booking.getStatus())
                || "PENDING_PAYMENT".equals(booking.getStatus())) {

            Button cancelBtn = new Button("Cancel");
            cancelBtn.getStyleClass().add("danger-button");
            cancelBtn.setPrefWidth(80);
            Tooltip.install(cancelBtn,
                    new Tooltip("Cancel this booking and process refund if applicable."));

            cancelBtn.setOnAction(e -> {
                try {
                    boolean cancelled = bookingManager.cancelBooking(
                            booking.getBookingId(), currentUserEmail);
                    if (cancelled) {
                        showAlert("Success",
                                "Booking cancelled successfully! Refund processed if applicable.");
                        showMyBookingsView();
                    }
                } catch (Exception ex) {
                    showAlert("Cancellation Failed", ex.getMessage());
                }
            });

            buttonBox.getChildren().add(cancelBtn);
        }

        card.getChildren().addAll(bookingInfo, buttonBox);
        return card;
    }

    // ==============================================================
    // ==========================  UTILS  ===========================
    // ==============================================================

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setPrefSize(400, 200);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        System.out.println("Starting YorkU Conference Room Booking System...");
        launch(args);
    }
}
