package shared.model;

/**
 * RoomStatus – Unified Room State Enumeration (All Scenarios)
 * ============================================================================
 * <p>This enum represents every possible state a room can occupy across
 * Scenarios 2, 3, and 4 of the Conference Room Scheduler.</p>
 *
 * <h2>Purpose</h2>
 * <ul>
 *     <li>Standardizes room status naming across the entire system.</li>
 *     <li>Used by RoomStatusManager (Scenario 3) for real-time occupancy logic.</li>
 *     <li>Used by Admin Dashboard (Scenario 4) to enable/disable rooms.</li>
 *     <li>Supports CSV persistence and UI color/status mapping.</li>
 * </ul>
 *
 * <h2>Status Descriptions</h2>
 * <ul>
 *     <li><b>AVAILABLE</b> – Room is free to book.</li>
 *     <li><b>RESERVED</b> – A booking exists but the user has not checked in yet.</li>
 *     <li><b>IN_USE</b> – User has checked in; room is actively in use.</li>
 *     <li><b>OCCUPIED</b> – Admin-facing synonym of IN_USE (Scenario 4 styling).</li>
 *     <li><b>ENABLED</b> – Admin has explicitly enabled the room.</li>
 *     <li><b>DISABLED</b> – Admin disabled the room; cannot be booked.</li>
 *     <li><b>MAINTENANCE</b> – Room temporarily unavailable due to maintenance.</li>
 *     <li><b>NO_SHOW</b> – Booking window passed without check-in (Scenario 3).</li>
 *     <li><b>UNKNOWN</b> – Fallback when status cannot be determined.</li>
 * </ul>
 *
 * <h2>Used In</h2>
 * <ul>
 *     <li><b>Scenario 2</b> – Room selection + validation.</li>
 *     <li><b>Scenario 3</b> – Real-time check-in / no-show / occupancy tracking.</li>
 *     <li><b>Scenario 4</b> – Admin dashboard room toggling (ENABLED/DISABLED).</li>
 * </ul>
 *
 * <h2>Persistence</h2>
 * <p>Saved and loaded via {@link shared.util.CSVHelper}. Appears in rooms.csv.</p>
 * ============================================================================
 */

public enum RoomStatus {
    AVAILABLE,      // free to book
    RESERVED,       // booked but not yet in use
    IN_USE,         // currently being used
    OCCUPIED,       // same as IN_USE (Scenario 4 naming)
    ENABLED,        // Scenario 4 status
    DISABLED,       // Scenario 4 status
    MAINTENANCE,    // not available
    NO_SHOW,        // booking was missed
    UNKNOWN         // fallback
}

