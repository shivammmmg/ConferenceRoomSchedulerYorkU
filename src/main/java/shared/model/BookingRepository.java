package shared.model;

import shared.util.CSVHelper;
import java.util.ArrayList;
import java.util.List;

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
