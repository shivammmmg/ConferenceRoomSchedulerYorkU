package shared.model;

import javafx.beans.property.*;

/**
 * Room – Unified Model for Scenarios 2, 3, and 4
 * ============================================================================
 * <p>This class represents a single conference room in the system and serves as
 * the unified data model used by all scenarios:</p>
 *
 * <h2>Supported Scenarios</h2>
 * <ul>
 *     <li><b>Scenario 2</b> – Room listing, booking validation, amenities display</li>
 *     <li><b>Scenario 3</b> – Live room state (AVAILABLE / IN_USE / NO_SHOW)</li>
 *     <li><b>Scenario 4</b> – Admin editing, enabling/disabling, maintenance mode</li>
 * </ul>
 *
 * <h2>Design Features</h2>
 * <ul>
 *     <li><b>Core Fields</b> (roomId, name, capacity, location, amenities, building)</li>
 *     <li><b>Scenario 3 State</b> using {@link RoomStatus}
 *         (AVAILABLE, IN_USE, MAINTENANCE, DISABLED, etc.)</li>
 *     <li><b>JavaFX Properties</b> for Scenario 4 table bindings
 *         (roomNameProperty, capacityProperty, statusProperty, etc.)</li>
 *     <li><b>Multiple Constructors</b> used across scenarios for flexible room creation</li>
 * </ul>
 *
 * <h2>Why This Class Was Unified</h2>
 * <p>Earlier versions had mismatched constructors and inconsistent status loading.
 * The current version:</p>
 * <ul>
 *     <li>Ensures all fields persist correctly to CSV</li>
 *     <li>Supports JavaFX TableView binding seamlessly</li>
 *     <li>Provides backward compatibility for Scenario 2 and Scenario 4 code</li>
 *     <li>Correctly translates CSV text (e.g., “MAINT”, “DISABLED”) into RoomStatus enum</li>
 * </ul>
 *
 * <h2>CSV Integration</h2>
 * <p>The {@code toString()} format matches the rooms.csv schema used by
 * {@link shared.util.CSVHelper}, ensuring consistent save/load behaviour.</p>
 *
 * ============================================================================
 */

public class Room {

    // ============================================================
    // Primary data fields (written to CSV)
    // ============================================================
    private String roomId;
    private String roomName;
    private int capacity;
    private String location;
    private String amenities;
    private String building;

    // Scenario 3 fields
    private RoomStatus status = RoomStatus.AVAILABLE;
    private String currentBookingId = null;

    // ============================================================
    // JavaFX properties (Scenario 4)
    // ============================================================
    private final StringProperty roomIdProperty = new SimpleStringProperty();
    private final StringProperty roomNameProperty = new SimpleStringProperty();
    private final IntegerProperty capacityProperty = new SimpleIntegerProperty();
    private final StringProperty locationProperty = new SimpleStringProperty();
    private final StringProperty amenitiesProperty = new SimpleStringProperty();
    private final StringProperty buildingProperty = new SimpleStringProperty();
    private final StringProperty statusProperty = new SimpleStringProperty("AVAILABLE");

    // ============================================================
    // Constructors
    // ============================================================

    /**
     * FIXED — this was wrong before (you were assigning wrong variables).
     * Now it correctly assigns the passed parameters to the fields.
     */
    public Room(String id,
                String name,
                int capacity,
                String location,
                String amenities,
                String building,
                String statusString)
    {
        this.roomId = id;
        this.roomName = name;
        this.capacity = capacity;
        this.location = location;
        this.amenities = amenities;
        this.building = building;

        // Load correct status into enum
        setStatus(statusString);

        syncToProperties();
    }

    // NEW: Restore old 6-argument constructor used by Scenario 2 + 4
    public Room(String id,
                String name,
                int capacity,
                String location,
                String amenities,
                String building)
    {
        this(id, name, capacity, location, amenities, building, "AVAILABLE");
    }


    // Minimal constructors still allowed for other scenarios
    public Room(String id, String name) {
        this.roomId = id;
        this.roomName = name;
        syncToProperties();
    }

