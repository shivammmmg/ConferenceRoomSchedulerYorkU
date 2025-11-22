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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;


import shared.util.GlobalNavigationHelper;
import scenario2.controller.BookingManager;
import shared.model.Booking;
import shared.model.Room;
import shared.model.User;
import scenario1.controller.UserManager;
import scenario3.SensorSystem;
import scenario3.RoomStatusManager;
import scenario3.ui.BookingStatusObserver;
import scenario2.controller.BookingManager;


import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.Animation;
import javafx.scene.layout.Region;

/**
 * BookingFX (Scenario 2 GUI)
 * ---------------------------------------------------------------
 * This JavaFX screen handles:
 *   • room search
 *   • booking flow
 *   • payment modal
 *   • editing bookings
 *   • cancellation modal
 * Uses BookingManager (Singleton) + Rooms/Bookings CSV data.
 */
public class BookingFX extends Application {

    // Core system singletons + user session state
    private BookingManager bookingManager;
    private String currentUserEmail;
    private String currentUserType;

    private User currentUser;

    // Main UI containers
    private VBox mainContent;
    private StackPane overlayPane;

    // Modal windows (overlays)
    private VBox paymentModal;
    private VBox confirmationModal;
    private VBox editBookingModal;
    private VBox cancelModal;

    // Nav buttons (to toggle active state)
    private Button bookRoomBtn;
    private Button myBookingsBtn;
    private Button updateProfileBtn;
    private Button backToLoginBtn;

    // Sidebar email label so we can update it when email changes
    private Label sidebarEmailLabel;
    private String loggedInEmail;
    private String loggedInUserType;
    private Timeline countdownTimer = null;


    // Scenario 3 — Observer callback
    public void update() {
        System.out.println("[BookingFX] Observer event → refreshing MyBookings");

        if (myBookingsBtn != null
                && myBookingsBtn.getProperties().get("active").equals(true)) {

            showMyBookingsView(); // auto-refresh only if user is on MyBookings
        }
    }


    public void setLoggedInUser(String email, String userType) {
        this.loggedInEmail = email;
        this.loggedInUserType = userType;
    }


    @Override
    public void start(Stage stage) {

        // ==========================================================
        // =============== 1. INITIALIZE STATE =======================
        // ==========================================================

        bookingManager = BookingManager.getInstance();
        // Scenario 3 Observer — auto UI refresh
        RoomStatusManager.getInstance().attach(new BookingStatusObserver(this));

        currentUserEmail = (loggedInEmail != null) ? loggedInEmail : "test@yorku.ca";
        currentUserType  = (loggedInUserType != null) ? loggedInUserType : "student";
        // Load current user from Scenario 1 user store (if available)
        try {
            currentUser = UserManager.getInstance().findByEmail(currentUserEmail);
        } catch (Exception ex) {
            System.out.println("[Profile]   Could not load user for email: " + currentUserEmail);
            currentUser = null;
        }

        // ====================== SCENARIO 3 OBSERVER ATTACH =========================
        RoomStatusManager.getInstance().attach(new BookingStatusObserver(this));


        // ==========================================================
        // =============== 2. LEFT NAVIGATION PANEL =================
        // ==========================================================

        // YorkU-style vertical gradient
        // YorkU-style vertical gradient (matching AdminFX palette)
        Stop[] stops = {
                new Stop(0, Color.web("#BA0C2F")), // main York red
                new Stop(1, Color.web("#8B001A"))  // deeper shade for bottom
        };
        LinearGradient yorkGradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);


        VBox leftPanel = new VBox();
        leftPanel.setAlignment(Pos.TOP_CENTER);
        leftPanel.setPadding(new Insets(28, 22, 24, 22));
        leftPanel.setSpacing(24);
        leftPanel.setPrefWidth(260);
        leftPanel.setBackground(new Background(
                new BackgroundFill(yorkGradient, CornerRadii.EMPTY, Insets.EMPTY)
        ));

        // --------------------- Brand Section (MATCH MAIN PAGE) --------------------------
        Image img = new Image("images/yorku_logo.png", true);

        ImageView logoView = new ImageView(img);
        logoView.setPreserveRatio(true);
        logoView.setSmooth(true);
        logoView.setFitWidth(160);     // similar scale to main page sidebar
        logoView.setStyle("-fx-padding: 0 0 10 0;");

