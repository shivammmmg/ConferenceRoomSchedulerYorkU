package shared.model;

/**
 * Represents a conference room
 */
public class Room {
    private String roomId;
    private String roomName;
    private int capacity;
    private String location;
    private String amenities; // "Projector,Whiteboard,VideoConference"
    private String building;  // Added for building filtering

    public Room(String roomId, String roomName, int capacity, String location, String amenities, String building) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.capacity = capacity;
        this.location = location;
        this.amenities = amenities;
        this.building = building;
    }

    // Getters
    public String getRoomId() { return roomId; }
    public String getRoomName() { return roomName; }
    public int getCapacity() { return capacity; }
    public String getLocation() { return location; }
    public String getAmenities() { return amenities; }
    public String getBuilding() { return building; } // New getter

    public String getAmenitiesIcon() {
        if (amenities.contains("Projector") && amenities.contains("VideoConference")) {
            return "📊📹";
        } else if (amenities.contains("Projector")) {
            return "📊";
        } else if (amenities.contains("VideoConference")) {
            return "📹";
        } else {
            return "📋";
        }
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%d,%s,%s,%s",
                roomId, roomName, capacity, location, amenities, building);
    }
}
