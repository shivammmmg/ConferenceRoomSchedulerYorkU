package shared.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RoomRepositoryTest — Unit tests for {@link RoomRepository}.
 * --------------------------------------------------------------------
 * <p>This test suite validates the behavior of the RoomRepository class,
 * which is responsible for storing, retrieving, updating, and deleting
 * {@link Room} objects. The repository follows a Singleton design and
 * maintains an in-memory map of rooms keyed by room ID.</p>
 *
 * <h2>Test Coverage Objectives</h2>
 * <ul>
 *     <li>Verify Singleton instance behavior</li>
 *     <li>Ensure rooms can be added, retrieved, updated, and deleted</li>
 *     <li>Validate list and map accessors</li>
 *     <li>Confirm fail-safe behavior for missing rooms and invalid operations</li>
 *     <li>Ensure CSV save operation does not throw exceptions</li>
 *     <li>Achieve near or full 100% coverage of RoomRepository methods</li>
 * </ul>
 */
class RoomRepositoryTest {

    private RoomRepository repo;

    /**
     * Initializes the repository before each test.
     * Ensures the Singleton instance is reset to a clean state
     * by clearing its internal room map.
     */
    @BeforeEach
    void setUp() {
        repo = RoomRepository.getInstance();
        repo.getAllRooms().clear();
    }

    /**
     * Helper method to quickly create a basic Room object using
     * the constructor available in the model.
     *
     * @param id   room identifier
     * @param name room name
     * @return a Room instance
     */
    private Room createRoom(String id, String name) {
        return new Room(id, name);
    }

    /**
     * Test 1 — Verifies the Singleton pattern implementation by
     * confirming that multiple calls return the same instance.
     */
    @Test
    void testSingletonInstanceIsSame() {
        RoomRepository r1 = RoomRepository.getInstance();
        RoomRepository r2 = RoomRepository.getInstance();
        assertSame(r1, r2);
    }

    /**
     * Test 2 — Ensures that {@code addRoom()} correctly stores a room
     * and that {@code getById()} and {@code roomExists()} return it.
     */
    @Test
    void testAddRoomStoresRoom() {
        Room room = createRoom("R101", "Room 101");
        repo.addRoom(room);

        assertTrue(repo.roomExists("R101"));
        assertEquals(room, repo.getById("R101"));
    }

    /**
     * Test 3 — Validates that {@code roomExists()} correctly returns
     * {@code false} when a room ID is not present.
     */
    @Test
    void testRoomExistsForNonExistingRoom() {
        assertFalse(repo.roomExists("X9999"));
    }

    /**
     * Test 4 — Confirms that {@code getAllRooms()} returns a map
     * containing all stored rooms and reflects repository state.
     */
    @Test
    void testGetAllRoomsReturnsAllRooms() {
        Room r1 = createRoom("R1", "One");
        Room r2 = createRoom("R2", "Two");

        repo.addRoom(r1);
        repo.addRoom(r2);

        Map<String, Room> all = repo.getAllRooms();
        assertEquals(2, all.size());
        assertTrue(all.containsKey("R1"));
        assertTrue(all.containsKey("R2"));
    }

    /**
     * Test 5 — Ensures that {@code getAllRoomsList()} returns a list
     * with all rooms in the repository.
     */
    @Test
    void testGetAllRoomsListReturnsCorrectList() {
        Room r1 = createRoom("R1", "A");
        Room r2 = createRoom("R2", "B");
        Room r3 = createRoom("R3", "C");

        repo.addRoom(r1);
        repo.addRoom(r2);
        repo.addRoom(r3);

        List<Room> list = repo.getAllRoomsList();
        assertEquals(3, list.size());
        assertTrue(list.contains(r1));
        assertTrue(list.contains(r2));
        assertTrue(list.contains(r3));
    }

    /**
     * Test 6 — Verifies that {@code updateRoom()} replaces an existing
     * room with the same ID and retrieves the updated version.
     */
    @Test
    void testUpdateRoomReplacesExistingRoom() {
        Room original = createRoom("R1", "Old Name");
        repo.addRoom(original);

        Room updated = createRoom("R1", "New Name");
        repo.updateRoom(updated);

        Room fromRepo = repo.getById("R1");

        assertEquals("New Name", fromRepo.getRoomName());
        assertSame(updated, fromRepo);
    }

    /**
     * Test 7 — Confirms that {@code deleteRoom()} removes a room from
     * the repository, and subsequent lookups return null.
     */
    @Test
    void testDeleteRoomRemovesRoom() {
        Room r = createRoom("R1", "X");
        repo.addRoom(r);
        repo.deleteRoom("R1");

        assertFalse(repo.roomExists("R1"));
        assertNull(repo.getById("R1"));
    }

    /**
     * Test 8 — Ensures that calling {@code getById()} with an unknown ID
     * returns {@code null} and does not throw errors.
     */
    @Test
    void testGetByIdReturnsNullWhenMissing() {
        assertNull(repo.getById("MISSING"));
    }

    /**
     * Test 9 — Ensures that {@code saveToCSV()} completes successfully
     * without throwing exceptions. This test does not verify file contents,
     * only that saving is fail-safe.
     */
    @Test
    void testSaveToCSVDoesNotThrow() {
        Room r = createRoom("R1", "Test");
        repo.addRoom(r);

        assertDoesNotThrow(() -> repo.saveToCSV());
    }

    /**
     * Test 10 — Verifies that adding a room with an ID that already exists
     * overwrites the previous entry, matching map semantics.
     */
    @Test
    void testAddRoomWithExistingIdOverwrites() {
        Room first = createRoom("R1", "First");
        Room second = createRoom("R1", "Second");

        repo.addRoom(first);
        repo.addRoom(second);

        Room fromRepo = repo.getById("R1");
        assertEquals("Second", fromRepo.getRoomName());
        assertSame(second, fromRepo);
    }

    /**
     * Test 11 — Ensures that deleting a room that does not exist does not
     * throw exceptions and leaves repository state unchanged.
     */
    @Test
    void testDeleteNonExistingRoomDoesNotThrow() {
        assertDoesNotThrow(() -> repo.deleteRoom("NO_SUCH_ID"));
        assertEquals(0, repo.getAllRooms().size());
    }
}