        // FULL TITLE (no truncation)
        Label appTitle = new Label("YorkU Conference Room Scheduler");
        appTitle.setWrapText(true);
        appTitle.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 16;" +
                        "-fx-font-weight: 700;" +
                        "-fx-padding: 5 0 0 0;"
        );

        // Subtitle
        Label appSubtitle = new Label("Conference Rooms");
        appSubtitle.setStyle("-fx-text-fill: rgba(255,255,255,0.75); -fx-font-size: 12;");
        appSubtitle.setWrapText(true);

        // Vertical block (like MainPage)
        VBox titleBlock = new VBox(3, appTitle, appSubtitle);
        titleBlock.setAlignment(Pos.CENTER_LEFT);
        titleBlock.setPadding(new Insets(0, 20, 0, 20));

        VBox brandCard = new VBox(10, logoView, titleBlock);
        brandCard.setAlignment(Pos.CENTER_LEFT);
        brandCard.setPadding(new Insets(10, 10, 10, 10));
        brandCard.setMaxWidth(Double.MAX_VALUE);

        brandCard.setBackground(new Background(
                new BackgroundFill(Color.rgb(0, 0, 0, 0.18), new CornerRadii(20), Insets.EMPTY)
        ));

        brandCard.setBorder(new Border(new BorderStroke(
                Color.rgb(255, 255, 255, 0.18),
                BorderStrokeStyle.SOLID,
                new CornerRadii(20),
                new BorderWidths(1)
        )));

        // --------------------- User Info ----------------------------
        sidebarEmailLabel = new Label(currentUserEmail);
        sidebarEmailLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.9); -fx-font-size: 11;");

        String roleDisplay = switch (currentUserType.toUpperCase()) {
            case "STUDENT" -> "Student";
            case "FACULTY" -> "Faculty";
            case "STAFF"   -> "Staff";
            case "PARTNER" -> "Partner";
            default        -> "User";
        };

        Label userRoleChip = new Label(roleDisplay);

        userRoleChip.setStyle(
                "-fx-background-color: rgba(0,0,0,0.30);" +
                        "-fx-text-fill: #f8fafc;" +
                        "-fx-padding: 3 10;" +
                        "-fx-background-radius: 999;" +
                        "-fx-font-size: 10;"
        );

        VBox accountBox = new VBox(2, sidebarEmailLabel, userRoleChip);
        accountBox.setAlignment(Pos.CENTER_LEFT);

        VBox headerBox = new VBox(10, brandCard, accountBox);

        // --------------------- NAV BUTTONS --------------------------
        bookRoomBtn      = createNavButton("📅  Book Room", true);
        myBookingsBtn    = createNavButton("📋  My Bookings", false);
        updateProfileBtn = createNavButton("👤  Update Profile", false);
        backToLoginBtn   = createNavButton("🚪  Logout", false);

        VBox navButtons = new VBox(6, bookRoomBtn, myBookingsBtn, updateProfileBtn, backToLoginBtn);
        navButtons.setAlignment(Pos.CENTER_LEFT);
        navButtons.setPadding(new Insets(10, 8, 10, 8));
        navButtons.setBackground(new Background(
                new BackgroundFill(Color.rgb(0,0,0,0.18),
                        new CornerRadii(18), Insets.EMPTY)));

        // -------------------- Tips Bottom Section -------------------
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label tipTitle = new Label("Booking tips");
        tipTitle.setStyle("-fx-text-fill: rgba(255,255,255,0.9); -fx-font-size: 11; -fx-font-weight: bold;");

        Label tipLabel = new Label("⏰ Off-peak times (8–10am, 4–8pm)\nare usually easier to book.");
        tipLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.8); -fx-font-size: 11;");
        tipLabel.setWrapText(true);

        VBox tipBox = new VBox(4, tipTitle, tipLabel);
        tipBox.setAlignment(Pos.BOTTOM_LEFT);

        // Final left panel stack
        leftPanel.getChildren().addAll(headerBox, navButtons, spacer, tipBox);

        // ==========================================================
        // =============== 3. MAIN CONTENT AREA =====================
        // ==========================================================

        mainContent = new VBox();
        mainContent.setSpacing(20);
        mainContent.setAlignment(Pos.TOP_LEFT);
        mainContent.getStyleClass().add("main-content");

        // ==========================================================
        // =============== 4. OVERLAY SYSTEM ========================
        // ==========================================================

        overlayPane = new StackPane();
        overlayPane.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
        overlayPane.setVisible(false);

        paymentModal      = createPaymentModal();
        confirmationModal = createConfirmationModal();
        editBookingModal  = createEditBookingModal();
        cancelModal       = createCancelModal();

        // All modals inside overlay
        overlayPane.getChildren().addAll(
                paymentModal,
                confirmationModal,
                editBookingModal,
                cancelModal
        );

        // ==========================================================
        // =============== 5. ROOT LAYOUT ===========================
        // ==========================================================

        VBox contentWrapper = new VBox(mainContent);
        contentWrapper.setPadding(new Insets(30));

        ScrollPane scroll = new ScrollPane(contentWrapper);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");

        BorderPane layout = new BorderPane();
        layout.setLeft(leftPanel);
        layout.setCenter(scroll);

        StackPane rootContainer = new StackPane(layout, overlayPane);

        // Default screen
        showBookingView();
        setActiveNav(bookRoomBtn);

        // ==========================================================
        // =============== 6. NAVIGATION HANDLERS ===================
        // ==========================================================

        bookRoomBtn.setOnAction(e -> {
            setActiveNav(bookRoomBtn);
            showBookingView();
        });

        myBookingsBtn.setOnAction(e -> {
            setActiveNav(myBookingsBtn);
            showMyBookingsView();
        });

        updateProfileBtn.setOnAction(e -> {
            setActiveNav(updateProfileBtn);
            showUpdateProfileView();
        });

        backToLoginBtn.setOnAction(e -> {
            stage.close();
            GlobalNavigationHelper.navigateTo("/scenario1/fxml/login.fxml");
        });




        // ==========================================================
        // =============== 7. SCENE + CSS ===========================
        // ==========================================================

        Scene scene = new Scene(rootContainer, 1200, 720);

        URL css = getClass().getResource("/styles/booking.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        stage.setScene(scene);
        stage.setTitle("YorkU Conference Room Scheduler");
        stage.show();
    }

    // ==============================================================
    // ======================== NAV HELPERS ==========================
    // ==============================================================

    /**
     * Returns an ImageView for a room based on its name.
     * Each room name maps to an image inside /images/.
     */
    private ImageView getRoomImageView(Room room) {
        String path;

        switch (room.getRoomName()) {
            case "York Room"            -> path = "/images/york-room.png";
            case "Lassonde Room"        -> path = "/images/lassonde-room.png";
            case "Bergeron Room"        -> path = "/images/bergeron-room.png";
            case "Scott Library Room"   -> path = "/images/scott-library-room.png";
            case "Accolade Room"        -> path = "/images/accolade-room.png";
            case "Student Center Room"  -> path = "/images/student-center-room.png";
            default                     -> path = "/images/default-room.png";
        }

        InputStream is = getClass().getResourceAsStream(path);
        if (is == null) {
            System.out.println("Room image not found: " + path);
            return null;
        }

        ImageView view = new ImageView(new Image(is));
        view.setFitWidth(220);
        view.setFitHeight(120);

        Rectangle clip = new Rectangle(220, 120);
        clip.setArcWidth(22);
        clip.setArcHeight(22);
        view.setClip(clip);

        return view;
    }

    /** Small rounded tag used throughout the UI. */
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

    /**
     * Applies modern styling to text fields, combo boxes, date pickers.
     */
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

        control.focusedProperty().addListener((obs, old, focusedNow) ->
                control.setStyle(focusedNow ? focused : base));
    }

    // ---------- Navigation Button Styling ----------
    private static final String NAV_BASE_STYLE =
            "-fx-background-radius: 999;" +
                    "-fx-padding: 10 16;" +
                    "-fx-font-size: 13;" +
                    "-fx-font-weight: 600;" +
                    "-fx-text-fill: #f8fafc;";

    private static final String NAV_BG_INACTIVE =
            "-fx-background-color: transparent;";
    private static final String NAV_BG_ACTIVE =
            "-fx-background-color: rgba(248,249,250,0.28);";
    private static final String NAV_BG_HOVER =
            "-fx-background-color: rgba(248,249,250,0.16);";

    /**
     * Creates a round-pill left navigation button.
     */
    private Button createNavButton(String text, boolean isActive) {
        Button button = new Button(text);
        button.setPrefWidth(Double.MAX_VALUE);
        button.setMinHeight(40);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));

        button.getProperties().put("active", isActive);
        button.setStyle(NAV_BASE_STYLE + (isActive ? NAV_BG_ACTIVE : NAV_BG_INACTIVE));

        button.setOnMouseEntered(e ->
                applyNavStyle(button, isActive, true));

        button.setOnMouseExited(e ->
                applyNavStyle(button, isActive, false));

        return button;
    }

    private void applyNavStyle(Button button, boolean active, boolean hover) {
        String bg = active ? NAV_BG_ACTIVE : (hover ? NAV_BG_HOVER : NAV_BG_INACTIVE);
        button.setStyle(NAV_BASE_STYLE + bg);
    }

    /**
     * Updates which nav button is currently active.
     */
    private void setActiveNav(Button activeButton) {
        for (Button b : new Button[]{bookRoomBtn, myBookingsBtn, updateProfileBtn, backToLoginBtn}) {
            if (b == null) continue;
            boolean active = (b == activeButton);
            b.getProperties().put("active", active);
            applyNavStyle(b, active, false);
        }
    }



    // ==============================================================
    // ======================  MODAL OVERLAYS  ======================
    // ==============================================================
    private VBox createEditBookingModal() {
        VBox modal = new VBox(18);
        modal.setPadding(new Insets(24));
        modal.setAlignment(Pos.TOP_LEFT);
        modal.setMaxWidth(520);
        modal.setVisible(false);

        modal.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-effect: dropshadow(gaussian, rgba(15,23,42,0.18), 26, 0, 0, 8);"
        );

        Label title = new Label("Edit Booking");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#AD001D"));

        Label subtitle = new Label("Update room, date/time, or purpose.");
        subtitle.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 11;");

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(10);

        String labelStyle =
                "-fx-text-fill: #6b7280;" +
                        "-fx-font-size: 11;" +
                        "-fx-font-weight: bold;";

        Label dateLbl = new Label("Date");
        dateLbl.setStyle(labelStyle);
        DatePicker datePicker = new DatePicker();
        applyModernFieldStyle(datePicker);

        Label startLbl = new Label("Start");
        startLbl.setStyle(labelStyle);
        ComboBox<String> startBox = new ComboBox<>();
        Label endLbl = new Label("End");
        endLbl.setStyle(labelStyle);
        ComboBox<String> endBox = new ComboBox<>();

        // Start time: 7am to 9:45pm
        for (int hour = 7; hour <= 22; hour++) {
            for (int min = 0; min < 60; min += 15) {
                startBox.getItems().add(String.format("%02d:%02d", hour, min));
            }
        }

