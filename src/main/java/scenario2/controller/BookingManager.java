package scenario2.controller;

import scenario2.builder.BookingBuilder;
import shared.model.*;
import shared.util.CSVHelper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * BookingManager – Scenario 2 (Room Booking & Payment)
 * ============================================================================
 * <p>The central business-logic engine for all booking-related operations in
 * Scenario 2. This class is the single point of truth for:</p>
 *
 * <ul>
 *     <li>Searching rooms with capacity, building, and equipment filters</li>
 *     <li>Creating new bookings using {@link scenario2.builder.BookingBuilder}</li>
 *     <li>Handling deposits, estimated totals, and role-based pricing</li>
 *     <li>Editing, extending, and cancelling existing bookings</li>
 *     <li>Coordinating with Scenario 3 (check-in, no-show, sensors)</li>
 *     <li>Persisting all data through BookingRepository + CSVHelper</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li><b>Singleton</b>: Ensures one global booking engine used across
 *         Scenario 2, Scenario 3 (SensorSystem / RoomStatusManager), and
 *         BookingFX.</li>
 *     <li><b>Builder Pattern</b>: Used for constructing Booking objects in
 *         a readable, step-by-step manner.</li>
 *     <li><b>Repository Pattern</b>: Interacts with {@link RoomRepository} and
 *         {@link BookingRepository} for CSV-backed persistence.</li>
 * </ul>
 *
 * <h2>Why This Class Exists (D3 Justification)</h2>
 * <p>Scenario 2 requires a unified, reusable API to support:</p>
 * <ul>
 *     <li>Room search + availability checking</li>
 *     <li>Deposit and payment preparation before UI modal</li>
 *     <li>Accurate conflict detection for booking & extending</li>
 *     <li>Real-time integration with Scenario 3 (no-show + check-in events)</li>
 * </ul>
 *
 * <h2>Related Scenarios</h2>
 * <ul>
 *     <li><b>Scenario 1</b> – User accounts determine the pricing tier.</li>
 *     <li><b>Scenario 2</b> – Room Booking & Payment (primary use).</li>
 *     <li><b>Scenario 3</b> – Check-in, no-show, and live room monitoring.</li>
 *     <li><b>Scenario 4</b> – Admin may rely on booking data for analytics.</li>
 * </ul>
 *
 * <h2>Key Features</h2>
 * <ul>
 *     <li>15-minute time slot granularity</li>
 *     <li>Role-based pricing: student/faculty/staff/partner</li>
 *     <li>Automatic deposit rules</li>
 *     <li>Extension validation with next-slot availability check</li>
 *     <li>Robust conflict-checking logic for new & edited bookings</li>
 *     <li>Integrated logging for D3 demonstration</li>
 * </ul>
 * ============================================================================
 */

public class BookingManager {

    // =========================================================
    //                    SINGLETON
    // =========================================================
    private static BookingManager instance;
    private static final int EXTENSION_BUFFER_MINUTES = 10;  // must request ≥10 min before end
    private static final int TIME_SLOT_MINUTES       = 15;  // your granularity


    public static synchronized BookingManager getInstance() {
        if (instance == null) {
            instance = new BookingManager();
        }
        return instance;
    }

    private final RoomRepository roomRepo = RoomRepository.getInstance();
    private final BookingRepository bookingRepo = BookingRepository.getInstance();

    private BookingManager() { }

    // =========================================================
    //                    PRICING LOGIC
    // =========================================================

    // You can tweak these numbers freely – UI will just display them.
    // Req3: Hourly rates (students 20, faculty 30, staff 40, partners 50)
    // Req3: Hourly rates
// Students: 20, Faculty: 30, Staff: 40, Partners: 50
    private static final double STUDENT_RATE = 20.0;
    private static final double FACULTY_RATE = 30.0;
    private static final double STAFF_RATE   = 40.0;
    private static final double PARTNER_RATE = 50.0;

    // Default: use student rate for any new/unknown type (Req1: flexibility to add types)
    private static final double DEFAULT_RATE = STUDENT_RATE;


