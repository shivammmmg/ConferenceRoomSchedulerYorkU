package shared.model;

import java.util.HashMap;
import java.util.Map;

public class RoomRepository {

    private static RoomRepository instance;

    private final Map<String, Room> rooms = new HashMap<>();

    private RoomRepository() {}

    public static RoomRepository getInstance() {
        if (instance == null) {
            instance = new RoomRepository();
        }
        return instance;
    }

    // -------------------------------------------------------
    // ADD ROOM
    // -------------------------------------------------------
    public void addRoom(Room room) {
        rooms.put(room.getRoomId(), room);
    }

    // -------------------------------------------------------
    // CHECK EXISTENCE
    // -------------------------------------------------------
    public boolean roomExists(String id) {
        return rooms.containsKey(id);
    }

    // -------------------------------------------------------
    // GET ALL ROOMS AS MAP
    // -------------------------------------------------------
    public Map<String, Room> getAllRooms() {
        return rooms;
    }

    // -------------------------------------------------------
    // GET ALL ROOMS AS LIST
    // (Used by ManageRoomsTableBuilder)
    // -------------------------------------------------------
    public java.util.List<Room> getAllRoomsList() {
        return rooms.values().stream().toList();
    }

    // -------------------------------------------------------
    // UPDATE ROOM
    // -------------------------------------------------------
    public void updateRoom(Room room) {
        rooms.put(room.getRoomId(), room);
    }

    // -------------------------------------------------------
    // DELETE ROOM
    // -------------------------------------------------------
    public void deleteRoom(String id) {
        rooms.remove(id);
    }
}