    public Room(String roomId, int capacity, String location) {
        this.roomId = roomId;
        this.capacity = capacity;
        this.location = location;
        syncToProperties();
    }

    // ============================================================
    // Sync core fields → JavaFX properties
    // ============================================================
    private void syncToProperties() {
        roomIdProperty.set(roomId);
        roomNameProperty.set(roomName);
        capacityProperty.set(capacity);
        locationProperty.set(location);
        amenitiesProperty.set(amenities);
        buildingProperty.set(building);
        statusProperty.set(status.name());
    }

    // ============================================================
    // Basic Getters
    // ============================================================
    public String getRoomId() { return roomId; }
    public String getRoomName() { return roomName; }
    public int getCapacity() { return capacity; }
    public String getLocation() { return location; }
    public String getAmenities() { return amenities; }
    public String getBuilding() { return building; }

    // Scenario 3 compatibility
    public synchronized String getId() { return roomId; }
    public synchronized String getName() { return roomName; }

    // Emoji icon (Scenario 2)
    public String getAmenitiesIcon() {
        if (amenities == null) return "";
        if (amenities.contains("Projector") && amenities.contains("VideoConference")) return "📊📹";
        if (amenities.contains("Projector")) return "📊";
        if (amenities.contains("VideoConference")) return "📹";
        return "📋";
    }

    // ============================================================
    // STATUS — FIXED AND MADE CONSISTENT
    // ============================================================
    public synchronized String getStatus() { return status.name(); }
    public synchronized RoomStatus getStatusEnum() { return status; }

    public synchronized void setStatus(RoomStatus status) {
        this.status = status;
        statusProperty.set(status.name());
    }

    /**
     * FIXED — this correctly interprets CSV text status.
     */
    public synchronized void setStatus(String statusString) {
        if (statusString == null) {
            this.status = RoomStatus.AVAILABLE;
        } else {
            switch (statusString.toUpperCase()) {

                case "ENABLED":
                case "ACTIVE":
                case "AVAILABLE":
                    this.status = RoomStatus.AVAILABLE;
                    break;

                case "DISABLED":
                case "INACTIVE":
                    this.status = RoomStatus.DISABLED;
                    break;

                case "MAINTENANCE":
                case "MAINT":
                    this.status = RoomStatus.MAINTENANCE;
                    break;

                case "OCCUPIED":
                    this.status = RoomStatus.OCCUPIED;
                    break;

                default:
                    this.status = RoomStatus.AVAILABLE;
            }
        }

        statusProperty.set(this.status.name());
    }

    public synchronized String getCurrentBookingId() { return currentBookingId; }
    public synchronized void setCurrentBookingId(String bid) { this.currentBookingId = bid; }

    // ============================================================
    // Setters (sync both field & JavaFX prop)
    // ============================================================
    public void setRoomName(String name) {
        this.roomName = name;
        roomNameProperty.set(name);
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
        capacityProperty.set(capacity);
    }

    public void setLocation(String location) {
        this.location = location;
        locationProperty.set(location);
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
        amenitiesProperty.set(amenities);
    }

    public void setBuilding(String building) {
        this.building = building;
        buildingProperty.set(building);
    }

    // ============================================================
    // JavaFX property getters
    // ============================================================
    public StringProperty roomIdProperty() { return roomIdProperty; }
    public StringProperty roomNameProperty() { return roomNameProperty; }
    public IntegerProperty capacityProperty() { return capacityProperty; }
    public StringProperty locationProperty() { return locationProperty; }
    public StringProperty amenitiesProperty() { return amenitiesProperty; }
    public StringProperty buildingProperty() { return buildingProperty; }
    public StringProperty statusProperty() { return statusProperty; }

    // ============================================================
    // CSV FORMAT
    // ============================================================
    @Override
    public String toString() {
        return String.format(
                "%s,%s,%d,%s,%s,%s,%s",
                roomId,
                roomName != null ? roomName : "",
                capacity,
                location != null ? location : "",
                amenities != null ? amenities : "",
                building != null ? building : "",
                status.name()
        );
    }
}