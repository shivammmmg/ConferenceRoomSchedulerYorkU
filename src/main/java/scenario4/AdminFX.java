package scenario4;

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import scenario1.controller.UserManager;
import shared.model.*;
import shared.util.CSVHelper;
import scenario4.components.RoomDetailsPopup;
import scenario4.components.RoomOccupancyPopup;

public class AdminFX extends Application {

    // ============================================================
    //  GLOBAL STATE & UI ROOT STRUCTURE (UPDATED FOR TOAST + BLUR)
    // ============================================================

    private VBox mainContent;        // central UI container
    private StackPane rootStack;     // NEW: toast + blur layer
    private BorderPane rootPane;     // actual layout under StackPane

    private VBox leftPanel;          // sidebar container

    // Toast overlay container
    private VBox toastLayer;

    // Blur applied to main UI during dialogs
    private final GaussianBlur blurEffect = new GaussianBlur(0);

    // Navigation buttons
    private Button dashboardBtn, addRoomBtn, manageRoomsBtn,
            createAdminBtn, manageAdminsBtn, logoutBtn;

    // York theme
    private static final String YORK_RED = "#BA0C2F";

    // Navigation CSS
    private static final String NAV_BASE =
            "-fx-background-radius: 999;" +
                    "-fx-padding: 10 18;" +
                    "-fx-font-size: 13;" +
                    "-fx-font-weight: 600;" +
                    "-fx-text-fill: #f9fafb;" +
                    "-fx-cursor: hand;";

    private static final String NAV_ACTIVE   = "-fx-background-color: rgba(255,255,255,0.32);";
    private static final String NAV_INACTIVE = "-fx-background-color: transparent;";
    private static final String NAV_HOVER    = "-fx-background-color: rgba(255,255,255,0.18);";

    // UserManager single instance
    private final UserManager userManager = UserManager.getInstance();

    // Logged-in info
    private String loggedInEmail;
    private String loggedInType;

    public void setLoggedInAdmin(String email, String type) {
        this.loggedInEmail = email;
        this.loggedInType = type;
    }

    // ============================================================
    //  START — BUILD ROOT STRUCTURE
    // ============================================================

    @Override
    public void start(Stage stage) {

        // Load room data
        try {
            var rooms = CSVHelper.loadRooms("data/rooms.csv");
            RoomRepository repo = RoomRepository.getInstance();
            for (Room r : rooms) repo.addRoom(r);
        } catch (Exception ex) {
            System.out.println("Failed to load room CSV");
        }

        // Build main layout
        mainContent = new VBox();
        mainContent.setSpacing(20);
        mainContent.setPadding(new Insets(30));
        mainContent.setStyle("-fx-background-color: linear-gradient(to bottom, #f3f4f6, #e5e7eb);");
        mainContent.setEffect(blurEffect);

        ScrollPane sc = new ScrollPane(mainContent);
        sc.setFitToWidth(true);
        sc.setFitToHeight(true);
        sc.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        leftPanel = buildLeftPanel();

        rootPane = new BorderPane();
        rootPane.setLeft(leftPanel);
        rootPane.setCenter(sc);

        // rootStack: where Toast + Blur overlay live
        toastLayer = new VBox(10);
        toastLayer.setAlignment(Pos.TOP_RIGHT);
        toastLayer.setPadding(new Insets(20));
        toastLayer.setMouseTransparent(true); // clicks go through

        rootStack = new StackPane(rootPane, toastLayer);
        rootStack.setStyle("-fx-background-color:#e5e7eb;");

        Scene scene = new Scene(rootStack, 1200, 720);
        stage.setScene(scene);
        stage.setTitle("Scenario 4 — Admin Control Panel");

        setupKeyboardShortcuts(scene);

        // Load dashboard by default
        showDashboardView();
        setActiveNav(dashboardBtn);

        // Role-based access control
        if (loggedInType != null && loggedInType.equals("ADMIN")) {
            createAdminBtn.setManaged(false);
            createAdminBtn.setVisible(false);

            manageAdminsBtn.setManaged(false);
            manageAdminsBtn.setVisible(false);
        }

        stage.show();

        stage.setOnCloseRequest(e -> RoomRepository.getInstance().saveToCSV());
    }


    // ============================================================
    //  KEYBOARD SHORTCUTS
    // ============================================================

