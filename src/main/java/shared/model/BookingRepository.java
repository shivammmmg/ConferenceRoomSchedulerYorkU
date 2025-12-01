package shared.model;

import shared.util.CSVHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * BookingRepository – Centralized Booking Storage (All Scenarios)
 * ============================================================================
 * <p>This class provides a unified, in-memory repository for all
 * {@link Booking} objects in the system. It mirrors the same pattern used for
 * RoomRepository and UserManager, ensuring consistent data access across
 * scenarios.</p>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li><b>Singleton</b> – Only one repository instance exists.</li>
 *     <li><b>Repository Pattern</b> – Abstracts CSV persistence from controllers.</li>
 *     <li>Works with Scenario 2, 3, and 4 controllers that require booking lookup.</li>
 * </ul>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>Load all bookings from a CSV file at startup (default: {@code data/bookings.csv})</li>
 *     <li>Provide lookup operations by:
 *         <ul>
 *             <li>Booking ID</li>
 *             <li>Room ID</li>
 *         </ul>
 *     </li>
 *     <li>Persist all changes back to CSV using {@link CSVHelper}</li>
 * </ul>
 *
 * <h2>Supported Scenarios</h2>
 * <ul>
 *     <li><b>Scenario 2</b> – Booking creation, editing, validation</li>
 *     <li><b>Scenario 3</b> – Check-in, no-show detection, active room monitoring</li>
 *     <li><b>Scenario 4</b> – Admin panels requiring booking inspection</li>
 * </ul>
 *
 * <h2>Why This Class Exists</h2>
 * <p>Instead of allowing multiple controllers to load CSV files independently,
 * this repository ensures:</p>
 * <ul>
 *     <li>Centralized state</li>
 *     <li>No duplicate file reads</li>
 *     <li>Consistent in-memory objects across the entire system</li>
 * </ul>
 *
 * <h2>Testing Note</h2>
 * <p>Tests can override the CSV path by setting the system property
 * {@link #BOOKING_CSV_PROPERTY} <b>before</b> the first call to
 * {@link #getInstance()}, and by calling {@link #resetForTests()}.</p>
 * ============================================================================
 */
public class BookingRepository {

    /**
     * System property key used to override the CSV path.
     * <p>
     * Production run: property not set → {@code data/bookings.csv} is used. <br>
     * Tests: set this to something like {@code TestData/bookings.csv}.
     */
    public static final String BOOKING_CSV_PROPERTY = "booking.csv.path";

    /** Singleton instance. */
    private static BookingRepository instance;

    /** Actual CSV path used by this instance. */
    private final String bookingCsvPath;

    /** In-memory list of all bookings. */
    private final List<Booking> bookings = new ArrayList<>();

    /**
     * Returns the singleton instance, creating it on first use.
     */
    public static BookingRepository getInstance() {
        if (instance == null) {
            instance = new BookingRepository();
        }
        return instance;
    }

    /**
     * Resets the singleton instance – intended for unit tests.
     * <p>
     * After calling this, the next call to {@link #getInstance()} will create
     * a new repository using the current value of the
     * {@link #BOOKING_CSV_PROPERTY} system property.
     */
    public static void resetForTests() {
        instance = null;
    }

    /**
     * Private constructor – reads the CSV path and loads data.
     * <p>
     * If the system property {@link #BOOKING_CSV_PROPERTY} is set, that value
     * is used as the CSV path; otherwise, {@code data/bookings.csv} is used.
     */
    private BookingRepository() {
        String override = System.getProperty(BOOKING_CSV_PROPERTY);
        if (override == null || override.isBlank()) {
            bookingCsvPath = "data/bookings.csv";   // production default
        } else {
            bookingCsvPath = override;              // test or custom path
        }
        loadFromCSV();
    }

    // =========================================================
    //                  CSV LOAD / SAVE
    // =========================================================

    /** Loads all bookings from the configured CSV file into memory. */
    private void loadFromCSV() {
        try {
            List<Booking> loaded = CSVHelper.loadBookings(bookingCsvPath);
            bookings.clear();
            bookings.addAll(loaded);
        } catch (Exception e) {
            System.out.println("[BookingRepository] Could not load " + bookingCsvPath + ": " + e.getMessage());
        }
    }

    /** Saves all in-memory bookings back to the configured CSV file. */
    public void saveAll() {
        try {
            CSVHelper.saveBookings(bookingCsvPath, bookings);
        } catch (Exception e) {
            System.out.println("[BookingRepository] Could not save " + bookingCsvPath + ": " + e.getMessage());
        }
    }

    // =========================================================
    //                  QUERY / ACCESSORS
    // =========================================================

    /** Returns the live in-memory list of bookings. */
    public List<Booking> getAllBookings() {
        return bookings;
    }

    /** Finds a booking by its unique ID, or {@code null} if not found. */
    public Booking findById(String id) {
        for (Booking b : bookings) {
            if (b.getBookingId().equals(id)) return b;
        }
        return null;
    }

    /** Returns all bookings for a given room ID. */
    public List<Booking> getBookingsForRoom(String roomId) {
        List<Booking> result = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getRoomId().equals(roomId)) {
                result.add(b);
            }
        }
        return result;
    }
}
