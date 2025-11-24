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
 *     <li>Load all bookings from <code>data/bookings.csv</code> at startup</li>
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
 * ============================================================================
 */


public class BookingRepository {

    private static BookingRepository instance;
    private final String BOOKING_CSV = "data/bookings.csv";

    private final List<Booking> bookings = new ArrayList<>();

    public static BookingRepository getInstance() {
        if (instance == null)
            instance = new BookingRepository();
        return instance;
    }

    private BookingRepository() {
        loadFromCSV();
    }

    // Load from CSV
    private void loadFromCSV() {
        try {
            List<Booking> loaded = CSVHelper.loadBookings(BOOKING_CSV);
            bookings.clear();
            bookings.addAll(loaded);
        } catch (Exception e) {
            System.out.println("[BookingRepository] Could not load bookings.csv: " + e.getMessage());
        }
    }

    // Save all back to CSV
    public void saveAll() {
        try {
            CSVHelper.saveBookings(BOOKING_CSV, bookings);
        } catch (Exception e) {
            System.out.println("[BookingRepository] Could not save bookings.csv: " + e.getMessage());
        }
    }

    public List<Booking> getAllBookings() {
        return bookings;
    }

    public Booking findById(String id) {
        for (Booking b : bookings) {
            if (b.getBookingId().equals(id)) return b;
        }
        return null;
    }

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