    /** Returns the hourly rate based on user type. */
    public double getHourlyRateForUserType(String userType) {
        if (userType == null || userType.isBlank()) {
            return DEFAULT_RATE;
        }

        switch (userType.trim().toUpperCase()) {
            case "STUDENT":
                return STUDENT_RATE;
            case "FACULTY":
                return FACULTY_RATE;
            case "STAFF":
                return STAFF_RATE;
            case "PARTNER":
                return PARTNER_RATE;
            default:
                // Future account types fall back to DEFAULT_RATE
                return DEFAULT_RATE;
        }
    }


    /**
     * Deposit rule used in the UI:
     * - Deposit = 1 hour of the hourly rate (regardless of duration).
     */
    public double getDepositForUserTypeAndDuration(String userType, long durationHours) {
        return getHourlyRateForUserType(userType);
    }

    /** Simple estimated total = hours × hourlyRate. */
    public double getEstimatedTotalForUser(String userType, long durationHours) {
        if (durationHours <= 0) durationHours = 1;
        return getHourlyRateForUserType(userType) * durationHours;
    }

    // =========================================================
    //                    ROOM HELPERS
    // =========================================================

    /** All rooms as a List (used by BookingFX previews). */
    public List<Room> getAllRooms() {
        return roomRepo.getAllRoomsList(); // defensive copy from repo
    }

    /** Lookup a room by ID (used by BookingFX cards). */
    public Room getRoomById(String roomId) {
        if (roomId == null) return null;
        return roomRepo.getById(roomId);
    }

    /** Distinct buildings from all rooms (for building filter ComboBox). */
    public List<String> getAvailableBuildings() {
        Set<String> set = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (Room r : roomRepo.getAllRoomsList()) {
            String b = r.getBuilding();
            if (b != null) {
                b = b.trim();
                if (!b.isEmpty()) set.add(b);
            }
        }
        return new ArrayList<>(set);
    }

    /** Distinct equipment/amenity tokens from all rooms (for equipment filter ComboBox). */
    public List<String> getAvailableEquipment() {
        Set<String> set = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (Room r : roomRepo.getAllRoomsList()) {
            String amenities = r.getAmenities();
            if (amenities == null) continue;

            String[] parts = amenities.split(",");
            for (String raw : parts) {
                String s = raw.trim();
                if (!s.isEmpty()) set.add(s);
            }
        }
        return new ArrayList<>(set);
    }

    // =========================================================
    //             SEARCH AVAILABLE ROOMS FOR A SLOT
    // =========================================================

    public List<Room> searchAvailableRooms(LocalDateTime start,
                                           LocalDateTime end,
                                           int capacityNeeded,
                                           String buildingFilter,
                                           String equipmentFilter) {

        if (start == null || end == null)
            throw new IllegalArgumentException("Start and end time are required.");
        if (!end.isAfter(start))
            throw new IllegalArgumentException("End time must be after start time.");

        String buildingF = (buildingFilter != null && !buildingFilter.isBlank())
                ? buildingFilter.trim().toLowerCase(Locale.ROOT)
                : null;

        String equipmentF = (equipmentFilter != null && !equipmentFilter.isBlank())
                ? equipmentFilter.trim().toLowerCase(Locale.ROOT)
                : null;

        List<Room> matches = new ArrayList<>();

        for (Room room : roomRepo.getAllRoomsList()) {
            RoomStatus st = room.getStatusEnum();
            if (st == RoomStatus.DISABLED || st == RoomStatus.MAINTENANCE) {
                continue; // room should not be bookable
            }
            // Capacity
            if (room.getCapacity() < capacityNeeded) continue;

            // Building filter
            if (buildingF != null) {
                String b = room.getBuilding();
                if (b == null || !b.trim().toLowerCase(Locale.ROOT).equals(buildingF)) {
                    continue;
                }
            }

            // Equipment filter
            if (equipmentF != null) {
                String amenities = room.getAmenities();
                if (amenities == null ||
                        !amenities.toLowerCase(Locale.ROOT).contains(equipmentF)) {
                    continue;
                }
            }

            // Time conflict check
            if (isRoomFree(room.getRoomId(), start, end)) {
                matches.add(room);
            }
        }

        return matches;
    }

