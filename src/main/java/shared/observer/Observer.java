package shared.observer;

/**
 * Observer – Observer Design Pattern (Shared Across Scenarios)
 * ------------------------------------------------------------------------
 * <p>This interface represents the <b>Observer</b> component in the classic
 * Observer Design Pattern. Any class that wants to receive notifications
 * about changes in room status, booking updates, or sensor events must
 * implement this interface.</p>
 *
 * <h2>Purpose</h2>
 * <ul>
 *     <li>Defines a single callback method: {@code update()}.</li>
 *     <li>Enables UI screens (e.g., BookingFX) to auto-refresh without polling.</li>
 *     <li>Allows Scenario 3’s {@code RoomStatusManager} (Subject) to push changes
 *         to multiple observers in real time.</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li><b>Observer Pattern</b>: This is the observer side.</li>
 *     <li>Used together with {@code Subject} interface and
 *         {@code RoomStatusManager} (Concrete Subject).</li>
 *     <li>Promotes loose coupling between UI and backend logic.</li>
 * </ul>
 *
 * <h2>Where Used</h2>
 * <ul>
 *     <li><b>Scenario 3</b> – MyBookings auto-refresh, sensor updates.</li>
 *     <li><b>Scenario 2</b> – BookingFX observes changes to booking status.</li>
 *     <li><b>Scenario 4</b> – Admin dashboard can also observe updates if needed.</li>
 * </ul>
 */

public interface Observer {
    void update();
}
