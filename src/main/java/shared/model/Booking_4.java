package shared.model;

import java.time.LocalDateTime;

public class Booking_4 {

    private String bookingId;
    private String roomId;
    private String bookedBy;
    private LocalDateTime start;
    private LocalDateTime end;

    public Booking_4(String bookingId, String roomId, String bookedBy,
                     LocalDateTime start, LocalDateTime end) {

        this.bookingId = bookingId;
        this.roomId = roomId;
        this.bookedBy = bookedBy;
        this.start = start;
        this.end = end;
    }

    public String getBookingId() { return bookingId; }
    public String getRoomId() { return roomId; }
    public String getBookedBy() { return bookedBy; }
    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }
}
