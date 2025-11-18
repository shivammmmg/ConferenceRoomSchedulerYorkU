package shared.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a room booking in the system
 */
public class Booking {
    private String bookingId;
    private String roomId;
    private String userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String purpose;
    private String status; // "CONFIRMED", "CANCELLED", "PENDING_PAYMENT"
    private String paymentStatus; // "PENDING", "APPROVED", "FAILED"
    private double depositAmount;

    public Booking(String bookingId, String roomId, String userId,
                   LocalDateTime startTime, LocalDateTime endTime, String purpose) {
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

    // Getters and Setters
    public String getBookingId() { return bookingId; }
    public String getRoomId() { return roomId; }
    public String getUserId() { return userId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getPurpose() { return purpose; }
    public String getStatus() { return status; }
    public String getPaymentStatus() { return paymentStatus; }
    public double getDepositAmount() { return depositAmount; }

    public void setStatus(String status) { this.status = status; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public void setDepositAmount(double depositAmount) { this.depositAmount = depositAmount; }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        return startTime.format(formatter);
    }

    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return startTime.format(formatter) + " - " + endTime.format(formatter);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%.2f",
                bookingId, roomId, userId,
                startTime.format(formatter), endTime.format(formatter),
                purpose, status, paymentStatus, depositAmount);
    }
}
