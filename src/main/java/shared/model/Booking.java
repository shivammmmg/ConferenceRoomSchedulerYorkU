package shared.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single room reservation in the YorkU Conference Room Scheduler.
 *
 * <p>This model class stores all details required for Scenario 2:
 * <ul>
 *     <li>User making the booking</li>
 *     <li>Room being reserved</li>
 *     <li>Date and time range</li>
 *     <li>Purpose of the meeting</li>
 *     <li>Booking status (confirmed/cancelled/pending payment)</li>
 *     <li>Payment status (pending/approved/failed)</li>
 *     <li>Deposit amount charged at booking time</li>
 * </ul>
 *
 * <p>Bookings are persisted using CSV files (simulated database) through {@code CSVHelper}.
 * This class is used extensively by:
 * <ul>
 *     <li>{@code BookingManager} (Singleton – business logic)</li>
 *     <li>{@code BookingBuilder} (Builder pattern – object creation)</li>
 *     <li>{@code BookingFX} (JavaFX UI – creating, editing, displaying bookings)</li>
 * </ul>
 */
public class Booking {

    /** Unique identifier for the booking (generated automatically). */
    private String bookingId;

    /** Room ID associated with the booking. */
    private String roomId;

    /** Email or ID of the user who created the booking. */
    private String userId;

    /** Start date and time of the reservation. */
    private LocalDateTime startTime;

    /** End date and time of the reservation. */
    private LocalDateTime endTime;

    /** Purpose or description of the meeting (e.g., study group, interview). */
    private String purpose;

    /**
     * Status of the booking:
     * <ul>
     *     <li>{@code CONFIRMED} – booking is valid</li>
     *     <li>{@code CANCELLED} – user cancelled the booking</li>
     *     <li>{@code PENDING_PAYMENT} – waiting for payment approval</li>
     * </ul>
     */
    private String status;

    /**
     * Status of the payment process:
     * <ul>
     *     <li>{@code PENDING} – payment not completed yet</li>
     *     <li>{@code APPROVED} – payment successful</li>
     *     <li>{@code FAILED} – payment encountered an error</li>
     * </ul>
     */
    private String paymentStatus;

    /** Deposit amount collected (always equals 1 × hourly rate). */
    private double depositAmount;

    /**
     * Constructor used when creating a new booking from the UI or logic layer.
     * Defaults:
     * <ul>
     *     <li>status = {@code CONFIRMED}</li>
     *     <li>paymentStatus = {@code PENDING}</li>
     *     <li>depositAmount = 0.0 (assigned later by BookingManager)</li>
     * </ul>
     *
     * @param bookingId   unique ID of the booking
     * @param roomId      ID of the room being booked
     * @param userId      ID/email of the user creating the booking
     * @param startTime   start timestamp of the booking
     * @param endTime     end timestamp of the booking
     * @param purpose     purpose of the meeting
     */
    public Booking(String bookingId,
                   String roomId,
                   String userId,
                   LocalDateTime startTime,
                   LocalDateTime endTime,
                   String purpose) {

        this.bookingId = bookingId;
        this.roomId = roomId;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.purpose = purpose;

        this.status = "CONFIRMED";
        this.paymentStatus = "PENDING";
        this.depositAmount = 0.0;
    }

    /**
     * Full constructor used when loading bookings from CSV storage
     * or when creating bookings using the Builder pattern.
     *
     * @param bookingId     unique booking ID
     * @param roomId        room being reserved
     * @param userId        user who made the booking
     * @param startTime     booking start time
     * @param endTime       booking end time
     * @param purpose       purpose/description
     * @param status        booking status (confirmed/cancelled/pending)
     * @param paymentStatus payment status (pending/approved/failed)
     * @param depositAmount deposit amount charged
     */
    public Booking(String bookingId,
                   String roomId,
                   String userId,
                   LocalDateTime startTime,
                   LocalDateTime endTime,
                   String purpose,
                   String status,
                   String paymentStatus,
                   double depositAmount) {

        this.bookingId = bookingId;
        this.roomId = roomId;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.purpose = purpose;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.depositAmount = depositAmount;
    }

    // =====================================================
    //                     GETTERS
    // =====================================================

    public String getBookingId() { return bookingId; }
    public String getRoomId() { return roomId; }
    public String getUserId() { return userId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getPurpose() { return purpose; }
    public String getStatus() { return status; }
    public String getPaymentStatus() { return paymentStatus; }
    public double getDepositAmount() { return depositAmount; }

    // =====================================================
    //                     SETTERS
    // =====================================================

    public void setStatus(String status) { this.status = status; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public void setDepositAmount(double depositAmount) { this.depositAmount = depositAmount; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    // =====================================================
    //                     HELPERS
    // =====================================================

    /**
     * @return formatted date (e.g., "Nov 18, 2025")
     */
    public String getFormattedDate() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        return startTime.format(fmt);
    }

    /**
     * @return formatted time range (e.g., "11:00 - 12:30")
     */
    public String getFormattedTime() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
        return startTime.format(fmt) + " - " + endTime.format(fmt);
    }
    public boolean overlapsInterval(LocalDateTime otherStart, LocalDateTime otherEnd) {
        if (otherStart == null || otherEnd == null) return false;
        // [startTime, endTime) overlaps [otherStart, otherEnd)
        return startTime.isBefore(otherEnd) && endTime.isAfter(otherStart);
    }

    /**
     * Extend this booking's end time.
     * New end must be strictly after the current end.
     */
    public void extendTo(LocalDateTime newEndTime) {
        if (newEndTime == null) {
            throw new IllegalArgumentException("New end time cannot be null");
        }
        if (!newEndTime.isAfter(endTime)) {
            throw new IllegalArgumentException("New end time must be after current end time");
        }
        this.endTime = newEndTime;
    }

    /**
     * Serializes the booking to CSV format.
     * Matches the format expected by {@code CSVHelper}.
     *
     * @return comma-separated record representing this booking
     */
    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format(
                "%s,%s,%s,%s,%s,%s,%s,%s,%.2f",
                bookingId,
                roomId,
                userId,
                startTime.format(fmt),
                endTime.format(fmt),
                purpose,
                status,
                paymentStatus,
                depositAmount
        );
    }
}
