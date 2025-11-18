package shared.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Unified Booking class for Scenarios 2, 3, and 4.
 *
 * This class merges all Booking versions from the project and matches the UML class diagram.
 * It provides:
 *   - Core booking information
 *   - Payment + status logic (Scenario 2)
 *   - Check-in + no-show logic (Scenario 3)
 *   - Minimal usage for Scenario 4
 *
 * Backward compatibility is maintained by including:
 *   - getStart() and getStartTime()
 *   - getEnd() and getEndTime()
 *   - Both constructors used by different scenarios
 */
public class Booking {

    // -------------------------
    // Core Fields (all scenarios)
    // -------------------------
    private String bookingId;
    private String roomId;
    private String userId;     // replaces "bookedBy" from Booking_4
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // -------------------------
    // Scenario 2 fields (payments/status/purpose)
    // -------------------------
    private String purpose;
    private String status;          // CONFIRMED, CANCELLED, PENDING_PAYMENT
    private String paymentStatus;   // PENDING, APPROVED, FAILED
    private double depositAmount;
    private double finalCost;
    private double snapshotRate;
    private int duration;

    // Timestamps to match the class diagram
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Scenario 2 extra field (from UML)
    private String orgIdProvided;

    // -------------------------
    // Scenario 3 fields (check-in/no-show)
    // -------------------------
    private LocalDateTime checkInTime;
    private boolean depositForfeited = false;
    private boolean checkedIn = false;
    private boolean forfeitPopupShown = false;


    // =======================================================
    // Constructors (supports all 3 original Booking versions)
    // =======================================================

    /**
     * Full constructor used by Scenario 2 (with purpose).
     */
    public Booking(String bookingId, String roomId, String userId,
                   LocalDateTime startTime, LocalDateTime endTime, String purpose) {

        this.bookingId = bookingId;
        this.roomId = roomId;
        this.userId = userId;

        this.startTime = startTime;
        this.endTime = endTime;

        this.purpose = purpose;

        // Default values matching Scenario 2 logic
        this.status = "CONFIRMED";
        this.paymentStatus = "PENDING";
        this.depositAmount = 0.0;

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        // auto-calculate initial duration (in hours)
        this.duration = (int) java.time.Duration.between(startTime, endTime).toHours();
    }

    /**
     * Constructor used by Scenario 3 and Scenario 4 (no purpose).
     */
    public Booking(String bookingId, String roomId, String userId,
                   LocalDateTime startTime, LocalDateTime endTime) {
        this(bookingId, roomId, userId, startTime, endTime, "");
    }


    // =======================================================
    // BASIC GETTERS (backward compatible)
    // =======================================================

    public String getBookingId() { return bookingId; }
    public String getRoomId() { return roomId; }
    public String getUserId() { return userId; }

    // Scenario 2 used getStartTime/getEndTime
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }

    // Scenario 3 and 4 used getStart/getEnd
    public LocalDateTime getStart() { return startTime; }
    public LocalDateTime getEnd() { return endTime; }

    public String getPurpose() { return purpose; }
    public String getStatus() { return status; }
    public String getPaymentStatus() { return paymentStatus; }
    public double getDepositAmount() { return depositAmount; }
    public double getFinalCost() { return finalCost; }
    public String getOrgIdProvided() { return orgIdProvided; }
    public int getDuration() { return duration; }

    // Scenario 4 compatibility (Booking_4 used bookedBy)
    public String getBookedBy() {
        return userId;
    }



    // =======================================================
    // SETTERS (Scenario 2 logic)
    // =======================================================
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public void setStatus(String status) { this.status = status; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public void setDepositAmount(double depositAmount) { this.depositAmount = depositAmount; }
    public void setSnapshotRate(double snapshotRate) { this.snapshotRate = snapshotRate; }
    public void setOrgIdProvided(String id) { this.orgIdProvided = id; }


    // =======================================================
    // SCENARIO 3 LOGIC (Check-in / No-show)
    // =======================================================

    public boolean isCheckedIn() { return checkedIn; }
    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
        if (checkedIn) this.checkInTime = LocalDateTime.now();
    }

    public boolean isDepositForfeited() { return depositForfeited; }
    public void forfeitDeposit() { this.depositForfeited = true; }

    public boolean isForfeitPopupShown() { return forfeitPopupShown; }
    public void setForfeitPopupShown(boolean shown) { this.forfeitPopupShown = shown; }

    public boolean isCheckInValid() {
        // Scenario 3 logic: check-in allowed only within 30 minutes of start
        LocalDateTime now = LocalDateTime.now();
        return !now.isAfter(startTime.plusMinutes(30));
    }

    public void handleNoShow() {
        if (!checkedIn) {
            this.depositForfeited = true;
        }
    }


    // =======================================================
    // SCENARIO 2 BUSINESS METHODS (from UML)
    // =======================================================

    public boolean cancel() {
        if (status.equals("CANCELLED")) return false;
        status = "CANCELLED";
        updatedAt = LocalDateTime.now();
        return true;
    }

    public boolean extend(int extraSlots) {
        this.endTime = endTime.plusHours(extraSlots);
        this.duration += extraSlots;
        updatedAt = LocalDateTime.now();
        return true;
    }

    public double calculateCost() {
        this.finalCost = duration * snapshotRate;
        return finalCost;
    }

    public double calculateDepositAmount(String userType, int duration) {
        // Simple rule: students pay lower deposit
        if (userType.equalsIgnoreCase("student")) {
            this.depositAmount = duration * 2.0;
        } else {
            this.depositAmount = duration * 5.0;
        }
        return depositAmount;
    }

    // Overlap logic from UML
    public boolean overlaps(LocalDateTime otherStart, LocalDateTime otherEnd) {
        return startTime.isBefore(otherEnd) && otherStart.isBefore(endTime);
    }


    // =======================================================
    // Formatting helpers (Scenario 2 UI)
    // =======================================================
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
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format(
                "%s,%s,%s,%s,%s,%s,%s,%s,%.2f",
                bookingId, roomId, userId,
                startTime.format(fmt), endTime.format(fmt),
                purpose, status, paymentStatus, depositAmount
        );
    }
}