// End time: 7:15am to 10pm
        for (int hour = 7; hour <= 22; hour++) {
            for (int min = 0; min < 60; min += 15) {
                endBox.getItems().add(String.format("%02d:%02d", hour, min));
            }
        }

        applyModernFieldStyle(startBox);
        applyModernFieldStyle(endBox);

        Label roomLbl = new Label("Room");
        roomLbl.setStyle(labelStyle);
        ComboBox<String> roomBox = new ComboBox<>();
        applyModernFieldStyle(roomBox);

        Label purposeLbl = new Label("Purpose");
        purposeLbl.setStyle(labelStyle);
        TextArea purposeArea = new TextArea();
        purposeArea.setPrefRowCount(3);
        applyModernFieldStyle(purposeArea);

        grid.add(dateLbl, 0, 0);
        grid.add(datePicker, 0, 1);

        grid.add(startLbl, 1, 0);
        grid.add(startBox, 1, 1);

        grid.add(endLbl, 2, 0);
        grid.add(endBox, 2, 1);

        grid.add(roomLbl, 0, 2);
        grid.add(roomBox, 0, 3, 3, 1);

        grid.add(purposeLbl, 0, 4);
        grid.add(purposeArea, 0, 5, 3, 1);

        // inline error label (instead of Alert)
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11;");
        errorLabel.setVisible(false);

        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle(
                "-fx-background-color: #e5e7eb;" +
                        "-fx-text-fill: #374151;" +
                        "-fx-background-radius: 999;" +
                        "-fx-padding: 6 16;"
        );

        Button saveBtn = new Button("Save Changes");
        saveBtn.setStyle(
                "-fx-background-color: #AD001D;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 999;" +
                        "-fx-padding: 6 20;"
        );

        buttons.getChildren().addAll(cancelBtn, saveBtn);

        modal.getChildren().addAll(title, subtitle, grid, errorLabel, buttons);

        modal.setUserData(new Object[]{
                datePicker, startBox, endBox, roomBox, purposeArea,
                errorLabel, saveBtn, cancelBtn
        });

        return modal;
    }

    private VBox createPaymentModal() {
        VBox modal = new VBox(18);
        modal.setPadding(new Insets(24));
        modal.setAlignment(Pos.TOP_LEFT);
        modal.setMaxWidth(480);

        modal.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-effect: dropshadow(gaussian, rgba(15,23,42,0.18), 26, 0, 0, 8);"
        );

        Label title = new Label("Complete Payment");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#AD001D"));

        Label subtitle = new Label("Review your booking details and confirm the deposit.");
        subtitle.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 11;");

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

        Separator sep = new Separator();
        sep.setOpacity(0.25);

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
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11;");
        errorLabel.setVisible(false);


        paymentForm.getChildren().addAll(
                paymentTitle,
                cardLabel,
                cardField,
                expiryCvvRow,
                nameBox,
                disclaimer
        );

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
                errorLabel,
                buttonRow
        );

        modal.setUserData(new Object[]{
                roomLabel, timeLabel, amountLabel,
                cardField, expiryField, cvvField, nameField,
                payBtn, cancelBtn, errorLabel
        });

        modal.setVisible(false);
        return modal;
    }
    private void showCancelModal(Booking booking) {
        Object[] refs = (Object[]) cancelModal.getUserData();
        Label message = (Label) refs[0];
        Button yesBtn = (Button) refs[1];
        Button noBtn  = (Button) refs[2];

        message.setText(
                "Are you sure you want to cancel the booking?\n" +
                        "Room: " + booking.getRoomId() + "\n" +
                        "Refund will be processed if applicable."
        );

        yesBtn.setOnAction(ev -> {
            try {
                boolean cancelled = bookingManager.cancelBooking(
                        booking.getBookingId(),
                        currentUserEmail
                );

                if (cancelled) {
                    hideOverlay();
                    cancelModal.setVisible(false);
                    showMyBookingsView();
                }

            } catch (Exception ex) {
                message.setText(ex.getMessage());
            }
        });

        noBtn.setOnAction(ev -> {
            cancelModal.setVisible(false);
            hideOverlay();
        });

        // Hide other modals
        paymentModal.setVisible(false);
        confirmationModal.setVisible(false);
        editBookingModal.setVisible(false);

        cancelModal.setVisible(true);
        showOverlay();
    }

    private VBox createCancelModal() {
        VBox modal = new VBox(12);
        modal.setPadding(new Insets(16));
        modal.setAlignment(Pos.TOP_LEFT);

        // keep it compact
        modal.setPrefWidth(320);
        modal.setMaxWidth(320);
        modal.setMinWidth(280);

        // IMPORTANT: height should follow content, not stretch
        modal.setPrefHeight(Region.USE_COMPUTED_SIZE);
        modal.setMaxHeight(Region.USE_PREF_SIZE);

        modal.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(15,23,42,0.18), 20, 0, 0, 6);"
        );

        Label title = new Label("Cancel Booking?");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        title.setTextFill(Color.web("#AD001D"));

        Label message = new Label();
        message.setWrapText(true);
        message.setStyle("-fx-text-fill: #374151; -fx-font-size: 11;");
        message.setMaxWidth(280);

        HBox btnRow = new HBox(8);
        btnRow.setAlignment(Pos.CENTER_RIGHT);

        Button noBtn = new Button("No");
        noBtn.setStyle(
                "-fx-background-color: #e5e7eb;" +
                        "-fx-text-fill: #374151;" +
                        "-fx-background-radius: 999;" +
                        "-fx-padding: 4 12;"
        );

        Button yesBtn = new Button("Yes, Cancel");
        yesBtn.setStyle(
                "-fx-background-color: #AD001D;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 999;" +
                        "-fx-padding: 4 14;"
        );

        btnRow.getChildren().addAll(noBtn, yesBtn);

        modal.getChildren().addAll(title, message, btnRow);

        modal.setUserData(new Object[]{message, yesBtn, noBtn});
        modal.setVisible(false);

        // center this compact card in the overlay
        StackPane.setAlignment(modal, Pos.CENTER);

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
        Label errorLabel      = (Label) refs[9];

        roomLabel.setText("Room: " + room.getRoomName());
        timeLabel.setText("Time: " + startTime.toLocalDate() + " "
                + startTime.toLocalTime() + " - " + endTime.toLocalTime());
        amountLabel.setText("Deposit (charged now): $" + String.format("%.2f", depositAmount));
        errorLabel.setText("");
        errorLabel.setVisible(false);


        cardField.clear();
        expiryField.clear();
        cvvField.clear();
        nameField.clear();
