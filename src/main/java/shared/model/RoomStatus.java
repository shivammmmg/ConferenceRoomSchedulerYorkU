package shared.model;

/**
 * ENUM: RoomStatus
 * Possible states for a room.
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

