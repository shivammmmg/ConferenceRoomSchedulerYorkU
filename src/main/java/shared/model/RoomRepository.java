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
            String path = getCsvPath();
            CSVHelper.saveRooms(path, new java.util.ArrayList<>(rooms.values()));
        } catch (Exception e) {
            System.out.println("[RoomRepository] Failed to save rooms.csv: " + e.getMessage());
        }
    }

    private String getCsvPath() {
        String base = System.getProperty("user.dir");

        // If running inside target/classes, go back to project root
        if (base.endsWith("target/classes")) {
            return base.replace("target/classes", "data/rooms.csv");
        }
        return base + "/data/rooms.csv";
    }

    // -------------------------------------------------------
    // LOAD ROOMS FROM CSV
    // -------------------------------------------------------
    private void loadRoomsFromCSV() {
        try {
            String path = getCsvPath();

            var loaded = CSVHelper.loadRooms(path);
            rooms.clear();

            for (Room r : loaded) {
                rooms.put(r.getRoomId(), r);
            }

            System.out.println("[RoomRepository] Loaded " + rooms.size() + " rooms from " + path);
        } catch (Exception ex) {
            System.out.println("[RoomRepository] Failed to read rooms.csv: " + ex.getMessage());
        }
    }

}
