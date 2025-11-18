package shared.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository {

    private static BookingRepository instance;

    private final List<Booking> bookings = new ArrayList<>();

    public static BookingRepository getInstance() {
        if (instance == null)
            instance = new BookingRepository();
        return instance;
    }

    private BookingRepository() {
        // ----- Dummy sample bookings for testing -----
        bookings.add(new Booking(
                "B1", "2", "studentA",
                LocalDateTime.now().minusHours(3),
                LocalDateTime.now().minusHours(1)
        ));
        bookings.add(new Booking(
                "B2", "3", "studentB",
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(3)
        ));
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

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public List<Booking> getAllBookings() {
        return bookings;
    }
}