    /** Returns true if the room has no non-cancelled bookings overlapping [start, end]. */
    private boolean isRoomFree(String roomId, LocalDateTime start, LocalDateTime end) {
        List<Booking> forRoom = bookingRepo.getBookingsForRoom(roomId);

        for (Booking b : forRoom) {
            String status = b.getStatus();
            if ("CANCELLED".equals(status) || "NO_SHOW".equals(status)) {
                continue; // ignore dead bookings
            }

            boolean overlap =
                    !b.getEndTime().isBefore(start) && !b.getStartTime().isAfter(end);
            if (overlap) return false;
        }
        return true;
    }

    // =========================================================
    //     INTERNAL HELPERS – ADD / SAVE USING CURRENT REPO
    // =========================================================

    /** Adds a booking to the in-memory list + persists via saveAll(). */
    private void addBookingInternal(Booking booking) {
        bookingRepo.getAllBookings().add(booking);
        bookingRepo.saveAll();
    }
    public double calculateExtensionDeposit(String userType, long extraMinutes) {
        long extraHours = (long) Math.ceil(extraMinutes / 60.0);
        return getDepositForUserTypeAndDuration(userType, extraHours);
    }
    public Booking findBookingById(String bookingId) {
        for (Booking b : bookingRepo.getAllBookings()) {   // ✅ use the repo list
            if (b.getBookingId().equals(bookingId)) {
                return b;
            }
        }
        return null;
    }
    public boolean canExtendBooking(Booking booking, long extraMinutes) {
        if (booking == null) return false;

        LocalDateTime now         = LocalDateTime.now();
        LocalDateTime originalEnd = booking.getEndTime();

        // 1) Must be before end - buffer (Req 9)
        if (!now.isBefore(originalEnd.minusMinutes(EXTENSION_BUFFER_MINUTES))) {
            return false;
        }

        // 2) Only full time-slot increments (15, 30, 45, 60 ... minutes)
        if (extraMinutes <= 0 || extraMinutes % TIME_SLOT_MINUTES != 0) {
            return false;
        }

        LocalDateTime newEnd = originalEnd.plusMinutes(extraMinutes);

        // 3) Check if room is fully free in [originalEnd, newEnd)
        //    (no overlap with any other booking for the same room)
        for (Booking other : bookingRepo.getAllBookings()) {
            if (other.getBookingId().equals(booking.getBookingId())) continue;
            if (!other.getRoomId().equals(booking.getRoomId()))     continue;

            LocalDateTime otherStart = other.getStartTime();
            LocalDateTime otherEnd   = other.getEndTime();

            // overlap if:
            //   [originalEnd, newEnd) ∩ [otherStart, otherEnd) ≠ ∅
            boolean overlaps =
                    originalEnd.isBefore(otherEnd) &&   // this extension starts before other ends
                            otherStart.isBefore(newEnd);        // and other starts before extension ends

            if (overlaps) {
                // any overlap, even partial → NOT allowed (Req 9 edge case)
                return false;
            }
        }

        return true;
    }

    public Booking extendBooking(String bookingId,
                                 String userEmail,
                                 long extraMinutes,
                                 String userType) throws Exception {

        Booking booking = findBookingById(bookingId);
        if (booking == null) {
            throw new Exception("Booking not found.");
        }

        // Ownership check (you use email as userId)
        if (!booking.getUserId().equalsIgnoreCase(userEmail)) {
            throw new Exception("You can only extend your own bookings.");
        }

        String status = booking.getStatus();
        if (!"CONFIRMED".equals(status) && !"IN_USE".equals(status)) {
            throw new Exception("Only active bookings can be extended.");
        }

        if (!canExtendBooking(booking, extraMinutes)) {
            throw new Exception("Room is not available for that extension window " +
                    "or the request is too close to the end time.");
        }

        double additionalDeposit = calculateExtensionDeposit(userType, extraMinutes);

        // Apply the extension
        booking.setEndTime(booking.getEndTime().plusMinutes(extraMinutes));
        booking.setDepositAmount(
                booking.getDepositAmount() + additionalDeposit
        );

        // If you store overall payment or history, update that here.
        // TODO: persist to CSV/DB if that's what you normally do here.

        return booking;
    }

