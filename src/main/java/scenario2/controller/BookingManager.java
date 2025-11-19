package scenario2.controller;

import shared.model.Booking;
import shared.model.Room;
import shared.util.CSVHelper;
import scenario2.builder.BookingBuilder;

import java.time.LocalDateTime;
import java.util.*;

/**
 * BookingManager (Singleton)
 * ------------------------------------------------------------
 * Core controller for Scenario 2 – Room Booking & Payment.
 *
 * <p>This class implements the <b>Singleton Design Pattern</b> to ensure
 * there is only one global booking controller managing:
 * <ul>
 *   <li>Room availability</li>
 *   <li>Booking creation & editing</li>
 *   <li>Deposit calculation (Req4)</li>
 *   <li>Payment verification</li>
 *   <li>CSV-based persistence simulating a database</li>
 * </ul>
 *
 * <p>Collaborating patterns:
 * <ul>
 *   <li><b>Builder Pattern</b> → Construct Booking objects cleanly</li>
 *   <li><b>Strategy Pattern-ready</b> → Payment behavior is encapsulated inside PaymentService</li>
 *   <li><b>Singleton</b> → One shared BookingManager instance</li>
 * </ul>
 *
 * <p>This manager is called by the JavaFX UI layer (BookingViewFX)
 * and by the Scenario2 payment workflow.
 *
 * <p><b>Requirements Supported:</b>
 * <ul>
 *   <li>Req 3 – Hourly rates per user type</li>
 *   <li>Req 4 – One-hour deposit collected at booking time</li>
 *   <li>Req 7 – View room details</li>
 *   <li>Req 8 – Edit booking</li>
 *   <li>Req 9 – Extend booking</li>
 *   <li>Req 10 – Payment service integration</li>
 * </ul>
 */
public class BookingManager {

    /** Singleton instance */
    private static BookingManager instance;

    /** CSV files simulate the database as per Deliverable-2 instructions. */
    private final String BOOKING_CSV = "src/shared/data/bookings.csv";
    private final String ROOM_CSV    = "src/shared/data/rooms.csv";

    /** In-memory collections loaded from CSV. */
    private ArrayList<Booking> bookings = new ArrayList<>();
    private ArrayList<Room> rooms       = new ArrayList<>();

    /** Internal payment component (Strategy-ready). */
    private PaymentService paymentService;


    // ============================================================
    //                SINGLETON ENTRY POINT
    // ============================================================

    /**
     * Returns the global BookingManager instance.
     *
     * @return single instance of BookingManager
     */
    public static synchronized BookingManager getInstance() {
        if (instance == null) {
            instance = new BookingManager();
        }
        return instance;
    }

    /**
     * Private constructor for the Singleton Pattern.
     * Loads rooms + bookings from CSV (simulated database).
     */
    private BookingManager() {
        this.paymentService = new PaymentService();
        loadInitialData();
    }

    // ============================================================
    //                     DATA INITIALIZATION
    // ============================================================

    /**
     * Loads room and booking data from CSV files.
     * If empty, generates sample rooms.
     */
    private void loadInitialData() {
        try {
            bookings = CSVHelper.loadBookings(BOOKING_CSV);
            rooms    = CSVHelper.loadRooms(ROOM_CSV);

            if (rooms.isEmpty()) {
                createSampleRooms();
            }
        } catch (Exception e) {
            System.out.println("[INFO] Starting with empty CSV data.");
            createSampleRooms();
        }
    }

    /**
     * Creates a default set of rooms when no CSV data exists.
     * Helps the UI function immediately.
     */
    private void createSampleRooms() {
        rooms.add(new Room("R101", "York Room", 10, "First Floor",
                "Projector,Whiteboard", "Main Building"));
        rooms.add(new Room("R102", "Lassonde Room", 20, "Second Floor",
                "Projector,VideoConference", "Lassonde Building"));
        rooms.add(new Room("R201", "Bergeron Room", 15, "Third Floor",
                "Whiteboard", "Bergeron Center"));
        rooms.add(new Room("R202", "Scott Library Room", 30, "Library Wing",
                "Projector,VideoConference,Whiteboard", "Scott Library"));
        rooms.add(new Room("R301", "Accolade Room", 25, "East Wing",
                "Projector,VideoConference", "Accolade Building"));
        rooms.add(new Room("R302", "Student Center Room", 8, "Student Center",
                "Whiteboard", "Student Center"));

        saveRooms();
    }

    // ============================================================
    //                       PAYMENT SERVICE
    // ============================================================

