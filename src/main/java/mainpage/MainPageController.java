package mainpage;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import shared.model.Room;
import shared.model.RoomRepository;
import shared.util.GlobalNavigationHelper;

import java.util.Objects;

/**
 * Controller for the Main Landing Page of the Conference Room Scheduler.
 *
 * <p>This screen is part of the entry workflow shown before Scenario 1 (Registration
 * & Login). It displays room preview cards, live room availability, and acts as the
 * gateway to the Login page.</p>
 *
 * <h2>Relevant Scenarios</h2>
 * • Scenario 1 – Registration & Account Management
 * • Scenario 4 – Admin & Room Management (room status / capacity pulled from CSV)
 *
 * <h2>Key Responsibilities</h2>
 * <ul>
 *   <li>Loads York University branding and background elements.</li>
 *   <li>Smooth scroll + fade-in animation for a polished UI experience.</li>
 *   <li>Loads room capacities from rooms.csv via {@link RoomRepository} (Singleton).</li>
 *   <li>Auto-refreshes room availability every 30 seconds.</li>
 *   <li>Routes user to Login page when "Get Started" is clicked.</li>
 * </ul>
 *
 * <h2>Design Patterns Used</h2>
 * <ul>
 *   <li><b>Singleton</b>: RoomRepository is used to fetch live room data.</li>
 *   <li><b>Facade</b>: GlobalNavigationHelper abstracts navigation logic.</li>
 * </ul>
 *
 * @author Your Name
 */

public class MainPageController {

    @FXML private ScrollPane mainScroll;
    @FXML private ImageView yorkLogo;

    // LIVE ROOM STATUS LABELS
    @FXML private Label status202;
    @FXML private Label status301;
    @FXML private Label status302;

    @FXML private Label cap202;
    @FXML private Label cap301;
    @FXML private Label cap302;

    private Timeline liveStatusUpdater;


    /**
     * Initializes the main landing page.
     *
     * <p>Runs immediately after the FXML loads. Handles UI setup,
     * animations, room capacity loading, and starting the live room
     * status refresh scheduler.</p>
     *
     * <h2>Related Scenario</h2>
     * Scenario 1 — Entry point before Login.
     */


    /**
     * Loads the York University logo displayed at the top-left corner.
     *
     * <p>Reads the logo image from the resources folder and displays it
     * inside an ImageView component.</p>
     */

    @FXML
    public void initialize() {
        loadLogo();
        fadeIn(mainScroll.getParent());

        loadCapacityFromCSV();  // NEW
        startLiveRoomStatusUpdater();
    }

    /* Load top-left YorkU logo */
    private void loadLogo() {
        yorkLogo.setImage(new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/mainpage/images/yorku_logo.png")
                )
        ));
    }

    /**
     * Smoothly scrolls the main page to a particular vertical position.
     *
     * @param target the destination scroll value (0.0 to 1.0)
     *
     * <p>Used for animated transitions between sections of the main page.</p>
     */

    public void smoothScrollTo(double target) {
        if (mainScroll == null) return;

        Timeline timeline = new Timeline();
        javafx.animation.KeyValue kv = new javafx.animation.KeyValue(mainScroll.vvalueProperty(), target);
        javafx.animation.KeyFrame kf = new javafx.animation.KeyFrame(Duration.millis(450), kv);

        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    /**
     * Applies a fade-in animation to the given UI node.
     *
     * @param node the UI component to fade in
     *
     * <p>Creates a polished visual transition when the page loads.</p>
     */

    private void fadeIn(Node node) {
        node.setOpacity(0);

        FadeTransition ft = new FadeTransition(Duration.millis(500), node);
        ft.setToValue(1);
        ft.play();
    }

    /**
     * Handles the “Get Started” button.
     *
     * <p>Initializes the UserManager singleton and navigates the user
     * to the Login screen (Scenario 1).</p>
     */

    @FXML
    private void onGetStarted() {
        scenario1.controller.UserManager.getInstance();
        GlobalNavigationHelper.navigateTo("/scenario1/fxml/login.fxml");
    }

    /**
     * Loads room capacities from the rooms.csv file.
     *
     * <p>Uses RoomRepository (Singleton) to fetch data for preview rooms
     * displayed on the main landing page.</p>
     */

    private void loadCapacityFromCSV() {
        Room r202 = RoomRepository.getInstance().getById("R202");
        Room r301 = RoomRepository.getInstance().getById("R301");
        Room r302 = RoomRepository.getInstance().getById("R302");

        if (r202 != null) cap202.setText("Capacity: " + r202.getCapacity());
        if (r301 != null) cap301.setText("Capacity: " + r301.getCapacity());
        if (r302 != null) cap302.setText("Capacity: " + r302.getCapacity());
    }

    /**
     * Starts a timeline that refreshes room availability every 30 seconds.
     *
     * <p>This simulates real-time dashboard behavior by continuously
     * reading room status from the CSV repository.</p>
     */

    private void startLiveRoomStatusUpdater() {
        liveStatusUpdater = new Timeline(
                new KeyFrame(Duration.ZERO, e -> refreshRoomStatusCards()),
                new KeyFrame(Duration.seconds(30))
        );

        liveStatusUpdater.setCycleCount(Timeline.INDEFINITE);
        liveStatusUpdater.play();
    }

    /**
     * Updates the preview cards for all featured rooms.
     *
     * <p>Reads the status from rooms.csv and updates the UI labels.</p>
     */

    private void refreshRoomStatusCards() {
        updateRoomCard(status202, "R202");
        updateRoomCard(status301, "R301");
        updateRoomCard(status302, "R302");
    }

    /**
     * Updates the room status label for a specific room.
     *
     * @param label  the UI label to update
     * @param roomId the ID of the room (e.g., "R202")
     *
     * <p>Applies the correct status text and CSS class based on the room’s state:
     * AVAILABLE, DISABLED, or MAINTENANCE.</p>
     */

    private void updateRoomCard(Label label, String roomId) {

        Room room = RoomRepository.getInstance().getById(roomId);

        if (room == null) {
            label.setText("Unavailable");
            return;
        }

        // remove old styles
        label.getStyleClass().removeAll(
                "room-status-available",
                "room-status-busy",
                "room-status-maint"
        );

        String status = room.getStatus();

        switch (status) {

            case "AVAILABLE" -> {
                label.setText("Available");
                label.getStyleClass().add("room-status-available");
            }

            case "DISABLED" -> {
                label.setText("Disabled");
                label.getStyleClass().add("room-status-busy"); // RED
            }

            case "MAINTENANCE" -> {
                label.setText("Maintenance");
                label.getStyleClass().add("room-status-maint"); // YELLOW
            }

            default -> {
                label.setText(status);
            }
        }
    }
}
