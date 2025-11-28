package shared.util;

import org.junit.Before;
import org.junit.Test;
import shared.model.Booking;
import shared.model.Room;
import shared.model.SystemUser;
import shared.model.User;
import shared.model.UserType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link CSVHelper}.
 *
 * <p>This suite focuses on the file-based persistence utilities used by
 * the application to read and write CSV data for:</p>
 * <ul>
 *     <li>Users ({@link CSVHelper#loadUsers(String)}, {@link CSVHelper#saveUsers(String, ArrayList)})</li>
 *     <li>Bookings ({@link CSVHelper#loadBookings(String)}, {@link CSVHelper#saveBookings(String, List)})</li>
 *     <li>Rooms ({@link CSVHelper#loadRooms(String)}, {@link CSVHelper#saveRooms(String, List)})</li>
 * </ul>
 *
 * <p>Specifically, we verify that:</p>
 * <ul>
 *     <li>Missing CSV files are created and return empty lists.</li>
 *     <li>Domain objects round-trip correctly through CSV (save → load).</li>
 *     <li>Special handling (comma-escaping, defaults, active flag) behaves as expected.</li>
 *     <li>Invalid or too-short lines are safely ignored.</li>
 * </ul>
 *
 * <p>All tests use a dedicated directory under {@code target/} so that they
 * do not interfere with production CSV files.</p>
 */
public class CSVHelperTest {

    /**
     * Base directory under {@code target/} where test CSV files are created.
     * This is cleaned/created per test run and is not committed to source control.
     */
    private File tempDir;

    /**
     * Ensures the temporary directory exists before each test execution.
     * Tests then create per-test files inside this directory.
     */
    @Before
    public void setUp() {
        tempDir = new File("target/test-csvhelper");
        if (!tempDir.exists()) {
            // Best-effort directory creation; ignore return value in tests.
            //noinspection ResultOfMethodCallIgnored
            tempDir.mkdirs();
        }
    }

    /**
     * Helper for constructing a full path under the temporary directory.
     *
     * @param fileName simple file name (e.g., {@code "users-one.csv"}).
     * @return full path for the file inside {@link #tempDir}.
     */
    private String path(String fileName) {
        return new File(tempDir, fileName).getPath();
    }

    // ============================================================
    // USERS
    // ============================================================

    /**
     * When the users CSV file does not exist, {@link CSVHelper#loadUsers(String)}
     * should create an empty file and return an empty list rather than failing.
     */
    @Test
    public void loadUsers_missingFile_createsFileAndReturnsEmptyList() throws Exception {
        // Arrange – ensure file does not exist
        String filePath = path("users-missing.csv");
        File file = new File(filePath);
        if (file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }

        // Act
        ArrayList<User> users = CSVHelper.loadUsers(filePath);

        // Assert
        assertTrue("File should be created when loading users", file.exists());
        assertNotNull("List of users should not be null", users);
        assertTrue("List of users should be empty for new file", users.isEmpty());
    }

    /**
     * Verifies that saving a single {@link SystemUser} and then reloading
     * it results in an equivalent object (all key fields preserved).
     */
    @Test
    public void saveUsers_thenLoadUsers_roundTripsSingleUser() throws Exception {
        String filePath = path("users-one.csv");

        ArrayList<User> original = new ArrayList<>();

        // Arrange – build a SystemUser with full metadata
        SystemUser user = new SystemUser(
                "Kartik Sharma",
                "kartik@yorku.ca",
                "hash123",
                UserType.STUDENT,
                "ORG1",
                "S1234567"
        );
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.of(2025, 1, 1, 12, 30);

        user.setUserId(id);
        user.setCreatedAt(createdAt);

        original.add(user);

        // Act – save then load from CSV
        CSVHelper.saveUsers(filePath, original);
        ArrayList<User> loaded = CSVHelper.loadUsers(filePath);

        // Assert – single user loaded and all fields preserved
        assertEquals(1, loaded.size());

        SystemUser loadedUser = (SystemUser) loaded.get(0);
        assertEquals(id, loadedUser.getUserId());
        assertEquals("Kartik Sharma", loadedUser.getName());
        assertEquals("kartik@yorku.ca", loadedUser.getEmail());
        assertEquals("hash123", loadedUser.getPasswordHash());
        assertEquals(UserType.STUDENT, loadedUser.getType());
        assertEquals("ORG1", loadedUser.getOrgId());
        assertEquals("S1234567", loadedUser.getStudentId());
        assertTrue("User should be active after reload", loadedUser.isActive());
        assertEquals(createdAt, loadedUser.getCreatedAt());
    }

    /**
     * Verifies that the "active" flag on {@link SystemUser} instances is
     * correctly persisted and restored via CSV.
     *
     * <p>One user is active, the other is deactivated; after reload, their
     * activity states must match.</p>
     */
    @Test
    public void saveUsers_preservesActiveFlagOnReload() throws Exception {
        String filePath = path("users-active.csv");

        ArrayList<User> original = new ArrayList<>();

        // Active user
        SystemUser activeUser = new SystemUser(
                "Active User",
                "active@yorku.ca",
                "hashActive",
                UserType.STAFF,
                "ORG2",
                null
        );
        activeUser.setUserId(UUID.randomUUID());
        activeUser.setCreatedAt(LocalDateTime.of(2025, 2, 1, 9, 0));

        // Inactive user
        SystemUser inactiveUser = new SystemUser(
                "Inactive User",
                "inactive@yorku.ca",
                "hashInactive",
                UserType.FACULTY,
                "ORG3",
                null
        );
        inactiveUser.setUserId(UUID.randomUUID());
        inactiveUser.setCreatedAt(LocalDateTime.of(2025, 3, 1, 10, 0));
        inactiveUser.deactivate();

        original.add(activeUser);
        original.add(inactiveUser);

        // Act – save then load
        CSVHelper.saveUsers(filePath, original);
        ArrayList<User> loaded = CSVHelper.loadUsers(filePath);

        // Assert
        assertEquals(2, loaded.size());

        SystemUser loadedActive = (SystemUser) loaded.get(0);
        SystemUser loadedInactive = (SystemUser) loaded.get(1);

        assertTrue("First user should remain active after reload", loadedActive.isActive());
        assertFalse("Second user should remain inactive after reload", loadedInactive.isActive());
    }

    // ============================================================
    // BOOKINGS
    // ============================================================

    /**
     * When the bookings CSV file does not exist, {@link CSVHelper#loadBookings(String)}
     * should create it and return an empty list instead of throwing an error.
     */
    @Test
    public void loadBookings_missingFile_createsFileAndReturnsEmptyList() throws Exception {
        // Arrange
        String filePath = path("bookings-missing.csv");
        File file = new File(filePath);
        if (file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }

        // Act
        ArrayList<Booking> bookings = CSVHelper.loadBookings(filePath);

        // Assert
        assertTrue("File should be created when loading bookings", file.exists());
        assertNotNull("List of bookings should not be null", bookings);
        assertTrue("List of bookings should be empty for new file", bookings.isEmpty());
    }

    /**
     * Verifies that a single {@link Booking} can be saved and then reloaded
     * with all its fields (including deposit and statuses) intact.
     */
    @Test
    public void saveBookings_thenLoadBookings_roundTripsSingleBooking() throws Exception {
        String filePath = path("bookings-one.csv");

        List<Booking> original = new ArrayList<>();
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 1, 11, 0);

        // Arrange
        Booking booking = new Booking(
                "B1",
                "R101",
                "user@yorku.ca",
                start,
                end,
                "Study Session",
                "CONFIRMED",
                "APPROVED",
                20.0
        );
        original.add(booking);

        // Act – save then load
        CSVHelper.saveBookings(filePath, original);
        ArrayList<Booking> loaded = CSVHelper.loadBookings(filePath);

        // Assert
        assertEquals(1, loaded.size());

        Booking loadedBooking = loaded.get(0);
        assertEquals("B1", loadedBooking.getBookingId());
        assertEquals("R101", loadedBooking.getRoomId());
        assertEquals("user@yorku.ca", loadedBooking.getUserId());
        assertEquals(start, loadedBooking.getStartTime());
        assertEquals(end, loadedBooking.getEndTime());
        assertEquals("Study Session", loadedBooking.getPurpose());
        assertEquals("CONFIRMED", loadedBooking.getStatus());
        assertEquals("APPROVED", loadedBooking.getPaymentStatus());
        assertEquals(20.0, loadedBooking.getDepositAmount(), 0.0001);
    }

    /**
     * Verifies that {@link CSVHelper} correctly escapes and restores commas
     * inside the {@code purpose} field when saving and loading bookings.
     */
    @Test
    public void saveBookings_escapesAndRestoresPurposeCommas() throws Exception {
        String filePath = path("bookings-purpose.csv");

        List<Booking> original = new ArrayList<>();
        LocalDateTime start = LocalDateTime.of(2025, 2, 1, 14, 0);
        LocalDateTime end = LocalDateTime.of(2025, 2, 1, 15, 30);

        String purposeWithComma = "Group work, EECS 3311 design review";

        // Arrange
        Booking booking = new Booking(
                "B2",
                "R202",
                "user2@yorku.ca",
                start,
                end,
                purposeWithComma,
                "CONFIRMED",
                "PENDING",
                30.0
        );

        original.add(booking);

        // Act – save then load
        CSVHelper.saveBookings(filePath, original);
        ArrayList<Booking> loaded = CSVHelper.loadBookings(filePath);

        // Assert – purpose should be restored exactly
        assertEquals(1, loaded.size());
        Booking loadedBooking = loaded.get(0);
        assertEquals(purposeWithComma, loadedBooking.getPurpose());
    }

    /**
     * Verifies that {@link CSVHelper#loadBookings(String)} ignores the header
     * row and any lines that have fewer columns than required (too short).
     */
    @Test
    public void loadBookings_ignoresHeaderAndTooShortLines() throws Exception {
        String filePath = path("bookings-invalid.csv");
        File file = new File(filePath);

        // Arrange – manually write a header plus an invalid short line
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("bookingId,roomId,userId,startTime,endTime,purpose,status,paymentStatus,depositAmount");
            bw.newLine();
            bw.write("INVALID,LINE"); // p.length < 9 -> should be skipped
            bw.newLine();
        }

        // Act
        ArrayList<Booking> loaded = CSVHelper.loadBookings(filePath);

        // Assert – no valid bookings should be parsed
        assertNotNull(loaded);
        assertTrue("Invalid and header-only file should produce empty list", loaded.isEmpty());
    }

    // ============================================================
    // ROOMS
    // ============================================================

    /**
     * When the rooms CSV file does not exist, {@link CSVHelper#loadRooms(String)}
     * should create it and return an empty list instead of throwing.
     */
    @Test
    public void loadRooms_missingFile_createsFileAndReturnsEmptyList() throws Exception {
        // Arrange
        String filePath = path("rooms-missing.csv");
        File file = new File(filePath);
        if (file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }

        // Act
        ArrayList<Room> rooms = CSVHelper.loadRooms(filePath);

        // Assert
        assertTrue("File should be created when loading rooms", file.exists());
        assertNotNull("List of rooms should not be null", rooms);
        assertTrue("List of rooms should be empty for new file", rooms.isEmpty());
    }

    /**
     * Verifies that a single {@link Room} object round-trips correctly through
     * {@link CSVHelper#saveRooms(String, List)} and {@link CSVHelper#loadRooms(String)}.
     */
    @Test
    public void saveRooms_thenLoadRooms_roundTripsSingleRoom() throws Exception {
        String filePath = path("rooms-one.csv");

        List<Room> original = new ArrayList<>();

        // Arrange
        Room room = new Room(
                "R101",
                "York Room",
                10,
                "First Floor",
                "Projector;Whiteboard",
                "Launcher Building",
                "AVAILABLE"
        );

        original.add(room);

        // Act – save and reload
        CSVHelper.saveRooms(filePath, original);
        ArrayList<Room> loaded = CSVHelper.loadRooms(filePath);

        // Assert – one room, with fields preserved
        assertEquals(1, loaded.size());

        Room loadedRoom = loaded.get(0);
        assertEquals("R101", loadedRoom.getRoomId());
        assertEquals(10, loadedRoom.getCapacity());
        assertEquals("Launcher Building", loadedRoom.getBuilding());
        assertEquals("Projector;Whiteboard", loadedRoom.getAmenities());
        assertEquals("AVAILABLE", loadedRoom.getStatusEnum().name());
    }

    /**
     * Verifies that when a CSV line for a room is too short (missing columns),
     * {@link CSVHelper#loadRooms(String)} still instantiates a {@link Room} using
     * sensible default values for missing fields.
     */
    @Test
    public void loadRooms_shortLineUsesDefaultsForMissingColumns() throws Exception {
        String filePath = path("rooms-short.csv");
        File file = new File(filePath);

        // Arrange – manually write a line with only the roomId present
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("R777"); // only roomId, rest missing
            bw.newLine();
        }

        // Act
        ArrayList<Room> rooms = CSVHelper.loadRooms(filePath);

        // Assert – defaults should be applied
        assertEquals(1, rooms.size());

        Room r = rooms.get(0);
        assertEquals("R777", r.getRoomId());
        assertEquals(0, r.getCapacity());          // default 0
        assertEquals("", r.getLocation());         // default ""
        assertEquals("", r.getAmenities());        // default ""
        assertEquals("", r.getBuilding());         // default ""
        assertEquals("AVAILABLE", r.getStatusEnum().name()); // default status
    }
}
