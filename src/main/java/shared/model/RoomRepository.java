package shared.model;

import shared.util.CSVHelper;

import java.util.HashMap;
import java.util.Map;

public class RoomRepository {

    private static RoomRepository instance;

    private final Map<String, Room> rooms = new HashMap<>();

    private RoomRepository() {
        loadRoomsFromCSV();
    }

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
        saveToCSV();        // auto-save
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
    // -------------------------------------------------------
    public java.util.List<Room> getAllRoomsList() {
        return new java.util.ArrayList<>(rooms.values());
    }

    // -------------------------------------------------------
    // UPDATE ROOM
    // -------------------------------------------------------
    public void updateRoom(Room room) {
        rooms.put(room.getRoomId(), room);
        saveToCSV();        // auto-save
    }

    // -------------------------------------------------------
    // DELETE ROOM
    // -------------------------------------------------------
    public void deleteRoom(String id) {
        rooms.remove(id);
        saveToCSV();        // auto-save
    }

    // -------------------------------------------------------
    // SAVE ROOMS TO CSV
    // -------------------------------------------------------
    public void saveToCSV() {
        try {
            CSVHelper.saveRooms("data/rooms.csv", new java.util.ArrayList<>(rooms.values()));
            System.out.println("[RoomRepository] Saved " + rooms.size() + " rooms to rooms.csv");
        } catch (Exception e) {
            System.out.println("[RoomRepository] Failed to save rooms.csv: " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // LOAD ROOMS FROM CSV
    // -------------------------------------------------------
    private void loadRoomsFromCSV() {
        try {
            var loaded = CSVHelper.loadRooms("data/rooms.csv");
            rooms.clear();
            for (Room r : loaded) {
                rooms.put(r.getRoomId(), r);
            }
            System.out.println("[RoomRepository] Loaded " + rooms.size() + " rooms from rooms.csv");
        } catch (Exception ex) {
            System.out.println("[RoomRepository] Failed to read rooms.csv: " + ex.getMessage());
        }
    }
}
