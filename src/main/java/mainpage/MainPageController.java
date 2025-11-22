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

    /* Smooth Scroll Animation */
    public void smoothScrollTo(double target) {
        if (mainScroll == null) return;

        Timeline timeline = new Timeline();
        javafx.animation.KeyValue kv = new javafx.animation.KeyValue(mainScroll.vvalueProperty(), target);
        javafx.animation.KeyFrame kf = new javafx.animation.KeyFrame(Duration.millis(450), kv);

        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    /* Fade In */
    private void fadeIn(Node node) {
        node.setOpacity(0);

        FadeTransition ft = new FadeTransition(Duration.millis(500), node);
        ft.setToValue(1);
        ft.play();
    }

    @FXML
    private void onGetStarted() {
        scenario1.controller.UserManager.getInstance();
        GlobalNavigationHelper.navigateTo("/scenario1/fxml/login.fxml");
    }

    /* Load room capacities from rooms.csv */
    private void loadCapacityFromCSV() {
        Room r202 = RoomRepository.getInstance().getById("R202");
        Room r301 = RoomRepository.getInstance().getById("R301");
        Room r302 = RoomRepository.getInstance().getById("R302");

        if (r202 != null) cap202.setText("Capacity: " + r202.getCapacity());
        if (r301 != null) cap301.setText("Capacity: " + r301.getCapacity());
        if (r302 != null) cap302.setText("Capacity: " + r302.getCapacity());
    }

    /* Auto-refresh every 30 seconds */
    private void startLiveRoomStatusUpdater() {
        liveStatusUpdater = new Timeline(
                new KeyFrame(Duration.ZERO, e -> refreshRoomStatusCards()),
                new KeyFrame(Duration.seconds(30))
        );

        liveStatusUpdater.setCycleCount(Timeline.INDEFINITE);
        liveStatusUpdater.play();
    }

    /* Reads status from rooms.csv */
    private void refreshRoomStatusCards() {
        updateRoomCard(status202, "R202");
        updateRoomCard(status301, "R301");
        updateRoomCard(status302, "R302");
    }

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