    private void setupKeyboardShortcuts(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {

            if (e.isControlDown()) {
                switch (e.getCode()) {

                    case D:
                        showDashboardView();
                        setActiveNav(dashboardBtn);
                        break;

                    case R:
                        showManageRoomsView();
                        setActiveNav(manageRoomsBtn);
                        break;

                    case A:
                        showAddRoomView();
                        setActiveNav(addRoomBtn);
                        break;
                }
            }
        });
    }

    // ============================================================
    //  SIDEBAR BUILD
    // ============================================================

    private VBox buildLeftPanel() {

        VBox left = new VBox(22);
        left.setPadding(new Insets(25));
        left.setPrefWidth(260);
        left.setAlignment(Pos.TOP_LEFT);

        left.setStyle(
                "-fx-background-color: linear-gradient(to bottom," + YORK_RED + ", #8B001A, #450A0A);" +
                        "-fx-border-color: rgba(15,23,42,0.35);" +
                        "-fx-border-width: 0 1 0 0;"
        );

        Label title = new Label("Admin Control Panel");
        title.setStyle("""
    -fx-font-size: 20px;
    -fx-font-weight: bold;
    -fx-text-fill: white;
    -fx-padding: 8 0 12 0;   /* increased vertical spacing */
""");

// make the text slightly wider horizontally
        title.setScaleX(1.10);   // try 1.05–1.10 depending on how wide you want it


        Label subtitle = new Label("Room & User Management");
        subtitle.setFont(Font.font("Segoe UI", 12));
        subtitle.setTextFill(Color.rgb(255,255,255,0.85));

        String roleText =
                (loggedInType == null) ? "Super Admin" :
                        loggedInType.equalsIgnoreCase("CHIEF") ? "Chief Event Coordinator" : "Admin";

        Label roleBadge = new Label(roleText);
        roleBadge.setStyle(
                "-fx-background-color: rgba(255,255,255,0.28);" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-padding: 3 10;" +
                        "-fx-background-radius: 999;" +
                        "-fx-font-size: 10;"
        );

        VBox header = new VBox(6, title, subtitle, roleBadge);

        dashboardBtn    = createNavButton("🏠  Dashboard");
        addRoomBtn      = createNavButton("➕  Add Room");
        manageRoomsBtn  = createNavButton("🛠  Manage Rooms");
        createAdminBtn  = createNavButton("👤  Create Admin");
        manageAdminsBtn = createNavButton("👥  Manage Admins");
        logoutBtn       = createNavButton("🚪  Logout");

        dashboardBtn.setOnAction(e -> { setActiveNav(dashboardBtn); showDashboardView(); });
        addRoomBtn.setOnAction(e -> { setActiveNav(addRoomBtn); showAddRoomView(); });
        manageRoomsBtn.setOnAction(e -> { setActiveNav(manageRoomsBtn); showManageRoomsView(); });
        createAdminBtn.setOnAction(e -> { setActiveNav(createAdminBtn); showCreateAdminView(); });
        manageAdminsBtn.setOnAction(e -> { setActiveNav(manageAdminsBtn); showManageAdminsView(); });
        logoutBtn.setOnAction(e -> ((Stage) logoutBtn.getScene().getWindow()).close());

        VBox nav = new VBox(6,
                dashboardBtn, addRoomBtn, manageRoomsBtn,
                createAdminBtn, manageAdminsBtn, logoutBtn
        );
        nav.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label footer = new Label("Today • " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM d, yyyy")));
        footer.setStyle("-fx-text-fill: rgba(255,255,255,0.75); -fx-font-size: 10;");

        left.getChildren().addAll(header, nav, spacer, footer);

        leftPanel = left;
        return left;
    }

    private Button createNavButton(String text) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setStyle(NAV_BASE + NAV_INACTIVE);
        b.setFocusTraversable(false);

        b.setOnMouseEntered(e -> { if (!isActive(b)) b.setStyle(NAV_BASE + NAV_HOVER); });
        b.setOnMouseExited(e -> { if (!isActive(b)) b.setStyle(NAV_BASE + NAV_INACTIVE); });

        return b;
    }

    private boolean isActive(Button b) {
        Object a = b.getProperties().get("active");
        return a instanceof Boolean && (Boolean) a;
    }

    private void setActiveNav(Button active) {
        for (Button b : new Button[]{
                dashboardBtn, addRoomBtn, manageRoomsBtn,
                createAdminBtn, manageAdminsBtn, logoutBtn
        }) {
            if (b == null) continue;
            boolean isActive = (b == active);
            b.getProperties().put("active", isActive);
            b.setStyle(NAV_BASE + (isActive ? NAV_ACTIVE : NAV_INACTIVE));
        }
    }

    // ============================================================
    //  BLUR EFFECT (FOR MODALS)
    // ============================================================

    private void applyBlur() {
        Timeline t = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(blurEffect.radiusProperty(), blurEffect.getRadius())),
                new KeyFrame(Duration.millis(180), new KeyValue(blurEffect.radiusProperty(), 12))
        );
        t.play();
    }

    private void removeBlur() {
        Timeline t = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(blurEffect.radiusProperty(), blurEffect.getRadius())),
                new KeyFrame(Duration.millis(180), new KeyValue(blurEffect.radiusProperty(), 0))
        );
        t.play();
    }

    // ============================================================
    //  TOAST MANAGER (SLIDE + FADE)
    // ============================================================

    private enum ToastType { SUCCESS, WARNING, ERROR }

    private void showToast(String message, ToastType type) {

        HBox toast = new HBox(10);
        toast.setAlignment(Pos.CENTER_LEFT);
        toast.setPadding(new Insets(12, 18, 12, 18));
        toast.setMaxWidth(360);
        toast.setOpacity(0);

        String bg, icon;

        switch (type) {
            case SUCCESS:
                bg = "#16a34a";
                icon = "✔";
                break;
            case WARNING:
                bg = "#f59e0b";
                icon = "⚠";
                break;
            default:
                bg = "#dc2626";
                icon = "✖";
                break;
        }

        Label iconLabel = new Label(icon);
        iconLabel.setTextFill(Color.WHITE);
        iconLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        Label msg = new Label(message);
        msg.setTextFill(Color.WHITE);
        msg.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));

        toast.getChildren().addAll(iconLabel, msg);
        toast.setStyle(
                "-fx-background-color:" + bg + ";" +
                        "-fx-background-radius:12;"
        );

        toastLayer.getChildren().add(0, toast);

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(220), toast);
        slideIn.setFromX(100);
        slideIn.setToX(0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(180), toast);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ParallelTransition show = new ParallelTransition(slideIn, fadeIn);
        show.play();

        PauseTransition stay = new PauseTransition(Duration.seconds(3));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(220), toast);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeOut.setOnFinished(e -> toastLayer.getChildren().remove(toast));

        SequentialTransition seq = new SequentialTransition(show, stay, fadeOut);
        seq.play();
    }

    private void alertSuccess(String msg) { showToast(msg, ToastType.SUCCESS); }
    private void alertWarning(String msg) { showToast(msg, ToastType.WARNING); }
    private void alertError(String msg)   { showToast(msg, ToastType.ERROR); }

    // ============================================================
// PART 1 END
// (Next: Dashboard View, Charts, Badges, Add/Manage Rooms…)
// ============================================================
    // ============================================================
    //  DASHBOARD VIEW (with chart hover effects)
    // ============================================================
    private VBox statCard(String title, String value, String description) {
        Label t = new Label(title);
        t.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        t.setTextFill(Color.web("#6b7280"));

        Label v = new Label(value);
        v.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        v.setTextFill(Color.web("#111827"));

        Label d = new Label(description);
        d.setWrapText(true);
        d.setStyle("-fx-text-fill: #4b5563; -fx-font-size: 11;");

        VBox box = new VBox(4, t, v, d);
        box.setPadding(new Insets(18));
        box.setPrefWidth(220);

        box.setStyle(
                "-fx-background-color: linear-gradient(to bottom,#ffffff,#f9fafb);" +
                        "-fx-background-radius: 18;" +
                        "-fx-border-radius: 18;" +
                        "-fx-border-color: rgba(148,163,184,0.45);" +
                        "-fx-border-width: 0.6;" +
                        "-fx-effect: dropshadow(gaussian, rgba(15,23,42,0.10), 18, 0, 0, 4);"
        );

        return box;
    }

    // ============================================================