    /**
     * Inner service encapsulating payment behavior.
     *
     * <p>This class is intentionally isolated so it can later be replaced
     * with a full <b>Strategy Pattern</b>:
     * <ul>
     *   <li>CreditCardPaymentStrategy</li>
     *   <li>InstitutionBillingStrategy</li>
     *   <li>DigitalWalletStrategy</li>
     * </ul>
     */
    private class PaymentService {

        /** Payment outcome enum. */
        public enum PaymentStatus { PENDING, APPROVED, FAILED }

        /**
         * Returns hourly rate per user type (Req3).
         *
         * @param userType Student/Faculty/Staff/Partner
         * @return hourly rate
         */
        public double getHourlyRate(String userType) {
            String t = userType == null ? "" : userType.trim().toLowerCase();
            return switch (t) {
                case "student" -> 20.0;
                case "faculty" -> 30.0;
                case "staff"   -> 40.0;
                case "partner" -> 50.0;
                default        -> 50.0;
            };
        }

        /**
         * Processes deposit payments.
         * Only performs simple validation for this course project.
         *
         * @param userType type of user
         * @param amount one-hour deposit
         * @param userId user email
         * @return APPROVED / FAILED / PENDING
         */
        public PaymentStatus processDeposit(String userType, double amount, String userId) {
            if (amount <= 0) return PaymentStatus.FAILED;

            if ("partner".equalsIgnoreCase(userType) && amount >= 50.0) {
                return PaymentStatus.PENDING; // requires manual review
            }

            return PaymentStatus.APPROVED;
        }

        /**
         * Req4: Deposit = 1 × hourly rate, independent of duration.
         */
        public double calculateDeposit(String userType) {
            return getHourlyRate(userType);
        }

        /**
         * Optional: display total cost estimate in UI.
         */
        public double calculateEstimatedTotal(String userType, long durationHours) {
            if (durationHours <= 0) durationHours = 1;
            return getHourlyRate(userType) * durationHours;
        }
    }

    // ============================================================
//                PUBLIC API FOR UI (JavaFX)
// ============================================================

    /**
     * Req4 – Returns the one-hour deposit for a given user type.
     * Independent of booking duration.
     *
     * @param userType Student, Faculty, Staff, Partner, etc.
     * @return fixed 1-hour deposit based on rate table
     */
    public double getDepositForUserType(String userType) {
        return paymentService.calculateDeposit(userType);
    }

    /**
     * Same as getDepositForUserType(), but accepts durationHours
     * to match the assignment requirement signature.
     *
     * <p><b>Important:</b> Duration does NOT change the deposit,
     * deposit is always 1 hour (Req4).
     *
     * @param userType user category
     * @param durationHours any duration (ignored as per Req4)
     * @return fixed 1-hour deposit
     */
    public double getDepositForUserTypeAndDuration(String userType, long durationHours) {
        return paymentService.calculateDeposit(userType);
    }

    /**
     * Returns hourly rate (Req3).
     *
     * @param userType Student/Faculty/Staff/Partner
     * @return hourly rate
     */
    public double getHourlyRateForUserType(String userType) {
        return paymentService.getHourlyRate(userType);
    }

    /**
     * Optional helper:
     * Calculates full estimated cost for the entire duration.
     * Useful if the UI wants to display total price.
     *
     * @param userType type of user
     * @param durationHours total hours
     * @return estimated total cost
     */
    public double getEstimatedTotalForUser(String userType, long durationHours) {
        return paymentService.calculateEstimatedTotal(userType, durationHours);
    }

    /**
     * Wrapper for full room search, mostly used by UI filters.
     * Supports:
     * <ul>
     *   <li>capacity filter</li>
     *   <li>building filter</li>
     *   <li>equipment filter</li>
     *   <li>availability check</li>
     * </ul>
     *
     * @param startTime booking start
     * @param endTime booking end
     * @param capacity minimum required capacity
     * @param building building name filter (optional)
     * @param equipment keyword for equipment (e.g., "projector")
     * @return list of available rooms matching all filters
     */
    public List<Room> searchAvailableRooms(
            LocalDateTime startTime,
            LocalDateTime endTime,
            int capacity,
            String building,
            String equipment
    ) {
        return getAvailableRooms(startTime, endTime, capacity, building, equipment);
    }


    // ============================================================
    //                 BOOKING CREATION (REQ 3, 4, 10)
    // ============================================================