    public Booking extendBooking(String bookingId,
                                 LocalDateTime newEndTime,
                                 double extraAmount) throws Exception {

        // 1) load all bookings
        List<Booking> all = CSVHelper.loadBookings("data/bookings.csv");

        // 2) find the one we are extending
        Booking target = null;
        for (Booking b : all) {
            if (b.getBookingId().equals(bookingId)) {
                target = b;
                break;
            }
        }

        if (target == null) {
            throw new IllegalArgumentException("Booking not found: " + bookingId);
        }

        // Only allow extending ACTIVE / CONFIRMED type statuses
        String st = target.getStatus() == null ? "" : target.getStatus().toUpperCase();
        if (!(st.contains("ACTIVE") || st.contains("CONFIRMED"))) {
            throw new IllegalStateException("Only active/confirmed bookings can be extended.");
        }

        LocalDateTime oldEnd = target.getEndTime();

        if (!newEndTime.isAfter(oldEnd)) {
            throw new IllegalArgumentException("New end time must be after current end time.");
        }

        // 3) conflict check ONLY on the extra interval [oldEnd, newEndTime)
        for (Booking other : all) {
            if (other == target) continue;
            if (!other.getRoomId().equals(target.getRoomId())) continue;

            // Only clash if other booking overlaps the extension window
            if (other.overlapsInterval(oldEnd, newEndTime)) {
                throw new IllegalStateException(
                        "Room " + target.getRoomId() +
                                " is not available for extension; it is already booked in that time."
                );
            }
        }

        // 4) PAYMENT STEP (hook up your Payment Service here)
        // ---------------------------------------------------
        // Example idea (pseudo-code, so you don't get compile errors):
        //
        // PaymentResult result = paymentService.chargeExtension(target, extraAmount);
        // if (!result.isSuccessful()) {
        //     throw new IllegalStateException("Payment failed: " + result.getMessage());
        // }
        //
        // For now we just assume payment succeeded:
        target.setDepositAmount(target.getDepositAmount() + extraAmount);

        // 5) actually extend the booking
        target.extendTo(newEndTime);

        // 6) save all bookings back
        CSVHelper.saveBookings("data/bookings.csv", all);

        return target;
    }

    /**
     * Convenience method for "Extend Booking" by exactly one slot.
     * - slotMinutes = length of one slot (e.g., 60)
     * - pricePerSlot = how much to charge for one extra slot
     *
     * This matches the requirement "user may extend booking if next slot is available".
     */
    public Booking extendBookingOneSlot(String bookingId,
                                        int slotMinutes,
                                        double pricePerSlot) throws Exception {

        // load again just to compute new end time based on current state
        List<Booking> all = CSVHelper.loadBookings("data/bookings.csv");

        Booking target = null;
        for (Booking b : all) {
            if (b.getBookingId().equals(bookingId)) {
                target = b;
                break;
            }
        }

        if (target == null) {
            throw new IllegalArgumentException("Booking not found: " + bookingId);
        }

        LocalDateTime newEnd = target.getEndTime().plusMinutes(slotMinutes);

        // Re-use core logic
        return extendBooking(bookingId, newEnd, pricePerSlot);
    }

    /** Saves current in-memory bookings list to CSV. */
    private void saveBookings() {
        bookingRepo.saveAll();
    }

    // =========================================================
    //                    CREATE NEW BOOKING
    // =========================================================