// reset handlers
        payBtn.setOnAction(null);
        cancelBtn.setOnAction(null);

        payBtn.setOnAction(event -> {
            errorLabel.setText("");
            errorLabel.setVisible(false);

            String error = validateCardForm(
                    cardField.getText(),
                    expiryField.getText(),
                    cvvField.getText(),
                    nameField.getText()
            );

            if (error != null) {
                errorLabel.setText(error);
                errorLabel.setVisible(true);
                return;
            }

            // card is valid → now process booking + payment
            processPaymentAndConfirm(
                    room, startTime, endTime, purpose, currentUserType
            );
        });

        cancelBtn.setOnAction(event -> hideOverlay());





        cancelBtn.setOnAction(event -> hideOverlay());

        paymentModal.setVisible(true);
        confirmationModal.setVisible(false);
        showOverlay();
    }

    private void processPaymentAndConfirm(Room room,
                                          LocalDateTime startTime,
                                          LocalDateTime endTime,
                                          String purpose,
                                          String userType) {
        try {
            Booking booking = bookingManager.bookRoom(
                    room.getRoomId(),
                    currentUserEmail,
                    startTime,
                    endTime,
                    purpose,
                    userType

            );

            // Scenario 3: Start no-show countdown
            scenario3.SensorSystem.getInstance().registerNewBooking(booking);


            showConfirmationModal(booking, room, startTime, endTime);

            SensorSystem.getInstance().registerNewBooking(booking);


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
        amountLabel.setText("Deposit Paid: $" + String.format("%.2f", booking.getDepositAmount()));

        String statusText = "Payment Status: " + booking.getPaymentStatus();
        statusLabel.setText(statusText);
        statusLabel.setStyle(
                "-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: "
                        + ("APPROVED".equals(booking.getPaymentStatus()) ? "#5cb85c" : "#f0ad4e")
                        + ";");

        doneBtn.setOnAction(event -> {
            hideOverlay();

            if ("APPROVED".equals(booking.getPaymentStatus())) {
                // highlight the correct tab in the sidebar
                setActiveNav(myBookingsBtn);

                // show My Bookings content
                showMyBookingsView();
            } else {
                // optional: if not approved, keep them on Book Room
                setActiveNav(bookRoomBtn);
                showBookingView();
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
        for (int hour = 7; hour <= 21; hour++) {
            for (int min = 0; min < 60; min += 15) {
                startTimeBox.getItems().add(String.format("%02d:%02d", hour, min));
            }
        }
        startTimeBox.setValue("09:00");
        applyModernFieldStyle(startTimeBox);

        Label endTimeLbl = new Label("End Time *");
        endTimeLbl.setStyle(labelStyle);
        ComboBox<String> endTimeBox = new ComboBox<>();
        for (int hour = 7; hour <= 22; hour++) {
            for (int min = 0; min < 60; min += 15) {

                // prevent anything after 22:00
                if (hour == 22 && min > 0) continue;

                endTimeBox.getItems().add(String.format("%02d:%02d", hour, min));
            }
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
        purposeErrorLabel.setManaged(false);

        purposeArea.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.trim().isEmpty()) {
                purposeErrorLabel.setVisible(false);
                purposeErrorLabel.setManaged(false);
                applyModernFieldStyle(purposeArea);
            }
        });

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


        Button smallGroupBtn = createPresetChip("Study group", () -> {
            capacitySpinner.getValueFactory().setValue(6);
            purposeArea.setText("Study group / project work session");
        });

        Button presentationBtn = createPresetChip("Presentation", () -> {
            capacitySpinner.getValueFactory().setValue(
                    Math.max(capacitySpinner.getValue(), 10)
            );
            purposeArea.setText("Presentation / talk with slides");
        });

        Button afternoonBtn = createPresetChip("Afternoon session", () -> {
            capacitySpinner.getValueFactory().setValue(
                    Math.max(capacitySpinner.getValue(), 4)
            );
            purposeArea.setText("Afternoon work session");
        });

        Button teamMeetingBtn = createPresetChip("Team Meeting", () -> {
            capacitySpinner.getValueFactory().setValue(
                    Math.max(capacitySpinner.getValue(), 4)
            );
            purposeArea.setText("Team meeting / discussion");
        });


        quickBox.getChildren().addAll(smallGroupBtn, presentationBtn,afternoonBtn, teamMeetingBtn);
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

        ImageView preview = getRoomImageView(room);
        if (preview != null) {
            preview.setFitWidth(150);
            preview.setFitHeight(95);

            Rectangle clip = new Rectangle(150, 95);
            clip.setArcWidth(16);
            clip.setArcHeight(16);
            preview.setClip(clip);

            card.getChildren().add(preview);
        }

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

        long durationMinutes = java.time.Duration.between(startTime, endTime).toMinutes();
        long durationHours = (long) Math.ceil(durationMinutes / 60.0);

        double hourlyRate    = bookingManager.getHourlyRateForUserType(currentUserType);
        double depositAmount = bookingManager.getDepositForUserTypeAndDuration(currentUserType, durationHours);

        Label depositLabel = new Label(
                String.format(
                        "💰 Deposit: $%.2f (1 hour × $%.2f/hour) • Est. total ≈ $%.2f",
                        depositAmount,
                        hourlyRate,
                        bookingManager.getEstimatedTotalForUser(currentUserType, durationHours)
                ));
        depositLabel.setStyle("-fx-text-fill: #AD001D; -fx-font-size: 11; -fx-font-weight: bold;");

        Label tipLabel = new Label("Ideal for " + getRoomVibeText(room));
        tipLabel.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 11;");

        infoBox.getChildren().addAll(nameLabel, locationLabel, amenitiesLabel, depositLabel, tipLabel);

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
                showPaymentModal(room, startTime, endTime, purpose, depositAmount);
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

        ImageView preview = getRoomImageView(room);
        if (preview != null) {
            StackPane imageWrapper = new StackPane(preview);
            imageWrapper.setPadding(new Insets(0, 0, 6, 0));
            card.getChildren().add(imageWrapper);
        }

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

        Label building = new Label("🏢 " + room.getBuilding());
        building.setStyle("-fx-font-size: 11; -fx-text-fill: #6b7280;");

        Label capacity = new Label("👥 Up to " + room.getCapacity() + " people");
        capacity.setStyle("-fx-font-size: 11; -fx-text-fill: #6b7280;");

        Label vibe = new Label(getRoomVibeText(room));
        vibe.setStyle("-fx-font-size: 11; -fx-text-fill: #9ca3af;");
        vibe.setWrapText(true);

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
                "Review your reservations, check payment status, and revisit your history."
        );

        List<Booking> allBookings = bookingManager.getAllUserBookings(currentUserEmail);

        // --- Split into UPCOMING and PAST ---
        List<Booking> upcoming = new ArrayList<>();
        List<Booking> past = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        for (Booking b : allBookings) {

            boolean isPast =
                    b.getEndTime().isBefore(now)
                            || b.getStatus().equals("CANCELLED")
                            || b.getStatus().equals("NO_SHOW")
                            || b.getStatus().equals("FINISHED")
                            || (b.getStatus().equals("IN_USE") && b.getEndTime().isBefore(now));

            if (isPast)
                past.add(b);
            else
                upcoming.add(b);
        }

        // ============================================================
        // SORT BOOKINGS
        // ============================================================

        // Upcoming: soonest → latest
        upcoming.sort(Comparator.comparing(Booking::getStartTime));

        // Past: latest → oldest
        past.sort(Comparator.comparing(Booking::getStartTime).reversed());


        // ------------------------------
        // If no upcoming AND no past
        // ------------------------------
        if (upcoming.isEmpty() && past.isEmpty()) {
            Label noBookings = new Label("You don't have any bookings yet.");
            noBookings.setStyle("-fx-text-fill: #666; -fx-font-size: 16;");

            Label hint = new Label("Head over to \"Book Room\" to make your first reservation.");
            hint.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 11;");

            VBox emptyState = new VBox(6, noBookings, hint);
            emptyState.setAlignment(Pos.CENTER_LEFT);

            mainContent.getChildren().addAll(header, emptyState);
            playMainContentFadeIn();
            return;
        }

        VBox container = new VBox(20);

        // ============================================================
        // UPCOMING BOOKINGS (Normal section)
        // ============================================================
        if (!upcoming.isEmpty()) {
            Label upcomingHeader = new Label("Upcoming Bookings");
            upcomingHeader.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

            VBox upcomingList = new VBox(10);
            for (Booking b : upcoming) {
                HBox card = createBookingCard(b);
                upcomingList.getChildren().add(card);
                animateCardIn(card);
            }

            container.getChildren().addAll(upcomingHeader, upcomingList);
        }

        // ============================================================
        // PAST BOOKINGS — collapsible section (TitledPane)
        // ============================================================
        if (!past.isEmpty()) {
            Label pastHeader = new Label("Past Bookings");
            pastHeader.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

            VBox pastList = new VBox(10);
            for (Booking b : past) {
                HBox card = createBookingCard(b);
                // Disable all buttons for past bookings
                card.setDisable(true);
                pastList.getChildren().add(card);
            }

            TitledPane pastPane = new TitledPane("Show Past Bookings", pastList);
            pastPane.setExpanded(false);
            pastPane.setCollapsible(true);
            pastPane.setStyle("-fx-font-size: 14;");

            container.getChildren().addAll(pastHeader, pastPane);
        }

        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        scrollPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        scrollPane.setMinHeight(0);

        mainContent.getChildren ().addAll(header, scrollPane);
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
                        + " | Deposit: $" + String.format("%.2f", booking.getDepositAmount()));
        payment.setStyle("-fx-text-fill: " + paymentColor
                + "; -fx-font-size: 11; -fx-font-weight: bold;");

        Label statusBadge = new Label(booking.getStatus());
        statusBadge.setStyle(getStatusBadgeStyle(booking.getStatus()));

        Label bookingId = new Label("ID: " + booking.getBookingId());
        bookingId.setStyle("-fx-text-fill: #999; -fx-font-size: 10;");

        bookingInfo.getChildren().addAll(
                roomName,
                time,
                purpose,
                payment,
                statusBadge,
                bookingId
        );

        HBox buttonBox = new HBox(10);

        // ======================= Scenario 3: Check-In Button =======================
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime earliestCheckIn = booking.getStartTime().minusMinutes(10);
        LocalDateTime latestCheckIn   = booking.getStartTime().plusMinutes(2); // demo window

        boolean allowCheckIn =
                booking.getStatus().equals("CONFIRMED") &&
                        now.isAfter(earliestCheckIn) &&
                        now.isBefore(latestCheckIn) &&
                        !booking.getStatus().equals("IN_USE") &&
                        !booking.getStatus().equals("NO_SHOW");

        if (allowCheckIn) {

            // ======================= COUNTDOWN TIMER =======================
            Label countdownLabel = new Label();
            countdownLabel.setStyle("-fx-text-fill: #d9534f; -fx-font-size: 11; -fx-font-weight: bold;");

            LocalDateTime deadline = latestCheckIn;

            // Create timer reference as array so it can be stopped inside lambda
            final Timeline[] countdownTimer = new Timeline[1];

            countdownTimer[0] = new Timeline(
                    new KeyFrame(Duration.seconds(1), ev -> {

                        long secondsLeft = java.time.Duration.between(LocalDateTime.now(), deadline).getSeconds();

                        if (secondsLeft <= 0) {
                            countdownLabel.setText("Check-in window closed");

                            try {
                                scenario3.RoomStatusManager.getInstance().forceNoShow(
                                        booking.getBookingId(),
                                        booking.getRoomId(),
                                        booking.getUserId()
                                );
                            } catch (Exception ex) {
                                System.out.println("[ERROR] Auto no-show: " + ex.getMessage());
                            }

                            countdownTimer[0].stop();
                            showMyBookingsView();
                            return;
                        }

                        long mins = secondsLeft / 60;
                        long secs = secondsLeft % 60;
                        countdownLabel.setText("⏳ Check-in closes in " + mins + "m " + secs + "s");

                    })
            );

            countdownTimer[0].setCycleCount(Animation.INDEFINITE);
            countdownTimer[0].play();

            // ========================= CHECK-IN BUTTON =========================
            Button checkInBtn = new Button("Check-In");
            checkInBtn.setPrefWidth(90);
            checkInBtn.setStyle(
                    "-fx-background-color: #28a745;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 999;" +
                            "-fx-padding: 6 16;"
            );
            Tooltip.install(checkInBtn, new Tooltip("Tap to check into your booked room."));

            checkInBtn.setOnAction(e -> {
                try {
                    // Stop countdown
                    countdownTimer[0].stop();

                    // Process check-in
                    RoomStatusManager.getInstance().checkIn(
                            booking.getBookingId(),
                            booking.getRoomId(),
                            booking.getUserId()
                    );

                    // Standard popup (Scenario 1 style)
                    showAlert("Check-In Successful",
                            "Sensor verified. Room is now marked as IN USE.");

                    // Refresh UI
                    showMyBookingsView();

                } catch (Exception ex) {
                    showAlert("Check-In Failed", ex.getMessage());
                }
            });


            buttonBox.getChildren().add(checkInBtn);
            buttonBox.getChildren().add(countdownLabel);
        }
        // ⭐ FIX 1 — CLOSE allowCheckIn IF


        // ======================= Edit + Cancel =======================
        if ("CONFIRMED".equals(booking.getStatus())
                || "PENDING_PAYMENT".equals(booking.getStatus())) {

            Button editBtn = new Button("Edit");
            editBtn.setPrefWidth(80);
            editBtn.setStyle(
                    "-fx-background-color: #e5e7eb;" +
                            "-fx-text-fill: #374151;" +
                            "-fx-background-radius: 999;" +
                            "-fx-padding: 6 16;");
            Tooltip.install(editBtn, new Tooltip("Change room, date/time, or purpose."));

            editBtn.setOnAction(e -> showEditBookingModal(booking));

            Button cancelBtn = new Button("Cancel");
            cancelBtn.getStyleClass().add("danger-button");
            cancelBtn.setPrefWidth(80);
            Tooltip.install(cancelBtn,
                    new Tooltip("Cancel this booking and process refund if applicable."));

            cancelBtn.setOnAction(e2 -> showCancelModal(booking));

            buttonBox.getChildren().addAll(editBtn, cancelBtn);
        }


        card.getChildren().addAll(bookingInfo, buttonBox);

        return card;
    }

    // ====================== Edit Booking Dialog ======================
    private void showEditBookingModal(Booking booking) {

        Object[] refs = (Object[]) editBookingModal.getUserData();
        DatePicker datePicker      = (DatePicker) refs[0];
        ComboBox<String> startBox  = (ComboBox<String>) refs[1];
        ComboBox<String> endBox    = (ComboBox<String>) refs[2];
        ComboBox<String> roomBox   = (ComboBox<String>) refs[3];
        TextArea purposeArea       = (TextArea) refs[4];
        Label errorLabel           = (Label) refs[5];
        Button saveBtn             = (Button) refs[6];
        Button cancelBtn           = (Button) refs[7];

        // Reset
        errorLabel.setText("");
        errorLabel.setVisible(false);

        // Prefill
        LocalDateTime start = booking.getStartTime();
        LocalDateTime end   = booking.getEndTime();

        datePicker.setValue(start.toLocalDate());
        startBox.setValue(String.format("%02d:%02d", start.getHour(), start.getMinute()));
        endBox.setValue(String.format("%02d:%02d", end.getHour(), end.getMinute()));
        purposeArea.setText(booking.getPurpose());

        roomBox.getItems().clear();
        for (Room r : bookingManager.getAllRooms()) {
            String label = r.getRoomId() + " - " + r.getRoomName();
            roomBox.getItems().add(label);
            if (r.getRoomId().equals(booking.getRoomId())) {
                roomBox.setValue(label);
            }
        }

        saveBtn.setOnAction(event -> {
            errorLabel.setVisible(false);

            try {
                LocalDate date = datePicker.getValue();
                if (date == null) {
                    showInlineError(errorLabel, "Please choose a date.");
                    return;
                }

                LocalTime newStart = LocalTime.parse(startBox.getValue());
                LocalTime newEnd   = LocalTime.parse(endBox.getValue());
                if (!newEnd.isAfter(newStart)) {
                    showInlineError(errorLabel, "End time must be after start time.");
                    return;
                }

                if (roomBox.getValue() == null) {
                    showInlineError(errorLabel, "Select a room.");
                    return;
                }

                String roomId = roomBox.getValue().split(" - ")[0];
                String purpose = purposeArea.getText().trim();
                if (purpose.isEmpty()) {
                    showInlineError(errorLabel, "Purpose is required.");
                    return;
                }

                bookingManager.editBooking(
                        booking.getBookingId(),
                        currentUserEmail,
                        roomId,
                        LocalDateTime.of(date, newStart),
                        LocalDateTime.of(date, newEnd),
                        purpose
                );

                // Close modal, refresh list
                editBookingModal.setVisible(false);
                hideOverlay();
                showMyBookingsView();

            } catch (Exception ex) {
                showInlineError(errorLabel, ex.getMessage());
            }
        });

        cancelBtn.setOnAction(e -> {
            editBookingModal.setVisible(false);
            hideOverlay();
        });

        // SHOW THE SMALL POPUP ON SAME PAGE
        paymentModal.setVisible(false);
        confirmationModal.setVisible(false);
        editBookingModal.setVisible(true);
        showOverlay();
    }

    // ==============================================================
    /** Refreshes the My Bookings screen after observer updates */
    public void refreshMyBookingsView() {
        showMyBookingsView();  // rebuilds full list
    }

    // ==============================================================
    // ====================  UPDATE PROFILE VIEW  ===================
    // ==============================================================

    private void showUpdateProfileView() {
        mainContent.getChildren().clear();

        HBox header = createPageHeader(
                "Update Profile",
                "Change your name, email address, or password linked to this account."
        );

        // If for some reason we don't have a loaded user (e.g. guest)
        if (currentUser == null) {
            Label msg = new Label(
                    "Profile updates are only available after logging in with a registered account.");
            msg.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 13;");

            VBox box = new VBox(10, header, msg);
            box.setAlignment(Pos.TOP_LEFT);

            mainContent.getChildren().add(box);
            playMainContentFadeIn();
            return;
        }

        VBox card = new VBox(18);
        card.setPadding(new Insets(26));
        card.setSpacing(16);
        card.setMaxWidth(550);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 24;" +
                        "-fx-effect: dropshadow(gaussian, rgba(15,23,42,0.12), 22, 0, 0, 6);"
        );

        Label title = new Label("Account Details");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#AD001D"));

        Label subtitle = new Label(
                "Update your display name, YorkU email, or password. " +
                        "Some fields may have restrictions based on your role.");
        subtitle.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 12;");
        subtitle.setWrapText(true);

        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(10);

        String labelStyle =
                "-fx-text-fill: #6b7280;" +
                        "-fx-font-size: 11;" +
                        "-fx-font-weight: bold;";

        // -------- Name --------
        Label nameLbl = new Label("Full Name");
        nameLbl.setStyle(labelStyle);
        TextField nameField = new TextField(currentUser.getName());
        applyModernFieldStyle(nameField);

        // -------- Email --------
        Label emailLbl = new Label("Email Address");
        emailLbl.setStyle(labelStyle);
        TextField emailField = new TextField(currentUser.getEmail());
        applyModernFieldStyle(emailField);

        Label emailHint = new Label(
                "Students must use @my.yorku.ca. Faculty/Staff must use @yorku.ca or @my.yorku.ca.");
        emailHint.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 10;");
        emailHint.setWrapText(true);

        // -------- Password --------
        Label passLbl = new Label("New Password");
        passLbl.setStyle(labelStyle);
        PasswordField newPassField = new PasswordField();
        newPassField.setPromptText("Leave blank to keep existing password");
        applyModernFieldStyle(newPassField);

        Label confirmLbl = new Label("Confirm New Password");
        confirmLbl.setStyle(labelStyle);
        PasswordField confirmPassField = new PasswordField();
        confirmPassField.setPromptText("Re-enter new password");
        applyModernFieldStyle(confirmPassField);

        Label passHint = new Label(
                "Password must have 8+ characters, including uppercase, lowercase, digit, and symbol.");
        passHint.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 10;");
        passHint.setWrapText(true);

        // Inline error label
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11;");
        errorLabel.setVisible(false);

        // Layout in grid
        grid.add(nameLbl, 0, 0);
        grid.add(nameField, 0, 1, 2, 1);

        grid.add(emailLbl, 0, 2);
        grid.add(emailField, 0, 3, 2, 1);
        grid.add(emailHint, 0, 4, 2, 1);

        grid.add(passLbl, 0, 5);
        grid.add(newPassField, 0, 6, 2, 1);

        grid.add(confirmLbl, 0, 7);
        grid.add(confirmPassField, 0, 8, 2, 1);
        grid.add(passHint, 0, 9, 2, 1);

        HBox buttonRow = new HBox(10);
        buttonRow.setAlignment(Pos.CENTER_RIGHT);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle(
                "-fx-background-color: #e5e7eb;" +
                        "-fx-text-fill: #374151;" +
                        "-fx-background-radius: 999;" +
                        "-fx-padding: 6 16;"
        );

        Button saveBtn = new Button("Save Changes");
        saveBtn.setStyle(
                "-fx-background-color: #AD001D;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 999;" +
                        "-fx-padding: 6 22;"
        );

        buttonRow.getChildren().addAll(cancelBtn, saveBtn);

        card.getChildren().addAll(title, subtitle, grid, errorLabel, buttonRow);

        // ---------- Button Logic ----------
        saveBtn.setOnAction(e -> {
            errorLabel.setVisible(false);
            String nameInput   = nameField.getText().trim();
            String emailInput  = emailField.getText().trim();
            String newPass     = newPassField.getText();
            String confirmPass = confirmPassField.getText();

            if (nameInput.isEmpty()) {
                errorLabel.setText("Name cannot be empty.");
                errorLabel.setVisible(true);
                return;
            }
            if (emailInput.isEmpty()) {
                errorLabel.setText("Email cannot be empty.");
                errorLabel.setVisible(true);
                return;
            }

            // Password validation: either both blank (no change) OR both filled and matching
            String passwordToApply = null;
            if (!newPass.isEmpty() || !confirmPass.isEmpty()) {
                if (newPass.isEmpty() || confirmPass.isEmpty()) {
                    errorLabel.setText("Please enter and confirm the new password.");
                    errorLabel.setVisible(true);
                    return;
                }
                if (!newPass.equals(confirmPass)) {
                    errorLabel.setText("New password and confirmation do not match.");
                    errorLabel.setVisible(true);
                    return;
                }
                passwordToApply = newPass;
            }

            try {
                UserManager um = UserManager.getInstance();

                // Update name + password
                String nameParam = nameInput.equals(currentUser.getName()) ? null : nameInput;
                um.updateProfile(currentUser, nameParam, passwordToApply);

                // Update email only if changed
                if (!emailInput.equalsIgnoreCase(currentUser.getEmail())) {
                    um.updateEmail(currentUser, emailInput);
                    currentUserEmail = emailInput;
                    if (sidebarEmailLabel != null) {
                        sidebarEmailLabel.setText(currentUserEmail);
                    }
                }

                showAlert("Profile Updated", "Your account details have been updated.");
                newPassField.clear();
                confirmPassField.clear();

            } catch (Exception ex) {
                errorLabel.setText(ex.getMessage());
                errorLabel.setVisible(true);
            }
        });

        cancelBtn.setOnAction(e -> {
            setActiveNav(bookRoomBtn);
            showBookingView();
        });

        mainContent.getChildren().addAll(header, card);
        playMainContentFadeIn();
    }




    // ==============================================================
    // ==========================  UTILS  ===========================
    // ==============================================================
    // ==================== Payment Validation Helpers ====================

    private boolean isValidCardNumber(String cardNumber) {
        // Remove spaces so "1234 5678 9012 3456" is allowed.
        String digitsOnly = cardNumber.replaceAll("\\s+", "");
        return digitsOnly.matches("\\d{16}");
    }

    private boolean isValidExpiry(String expiry) {
        if (expiry == null) return false;

        // MM/YY where MM is 01‒12
        if (!expiry.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            return false;
        }

        try {
            java.time.format.DateTimeFormatter formatter =
                    java.time.format.DateTimeFormatter.ofPattern("MM/yy");
            YearMonth exp = YearMonth.parse(expiry, formatter);
            YearMonth now = YearMonth.now();
            // Must be current month or later
            return !exp.isBefore(now);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidCvv(String cvv) {
        return cvv != null && cvv.matches("\\d{3}");
    }
    // ====================== Edit Booking Dialog ======================
    private String validateCardForm(String cardNumber,
                                    String expiry,
                                    String cvv,
                                    String nameOnCard) {

        if (nameOnCard == null || nameOnCard.trim().isEmpty()) {
            return "Name on card is required.";
        }

        String normalized = cardNumber == null ? "" : cardNumber.replaceAll("\\s+", "");
        if (!normalized.matches("\\d{16}")) {
            return "Card number must be exactly 16 digits.";
        }

        if (!isValidExpiry(expiry)) {
            return "Card expiry date is invalid or expired.";
        }

        if (cvv == null || !cvv.matches("\\d{3}")) {
            return "CVV must be 3 digits.";
        }

        return null; // valid
    }
    private void showModal(VBox modal) {
        modal.setVisible(true);
        FadeTransition ft = new FadeTransition(Duration.millis(200), modal);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    private void hideModal(VBox modal) {
        FadeTransition ft = new FadeTransition(Duration.millis(150), modal);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setOnFinished(e -> modal.setVisible(false));
        ft.play();
    }

    private void showLabelError(Label label, String text) {
        label.setText(text);
        label.setVisible(true);
    }

    private void showInlineError(Label label, String text) {
        label.setText(text);
        label.setVisible(true);
    }





    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);

        // Match EXACT behavior of RegisterController popups
        Stage stage = (Stage) mainContent.getScene().getWindow();
        alert.initOwner(stage);

        alert.showAndWait();
    }


    public static void main(String[] args) {
        System.out.println("Starting YorkU Conference Room Booking System...");
        launch(args);
    }
    private String getStatusBadgeStyle(String status) {
        switch (status) {
            case "CONFIRMED":
                return "-fx-background-color: #0275d8; -fx-text-fill: white; "
                        + "-fx-font-size: 10; -fx-font-weight: bold; "
                        + "-fx-padding: 3 8; -fx-background-radius: 12;";
            case "ACTIVE":
            case "IN_USE":
                return "-fx-background-color: #5cb85c; -fx-text-fill: white; "
                        + "-fx-font-size: 10; -fx-font-weight: bold; "
                        + "-fx-padding: 3 8; -fx-background-radius: 12;";
            case "NO_SHOW":
                return "-fx-background-color: #d9534f; -fx-text-fill: white; "
                        + "-fx-font-size: 10; -fx-font-weight: bold; "
                        + "-fx-padding: 3 8; -fx-background-radius: 12;";
            case "CANCELLED":
                return "-fx-background-color: #6c757d; -fx-text-fill: white; "
                        + "-fx-font-size: 10; -fx-font-weight: bold; "
                        + "-fx-padding: 3 8; -fx-background-radius: 12;";
            default:
                return "-fx-background-color: #999; -fx-text-fill: white; "
                        + "-fx-font-size: 10; -fx-font-weight: bold; "
                        + "-fx-padding: 3 8; -fx-background-radius: 12;";
        }
    }

    /* private void handleCheckIn(Booking booking) {
        scenario3.SensorSystem.getInstance().simulateUserCheckIn(booking);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Check-In Successful");
        alert.setHeaderText("Check-In Successful");
        alert.setContentText("Sensor verified. Room is now marked as IN USE.");

        Stage owner = (Stage) mainContent.getScene().getWindow();
        alert.initOwner(owner);

        alert.showAndWait();

        showMyBookingsView(); // refresh UI
    } */



}