    /**
     * Creates a new booking with:
     * <ul>
     *   <li>validation</li>
     *   <li>room availability check</li>
     *   <li>deposit calculation (Req4)</li>
     *   <li>payment status determination</li>
     *   <li>BookingBuilder to construct the final Booking</li>
     *   <li>CSV persistence</li>
     * </ul>
     *
     * @throws Exception if validation fails
     */
    public Booking bookRoom(
            String roomId,
            String userId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String purpose,
            String userType
    ) throws Exception {

        // 1. Validation
        validateBookingParameters(roomId, startTime, endTime, purpose);

        // 2. Check availability
        if (!isRoomAvailable(roomId, startTime, endTime)) {
            throw new Exception("Room not available for the selected time slot.");
        }

        // 3. Duration restrictions
        long minutes = java.time.Duration.between(startTime, endTime).toMinutes();
        long hours   = (long) Math.ceil(minutes / 60.0);
        if (minutes < 30) throw new Exception("Minimum booking is 30 minutes.");
        if (hours > 4)    throw new Exception("Max booking duration is 4 hours.");

        // 4. Business hours rule
        if (startTime.getHour() < 8 || endTime.getHour() > 20 ||
                (endTime.getHour() == 20 && endTime.getMinute() > 0)) {
            throw new Exception("Bookings allowed only between 8:00 AM – 8:00 PM.");
        }

        // 5. Deposit = 1 hour rate (Req4)
        double depositAmount = paymentService.calculateDeposit(userType);

        PaymentService.PaymentStatus paymentStatus =
                paymentService.processDeposit(userType, depositAmount, userId);

        String initialStatus = paymentStatus == PaymentService.PaymentStatus.APPROVED
                ? "CONFIRMED"
                : "PENDING_PAYMENT";

        // 6. Construct booking using Builder Pattern
        String bookingId = "B" + System.currentTimeMillis();

        Booking newBooking = new BookingBuilder()
                .setBookingId(bookingId)
                .setRoomId(roomId)
                .setUserId(userId)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setPurpose(purpose)
                .setStatus(initialStatus)
                .setPaymentStatus(paymentStatus.name())
                .setDepositAmount(depositAmount)
                .build();

        bookings.add(newBooking);
        saveBookings();

        return newBooking;
    }

    // ============================================================
    //                   EDIT BOOKING (REQ 8)
    // ============================================================

    /**
     * Allows a user to modify an existing booking BEFORE it starts.
     * Room, date, time, and purpose can be updated.
     *
     * @return updated Booking object
     * @throws Exception if any validation fails
     */
    public Booking editBooking(
            String bookingId,
            String userId,
            String newRoomId,
            LocalDateTime newStart,
            LocalDateTime newEnd,
            String newPurpose
    ) throws Exception {

        Booking booking = findBookingById(bookingId);
        if (booking == null) throw new Exception("Booking not found.");

        if (!booking.getUserId().equals(userId)) {
            throw new Exception("You can edit only your own bookings.");
        }

        if (!LocalDateTime.now().isBefore(booking.getStartTime())) {
            throw new Exception("Cannot edit a booking that has already started.");
        }

        validateBookingParameters(newRoomId, newStart, newEnd, newPurpose);

        if (!isRoomAvailable(newRoomId, newStart, newEnd, bookingId)) {
            throw new Exception("Room unavailable for the updated time.");
        }

        // Update (deposit stays same as Req4)
        booking.setRoomId(newRoomId);
        booking.setStartTime(newStart);
        booking.setEndTime(newEnd);
        booking.setPurpose(newPurpose);

        saveBookings();
        return booking;
    }

    // ============================================================
    //                     CANCEL BOOKING
    // ============================================================

    /**
     * Cancels a booking if done at least 2 hours before start.
     * Refund is issued automatically if payment was approved.
     *
     * @return true on success
     */
    public boolean cancelBooking(String bookingId, String userId) throws Exception {
        Booking booking = findBookingById(bookingId);
        if (booking == null) throw new Exception("Booking not found.");

        if (!booking.getUserId().equals(userId)) {
            throw new Exception("You can cancel only your own bookings.");
        }

        if (LocalDateTime.now().plusHours(2).isAfter(booking.getStartTime())) {
            throw new Exception("Cannot cancel within 2 hours of start time.");
        }

        if ("APPROVED".equals(booking.getPaymentStatus())) {
            System.out.println("[REFUND] Refund issued: $" + booking.getDepositAmount());
        }

        booking.setStatus("CANCELLED");
        saveBookings();
        return true;
    }

    // ============================================================
    //                    USER'S BOOKINGS
    // ============================================================

