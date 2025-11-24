package shared.model;

import shared.util.CSVHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * RoomRepository – Shared Room Storage (All Scenarios)
 * ============================================================================
 * <p>This class acts as the <b>central room database</b> for the entire system.
 * It provides a Singleton-based in-memory repository backed by rooms.csv.</p>
 *
 * <h2>Purpose</h2>
 * <ul>
 *     <li>Loads all rooms from CSV on startup.</li>
 *     <li>Provides CRUD operations for room management.</li>
 *     <li>Keeps Scenario 2, Scenario 3, and Scenario 4 fully synchronized.</li>
 *     <li>Automatically persists all changes to rooms.csv.</li>
 * </ul>
 *
 * <h2>Design Patterns</h2>
 * <ul>
 *     <li><b>Singleton</b> – Only one global repository instance exists.</li>
 *     <li><b>Repository Pattern</b> – Abstracts room storage from business logic.</li>
 *     <li><b>Auto-Persistence</b> – Every add/update/delete writes to CSV.</li>
 * </ul>
 *
 * <h2>Used By</h2>
 * <ul>
 *     <li><b>Scenario 2</b> – Room listing, booking validation.</li>
 *     <li><b>Scenario 3</b> – RoomStatusManager (occupancy + no-show logic).</li>
 *     <li><b>Scenario 4</b> – Admin room enable/disable & maintenance operations.</li>
 * </ul>
 *
 * <h2>CSV Details</h2>
 * <p>Data format is defined inside {@link shared.util.CSVHelper} (rooms.csv).</p>
 *
 * ============================================================================
 */


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
        saveToCSV();
        System.out.println("[ROOM ADDED] " + room.getRoomId() + " - " + room.getRoomName());// auto-save
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

            //System.out.println("[RoomRepository] Loaded " + rooms.size() + " rooms from " + path);
        } catch (Exception ex) {
            System.out.println("[RoomRepository] Failed to read rooms.csv: " + ex.getMessage());
        }
    }

    public Room getById(String roomId) {
        return rooms.get(roomId);
    }


}
