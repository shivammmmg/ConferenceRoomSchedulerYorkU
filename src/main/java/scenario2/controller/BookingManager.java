package scenario2.controller;

import scenario2.builder.BookingBuilder;
import shared.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Central business logic for Scenario 2 (Room Booking & Payment).
 *
 * - Talks to RoomRepository (rooms.csv)
 * - Talks to BookingRepository (bookings.csv)
 * - Provides methods used by BookingFX + Scenario 3 (sensor/observer code)
 */
public class BookingManager {

    // =========================================================
    //                    SINGLETON
    // =========================================================
    private static BookingManager instance;

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
