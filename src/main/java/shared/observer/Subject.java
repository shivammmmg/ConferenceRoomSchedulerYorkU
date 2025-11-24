package shared.observer;

/**
 * Subject – Observer Design Pattern (Shared Across All Scenarios)
 * ------------------------------------------------------------------------
 * <p>This interface defines the <b>Subject</b> side of the Observer Pattern.
 * Any class that represents a change-producing component—such as
 * {@code RoomStatusManager}—must implement this interface so observers can
 * subscribe and react to updates.</p>
 *
 * <h2>Purpose</h2>
 * <ul>
 *     <li>Allows observers (UI controllers, dashboards) to register themselves.</li>
 *     <li>Enables automatic notification when system state changes.</li>
 *     <li>Decouples backend logic from UI refresh logic.</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li><b>Subject Interface</b>: Core part of the Observer Pattern.</li>
 *     <li>Implemented by {@code RoomStatusManager} in Scenario 3.</li>
 *     <li>Works together with {@link Observer} to provide push-based updates.</li>
 * </ul>
 *
 * <h2>Where Used</h2>
 * <ul>
 *     <li><b>Scenario 3</b> – Live check-in, no-show detection, occupancy updates.</li>
 *     <li><b>Scenario 2</b> – Booking screens refresh automatically through observers.</li>
 *     <li><b>Scenario 4</b> – Admin UI may attach observers to room status changes.</li>
 * </ul>
 */

public interface Subject {
    void attach(Observer o);
    void detach(Observer o);
    void notifyObservers();
}
