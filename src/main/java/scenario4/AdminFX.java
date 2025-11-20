package scenario4;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import scenario1.controller.UserManager;
import shared.model.SystemUser;
import shared.model.Room;
import shared.model.RoomRepository;
import shared.util.CSVHelper;
import scenario4.components.RoomDetailsPopup;
import scenario4.components.RoomOccupancyPopup;

public class AdminFX extends Application {

    private VBox mainContent;

    private Button dashboardBtn;
    private Button addRoomBtn;
    private Button manageRoomsBtn;
    private Button createAdminBtn;
    private Button manageAdminsBtn;
    private Button logoutBtn;

    // single source of truth for users/admins
    private final UserManager userManager = UserManager.getInstance();

    private static final String NAV_BASE =
            "-fx-background-radius: 999;" +
                    "-fx-padding: 10 16;" +
                    "-fx-font-size: 13;" +
                    "-fx-font-weight: 600;" +
                    "-fx-text-fill: #f8fafc;";

    private static final String NAV_ACTIVE   = "-fx-background-color: rgba(255,255,255,0.25);";
    private static final String NAV_INACTIVE = "-fx-background-color: transparent;";
    private static final String NAV_HOVER    = "-fx-background-color: rgba(255,255,255,0.15);";

    @Override
    public void start(Stage stage) {

        // 1️⃣ Load rooms from CSV before building UI
        try {
            var rooms = CSVHelper.loadRooms("data/rooms.csv");
            RoomRepository repo = RoomRepository.getInstance();
            for (Room r : rooms) {
                repo.addRoom(r);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("[AdminFX] Failed to load data/rooms.csv");
        }

        // 2️⃣ Build UI
        VBox left = buildLeftPanel();
        mainContent = new VBox();
        mainContent.setPadding(new Insets(30));
        mainContent.setSpacing(20);

        ScrollPane sc = new ScrollPane(mainContent);
        sc.setFitToWidth(true);
        sc.setFitToHeight(true);
        sc.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        BorderPane rootPane = new BorderPane();
        rootPane.setLeft(left);
        rootPane.setCenter(sc);

        Scene scene = new Scene(rootPane, 1200, 720);
        stage.setScene(scene);
        stage.setTitle("Scenario 4 — Admin Control Panel");

        showDashboardView();
        setActiveNav(dashboardBtn);

        stage.show();

        stage.setOnCloseRequest(e -> {
            RoomRepository.getInstance().saveToCSV();
        });
    }

    // LEFT PANEL
    private VBox buildLeftPanel() {
        VBox left = new VBox(20);
        left.setPadding(new Insets(25));
        left.setPrefWidth(250);
        left.setStyle("-fx-background-color: #8b0000;");

        Label title = new Label("Admin Control Panel");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("Room & User Management");
        subtitle.setTextFill(Color.rgb(255, 255, 255, 0.8));

        VBox header = new VBox(5, title, subtitle);

        dashboardBtn    = createNavButton("🏠  Dashboard");
        addRoomBtn      = createNavButton("➕  Add Room");
        manageRoomsBtn  = createNavButton("🛠  Manage Rooms");
        createAdminBtn  = createNavButton("👤  Create Admin");
        manageAdminsBtn = createNavButton("👥  Manage Admins");
        logoutBtn       = createNavButton("🚪  Logout");

        dashboardBtn.setOnAction(e -> { setActiveNav(dashboardBtn);     showDashboardView(); });
        addRoomBtn.setOnAction(e -> { setActiveNav(addRoomBtn);         showAddRoomView(); });
        manageRoomsBtn.setOnAction(e -> { setActiveNav(manageRoomsBtn); showManageRoomsView(); });
        createAdminBtn.setOnAction(e -> { setActiveNav(createAdminBtn); showCreateAdminView(); });
        manageAdminsBtn.setOnAction(e -> { setActiveNav(manageAdminsBtn); showManageAdminsView(); });
        logoutBtn.setOnAction(e -> ((Stage) logoutBtn.getScene().getWindow()).close());

        VBox navBox = new VBox(8,
                dashboardBtn, addRoomBtn, manageRoomsBtn,
                createAdminBtn, manageAdminsBtn, logoutBtn
        );

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        left.getChildren().addAll(header, navBox, spacer);
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
        return Boolean.TRUE.equals(b.getProperties().get("active"));
    }

    private void setActiveNav(Button active) {
        for (Button b : new Button[]{
                dashboardBtn, addRoomBtn, manageRoomsBtn,
                createAdminBtn, manageAdminsBtn, logoutBtn
        }) {
            if (b == null) continue;
            boolean activeBtn = (b == active);
            b.getProperties().put("active", activeBtn);
            b.setStyle(NAV_BASE + (activeBtn ? NAV_ACTIVE : NAV_INACTIVE));
        }
    }

    private void fade() {
        FadeTransition ft = new FadeTransition(Duration.millis(200), mainContent);
        mainContent.setOpacity(0);
        ft.setToValue(1);
        ft.play();
    }

    // -----------------------------------------
    // DASHBOARD
    // -----------------------------------------
    private void showDashboardView() {
        mainContent.getChildren().clear();

        Label title    = labelH1("Admin Dashboard");
        Label subtitle = labelSub("System overview.");

        RoomRepository roomRepo = RoomRepository.getInstance();

        Map<String, Room> rooms = roomRepo.getAllRooms();
        int totalRooms    = rooms.size();
        int activeRooms   = 0;
        int disabledRooms = 0;
        int maintenanceRooms = 0;

        for (Room r : rooms.values()) {
            String status = r.getStatus() == null ? "" : r.getStatus().toUpperCase();
            if (status.contains("MAINT")) maintenanceRooms++;
            else if (status.contains("DIS")) disabledRooms++;
            else activeRooms++;
        }

        // admins directly from UserManager / user.csv
        int totalAdmins = userManager.getAdminAccounts().size();

        HBox row1 = new HBox(20,
                statCard("Total Rooms", String.valueOf(totalRooms), "Configured rooms in the system."),
                statCard("Active Rooms", String.valueOf(activeRooms), "Currently available for booking."),
                statCard("Maintenance", String.valueOf(maintenanceRooms), "Rooms under maintenance.")
        );

        HBox row2 = new HBox(20,
                statCard("Disabled Rooms", String.valueOf(disabledRooms), "Rooms disabled by admin."),
                statCard("Admins", String.valueOf(totalAdmins), "Admin accounts in the system.")
        );

        VBox content = new VBox(20, title, subtitle, row1, row2);
        mainContent.getChildren().setAll(content);
        fade();
    }

    // -----------------------------------------
    // ADD ROOM
    // -----------------------------------------
    private void showAddRoomView() {
        mainContent.getChildren().clear();

        Label title    = labelH1("Add New Room");
        Label subtitle = labelSub("Create a fully detailed room for bookings.");

        TextField idField  = new TextField();
        idField.setPromptText("Enter room ID (e.g., R101)");

        TextField nameField = new TextField();
        nameField.setPromptText("Enter room name (e.g., York Room)");

        TextField capField = new TextField();
        capField.setPromptText("Enter room capacity");

        TextField locField = new TextField();
        locField.setPromptText("Enter room location (e.g., First Floor)");

        TextField amenitiesField = new TextField();
        amenitiesField.setPromptText("Enter amenities (e.g., Projector;Whiteboard)");

        TextField buildingField = new TextField();
        buildingField.setPromptText("Enter building name (e.g., Lassonde Building)");

        Button saveBtn = new Button("Save Room");
        saveBtn.setOnAction(e -> {
            try {
                String id   = idField.getText().trim();
                String name = nameField.getText().trim();
                String capStr = capField.getText().trim();
                String loc  = locField.getText().trim();
                String amenities = amenitiesField.getText().trim();
                String building  = buildingField.getText().trim();

                if (id.isEmpty() || name.isEmpty() || capStr.isEmpty() || loc.isEmpty()) {
                    alertWarning("Room ID, Room Name, Capacity, and Location are required.");
                    return;
                }

                int cap = Integer.parseInt(capStr);

                RoomRepository repo = RoomRepository.getInstance();
                if (repo.roomExists(id)) {
                    alertWarning("Room already exists!");
                    return;
                }

                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String ts = LocalDateTime.now().format(fmt);

                System.out.println("----- Room Add Log -----");
                System.out.println("[Time] " + ts);
                System.out.println("[RoomAdd] Room ID: " + id);
                System.out.println("[RoomAdd] Name: \"" + name + "\"");
                System.out.println("[RoomAdd] Capacity: " + cap);
                System.out.println("[RoomAdd] Location: \"" + loc + "\"");
                System.out.println("[RoomAdd] Amenities: \"" + amenities + "\"");
                System.out.println("[RoomAdd] Building: \"" + building + "\"");
                System.out.println("-----------------------------------------");

                Room room = new Room(id, name, cap, loc, amenities, building);

                repo.addRoom(room);      // auto-save
                alertSuccess("Room added successfully!");

                idField.clear();
                nameField.clear();
                capField.clear();
                locField.clear();
                amenitiesField.clear();
                buildingField.clear();

            } catch (Exception ex) {
                alertError("Invalid input.");
            }
        });

        VBox card = formCard(
                idField,
                nameField,
                capField,
                locField,
                amenitiesField,
                buildingField,
                saveBtn
        );

        mainContent.getChildren().addAll(title, subtitle, card);
        fade();
    }

    // -----------------------------------------
    // MANAGE ROOMS
    // -----------------------------------------
    private void showManageRoomsView() {
        mainContent.getChildren().clear();

        Label title    = labelH1("Manage Rooms");
        Label subtitle = labelSub("View, search, edit, enable/disable, and maintenance.");

        RoomRepository repo = RoomRepository.getInstance();

        TextField searchField = new TextField();
        searchField.setPromptText("Search by ID, name, location, building, or status...");

        TableView<Room> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Room, String> idCol = new TableColumn<>("Room ID");
        idCol.setCellValueFactory(c -> c.getValue().roomIdProperty());

        TableColumn<Room, String> nameCol = new TableColumn<>("Room Name");
        nameCol.setCellValueFactory(c -> c.getValue().roomNameProperty());

        TableColumn<Room, Integer> capCol = new TableColumn<>("Capacity");
        capCol.setCellValueFactory(c -> c.getValue().capacityProperty().asObject());

        TableColumn<Room, String> locCol = new TableColumn<>("Location");
        locCol.setCellValueFactory(c -> c.getValue().locationProperty());

        TableColumn<Room, String> amenCol = new TableColumn<>("Amenities");
        amenCol.setCellValueFactory(c -> c.getValue().amenitiesProperty());

        TableColumn<Room, String> buildingCol = new TableColumn<>("Building");
        buildingCol.setCellValueFactory(c -> c.getValue().buildingProperty());

        TableColumn<Room, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(c -> c.getValue().statusProperty());

        table.getColumns().addAll(
                idCol, nameCol, capCol, locCol, amenCol, buildingCol, statusCol
        );

        table.getItems().setAll(repo.getAllRooms().values());

        searchField.textProperty().addListener((obs, old, newText) -> {
            String q = newText.toLowerCase().trim();
            table.getItems().clear();
            for (Room r : repo.getAllRooms().values()) {
                if (r.getRoomId().toLowerCase().contains(q)
                        || (r.getRoomName() != null && r.getRoomName().toLowerCase().contains(q))
                        || r.getLocation().toLowerCase().contains(q)
                        || (r.getBuilding() != null && r.getBuilding().toLowerCase().contains(q))
                        || r.getStatus().toLowerCase().contains(q)) {
                    table.getItems().add(r);
                }
            }
        });

        Button enableBtn = new Button("Enable");
        enableBtn.setOnAction(e -> updateRoomStatusFromTable(table, repo, "ENABLED"));

        Button disableBtn = new Button("Disable");
        disableBtn.setOnAction(e -> updateRoomStatusFromTable(table, repo, "DISABLED"));

        Button maintenanceBtn = new Button("Maintenance");
        maintenanceBtn.setOnAction(e -> updateRoomStatusFromTable(table, repo, "MAINTENANCE"));

        Button editBtn = new Button("Edit");
        editBtn.setOnAction(e -> {
            Room selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                alertWarning("Select a room first.");
                return;
            }
            openEditRoomDialog(selected, repo, table);
        });

        Button deleteBtn = new Button("Delete");
        deleteBtn.setOnAction(e -> {
            Room selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                alertWarning("Select a room first.");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setHeaderText(null);
            confirm.setContentText("Delete room '" + selected.getRoomId() + "'?");
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.OK) {

                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String ts = LocalDateTime.now().format(fmt);

                    System.out.println("----- Room Delete Log -----");
                    System.out.println("[Time] " + ts);
                    System.out.println("[RoomDelete] Room ID: " + selected.getRoomId());
                    System.out.println("[RoomDelete] Name: \"" + selected.getRoomName() + "\"");
                    System.out.println("[RoomDelete] Capacity: " + selected.getCapacity());
                    System.out.println("[RoomDelete] Location: \"" + selected.getLocation() + "\"");
                    System.out.println("[RoomDelete] Amenities: \"" + selected.getAmenities() + "\"");
                    System.out.println("[RoomDelete] Building: \"" + selected.getBuilding() + "\"");
                    System.out.println("-----------------------------------------");

                    repo.deleteRoom(selected.getRoomId());
                    table.getItems().remove(selected);
                }
            });
        });

        Button occupancyBtn = new Button("View Occupancy");
        occupancyBtn.setStyle("-fx-background-color: #059669; -fx-text-fill: white;");
        occupancyBtn.setOnAction(e -> {
            Room selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                alertWarning("Select a room first.");
                return;
            }
            RoomOccupancyPopup.show(selected.getRoomId());
        });

        Button detailsBtn = new Button("View Details");
        detailsBtn.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white;");
        detailsBtn.setOnAction(e -> {
            Room selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                alertWarning("Select a room first.");
                return;
            }
            RoomDetailsPopup.show(selected);
        });

        HBox actions = new HBox(10,
                enableBtn, disableBtn, maintenanceBtn,
                editBtn, deleteBtn,
                occupancyBtn,
                detailsBtn
        );

        VBox card = formCard(searchField, table, actions);

        mainContent.getChildren().addAll(title, subtitle, card);
        fade();
    }

    private void updateRoomStatusFromTable(TableView<Room> table, RoomRepository repo, String status) {
        Room selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alertWarning("Please select a room.");
            return;
        }
        selected.setStatus(status);
        repo.updateRoom(selected);
        table.refresh();
    }

    private void openEditRoomDialog(Room room, RoomRepository repo, TableView<Room> table) {
        Stage dialog = new Stage();
        dialog.initOwner(mainContent.getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Edit Room - " + room.getRoomId());

        Label idLabel = new Label("Room ID: " + room.getRoomId());
        idLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        TextField nameField = new TextField(room.getRoomName());
        TextField capField  = new TextField(String.valueOf(room.getCapacity()));
        TextField locField  = new TextField(room.getLocation());
        TextField amenField = new TextField(room.getAmenities());
        TextField buildField = new TextField(room.getBuilding());

        Label nameLabel = new Label("Room Name:");
        Label capLabel  = new Label("Capacity:");
        Label locLabel  = new Label("Location:");
        Label amenLabel = new Label("Amenities:");
        Label buildLabel = new Label("Building:");

        Button saveBtn = new Button("Save");
        saveBtn.setStyle("-fx-background-color: #BA0C2F; -fx-text-fill: white;");
        saveBtn.setOnAction(e -> {
            try {
                String newName = nameField.getText().trim();
                int newCap = Integer.parseInt(capField.getText().trim());
                String newLoc = locField.getText().trim();
                String newAmen = amenField.getText().trim();
                String newBuild = buildField.getText().trim();

                String oldName = room.getRoomName();
                int oldCap     = room.getCapacity();
                String oldLoc  = room.getLocation();
                String oldAmen = room.getAmenities();
                String oldBuild= room.getBuilding();

                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String ts = LocalDateTime.now().format(fmt);

                System.out.println("----- Room Update Log (" + room.getRoomId() + ") -----");
                System.out.println("[Time] " + ts);

                if (!oldName.equals(newName))
                    System.out.println("[RoomUpdate] Name: \"" + oldName + "\" → \"" + newName + "\"");
                if (oldCap != newCap)
                    System.out.println("[RoomUpdate] Capacity: " + oldCap + " → " + newCap);
                if (!oldLoc.equals(newLoc))
                    System.out.println("[RoomUpdate] Location: \"" + oldLoc + "\" → \"" + newLoc + "\"");
                if (oldAmen != null && !oldAmen.equals(newAmen))
                    System.out.println("[RoomUpdate] Amenities: \"" + oldAmen + "\" → \"" + newAmen + "\"");
                if (oldBuild != null && !oldBuild.equals(newBuild))
                    System.out.println("[RoomUpdate] Building: \"" + oldBuild + "\" → \"" + newBuild + "\"");

                System.out.println("----------------------------------------------");

                room.setRoomName(newName);
                room.setCapacity(newCap);
                room.setLocation(newLoc);
                room.setAmenities(newAmen);
                room.setBuilding(newBuild);

                repo.updateRoom(room);
                table.refresh();
                dialog.close();

            } catch (Exception ex) {
                alertError("Invalid input.");
            }
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> dialog.close());

        HBox btnRow = new HBox(10, saveBtn, cancelBtn);
        btnRow.setAlignment(Pos.CENTER_RIGHT);

        VBox layout = new VBox(12,
                idLabel,
                nameLabel, nameField,
                capLabel, capField,
                locLabel, locField,
                amenLabel, amenField,
                buildLabel, buildField,
                btnRow
        );
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color:white; -fx-background-radius:12;");

        Scene scene = new Scene(layout, 400, 420);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    // -----------------------------------------
    // CREATE ADMIN  (now uses UserManager + user.csv)
    // -----------------------------------------
    private void showCreateAdminView() {
        mainContent.getChildren().clear();

        Label title    = labelH1("Create Admin");
        Label subtitle = labelSub("Add new admin accounts (saved in user.csv).");

        TextField email = new TextField();
        email.setPromptText("Enter admin email");

        PasswordField pass = new PasswordField();
        pass.setPromptText("Enter admin password");

        Button save = new Button("Create Admin");
        save.setOnAction(e -> {
            String em = email.getText().trim();
            String pw = pass.getText().trim();

            if (em.isEmpty() || pw.isEmpty()) {
                alertWarning("All fields are required.");
                return;
            }

            try {
                userManager.createAdminAccount(em, pw);
                alertSuccess("Admin created successfully!");
                email.clear();
                pass.clear();
            } catch (Exception ex) {
                alertWarning(ex.getMessage());
            }
        });

        VBox card = formCard(email, pass, save);

        mainContent.getChildren().addAll(title, subtitle, card);
        fade();
    }

    // -----------------------------------------
    // MANAGE ADMINS  (UserManager + SystemUser)
    // -----------------------------------------
    private void showManageAdminsView() {
        mainContent.getChildren().clear();

        Label title    = labelH1("Manage Admins");
        Label subtitle = labelSub("List of admin accounts (loaded from user.csv).");

        TableView<SystemUser> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<SystemUser, String> nameCol = new TableColumn<>("Username");
        nameCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getEmail()));

        TableColumn<SystemUser, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().isActive() ? "ACTIVE" : "DISABLED"));

        table.getColumns().addAll(nameCol, statusCol);

        // Load admins every time we open this view
        table.getItems().setAll(userManager.getAdminAccounts());

        Button deleteBtn = new Button("Delete");
        deleteBtn.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> {
            SystemUser selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                alertWarning("Please select an admin.");
                return;
            }

            boolean removed = userManager.deleteAdminByEmail(selected.getEmail());
            if (removed) {
                table.getItems().remove(selected);
                alertSuccess("Admin deleted.");
            } else {
                alertWarning("Failed to delete admin.");
            }
        });

        Button toggleBtn = new Button("Disable / Enable");
        toggleBtn.setStyle("-fx-background-color: #f0ad4e; -fx-text-fill: white;");
        toggleBtn.setOnAction(e -> {
            SystemUser selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                alertWarning("Please select an admin.");
                return;
            }

            // flip active flag
            if (selected.isActive()) {
                selected.deactivate();
            } else {
                selected.activate();
            }

            try {
                // persist new active flag to user.csv
                userManager.updateProfile(selected, null, null);
                table.refresh();
            } catch (Exception ex) {
                alertError("Failed to update admin status: " + ex.getMessage());
            }
        });

        HBox actions = new HBox(12, deleteBtn, toggleBtn);
        actions.setAlignment(Pos.CENTER);

        VBox card = formCard(table, actions);

        mainContent.getChildren().addAll(title, subtitle, card);
        fade();
    }

    // -----------------------------------------
    // HELPERS
    // -----------------------------------------
    private Label labelH1(String t) {
        Label l = new Label(t);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        l.setTextFill(Color.web("#111827"));
        return l;
    }

    private Label labelSub(String t) {
        Label l = new Label(t);
        l.setStyle("-fx-text-fill: #555;");
        return l;
    }

    private VBox formCard(Node... children) {
        VBox card = new VBox(15, children);
        card.setPadding(new Insets(20));
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 18, 0, 0, 4);"
        );
        return card;
    }

    private VBox statCard(String title, String value, String description) {
        Label t = new Label(title);
        t.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        t.setTextFill(Color.web("#6b7280"));

        Label v = new Label(value);
        v.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        v.setTextFill(Color.web("#111827"));

        Label d = new Label(description);
        d.setWrapText(true);
        d.setStyle("-fx-text-fill: #4b5563;");

        VBox box = new VBox(4, t, v, d);
        box.setPadding(new Insets(16));
        box.setPrefWidth(220);
        box.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 18, 0, 0, 4);"
        );
        return box;
    }

    private void alertWarning(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);
        a.setTitle("Warning");
        a.setContentText(msg);
        a.showAndWait();
    }

    private void alertError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setTitle("Error");
        a.setContentText(msg);
        a.showAndWait();
    }

    private void alertSuccess(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setTitle("Success");
        a.setContentText(msg);
        a.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }

    // old reflection helpers (no longer used, but kept for safety)
    private void roomNameSetter(Room r, String name) {
        try {
            var f = Room.class.getDeclaredField("roomName");
            f.setAccessible(true);
            f.set(r, name);
        } catch (Exception ignored) {}
    }

    private void roomAmenitiesSetter(Room r, String am) {
        try {
            var f = Room.class.getDeclaredField("amenities");
            f.setAccessible(true);
            f.set(r, am);
        } catch (Exception ignored) {}
    }

    private void roomBuildingSetter(Room r, String b) {
        try {
            var f = Room.class.getDeclaredField("building");
            f.setAccessible(true);
            f.set(r, b);
        } catch (Exception ignored) {}
    }
}