    /**
     * Called from BookingFX → after payment modal passes card validation.
     * We treat payment as APPROVED here and store the deposit.
     */
    public Booking bookRoom(String roomId,
                            String userId,
                            LocalDateTime start,
                            LocalDateTime end,
                            String purpose,
                            String userType) throws Exception {

        if (roomId == null || userId == null) {
            throw new IllegalArgumentException("roomId and userId are required.");
        }

        Room room = roomRepo.getById(roomId);
        if (room == null) {
            throw new Exception("Selected room does not exist.");
        }

        if (start == null || end == null || !end.isAfter(start)) {
            throw new Exception("Invalid start/end time.");
        }

        // Final conflict check in case something changed after search
        if (!isRoomFree(roomId, start, end)) {
            throw new Exception("Room is no longer available for that time.");
        }

        long minutes = Duration.between(start, end).toMinutes();
        long hours = minutes / 60;
        if (minutes % 60 != 0) hours++;
        if (hours <= 0) hours = 1;

        double deposit = getDepositForUserTypeAndDuration(userType, hours);

        String bookingId = generateBookingId();

        Booking booking = new BookingBuilder()
                .setBookingId(bookingId)
                .setRoomId(roomId)
                .setUserId(userId)
                .setStartTime(start)
                .setEndTime(end)
                .setPurpose(purpose)
                .setStatus("CONFIRMED")
                .setPaymentStatus("APPROVED")   // card passed in PaymentModal
                .setDepositAmount(deposit)
                .build();

        // Use internal add + save, compatible with BookingRepository
        addBookingInternal(booking);

        // ===================== BOOKING LOG (SUCCESS) ============================
        LocalDateTime logNow = LocalDateTime.now();
        DateTimeFormatter logFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println("");
        System.out.println("┌──────────────────────────────────────── BOOKING CREATED ─────────────────────────────────────────┐");

        String logLine = "│ %-12s : %-81s │";

        System.out.println(String.format(logLine, "BookingID", bookingId));
        System.out.println(String.format(logLine, "RoomID", roomId));
        System.out.println(String.format(logLine, "User", userId));
        System.out.println(String.format(logLine, "Start", start.toString()));
        System.out.println(String.format(logLine, "End", end.toString()));
        System.out.println(String.format(logLine, "Purpose", purpose));
        System.out.println(String.format(logLine, "Deposit", String.format("$%.2f", deposit)));
        System.out.println(String.format(logLine, "Status", "CONFIRMED"));
        System.out.println(String.format(logLine, "Timestamp", logNow.format(logFmt)));

        System.out.println("└──────────────────────────────────────────────────────────────────────────────────────────────────┘");
        System.out.println("");
        // =======================================================================


        return booking;
    }

    private String generateBookingId() {
        return "B" + System.currentTimeMillis();
    }

    // =========================================================
    //               USER BOOKINGS (MY BOOKINGS TAB)
    // =========================================================

    /** All bookings belonging to a particular user. */
    public List<Booking> getAllUserBookings(String userId) {
        List<Booking> result = new ArrayList<>();
        if (userId == null) return result;

        for (Booking b : bookingRepo.getAllBookings()) {
            if (userId.equalsIgnoreCase(b.getUserId())) {
                result.add(b);
            }
        }
        return result;
    }

    // =========================================================
    //               CANCEL / EDIT EXISTING BOOKINGS
    // =========================================================

    public boolean cancelBooking(String bookingId, String userEmail) throws Exception {
        Booking booking = getBookingById(bookingId);
        if (booking == null) {
            throw new Exception("Booking not found.");
        }

        if (userEmail != null && !userEmail.equalsIgnoreCase(booking.getUserId())) {
            throw new Exception("You can cancel only your own bookings.");
        }

        if ("CANCELLED".equals(booking.getStatus())) {
            return false; // already cancelled
        }

        booking.setStatus("CANCELLED");
        // simple rule: treat as refunded on cancel
        booking.setPaymentStatus("REFUNDED");

        saveBookings();
        // ===================== BOOKING CANCELLED LOG ============================
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println();
        System.out.println("┌──────────────────────────────────────── BOOKING CANCELLED ─────────────────────────────────────────┐");

        String borderLine = "│ %-12s : %-83s │";

        System.out.println(String.format(borderLine, "BookingID", bookingId));
        System.out.println(String.format(borderLine, "RoomID", booking.getRoomId()));
        System.out.println(String.format(borderLine, "User", booking.getUserId()));
        System.out.println(String.format(borderLine, "Status", "CANCELLED"));
        System.out.println(String.format(borderLine, "Refund", "Deposit REFUNDED"));
        System.out.println(String.format(borderLine, "Timestamp", now.format(fmt)));

        System.out.println("└────────────────────────────────────────────────────────────────────────────────────────────────────┘");
        System.out.println();
// =======================================================================

        return true;
    }

