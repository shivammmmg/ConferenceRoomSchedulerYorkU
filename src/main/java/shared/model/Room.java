package shared.model;

import javafx.beans.property.*;

/**
 * Unified Room class combining Scenario 2, 3, and 4.
 *
 * Supports:
 *  - Scenario 2: roomId, roomName, capacity, location, amenities, building, amenitiesIcon
 *  - Scenario 3: RoomStatus enum + currentBookingId + synchronized logic
 *  - Scenario 4: JavaFX properties and String-based status updates
 *
 * Completely backward-compatible with all scenario code.
 */
public class Room {

    // ============================================================
    // Scenario 2 Fields
    // ============================================================
    private String roomId;
    private String roomName;        // Used in Scenario 2 and 3
    private int capacity;
    private String location;
    private String amenities;
    private String building;

    // ============================================================
    // Scenario 3 Fields (enum logic)
    // ============================================================
    private RoomStatus status = RoomStatus.AVAILABLE;
    private String currentBookingId = null;

    // ============================================================
    // Scenario 4 JavaFX Properties
    // ============================================================
    private final StringProperty roomIdProperty = new SimpleStringProperty();
    private final IntegerProperty capacityProperty = new SimpleIntegerProperty();
    private final StringProperty locationProperty = new SimpleStringProperty();
    private final StringProperty statusProperty = new SimpleStringProperty("Active");

    // ============================================================
    // Constructors for all scenarios
    // ============================================================

    // Scenario 2 constructor
    public Room(String roomId, String roomName, int capacity,
                String location, String amenities, String building) {

        this.roomId = roomId;
        this.roomName = roomName;
        this.capacity = capacity;
        this.location = location;
        this.amenities = amenities;
        this.building = building;

        // Sync with JavaFX
        roomIdProperty.set(roomId);
        capacityProperty.set(capacity);
        locationProperty.set(location);
        statusProperty.set("Active");
    }

    // Scenario 3 constructor
    public Room(String id, String name) {
        this.roomId = id;
        this.roomName = name;

        roomIdProperty.set(id);
        locationProperty.set("");
        statusProperty.set("Active");
    }

    // Scenario 4 simple constructor
    public Room(String roomId, int capacity, String location) {
        this.roomId = roomId;
        this.capacity = capacity;
        this.location = location;

        roomIdProperty.set(roomId);
        capacityProperty.set(capacity);
        locationProperty.set(location);
        statusProperty.set("Active");
    }

    // ============================================================
    // Scenario 2 Getters
    // ============================================================

    public String getRoomId() { return roomId; }
    public String getRoomName() { return roomName; }
    public int getCapacity() { return capacity; }
    public String getLocation() { return location; }
    public String getAmenities() { return amenities; }
    public String getBuilding() { return building; }

    public String getAmenitiesIcon() {
        if (amenities == null) return "";
        if (amenities.contains("Projector") && amenities.contains("VideoConference")) return "📊📹";
        if (amenities.contains("Projector")) return "📊";
        if (amenities.contains("VideoConference")) return "📹";
        return "📋";
    }

    // ============================================================
    // Scenario 3 Logic (enum-based status)
    // ============================================================

    /** Scenario 4's GUI expects a STRING from getStatus() */
    public synchronized String getStatus() {
        return status == null ? "" : status.name();
    }

    /** Scenario 3 requires access to the ENUM */
    public synchronized RoomStatus getStatusEnum() {
        return status;
    }

    /** Scenario 3 sets the enum directly */
    public synchronized void setStatus(RoomStatus status) {
        this.status = status;
        this.statusProperty.set(status.name());
    }

    /** Scenario 3 uses these for booking assignment */
    public synchronized String getCurrentBookingId() { return currentBookingId; }
    public synchronized void setCurrentBookingId(String bid) { this.currentBookingId = bid; }

    // Scenario 3 compatibility
    public synchronized String getId() { return roomId; }
    public synchronized String getName() { return roomName; }

    // ============================================================
    // Scenario 4 Logic (String-based status setter)
    // ============================================================
    /**
     * Accepts a String (ENABLED, DISABLED, etc.)
     * and maps it to the correct RoomStatus enum.
     */
    public synchronized void setStatus(String statusString) {
        if (statusString == null) return;

        switch (statusString.toUpperCase()) {

            case "ENABLED":
            case "ACTIVE":
                this.status = RoomStatus.AVAILABLE;
                break;

            case "DISABLED":
            case "INACTIVE":
                this.status = RoomStatus.MAINTENANCE;
                break;

            case "OCCUPIED":
                this.status = RoomStatus.OCCUPIED;
                break;

            default:
                // fallback
                this.status = RoomStatus.AVAILABLE;
        }

        statusProperty.set(this.status.name());
    }

    // ============================================================
    // JavaFX Properties (Scenario 4)
    // ============================================================

    public StringProperty roomIdProperty() { return roomIdProperty; }
    public IntegerProperty capacityProperty() { return capacityProperty; }
    public StringProperty locationProperty() { return locationProperty; }
    public StringProperty statusProperty() { return statusProperty; }

    // ============================================================
    // toString (Scenario 2)
    // ============================================================
    @Override
    public String toString() {
        return String.format("%s,%s,%d,%s,%s,%s",
                roomId,
                roomName != null ? roomName : "",
                capacity,
                location != null ? location : "",
                amenities != null ? amenities : "",
                building != null ? building : ""
        );
    }
}
