package shared.observer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ObserverTest — Unit tests for the {@link Observer} interface.
 * ------------------------------------------------------------------
 * <p>This test suite verifies the behavior of objects implementing
 * the Observer design pattern used throughout the system.</p>
 *
 * <h2>Goals</h2>
 * <ul>
 *     <li>Ensure Observer.update() is invoked correctly</li>
 *     <li>Validate internal state changes after update calls</li>
 *     <li>Test independence between multiple Observer instances</li>
 *     <li>Verify polymorphic usage through the Observer interface</li>
 *     <li>Confirm observers can be reused/reset successfully</li>
 *     <li>Achieve full coverage of the update behavior</li>
 * </ul>
 */
class ObserverTest {

    /**
     * A simple test implementation of {@link Observer}, used for
     * verifying update behavior without external dependencies.
     */
    static class TestObserver implements Observer {
        boolean updated = false;
        int updateCount = 0;

        @Override
        public void update() {
            updated = true;
            updateCount++;
        }

        /** Resets testing flags to simulate fresh state. */
        void reset() {
            updated = false;
            updateCount = 0;
        }
    }

    /**
     * Test 1 — When a new observer is created, its update state
     * should be false and count should be zero.
     */
    @Test
    void testInitialStateNotUpdated() {
        TestObserver obs = new TestObserver();
        assertFalse(obs.updated);
        assertEquals(0, obs.updateCount);
    }

    /**
     * Test 2 — A single update call should set the updated flag to
     * true and increment the update count to 1.
     */
    @Test
    void testSingleUpdateSetsFlagAndCountOne() {
        TestObserver obs = new TestObserver();
        obs.update();
        assertTrue(obs.updated);
        assertEquals(1, obs.updateCount);
    }

    /**
     * Test 3 — Multiple update calls should increment the counter
     * each time.
     */
    @Test
    void testMultipleUpdatesIncreaseCount() {
        TestObserver obs = new TestObserver();
        obs.update();
        obs.update();
        obs.update();
        assertEquals(3, obs.updateCount);
    }

    /**
     * Test 4 — Two observers should maintain independent state:
     * updating one must not update the other.
     */
    @Test
    void testTwoObserversIndependentFlags() {
        TestObserver a = new TestObserver();
        TestObserver b = new TestObserver();

        a.update();
        assertTrue(a.updated);
        assertFalse(b.updated);
    }

    /**
     * Test 5 — Two observers must track update counts independently.
     */
    @Test
    void testTwoObserversIndependentCounts() {
        TestObserver a = new TestObserver();
        TestObserver b = new TestObserver();

        a.update();
        a.update();
        b.update();

        assertEquals(2, a.updateCount);
        assertEquals(1, b.updateCount);
    }

    /**
     * Test 6 — Calling update() polymorphically through the
     * Observer interface must trigger the correct implementation.
     */
    @Test
    void testPolymorphicCallThroughObserverInterface() {
        Observer obs = new TestObserver();
        obs.update();

        assertTrue(((TestObserver) obs).updated);
        assertEquals(1, ((TestObserver) obs).updateCount);
    }

    /**
     * Test 7 — Updating each observer in a collection individually
     * should result in each being updated once.
     */
    @Test
    void testAllObserversInCollectionAreUpdated() {
        TestObserver a = new TestObserver();
        TestObserver b = new TestObserver();
        TestObserver c = new TestObserver();

        Observer[] list = {a, b, c};
        for (Observer o : list) {
            o.update();
        }

        assertEquals(1, a.updateCount);
        assertEquals(1, b.updateCount);
        assertEquals(1, c.updateCount);
    }

    /**
     * Test 8 — A reset observer should return to default state and
     * behave correctly when updated again.
     */
    @Test
    void testObserverCanBeReusedAfterReset() {
        TestObserver obs = new TestObserver();

        obs.update();
        obs.update();
        obs.reset();

        assertFalse(obs.updated);
        assertEquals(0, obs.updateCount);

        obs.update();
        assertEquals(1, obs.updateCount);
    }

    /**
     * Test 9 — Many update calls (e.g., looped) must yield the
     * correct count without error.
     */
    @Test
    void testManyUpdatesProduceCorrectCount() {
        TestObserver obs = new TestObserver();

        for (int i = 0; i < 10; i++) obs.update();

        assertEquals(10, obs.updateCount);
    }

    /**
     * Test 10 — If update() is never called, the observer should
     * remain in its default state.
     */
    @Test
    void testNoUpdateKeepsDefaultState() {
        TestObserver obs = new TestObserver();

        assertFalse(obs.updated);
        assertEquals(0, obs.updateCount);
    }
}