    public void editBooking(String bookingId,
                            String userEmail,
                            String newRoomId,
                            LocalDateTime newStart,
                            LocalDateTime newEnd,
                            String newPurpose) throws Exception {

        Booking booking = getBookingById(bookingId);
        if (booking == null) {
            throw new Exception("Booking not found.");
        }

        // Capture old values BEFORE editing
        String oldRoom    = booking.getRoomId();
        LocalDateTime oldStart = booking.getStartTime();
        LocalDateTime oldEnd   = booking.getEndTime();
        String oldPurpose      = booking.getPurpose();


        if (userEmail != null && !userEmail.equalsIgnoreCase(booking.getUserId())) {
            throw new Exception("You can edit only your own bookings.");
        }

        if (!"CONFIRMED".equals(booking.getStatus())
                && !"PENDING_PAYMENT".equals(booking.getStatus())) {
            throw new Exception("Only upcoming bookings can be edited.");
        }

        Room newRoom = roomRepo.getById(newRoomId);
        if (newRoom == null) {
            throw new Exception("Selected room does not exist.");
        }

        if (newStart == null || newEnd == null || !newEnd.isAfter(newStart)) {
            throw new Exception("Invalid start/end time.");
        }

        // Check conflicts in the new room, but ignore this same booking
        List<Booking> forRoom = bookingRepo.getBookingsForRoom(newRoomId);
        for (Booking other : forRoom) {
            if (other.getBookingId().equals(bookingId)) continue;

            String status = other.getStatus();
            if ("CANCELLED".equals(status) || "NO_SHOW".equals(status)) continue;

            boolean overlap =
                    !other.getEndTime().isBefore(newStart) &&
                            !other.getStartTime().isAfter(newEnd);

            if (overlap) {
                throw new Exception("Room is not available for the new time.");
            }
        }

        booking.setRoomId(newRoomId);
        booking.setStartTime(newStart);
        booking.setEndTime(newEnd);
        booking.setPurpose(newPurpose);

        saveBookings();

        // ===================== BOOKING EDITED LOG ============================
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println();
        System.out.println("┌──────────────────────────────────────── BOOKING EDITED ─────────────────────────────────────────┐");

        String borderLine = "│ %-12s : %-77s │";

        System.out.println(String.format(borderLine, "BookingID", bookingId));
        System.out.println(String.format(borderLine, "Room", oldRoom + " → " + newRoomId));
        System.out.println(String.format(borderLine, "Start", oldStart + " → " + newStart));
        System.out.println(String.format(borderLine, "End", oldEnd + " → " + newEnd));
        System.out.println(String.format(borderLine, "Purpose", oldPurpose + " → " + newPurpose));
        System.out.println(String.format(borderLine, "Timestamp", now.format(fmt)));

        System.out.println("└──────────────────────────────────────────────────────────────────────────────────────────────────┘");
        System.out.println();
// =======================================================================

    }

    // =========================================================
    //          METHODS USED BY SCENARIO 3 (SENSORS)
    // =========================================================

    /** Simple lookup by ID – used all over Scenario 3. */
    public Booking getBookingById(String id) {
        if (id == null) return null;
        // BookingRepository currently exposes findById()
        return bookingRepo.findById(id);
    }

    /**
     * When user successfully checks in (via sensor or Check-In button),
     * we treat the deposit as applied and mark the booking IN_USE.
     */
    public void applyDepositToFinalCost(String bookingId) {
        Booking b = getBookingById(bookingId);
        if (b == null) return;

        b.setStatus("IN_USE");
        saveBookings();
    }

    /**
     * When the no-show timer fires, we mark booking as NO_SHOW and
     * forfeit the deposit.
     */
    public void markDepositForfeited(String bookingId) {
        Booking b = getBookingById(bookingId);
        if (b == null) return;

        b.setStatus("NO_SHOW");
        b.setPaymentStatus("FORFEITED");
        saveBookings();
    }

    /**
     * Used by RoomStatusManager + RoomOccupancyPopup to figure out
     * whether there is an active booking for a room at a given time.
     */
    public Booking getActiveBookingForRoom(String roomId, LocalDateTime now) {
        if (roomId == null || now == null) return null;

        List<Booking> forRoom = bookingRepo.getBookingsForRoom(roomId);
        for (Booking b : forRoom) {

            String status = b.getStatus();
            if ("CANCELLED".equals(status) || "NO_SHOW".equals(status)) continue;

            boolean inWindow =
                    !now.isBefore(b.getStartTime()) && !now.isAfter(b.getEndTime());

            if (inWindow) return b;
        }
        return null;
    }
}
