package mainpage;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import shared.util.GlobalNavigationHelper;


import java.util.Objects;

public class MainPageController {

    @FXML private ScrollPane mainScroll;
    @FXML private ImageView yorkLogo;

    @FXML
    public void initialize() {
        loadLogo();
        fadeIn(mainScroll.getParent());
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
        GlobalNavigationHelper.navigateTo("/scenario1/fxml/login.fxml");
    }

}
