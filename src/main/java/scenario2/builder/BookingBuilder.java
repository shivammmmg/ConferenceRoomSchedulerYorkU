package scenario2.builder;

import shared.model.Booking;
import java.time.LocalDateTime;

/**
 * EECS 3311 - YorkU Conference Room Scheduler
 * Builder Pattern for Booking objects
 */


public class BookingBuilder {
    private String bookingId;
    private String roomId;
    private String userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String purpose;
    private String paymentStatus;
    private double depositAmount;

    public BookingBuilder setBookingId(String bookingId) {
        this.bookingId = bookingId;
        return this;
    }

    public BookingBuilder setRoomId(String roomId) {
        this.roomId = roomId;
        return this;
    }

    public BookingBuilder setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public BookingBuilder setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public BookingBuilder setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public BookingBuilder setPurpose(String purpose) {
        this.purpose = purpose;
        return this;
    }

    public BookingBuilder setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
        return this;
    }

    public BookingBuilder setDepositAmount(double depositAmount) {
        this.depositAmount = depositAmount;
        return this;
    }

    public Booking build() {
        Booking booking = new Booking(bookingId, roomId, userId, startTime, endTime, purpose);
        booking.setPaymentStatus(paymentStatus);
        booking.setDepositAmount(depositAmount);
        return booking;
    }
}
