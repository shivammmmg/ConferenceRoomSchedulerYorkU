package scenario3.model;

import java.time.LocalDateTime;

/**
 * MODEL: Booking
 * Represents a simple booking record used for check-in/no-show logic.
 */
public class Booking {
    private final String bookingId;
    private final String roomId;
    private final String userId;
    private final LocalDateTime start;
    private final LocalDateTime end;

    private boolean depositForfeited = false;
    private boolean checkedIn = false;
    private boolean forfeitPopupShown = false; // ensures popup only shows once

    public Booking(String bookingId, String roomId, String userId, LocalDateTime start, LocalDateTime end) {
        this.bookingId = bookingId;
        this.roomId = roomId;
        this.userId = userId;
        this.start = start;
        this.end = end;
    }

    public String getBookingId() { return bookingId; }
    public String getRoomId() { return roomId; }
    public String getUserId() { return userId; }
    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }

    public boolean isDepositForfeited() { return depositForfeited; }
    public void forfeitDeposit() { depositForfeited = true; }

    public boolean isCheckedIn() { return checkedIn; }
    public void setCheckedIn(boolean checkedIn) { this.checkedIn = checkedIn; }

    public boolean isForfeitPopupShown() { return forfeitPopupShown; }
    public void setForfeitPopupShown(boolean shown) { this.forfeitPopupShown = shown; }
}
