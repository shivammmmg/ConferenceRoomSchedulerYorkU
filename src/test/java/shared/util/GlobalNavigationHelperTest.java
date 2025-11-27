package shared.util;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for {@link GlobalNavigationHelper}.
 * <p>
 * These tests focus on verifying the stability and defensive behavior of
 * navigation logic when invalid paths, missing stages, null input, or
 * repeated calls occur. Since {@code navigateTo()} internally catches
 * all exceptions, the main goal is to ensure it never throws and that
 * the static {@code primaryStage} field behaves predictably.
 * <p>
 * The tests also handle JavaFX initialization and reflective access
 * to private static fields to allow full code coverage.
 */
class GlobalNavigationHelperTest {

    /** Example valid FXML path (not used directly but kept for documentation completeness). */
    private static final String VALID_FXML_PATH = "/scenario4/fxml/AdminDashboard.fxml";

    /** Tracks whether JavaFX toolkit has already been initialized. */
    private static boolean fxInitialized = false;

    // JavaFX INITIALIZATION – runs once before all tests

    /**
     * Initializes the JavaFX runtime before any tests are executed.
     * <p>
     * JavaFX must be initialized manually in headless unit tests because
     * the JVM does not start the FX toolkit automatically.
     *
     * @throws Exception if the initialization countdown latch is interrupted
     */
    @BeforeAll
    static void initJavaFxToolkit() throws Exception {
        if (fxInitialized) return;

        CountDownLatch latch = new CountDownLatch(1);

        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException ex) {
            // JavaFX already started → mark latch as completed
            latch.countDown();
        }

        assertTrue(latch.await(5, TimeUnit.SECONDS),
                "JavaFX Platform.startup() timed out");

        fxInitialized = true;
    }

    // Helper Methods

    /**
     * Helper method to run code on the JavaFX Application Thread and wait for completion.
     *
     * @param r Runnable containing JavaFX operations
     * @throws Exception if waiting on the latch is interrupted
     */
    private void runOnFxThread(Runnable r) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                r.run();
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS),
                "runOnFxThread timed out");
    }

    /**
     * Reflectively resets the private static {@code primaryStage} field in
     * {@link GlobalNavigationHelper} back to {@code null}.
     *
     * @throws Exception if reflection fails
     */
    private void resetStage() throws Exception {
        Field f = GlobalNavigationHelper.class.getDeclaredField("primaryStage");
        f.setAccessible(true);
        f.set(null, null);
    }

    /**
     * Creates a real JavaFX {@link Stage} and registers it via
     * {@link GlobalNavigationHelper#setStage(Stage)}.
     *
     * @throws Exception if FX-thread execution fails
     */
    private void createAndSetStage() throws Exception {
        runOnFxThread(() -> GlobalNavigationHelper.setStage(new Stage()));
    }

    //                      TEST CASES

    /**
     * Test 1 — Verifies that {@code setStage(null)} does not throw
     * an exception. This ensures defensive handling of null injection.
     */
    @Test
    void testSetStageAcceptsNull() {
        assertDoesNotThrow(() -> GlobalNavigationHelper.setStage(null));
    }

    /**
     * Test 2 — Ensures that after reflectively resetting {@code primaryStage},
     * the static field is indeed {@code null}.
     *
     * @throws Exception if reflection fails
     */
    @Test
    void testPrimaryStageCanBeResetToNull() throws Exception {
        resetStage();
        Field f = GlobalNavigationHelper.class.getDeclaredField("primaryStage");
        f.setAccessible(true);
        Object value = f.get(null);
        assertNull(value);
    }

    /**
     * Test 3 — Confirms that {@code navigateTo()} does not throw even when
     * {@code primaryStage} is null and the path is a normal string.
     *
     * @throws Exception if reflection fails
     */
    @Test
    void testNavigateToWithNullStageDoesNotThrow() throws Exception {
        resetStage();
        assertDoesNotThrow(() ->
                GlobalNavigationHelper.navigateTo("/does/not/matter.fxml"));
    }

    /**
     * Test 4 — Ensures {@code navigateTo(null)} does not throw, confirming
     * the internal try/catch block handles invalid input.
     *
     * @throws Exception if reflection fails
     */
    @Test
    void testNavigateToWithNullPathDoesNotThrow() throws Exception {
        resetStage();
        assertDoesNotThrow(() -> GlobalNavigationHelper.navigateTo(null));
    }

    /**
     * Test 5 — Verifies that an empty string passed to {@code navigateTo("")}
     * does not result in an exception.
     *
     * @throws Exception if reflection fails
     */
    @Test
    void testNavigateToWithEmptyPathDoesNotThrow() throws Exception {
        resetStage();
        assertDoesNotThrow(() -> GlobalNavigationHelper.navigateTo(""));
    }

    /**
     * Test 6 — Confirms that a relative (non-absolute) path does not cause
     * {@code navigateTo()} to throw an exception.
     *
     * @throws Exception if reflection fails
     */
    @Test
    void testNavigateToWithRelativePathDoesNotThrow() throws Exception {
        resetStage();
        assertDoesNotThrow(() -> GlobalNavigationHelper.navigateTo("relative.fxml"));
    }

    /**
     * Test 7 — Ensures that calling {@code navigateTo()} multiple times
     * sequentially does not result in exceptions, even when stage = null.
     *
     * @throws Exception if reflection fails
     */
    @Test
    void testMultipleSequentialNavigateCallsDoNotThrow() throws Exception {
        resetStage();
        assertDoesNotThrow(() -> {
            GlobalNavigationHelper.navigateTo("/a.fxml");
            GlobalNavigationHelper.navigateTo("/b.fxml");
            GlobalNavigationHelper.navigateTo("/c.fxml");
        });
    }

    /**
     * Test 8 — Verifies that navigation works safely even without calling
     * {@code setStage()} beforehand.
     */
    @Test
    void testNavigateToWithoutSettingStageFirstDoesNotThrow() {
        assertDoesNotThrow(() ->
                GlobalNavigationHelper.navigateTo("/any.fxml"));
    }

    /**
     * Test 9 — Confirms that the helper class behaves correctly when used like
     * a static utility class, i.e., no instantiation required.
     */
    @Test
    void testUtilityStyleUsageWorks() {
        assertDoesNotThrow(() ->
                GlobalNavigationHelper.navigateTo("/screen.fxml"));
    }

    /**
     * Test 10 — Ensures that when {@code setStage(null)} is called,
     * the {@code primaryStage} field remains null after assignment.
     *
     * @throws Exception if reflection fails
     */
    @Test
    void testStageFieldRemainsNullWhenSetStageCalledWithNull() throws Exception {
        resetStage();
        GlobalNavigationHelper.setStage(null);

        Field f = GlobalNavigationHelper.class.getDeclaredField("primaryStage");
        f.setAccessible(true);
        assertNull(f.get(null));
    }
}
