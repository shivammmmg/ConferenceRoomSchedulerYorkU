package shared.observer;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * SubjectTest — Unit tests for the {@link Subject} interface implementation.
 * ------------------------------------------------------------------------
 * <p>This test suite validates the behavior of a minimal Subject implementation
 * used for Observer Pattern functionality across the system.</p>
 *
 * <h2>Key Objectives</h2>
 * <ul>
 *     <li>Verify observer attachment and detachment behavior</li>
 *     <li>Ensure notification correctly triggers Observer.update()</li>
 *     <li>Validate handling of null observers</li>
 *     <li>Ensure stability with duplicate observers</li>
 *     <li>Confirm proper behavior with empty observer lists</li>
 *     <li>Provide full test coverage for Subject-like behavior</li>
 * </ul>
 */
class SubjectTest {

    /**
     * Test implementation of {@link Observer}, used to count update calls.
     */
    static class TestObserver implements Observer {
        int updateCount = 0;

        @Override
        public void update() {
            updateCount++;
        }
    }

    /**
     * Minimal concrete implementation of {@link Subject} used for testing.
     * Stores observers in a list and notifies them when required.
     */
    static class TestSubject implements Subject {

        private final List<Observer> observers = new ArrayList<>();

        @Override
        public void attach(Observer o) {
            observers.add(o);
        }

        @Override
        public void detach(Observer o) {
            observers.remove(o);
        }

        @Override
        public void notifyObservers() {
            for (Observer o : observers) {
                if (o != null) o.update();
            }
        }

        /**
         * Utility method for verifying number of observers stored.
         *
         * @return count of attached observers
         */
        int getObserverCount() {
            return observers.size();
        }
    }

    /**
     * Test 1 — Verifies that attaching a single observer increases count to 1.
     */
    @Test
    void testAttachSingleObserver() {
        TestSubject subject = new TestSubject();
        subject.attach(new TestObserver());
        assertEquals(1, subject.getObserverCount());
    }

    /**
     * Test 2 — Attaching multiple observers should correctly increment count.
     */
    @Test
    void testAttachMultipleObservers() {
        TestSubject subject = new TestSubject();
        subject.attach(new TestObserver());
        subject.attach(new TestObserver());
        subject.attach(new TestObserver());
        assertEquals(3, subject.getObserverCount());
    }

    /**
     * Test 3 — Detaching an existing observer should reduce the count.
     */
    @Test
    void testDetachExistingObserver() {
        TestSubject subject = new TestSubject();
        TestObserver obs = new TestObserver();

        subject.attach(obs);
        subject.attach(new TestObserver());

        subject.detach(obs);
        assertEquals(1, subject.getObserverCount());
    }

    /**
     * Test 4 — Detaching an observer not stored should not modify the list.
     */
    @Test
    void testDetachNonExistingObserverDoesNothing() {
        TestSubject subject = new TestSubject();
        TestObserver existing = new TestObserver();
        TestObserver notAttached = new TestObserver();

        subject.attach(existing);
        subject.detach(notAttached);

        assertEquals(1, subject.getObserverCount());
    }

    /**
     * Test 5 — notifyObservers() should trigger update() once for a single observer.
     */
    @Test
    void testNotifySingleObserver() {
        TestSubject subject = new TestSubject();
        TestObserver observer = new TestObserver();

        subject.attach(observer);
        subject.notifyObservers();

        assertEquals(1, observer.updateCount);
    }

    /**
     * Test 6 — All attached observers must receive update notifications.
     */
    @Test
    void testNotifyMultipleObservers() {
        TestSubject subject = new TestSubject();

        TestObserver a = new TestObserver();
        TestObserver b = new TestObserver();

        subject.attach(a);
        subject.attach(b);

        subject.notifyObservers();

        assertEquals(1, a.updateCount);
        assertEquals(1, b.updateCount);
    }

    /**
     * Test 7 — Calling notifyObservers() with no observers must not throw any exception.
     */
    @Test
    void testNotifyWithNoObserversDoesNotThrow() {
        TestSubject subject = new TestSubject();
        assertDoesNotThrow(subject::notifyObservers);
    }

    /**
     * Test 8 — If the same observer is attached twice, it must receive two updates.
     */
    @Test
    void testDuplicateObserverGetsTwoUpdates() {
        TestSubject subject = new TestSubject();
        TestObserver obs = new TestObserver();

        subject.attach(obs);
        subject.attach(obs); // duplicate entry

        subject.notifyObservers();
        assertEquals(2, obs.updateCount);
    }

    /**
     * Test 9 — Multiple notifications should accumulate update counts.
     */
    @Test
    void testMultipleNotifyAccumulates() {
        TestSubject subject = new TestSubject();
        TestObserver obs = new TestObserver();

        subject.attach(obs);
        subject.notifyObservers();
        subject.notifyObservers();

        assertEquals(2, obs.updateCount);
    }

    /**
     * Test 10 — Attaching a null observer should not throw, and notifyObservers should skip nulls gracefully.
     */
    @Test
    void testAttachNullObserverDoesNotThrow() {
        TestSubject subject = new TestSubject();
        assertDoesNotThrow(() -> subject.attach(null));
        assertDoesNotThrow(subject::notifyObservers);
    }

    /**
     * Test 11 — Detaching a null observer must not throw or change state.
     */
    @Test
    void testDetachNullObserverDoesNotThrow() {
        TestSubject subject = new TestSubject();
        subject.attach(new TestObserver());

        assertDoesNotThrow(() -> subject.detach(null));
        assertEquals(1, subject.getObserverCount());
    }
}
