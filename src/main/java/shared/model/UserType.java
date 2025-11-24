package shared.model;

/**
 * UserType – System Roles Enumeration (All Scenarios)
 * ------------------------------------------------------------------------
 * <p>This enum defines every user role supported in the Conference Room
 * Scheduler system. These values appear across multiple scenarios and are
 * referenced during registration, authentication, booking, and admin
 * workflows.</p>
 *
 * <h2>Purpose</h2>
 * <ul>
 *     <li>Standardizes role-based logic across the entire project.</li>
 *     <li>Determines UI routing (Scenario 1 → Scenario 2/4).</li>
 *     <li>Drives permissions for booking, check-in, and admin operations.</li>
 * </ul>
 *
 * <h2>Scenario Integration</h2>
 * <ul>
 *     <li><b>Scenario 1</b> – Registration & Login (role chosen/validated here)</li>
 *     <li><b>Scenario 2</b> – Booking features depend on user type</li>
 *     <li><b>Scenario 3</b> – Check-in restrictions based on booking owner</li>
 *     <li><b>Scenario 4</b> – Admin roles (ADMIN, CHIEF_EVENT_COORDINATOR)</li>
 * </ul>
 *
 * <h2>Notes</h2>
 * <ul>
 *     <li>Matches the roles specified in D1 and class diagrams.</li>
 *     <li>Used extensively by {@code UserManager}, LoginController, and AdminFX.</li>
 * </ul>
 */

public enum UserType {
    STUDENT,
    FACULTY,
    STAFF,
    PARTNER,
    ADMIN,
    CHIEF_EVENT_COORDINATOR
}