//  DASHBOARD VIEW v2.0 — Glassmorphism Edition
// ============================================================
    private void showDashboardView() {

        mainContent.getChildren().clear();

        // ---------- HEADER ----------
        Label loggedIn = new Label("Logged in as: " + loggedInEmail + " • Role: " + loggedInType);
        loggedIn.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 13;");

        Label title = labelH1("Admin Dashboard");
        Label subtitle = labelSub("Live system state, analytics, and indicators.");
        subtitle.setStyle("-fx-text-fill:#6b7280; -fx-font-size:13;");

        RoomRepository repo = RoomRepository.getInstance();
        Map<String, Room> rooms = repo.getAllRooms();

        // ---------- METRICS ----------
        int totalRooms = rooms.size();
        int active = 0, disabled = 0, maint = 0;

        Map<String, Integer> buildingCapacity = new LinkedHashMap<>();

        for (Room r : rooms.values()) {
            String st = (r.getStatus() == null ? "" : r.getStatus()).toUpperCase();

            if (st.contains("DIS")) disabled++;
            else if (st.contains("MAINT")) maint++;
            else active++;

            buildingCapacity.merge(
                    (r.getBuilding() == null || r.getBuilding().isBlank()) ? "Other" : r.getBuilding(),
                    r.getCapacity(),
                    Integer::sum
            );
        }

        int totalAdmins = userManager.getAdminAccounts().size();
        int systemLoad = new Random().nextInt(55) + 10;

        // ---------- KPI CARDS ----------
        HBox kpiRow = new HBox(20,
                glassKPI("Total Rooms", totalRooms, "📦"),
                glassKPI("Active Rooms", active, "🟢"),
                glassKPI("Maintenance", maint, "🛠"),
                glassKPI("Disabled", disabled, "🚫"),
                glassKPI("Admins", totalAdmins, "👥")
        );

        // ============================================================
        //  BOOKINGS TREND (Last 14 Days)
        // ============================================================

        LineChart<String, Number> trend = buildBookingsTrendChart();

        // 🔥 FIX: force chart height so it doesn't collapse
        trend.setMinHeight(260);
        trend.setPrefHeight(260);
        trend.setMaxHeight(Double.MAX_VALUE);

        VBox trendCard = glassCard(
                "Bookings (Last 14 Days)",
                "Daily booking activity trend.",
                trend
        );

        // ============================================================
        //  STATUS PIE
        // ============================================================

        PieChart pie = new PieChart();
        pie.setLabelsVisible(false);
        pie.setLegendVisible(true);

        if (totalRooms > 0) {
            pie.getData().add(new PieChart.Data("Active", active));
            pie.getData().add(new PieChart.Data("Disabled", disabled));
            pie.getData().add(new PieChart.Data("Maintenance", maint));
        }

        applyPieChartHoverEffects(pie);

        // 🔥 FIX: chart height
        pie.setMinHeight(260);
        pie.setPrefHeight(260);
        pie.setMaxHeight(Double.MAX_VALUE);

        VBox pieCard = glassCard("Room Status Mix", "Current system-wide room status distribution.", pie);

        // ============================================================
        //   CAPACITY BAR CHART
        // ============================================================

        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis();

        x.setLabel("Building");
        y.setLabel("Capacity");

        BarChart<String, Number> bar = new BarChart<>(x, y);
        bar.setLegendVisible(false);
        bar.setAnimated(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (var entry : buildingCapacity.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        bar.getData().add(series);

        applyBarChartHoverEffects(bar);

        // 🔥 FIX: force height
        bar.setMinHeight(260);
        bar.setPrefHeight(260);
        bar.setMaxHeight(Double.MAX_VALUE);

        VBox barCard = glassCard("Capacity by Building", "Total capacity grouped by building.", bar);

        HBox chartRow = new HBox(20, pieCard, barCard);

        // ============================================================
        //  ROOM STATUS GRID
        // ============================================================

        FlowPane grid = new FlowPane(10, 10);
        grid.setPrefWrapLength(600);
        for (Room r : rooms.values()) grid.getChildren().add(roomStatusBlock(r));

        VBox statusGridCard = glassCard("Room Status Grid", "Visual map of all rooms.", grid);

        // ============================================================
        //  SYSTEM HEALTH PANEL
        // ============================================================

        VBox healthCard = glassCard(
                "System Health Overview",
                "Backend system status & telemetry.",
                systemHealthWidget(systemLoad)
        );

        // ============================================================
        //  ADMIN ACTIVITY FEED
        // ============================================================

        VBox feed = new VBox(8,
                feedItem("Admin login", "System accessed successfully."),
                feedItem("Room update", "Maintenance scheduled for R301."),
                feedItem("User management", "Admin 'events@yorku.ca' created."),
                feedItem("Payment", "Reconciliation completed successfully."),
                feedItem("Sync", "CSV updated at " +
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")))
        );

        VBox feedCard = glassCard("Admin Activity Feed", "Most recent system-level actions.", feed);

        // ============================================================
        //  LAYOUT STRUCTURE
        // ============================================================

        VBox topSection = new VBox(10, loggedIn, title, subtitle, kpiRow);
        HBox secondRow = new HBox(20, trendCard, healthCard);
        HBox thirdRow = new HBox(20, statusGridCard, feedCard);

        // ---------- Final Assembly ----------
        mainContent.getChildren().addAll(
                topSection,
                new Separator(),
                secondRow,
                chartRow,
                thirdRow
        );

        fade();
    }

    // ============================================================
//  BOOKINGS TREND CHART (Last 14 Days)
// ============================================================
    private LineChart<String, Number> buildBookingsTrendChart() {

        // X axis (dates)
        CategoryAxis x = new CategoryAxis();
        x.setLabel("Date");

        // Y axis (count)
        NumberAxis y = new NumberAxis();
        y.setLabel("Bookings");

        LineChart<String, Number> chart = new LineChart<>(x, y);
        chart.setLegendVisible(false);
        chart.setCreateSymbols(true);
        chart.setAnimated(false);
        chart.setMinHeight(260);

        // Build empty series
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Collect bookings (from Booking CSV)
        // Collect bookings (from Booking CSV safely)
        List<Booking> all;
        try {
            all = CSVHelper.loadBookings("data/bookings.csv");
        } catch (Exception ex) {
            all = new ArrayList<>();
            System.out.println("[WARN] Failed to load bookings CSV: " + ex.getMessage());
        }


        // Count bookings per day for last 14 days
        Map<String, Integer> counts = new LinkedHashMap<>();

        LocalDateTime now = LocalDateTime.now();

        for (int i = 13; i >= 0; i--) {
            String d = now.minusDays(i).format(DateTimeFormatter.ofPattern("MMM d"));
            counts.put(d, 0);
        }

        for (Booking b : all) {
            String d = b.getStartTime().format(DateTimeFormatter.ofPattern("MMM d"));
            if (counts.containsKey(d)) {
                counts.put(d, counts.get(d) + 1);
            }
        }

        // Add to line series
        for (var e : counts.entrySet()) {
            series.getData().add(new XYChart.Data<>(e.getKey(), e.getValue()));
        }

        chart.getData().add(series);

        // Hover animation
        for (XYChart.Data<String, Number> d : series.getData()) {
            Node n = d.getNode();
            Tooltip.install(n, new Tooltip(d.getYValue() + " bookings"));

            n.setOnMouseEntered(ev -> {
                ScaleTransition st = new ScaleTransition(Duration.millis(140), n);
                st.setToX(1.25);
                st.setToY(1.25);
                n.setStyle("-fx-cursor:hand; -fx-opacity:0.92;");
                st.play();
            });

            n.setOnMouseExited(ev -> {
                ScaleTransition st = new ScaleTransition(Duration.millis(140), n);
                st.setToX(1.0);
                st.setToY(1.0);
                n.setStyle("-fx-opacity:1;");
                st.play();
            });
        }

        return chart;
    }

    private VBox glassCard(String title, String desc, Node content) {

        Label t = new Label(title);
        t.setStyle("""
        -fx-font-size: 15px;
        -fx-font-weight: bold;
        -fx-text-fill: #111827;
    """);

        // make card titles (incl. "System Health Overview") a bit wider
        t.setScaleX(1.10);   // you can tweak: 1.03–1.10 depending how wide you like

        Label d = new Label(desc);
        d.setStyle("-fx-text-fill:#6b7280; -fx-font-size:11;");

        VBox wrapper = new VBox(10, t, d, content);
        wrapper.setPadding(new Insets(18));
        wrapper.setStyle(
                "-fx-background-color: rgba(255,255,255,0.55);" +
                        "-fx-background-radius: 22;" +
                        "-fx-border-radius: 22;" +
                        "-fx-border-color: rgba(255,255,255,0.35);" +
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 28, 0.25, 0, 6);" +
                        "-fx-backdrop-filter: blur(22px);"
        );
        return wrapper;
    }

    // ----------- Chart Hover Helpers ----------------

    private void applyPieChartHoverEffects(PieChart pie) {

        for (PieChart.Data d : pie.getData()) {

            Tooltip.install(d.getNode(), new Tooltip((int)d.getPieValue() + " Rooms"));

            d.getNode().setOnMouseEntered(e -> {
                ScaleTransition st = new ScaleTransition(Duration.millis(180), d.getNode());
                st.setToX(1.08);
                st.setToY(1.08);
                st.play();
            });

            d.getNode().setOnMouseExited(e -> {
                ScaleTransition st = new ScaleTransition(Duration.millis(180), d.getNode());
                st.setToX(1.0);
                st.setToY(1.0);
                st.play();
            });
        }
    }
    private VBox glassKPI(String label, int value, String icon) {

        Label ic = new Label(icon);
        ic.setStyle("-fx-font-size:24;");

        Label v = new Label(String.valueOf(value));
        v.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        v.setTextFill(Color.web("#111827"));

        Label l = new Label(label);
        l.setStyle("-fx-text-fill:#6b7280; -fx-font-size:11;");

        VBox box = new VBox(4, ic, v, l);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(16));
        box.setPrefSize(140, 100);

        box.setStyle(
                "-fx-background-color: rgba(255,255,255,0.55);" +
                        "-fx-background-radius:18;" +
                        "-fx-border-radius:18;" +
                        "-fx-border-color: rgba(255,255,255,0.35);" +
                        "-fx-border-width:1;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 22, 0.2, 0, 4);"
        );

        // Hover animation
        box.setOnMouseEntered(e -> box.setTranslateY(-3));
        box.setOnMouseExited(e -> box.setTranslateY(0));

        return box;
    }
    private VBox roomStatusBlock(Room r) {

        String st = r.getStatus() == null ? "" : r.getStatus().toUpperCase();
        Color c = st.contains("DIS") ? Color.web("#dc2626")
                : st.contains("MAINT") ? Color.web("#fbbf24")
                : Color.web("#16a34a");

        Rectangle rect = new Rectangle(40, 40, c);
        rect.setArcWidth(10);
        rect.setArcHeight(10);

        Label id = new Label(r.getRoomId());
        id.setStyle("-fx-font-size:11; -fx-text-fill:#374151;");

        VBox box = new VBox(4, rect, id);
        box.setAlignment(Pos.CENTER);
        return box;
    }
    private VBox systemHealthWidget(int load) {

        Label l1 = new Label("System Load: " + load + "%");
        Label l2 = new Label("CSV Sync: Healthy");
        Label l3 = new Label("Failed Payments: 0");
        Label l4 = new Label("Last Sync: " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")));

        for (Label l : new Label[]{l1, l2, l3, l4})
            l.setStyle("-fx-text-fill:#374151; -fx-font-size:12;");

        return new VBox(6, l1, l2, l3, l4);
    }
    private HBox feedItem(String title, String desc) {

        VBox v = new VBox(
                new Label(title),
                new Label(desc)
        );

        ((Label)v.getChildren().get(0))
                .setStyle("-fx-font-weight:600; -fx-text-fill:#111827;");
        ((Label)v.getChildren().get(1))
                .setStyle("-fx-text-fill:#6b7280; -fx-font-size:11;");

        HBox row = new HBox(10, new Label("•"), v);
        row.setAlignment(Pos.TOP_LEFT);
        return row;
    }

    private void applyBarChartHoverEffects(BarChart<String, Number> bar) {

        for (XYChart.Series<String, Number> s : bar.getData()) {
            for (XYChart.Data<String, Number> d : s.getData()) {

                Node n = d.getNode();

                Tooltip.install(n, new Tooltip(d.getYValue().intValue() + " Total Capacity"));

                n.setOnMouseEntered(e -> {
                    ScaleTransition st = new ScaleTransition(Duration.millis(180), n);
                    st.setToX(1.08);
                    st.setToY(1.08);
                    n.setStyle("-fx-opacity:0.9;");
                    st.play();
                });

                n.setOnMouseExited(e -> {
                    ScaleTransition st = new ScaleTransition(Duration.millis(180), n);
                    st.setToX(1.0);
                    st.setToY(1.0);
                    n.setStyle("-fx-opacity:1.0;");
                    st.play();
                });
            }
        }
    }

    private Label labelChartHeader(String s) {
        Label l = new Label(s);
        l.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        l.setTextFill(Color.web("#111827"));
        return l;
    }

    private Label labelChartDesc(String s) {
        Label l = new Label(s);
        l.setStyle("-fx-text-fill:#6b7280; -fx-font-size:11;");
        return l;
    }

    // ============================================================
    //  MATERIAL TEXTFIELDS (ANIMATED UNDERLINE)
    // ============================================================

    private void applyMaterialTextField(TextField tf) {

        tf.setBackground(new Background(new BackgroundFill(
                Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

        tf.setStyle(
                "-fx-font-size: 13;" +
                        "-fx-prompt-text-fill: #9ca3af;" +
                        "-fx-text-fill: #111827;" +
                        "-fx-padding: 6 4 2 4;" +
                        "-fx-background-color: transparent;" +
                        "-fx-border-color: transparent transparent #d1d5db transparent;" +
                        "-fx-border-width: 0 0 1 0;"
        );

        tf.focusedProperty().addListener((obs, old, focused) -> {

            if (focused) {
                Timeline t = new Timeline(
                        new KeyFrame(Duration.ZERO,
                                new KeyValue(tf.borderProperty(),
                                        tf.getBorder())),
                        new KeyFrame(Duration.millis(200),
                                new KeyValue(tf.styleProperty(),
                                        "-fx-font-size:13;" +
                                                "-fx-prompt-text-fill:#9ca3af;" +
                                                "-fx-text-fill:#111827;" +
                                                "-fx-padding:6 4 2 4;" +
                                                "-fx-background-color:transparent;" +
                                                "-fx-border-color: transparent transparent " + YORK_RED + " transparent;" +
                                                "-fx-border-width:0 0 2 0;"))
                );
                t.play();

            } else {
                Timeline t = new Timeline(
                        new KeyFrame(Duration.ZERO,
                                new KeyValue(tf.styleProperty(),
                                        tf.getStyle())),
                        new KeyFrame(Duration.millis(200),
                                new KeyValue(tf.styleProperty(),
                                        "-fx-font-size:13;" +
                                                "-fx-prompt-text-fill:#9ca3af;" +
                                                "-fx-text-fill:#111827;" +
                                                "-fx-padding:6 4 2 4;" +
                                                "-fx-background-color:transparent;" +
                                                "-fx-border-color: transparent transparent #d1d5db transparent;" +
                                                "-fx-border-width:0 0 1 0;"))
                );
                t.play();
            }
        });
    }

    // ============================================================
    //  MANAGE ROOMS VIEW + PILL BADGES
    // ============================================================

    private void showManageRoomsView() {

        mainContent.getChildren().clear();

        Label title = labelH1("Manage Rooms");
        Label subtitle = labelSub("Search, filter, edit, and update statuses.");

        RoomRepository repo = RoomRepository.getInstance();

        // Search bar + status filter
        TextField search = new TextField();
        search.setPromptText("Search by ID, name, location…");
        applyMaterialTextField(search);

        ComboBox<String> filter = new ComboBox<>();
        filter.getItems().addAll("All", "Active", "Disabled", "Maintenance");
        filter.setValue("All");
        filter.setPrefWidth(160);

        HBox filterRow = new HBox(12, new Label("Filters:"), search, filter);
        filterRow.setAlignment(Pos.CENTER_LEFT);

        // Table
        TableView<Room> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Room,String> idCol = new TableColumn<>("Room ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("roomId"));

        TableColumn<Room,String> nameCol = new TableColumn<>("Room Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("roomName"));

        TableColumn<Room,Integer> capCol = new TableColumn<>("Capacity");
        capCol.setCellValueFactory(c -> c.getValue().capacityProperty().asObject());

        TableColumn<Room,String> locCol = new TableColumn<>("Location");
        locCol.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Room,String> amenCol = new TableColumn<>("Amenities");
        amenCol.setCellValueFactory(new PropertyValueFactory<>("amenities"));

        TableColumn<Room,String> buildCol = new TableColumn<>("Building");
        buildCol.setCellValueFactory(new PropertyValueFactory<>("building"));

        TableColumn<Room,String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        applyStatusPillCellFactory(statusCol);

        table.getColumns().addAll(
                idCol, nameCol, capCol, locCol, amenCol, buildCol, statusCol
        );
        // Color whole row based on status (persistent)
        table.setRowFactory(tv -> {
            TableRow<Room> row = new TableRow<>() {
                @Override
                protected void updateItem(Room room, boolean empty) {
                    super.updateItem(room, empty);
                    updateRoomRowStyle(this, room, empty);
                }
            };

            // Re-apply style when hover / selection changes so we never lose the color
            row.hoverProperty().addListener((obs, oldH, nowH) -> {
                Room r = row.getItem();
                updateRoomRowStyle(row, r, row.isEmpty());
            });

            row.selectedProperty().addListener((obs, oldS, nowS) -> {
                Room r = row.getItem();
                updateRoomRowStyle(row, r, row.isEmpty());
            });

            return row;
        });



        // Row double-click => details popup
        table.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Room r = table.getSelectionModel().getSelectedItem();
                if (r != null) RoomDetailsPopup.show(r);
            }
        });

        // Filtering logic
        Runnable refresh = () -> {

            table.getItems().clear();

            String q = search.getText().toLowerCase().trim();
            String f = filter.getValue();

            for (Room r : repo.getAllRooms().values()) {

                String st = (r.getStatus() == null ? "" : r.getStatus()).toUpperCase();

                boolean matchFilter =
                        switch (f) {
                            case "Active" -> !st.contains("DIS") && !st.contains("MAINT");
                            case "Disabled" -> st.contains("DIS");
                            case "Maintenance" -> st.contains("MAINT");
                            default -> true;
                        };

                boolean matchText =
                        r.getRoomId().toLowerCase().contains(q) ||
                                r.getRoomName().toLowerCase().contains(q) ||
                                r.getLocation().toLowerCase().contains(q) ||
                                (r.getBuilding() != null && r.getBuilding().toLowerCase().contains(q)) ||
                                st.toLowerCase().contains(q);

                if (matchFilter && matchText) table.getItems().add(r);
            }
        };

        refresh.run();
        search.textProperty().addListener((o,a,b)-> refresh.run());
        filter.setOnAction(e -> refresh.run());

        // Buttons
        Button enable = pillBtn("Enable", "#16a34a");
        enable.setOnAction(e -> updateStatus(table, repo, "ENABLED"));

        Button disable = pillBtn("Disable", "#f97316");
        disable.setOnAction(e -> updateStatus(table, repo, "DISABLED"));

        Button maint = pillBtn("Maintenance", "#fbbf24");
        maint.setOnAction(e -> updateStatus(table, repo, "MAINTENANCE"));

        Button edit = pillBtn("Edit", "#2563eb");
        edit.setOnAction(e -> {
            Room r = table.getSelectionModel().getSelectedItem();
            if (r == null) { alertWarning("Select a room first."); return; }
            openEditRoomDialog(r, repo, table);
        });

        Button del = pillBtn("Delete", YORK_RED);
        del.setOnAction(e -> {
            Room r = table.getSelectionModel().getSelectedItem();
            if (r == null) { alertWarning("Select a room first."); return; }

            applyBlur();

            Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
            conf.setHeaderText(null);
            conf.setContentText("Delete room '" + r.getRoomId() + "'?");
            conf.initOwner(rootStack.getScene().getWindow());
            conf.setOnHidden(ev -> removeBlur());

            conf.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.OK) {
                    repo.deleteRoom(r.getRoomId());
                    table.getItems().remove(r);
                    alertSuccess("Room deleted.");
                }
            });
        });

        Button occ = pillBtn("View Occupancy", "#16a34a");
        occ.setOnAction(e -> {
            Room r = table.getSelectionModel().getSelectedItem();
            if (r == null) { alertWarning("Select room first."); return; }
            RoomOccupancyPopup.show(r.getRoomId());
        });

        Button details = pillBtn("View Details", "#2563eb");
        details.setOnAction(e -> {
            Room r = table.getSelectionModel().getSelectedItem();
            if (r == null) { alertWarning("Select room first."); return; }
            RoomDetailsPopup.show(r);
        });

        HBox actions = new HBox(10, enable, disable, maint, edit, del, occ, details);
        actions.setAlignment(Pos.CENTER_RIGHT);

        VBox card = formCard(filterRow, table, actions);

        mainContent.getChildren().addAll(title, subtitle, card);
        fade();
    }


    // ----------- Status Pill Cell Factory ----------------

    private void applyStatusPillCellFactory(TableColumn<Room,String> col) {

        col.setCellFactory(c -> new TableCell<>() {

            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);

                if (empty || status == null) {
                    setGraphic(null);
                    return;
                }

                String st = status.toUpperCase();
                Color bg;
                String text;

                if (st.contains("DIS")) {
                    bg = Color.web("#6b7280");
                    text = "Disabled";
                } else if (st.contains("MAINT")) {
                    bg = Color.web("#fbbf24");
                    text = "Maintenance";
                } else {
                    bg = Color.web("#16a34a");
                    text = "Active";
                }

                Label pill = new Label(text);
                pill.setTextFill(Color.BLACK);
                pill.setStyle(
                        "-fx-padding: 4 12;" +
                                "-fx-background-radius: 999;" +
                                "-fx-font-size: 11;"
                );

                pill.setBackground(new Background(new BackgroundFill(
                        bg, new CornerRadii(999), Insets.EMPTY)));

                setGraphic(pill);
                setAlignment(Pos.CENTER);
            }
        });
    }

    // Button helper
    private Button pillBtn(String text, String bg) {
        Button b = new Button(text);
        b.setStyle(
                "-fx-background-color:" + bg + ";" +
                        "-fx-text-fill:white; -fx-font-weight:600;" +
                        "-fx-background-radius:999; -fx-padding:6 14;"
        );
        return b;
    }

    private void updateStatus(TableView<Room> table, RoomRepository repo, String newStatus) {

        Room r = table.getSelectionModel().getSelectedItem();
        if (r == null) {
            alertWarning("Select a room first.");
            return;
        }

        String old = r.getStatus();
        r.setStatus(newStatus);
        repo.updateRoom(r);

        table.refresh();

        alertSuccess("Status updated: " + old + " → " + newStatus);
    }
    // ============================================================
    //  ADD ROOM VIEW
    // ============================================================

    private void showAddRoomView() {

        mainContent.getChildren().clear();

        Label title = labelH1("Add New Room");
        Label subtitle = labelSub("Provide complete details to create a room.");

        TextField idField = new TextField();
        idField.setPromptText("Room ID (e.g., R101)");
        applyMaterialTextField(idField);

        TextField nameField = new TextField();
        nameField.setPromptText("Room Name");
        applyMaterialTextField(nameField);

        TextField capField = new TextField();
        capField.setPromptText("Capacity");
        applyMaterialTextField(capField);

        TextField locField = new TextField();
        locField.setPromptText("Location");
        applyMaterialTextField(locField);

        TextField amenitiesField = new TextField();
        amenitiesField.setPromptText("Amenities (Projector;Whiteboard)");
        applyMaterialTextField(amenitiesField);

        TextField buildingField = new TextField();
        buildingField.setPromptText("Building (e.g., Lassonde Building)");
        applyMaterialTextField(buildingField);

        Button saveBtn = pillBtn("Save Room", YORK_RED);

        saveBtn.setOnAction(e -> {
            try {
                String id = idField.getText().trim();
                String nm = nameField.getText().trim();
                String capStr = capField.getText().trim();
                String loc = locField.getText().trim();
                String am = amenitiesField.getText().trim();
                String b = buildingField.getText().trim();

                if (id.isEmpty() || nm.isEmpty() || capStr.isEmpty() || loc.isEmpty()) {
                    alertWarning("Room ID, Name, Capacity, and Location are required.");
                    return;
                }

                int cap = Integer.parseInt(capStr);

                RoomRepository repo = RoomRepository.getInstance();
                if (repo.roomExists(id)) {
                    alertWarning("Room already exists.");
                    return;
                }

                Room r = new Room(id, nm, cap, loc, am, b);
                repo.addRoom(r);

                alertSuccess("Room added successfully.");

                idField.clear(); nameField.clear(); capField.clear();
                locField.clear(); amenitiesField.clear(); buildingField.clear();

            } catch (Exception ex) {
                alertError("Invalid input.");
            }
        });

        VBox card = formCard(
                idField, nameField, capField,
                locField, amenitiesField, buildingField,
                saveBtn
        );

        mainContent.getChildren().addAll(title, subtitle, card);
        fade();
    }

    // ============================================================
    //  EDIT ROOM DIALOG (with blur)
    // ============================================================
    private void updateRoomRowStyle(TableRow<Room> row,
                                    Room room,
                                    boolean empty,
                                    boolean selected) {

        if (empty || room == null) {
            row.setStyle("");
            return;
        }

        // If not selected → keep default white/striped look
        if (!selected) {
            row.setStyle("");
            return;
        }

        String status = room.getStatus() == null ? "" : room.getStatus().toUpperCase();
        String bg;

        // Match the BUTTON colours:
        // Enable  -> green  (#16a34a)
        // Disable -> orange (#f97316)
        // Maintenance -> yellow (#fbbf24)
        if (status.contains("MAINT")) {
            bg = "rgba(251,191,36,0.28)";   // yellow
        } else if (status.contains("DIS")) {
            bg = "rgba(249,115,22,0.26)";   // orange
        } else {
            bg = "rgba(22,163,74,0.22)";    // green
        }

        row.setStyle(
                "-fx-background-color: " + bg + ";"
        );
    }


    private void openEditRoomDialog(Room room, RoomRepository repo, TableView<Room> table) {

        applyBlur();

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(rootStack.getScene().getWindow());
        dialog.setTitle("Edit Room");

        Label idLabel = new Label("Room ID: " + room.getRoomId());
        idLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));

        TextField nameField = new TextField(room.getRoomName());
        TextField capField  = new TextField(String.valueOf(room.getCapacity()));
        TextField locField  = new TextField(room.getLocation());
        TextField amenField = new TextField(room.getAmenities());
        TextField buildField = new TextField(room.getBuilding());

        applyMaterialTextField(nameField);
        applyMaterialTextField(capField);
        applyMaterialTextField(locField);
        applyMaterialTextField(amenField);
        applyMaterialTextField(buildField);

        Button saveBtn = pillBtn("Save", YORK_RED);
        saveBtn.setOnAction(e -> {

            try {
                String newName = nameField.getText().trim();
                int newCap = Integer.parseInt(capField.getText().trim());
                String newLoc = locField.getText().trim();
                String newAm = amenField.getText().trim();
                String newB = buildField.getText().trim();

                room.setRoomName(newName);
                room.setCapacity(newCap);
                room.setLocation(newLoc);
                room.setAmenities(newAm);
                room.setBuilding(newB);

                repo.updateRoom(room);
                table.refresh();
                alertSuccess("Room updated.");
                dialog.close();

            } catch (Exception ex) {
                alertError("Invalid input.");
            }
        });

        Button cancel = pillBtn("Cancel", "#6b7280");
        cancel.setOnAction(e -> dialog.close());

        HBox btns = new HBox(10, saveBtn, cancel);
        btns.setAlignment(Pos.CENTER_RIGHT);

        VBox layout = new VBox(12,
                idLabel,
                new Label("Name"), nameField,
                new Label("Capacity"), capField,
                new Label("Location"), locField,
                new Label("Amenities"), amenField,
                new Label("Building"), buildField,
                btns
        );

        layout.setPadding(new Insets(20));
        layout.setStyle(
                "-fx-background-color:white; -fx-background-radius:12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 20, 0, 0, 4);"
        );

        dialog.setScene(new Scene(layout, 420, 480));

        dialog.setOnHidden(e -> removeBlur());
        dialog.show();
    }


    // ============================================================
    //  CREATE ADMIN VIEW
    // ============================================================
    private void updateRoomRowStyle(TableRow<Room> row,
                                    Room room,
                                    boolean empty) {

        if (empty || room == null) {
            row.setStyle("");
            return;
        }

        String st = room.getStatus() == null
                ? ""
                : room.getStatus().toUpperCase();

        // Default: white for normal/active rooms
        String baseColor = "white";

        // Match your button colours:
        //  - Maintenance button: #fbbf24 (yellow)
        //  - Disable button:     #f97316 (orange)
        if (st.contains("MAINT")) {
            baseColor = "rgba(251,191,36,0.25)";   // soft yellow
        } else if (st.contains("DIS")) {
            baseColor = "rgba(249,115,22,0.22)";   // soft orange
        }
        // (If you ever want active to be light green, you could add:
        // else if (st.contains("ENABL") || st.contains("ACTIVE")) baseColor = "rgba(22,163,74,0.18)";

        // Keep the same color, just give a tiny lift on hover
        String extra =
                row.isHover()
                        ? " -fx-effect: dropshadow(gaussian, rgba(15,23,42,0.12), 8, 0, 0, 1);"
                        : "";

        row.setStyle("-fx-background-color: " + baseColor + ";" + extra);
    }

    private void showCreateAdminView() {

        mainContent.getChildren().clear();

        Label title = labelH1("Create Admin");
        Label subtitle = labelSub("Add a new admin to the system.");

        TextField email = new TextField();
        email.setPromptText("Admin email");
        applyMaterialTextField(email);

        PasswordField pass = new PasswordField();
        pass.setPromptText("Password");
        pass.setStyle(
                "-fx-font-size:13;" +
                        "-fx-prompt-text-fill:#9ca3af;" +
                        "-fx-text-fill:#111827;" +
                        "-fx-padding:6 4 2 4;" +
                        "-fx-background-color:transparent;" +
                        "-fx-border-color: transparent transparent #d1d5db transparent;" +
                        "-fx-border-width:0 0 1 0;"
        );

        pass.focusedProperty().addListener((obs,o,f)->{
            if(f) pass.setStyle(
                    "-fx-font-size:13;" +
                            "-fx-text-fill:#111827;" +
                            "-fx-background-color:transparent;" +
                            "-fx-border-color:transparent transparent " + YORK_RED + " transparent;" +
                            "-fx-border-width:0 0 2 0;");
            else pass.setStyle(
                    "-fx-font-size:13;" +
                            "-fx-text-fill:#111827;" +
                            "-fx-background-color:transparent;" +
                            "-fx-border-color:transparent transparent #d1d5db transparent;" +
                            "-fx-border-width:0 0 1 0;");
        });

        Button save = pillBtn("Create Admin", YORK_RED);
        save.setOnAction(e -> {

            String em = email.getText().trim();
            String pw = pass.getText().trim();

            if (em.isEmpty() || pw.isEmpty()) {
                alertWarning("Both fields required.");
                return;
            }

            try {
                userManager.createAdminAccount(em, pw);
                alertSuccess("Admin created.");

                email.clear();
                pass.clear();

            } catch (Exception ex) {
                alertError(ex.getMessage());
            }
        });

        VBox card = formCard(email, pass, save);

        mainContent.getChildren().addAll(title, subtitle, card);
        fade();
    }


    // ============================================================
    //  MANAGE ADMINS VIEW
    // ============================================================

    private void showManageAdminsView() {

        mainContent.getChildren().clear();

        Label title = labelH1("Manage Admins");
        Label subtitle = labelSub("Enable, disable or delete admin accounts.");

        TableView<SystemUser> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<SystemUser,String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));

        TableColumn<SystemUser,String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().isActive() ? "ACTIVE" : "DISABLED"));

        table.getColumns().addAll(emailCol, statusCol);

        table.getItems().setAll(userManager.getAdminAccounts());

        table.setRowFactory(tv -> {
            TableRow<SystemUser> row = new TableRow<>() {
                @Override
                protected void updateItem(SystemUser u, boolean empty) {
                    super.updateItem(u, empty);
                    updateAdminRowStyle(this, u, empty, isSelected());
                }
            };

            // Keep style in sync when the row gets selected/unselected
            row.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                SystemUser u = row.getItem();
                updateAdminRowStyle(row, u, row.isEmpty(), isNowSelected);
            });

            return row;
        });


        Button delete = pillBtn("Delete", YORK_RED);
        delete.setOnAction(e -> {
            SystemUser u = table.getSelectionModel().getSelectedItem();
            if (u == null) { alertWarning("Select a user."); return; }

            applyBlur();

            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setHeaderText(null);
            a.setContentText("Delete '" + u.getEmail() + "'?");
            a.initOwner(rootStack.getScene().getWindow());
            a.setOnHidden(ev -> removeBlur());

            a.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.OK) {
                    userManager.deleteAdminByEmail(u.getEmail());
                    table.getItems().remove(u);
                    alertSuccess("Admin deleted.");
                }
            });
        });

        Button toggle = pillBtn("Enable / Disable", "#f59e0b");
        toggle.setOnAction(e -> {
            SystemUser u = table.getSelectionModel().getSelectedItem();
            if (u == null) { alertWarning("Select a user."); return; }

            u.setActive(!u.isActive());
            try {
                userManager.updateProfile(u, null, null);
            } catch (Exception ex) {
                alertError("Failed to update admin: " + ex.getMessage());
            }

            table.refresh();

            alertSuccess("Updated.");
        });

        HBox actions = new HBox(12, delete, toggle);
        actions.setAlignment(Pos.CENTER_RIGHT);

        VBox card = formCard(table, actions);

        mainContent.getChildren().addAll(title, subtitle, card);
        fade();
    }


    // ============================================================
    //  UTILITY HELPERS
    // ============================================================

    private void fade() {
        FadeTransition ft = new FadeTransition(Duration.millis(200), mainContent);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    private Label labelH1(String text) {
        Label l = new Label(text);
        l.setWrapText(true);
        l.setMaxWidth(Double.MAX_VALUE);
        l.setStyle("""
        -fx-font-size: 24px;
        -fx-font-weight: bold;
        -fx-padding: 4 0 8 0;
        -fx-letter-spacing: 1.5px;    /* ADD THIS for horizontal spacing */
    """);
        return l;
    }

    private Label labelSub(String text) {
        Label l = new Label(text);
        l.setWrapText(true);
        l.setMaxWidth(Double.MAX_VALUE);
        l.setStyle("""
        -fx-font-size: 13px;
        -fx-text-fill: #6b7280;
        -fx-padding: 0 0 24 0;
        -fx-letter-spacing: 0.8px;    /* ADD THIS for horizontal spacing */
    """);
        return l;
    }





    // MAIN and ONLY formCard method — handles all cases cleanly
    private VBox formCard(Node... children) {
        VBox c = new VBox(15, children);
        c.setPadding(new Insets(22));

        String base =
                "-fx-background-color:rgba(255,255,255,0.98);" +
                        "-fx-background-radius:18;" +
                        "-fx-border-color:rgba(209,213,219,0.9);" +
                        "-fx-border-width:0.6;" +
                        "-fx-effect:dropshadow(gaussian, rgba(15,23,42,0.08),18,0,0,4);";

        String hover =
                "-fx-background-color:#ffffff;" +
                        "-fx-background-radius:18;" +
                        "-fx-border-color:" + YORK_RED + ";" +
                        "-fx-border-width:0.9;" +
                        "-fx-effect:dropshadow(gaussian, rgba(15,23,42,0.18),24,0,0,7);";

        c.setStyle(base);

        c.setOnMouseEntered(e -> {
            c.setStyle(hover);
            c.setTranslateY(-2);
        });

        c.setOnMouseExited(e -> {
            c.setStyle(base);
            c.setTranslateY(0);
        });

        return c;
    }
    private void updateAdminRowStyle(TableRow<SystemUser> row,
                                     SystemUser u,
                                     boolean empty,
                                     boolean selected) {

        if (empty || u == null) {
            row.setStyle("");
            return;
        }

        if (selected) {
            // Selected row – soft York-red tinted highlight
            row.setStyle(
                    "-fx-background-color: rgba(186,12,47,0.12);"
            );
        } else if (!u.isActive()) {
            // Disabled admin – keep your grey stripe
            row.setStyle(
                    "-fx-background-color: rgba(148,163,184,0.32);"
            );
        } else {
            // Active admin – transparent background
            row.setStyle(
                    "-fx-background-color: transparent;"
            );
        }
    }



    // ============================================================
    //  MAIN
    // ============================================================

    public static void main(String[] args) {
        launch();
    }
}