    /**
     * Returns all active bookings (CONFIRMED or PENDING_PAYMENT)
     * for the given user.
     */
    public List<Booking> getUserBookings(String userId) {
        List<Booking> list = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getUserId().equals(userId) &&
                    (b.getStatus().equals("CONFIRMED") ||
                            b.getStatus().equals("PENDING_PAYMENT"))) {

                list.add(b);
            }
        }
        list.sort(Comparator.comparing(Booking::getStartTime));
        return list;
    }

    // ============================================================
    //                  SEARCH / AVAILABILITY
    // ============================================================

    /**
     * Full room search with support for:
     * <ul>
     *   <li>capacity filter</li>
     *   <li>building filter</li>
     *   <li>equipment filter</li>
     *   <li>time availability</li>
     * </ul>
     */
    public List<Room> getAvailableRooms(
            LocalDateTime start,
            LocalDateTime end,
            int capacity,
            String building,
            String equipment
    ) {
        List<Room> available = new ArrayList<>();

        for (Room r : rooms) {

            boolean matchCapacity =
                    r.getCapacity() >= capacity;

            boolean matchBuilding =
                    building == null || building.isEmpty() ||
                            r.getBuilding().equalsIgnoreCase(building);

            boolean matchEquipment =
                    equipment == null || equipment.isEmpty() ||
                            r.getAmenities().toLowerCase().contains(equipment.toLowerCase());

            boolean matchAvailability =
                    isRoomAvailable(r.getRoomId(), start, end);

            if (matchCapacity && matchBuilding && matchEquipment && matchAvailability) {
                available.add(r);
            }
        }
        return available;
    }

    // ============================================================
    //                INTERNAL VALIDATION UTILS
    // ============================================================

    /** Validates room ID, time range, and purpose. */
    private void validateBookingParameters(
            String roomId,
            LocalDateTime start,
            LocalDateTime end,
            String purpose
    ) throws Exception {

        if (roomId == null || roomId.isBlank())
            throw new Exception("Room ID is required.");

        if (start == null || end == null)
            throw new Exception("Start and end times are required.");

        if (start.isBefore(LocalDateTime.now()))
            throw new Exception("Cannot book in the past.");

        if (!end.isAfter(start))
            throw new Exception("End time must be after start time.");

        if (purpose == null || purpose.isBlank())
            throw new Exception("Purpose is required.");

        if (findRoomById(roomId) == null)
            throw new Exception("Room not found.");
    }

    /** Availability check for new bookings. */
    private boolean isRoomAvailable(
            String roomId, LocalDateTime start, LocalDateTime end
    ) {
        return isRoomAvailable(roomId, start, end, null);
    }

    /** Availability check that excludes a booking ID (used for editing). */
    private boolean isRoomAvailable(
            String roomId, LocalDateTime start, LocalDateTime end, String excludeBookingId
    ) {
        for (Booking b : bookings) {
            if (!b.getRoomId().equals(roomId)) continue;

            if (excludeBookingId != null &&
                    excludeBookingId.equals(b.getBookingId())) {
                continue;
            }

            if (!b.getStatus().equals("CONFIRMED") &&
                    !b.getStatus().equals("PENDING_PAYMENT")) {
                continue;
            }

            boolean overlap =
                    start.isBefore(b.getEndTime()) &&
                            end.isAfter(b.getStartTime());

            if (overlap) return false;
        }
        return true;
    }

    // ============================================================
    //                        FINDERS
    // ============================================================

    private Booking findBookingById(String id) {
        for (Booking b : bookings) {
            if (b.getBookingId().equals(id)) return b;
        }
        return null;
    }

    private Room findRoomById(String id) {
        for (Room r : rooms) {
            if (r.getRoomId().equals(id)) return r;
        }
        return null;
    }

    // ============================================================
    //                    CSV PERSISTENCE WRAPPERS
    // ============================================================

    private void saveBookings() {
        try {
            CSVHelper.saveBookings(BOOKING_CSV, bookings);
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to save bookings: " + e.getMessage());
        }
    }


    private void saveRooms() {
        try {
            CSVHelper.saveRooms(ROOM_CSV, rooms);
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to save rooms: " + e.getMessage());
        }
    }


    // ============================================================
    //                     PUBLIC LIST ACCESSORS
    // ============================================================

    /** Returns all rooms in the system. */
    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms);
    }

    /** Returns one room by its ID. */
    public Room getRoomById(String id) {
        return findRoomById(id);
    }

    /** Returns all buildings (for filter UI). */
    public List<String> getAvailableBuildings() {
        Set<String> set = new HashSet<>();
        for (Room r : rooms) set.add(r.getBuilding());
        return new ArrayList<>(set);
    }

    /** Returns all equipment types (for filter UI). */
    public List<String> getAvailableEquipment() {
        Set<String> set = new HashSet<>();
        for (Room r : rooms) {
            for (String a : r.getAmenities().split(",")) {
                set.add(a.trim());
            }
        }
        return new ArrayList<>(set);
    }
}
