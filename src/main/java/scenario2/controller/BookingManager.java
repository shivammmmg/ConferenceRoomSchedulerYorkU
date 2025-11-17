package scenario2.controller;

import scenario2.builder.BookingBuilder;
import shared.model.Booking;
import shared.model.Room;
import shared.util.CSVHelper;

import java.time.LocalDateTime;
import java.util.*;

/**
 * EECS 3311 - YorkU Conference Room Scheduler
 * Singleton Pattern for Booking Management with Payment System
 */
public class BookingManager {
    private static BookingManager instance;
    private final String BOOKING_CSV = "src/shared/data/bookings.csv";
    private final String ROOM_CSV = "src/shared/data/rooms.csv";

    private ArrayList<Booking> bookings = new ArrayList<>();
    private ArrayList<Room> rooms = new ArrayList<>();
    private PaymentService paymentService;

    private BookingManager() {
        this.paymentService = new PaymentService();
        loadInitialData();
    }

    public static synchronized BookingManager getInstance() {
        if (instance == null)
            instance = new BookingManager();
        return instance;
    }

    private void loadInitialData() {
        try {
            bookings = CSVHelper.loadBookings(BOOKING_CSV);
            rooms = CSVHelper.loadRooms(ROOM_CSV);

            // Create sample rooms if none exist
            if (rooms.isEmpty()) {
                createSampleRooms();
            }
        } catch (Exception e) {
            System.out.println("[INFO] Starting with fresh booking data.");
            createSampleRooms();
        }
    }

    private void createSampleRooms() {
        rooms.add(new Room("R101", "York Room", 10, "First Floor", "Projector,Whiteboard", "Main Building"));
        rooms.add(new Room("R102", "Lassonde Room", 20, "Second Floor", "Projector,VideoConference", "Lassonde Building"));
        rooms.add(new Room("R201", "Bergeron Room", 15, "Third Floor", "Whiteboard", "Bergeron Center"));
        rooms.add(new Room("R202", "Scott Library Room", 30, "Library Wing", "Projector,VideoConference,Whiteboard", "Scott Library"));
        rooms.add(new Room("R301", "Accolade Room", 25, "East Wing", "Projector,VideoConference", "Accolade Building"));
        rooms.add(new Room("R302", "Student Center Room", 8, "Student Center", "Whiteboard", "Student Center"));
        saveRooms();
    }

    // =====================================================
    // Payment Service Inner Class
    // =====================================================
    private class PaymentService {
        public enum PaymentStatus { PENDING, APPROVED, FAILED }

        public PaymentStatus processDeposit(String userType, double amount, String userId) {
            // Simulate payment processing
            System.out.println("[PAYMENT] Processing deposit of $" + amount + " for " + userType + " user: " + userId);

            // Simulate different outcomes based on user type and amount
            if (amount > 100) {
                return PaymentStatus.PENDING; // Large amounts need manual review
            } else if (userType.equalsIgnoreCase("student") && amount > 50) {
                return PaymentStatus.FAILED; // Students have lower limits
            } else {
                return PaymentStatus.APPROVED; // Most payments approved
            }
        }

        public double calculateDeposit(String userType, long durationHours) {
            // Different rates for students vs faculty/staff
            double rate = userType.equalsIgnoreCase("student") ? 5.0 : 10.0;
            return rate * durationHours;
        }
    }

    // =====================================================
    // Core Booking Functionalities with Payment
    // =====================================================

    /**
     * Book a room with validation and payment processing
     */
    public Booking bookRoom(String roomId, String userId, LocalDateTime startTime,
                            LocalDateTime endTime, String purpose, String userType) throws Exception {

        // Validation
        validateBookingParameters(roomId, startTime, endTime, purpose);

        // Check room availability
        if (!isRoomAvailable(roomId, startTime, endTime)) {
            throw new Exception("Room not available for the selected time slot.");
        }

        // Check booking duration (max 4 hours)
        long durationHours = java.time.Duration.between(startTime, endTime).toHours();
        if (durationHours > 4) {
            throw new Exception("Booking duration cannot exceed 4 hours.");
        }

        // Check advance booking (max 30 days)
        if (startTime.isAfter(LocalDateTime.now().plusDays(30))) {
            throw new Exception("Cannot book more than 30 days in advance.");
        }

        // Check business hours (8 AM - 8 PM)
        if (startTime.getHour() < 8 || endTime.getHour() > 20 ||
                (endTime.getHour() == 20 && endTime.getMinute() > 0)) {
            throw new Exception("Bookings only allowed between 8:00 AM and 8:00 PM.");
        }

        // Calculate and process deposit
        double depositAmount = paymentService.calculateDeposit(userType, durationHours);
        PaymentService.PaymentStatus paymentStatus = paymentService.processDeposit(userType, depositAmount, userId);

        // Handle payment results
        if (paymentStatus == PaymentService.PaymentStatus.FAILED) {
            throw new Exception("Payment failed. Please try again or contact support.");
        }

        // Create booking
        String bookingId = "B" + System.currentTimeMillis();
        Booking newBooking = new BookingBuilder()
                .setBookingId(bookingId)
                .setRoomId(roomId)
                .setUserId(userId)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setPurpose(purpose)
                .setPaymentStatus(paymentStatus.name())
                .setDepositAmount(depositAmount)
                .build();

        // Only add to confirmed bookings if payment is approved
        if (paymentStatus == PaymentService.PaymentStatus.APPROVED) {
            bookings.add(newBooking);
            saveBookings();
            System.out.println("[INFO] Booking successful: " + bookingId + " | Deposit: $" + depositAmount);
        } else {
            newBooking.setStatus("PENDING_PAYMENT");
            System.out.println("[INFO] Booking pending payment: " + bookingId + " | Deposit: $" + depositAmount);
        }

        return newBooking;
    }

    /**
     * Get available rooms with advanced filtering
     */
    public List<Room> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime,
                                        int capacity, String building, String equipment) {
        List<Room> availableRooms = new ArrayList<>();

        for (Room room : rooms) {
            boolean capacityMatch = room.getCapacity() >= capacity;
            boolean buildingMatch = building == null || building.isEmpty() ||
                    room.getBuilding().equalsIgnoreCase(building);
            boolean equipmentMatch = equipment == null || equipment.isEmpty() ||
                    room.getAmenities().toLowerCase().contains(equipment.toLowerCase());
            boolean available = isRoomAvailable(room.getRoomId(), startTime, endTime);

            if (capacityMatch && buildingMatch && equipmentMatch && available) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }

    /**
     * Enhanced room search with all filters
     */
    public List<Room> searchAvailableRooms(LocalDateTime startTime, LocalDateTime endTime,
                                           int capacity, String building, String equipment) {
        return getAvailableRooms(startTime, endTime, capacity, building, equipment);
    }

    // =====================================================
    // Existing methods (updated as needed)
    // =====================================================

    /**
     * Cancel a booking with refund processing
     */
    public boolean cancelBooking(String bookingId, String userId) throws Exception {
        Booking booking = findBookingById(bookingId);

        if (booking == null) {
            throw new Exception("Booking not found.");
        }

        if (!booking.getUserId().equals(userId)) {
            throw new Exception("You can only cancel your own bookings.");
        }

        // Check cancellation policy (min 2 hours before)
        if (LocalDateTime.now().plusHours(2).isAfter(booking.getStartTime())) {
            throw new Exception("Cannot cancel within 2 hours of booking start time.");
        }

        // Process refund for approved payments
        if ("APPROVED".equals(booking.getPaymentStatus())) {
            System.out.println("[REFUND] Processing refund of $" + booking.getDepositAmount() + " for booking: " + bookingId);
        }

        booking.setStatus("CANCELLED");
        saveBookings();

        System.out.println("[INFO] Booking cancelled: " + bookingId);
        return true;
    }

    /**
     * Get user's bookings including payment status
     */
    public List<Booking> getUserBookings(String userId) {
        List<Booking> userBookings = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getUserId().equals(userId) &&
                    (b.getStatus().equals("CONFIRMED") || b.getStatus().equals("PENDING_PAYMENT"))) {
                userBookings.add(b);
            }
        }
        // Sort by start time (soonest first)
        userBookings.sort((b1, b2) -> b1.getStartTime().compareTo(b2.getStartTime()));
        return userBookings;
    }

    // =====================================================
    // Helper Methods
    // =====================================================

    private void validateBookingParameters(String roomId, LocalDateTime startTime,
                                           LocalDateTime endTime, String purpose) throws Exception {
        if (roomId == null || roomId.trim().isEmpty()) {
            throw new Exception("Room ID is required.");
        }

        if (startTime == null || endTime == null) {
            throw new Exception("Start and end time are required.");
        }

        if (startTime.isBefore(LocalDateTime.now())) {
            throw new Exception("Cannot book in the past.");
        }

        if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
            throw new Exception("End time must be after start time.");
        }

        if (purpose == null || purpose.trim().isEmpty()) {
            throw new Exception("Booking purpose is required.");
        }

        if (findRoomById(roomId) == null) {
            throw new Exception("Room not found.");
        }
    }

    private boolean isRoomAvailable(String roomId, LocalDateTime startTime, LocalDateTime endTime) {
        for (Booking booking : bookings) {
            if (booking.getRoomId().equals(roomId) &&
                    (booking.getStatus().equals("CONFIRMED") || booking.getStatus().equals("PENDING_PAYMENT"))) {
                // Check for time overlap
                if (startTime.isBefore(booking.getEndTime()) && endTime.isAfter(booking.getStartTime())) {
                    return false;
                }
            }
        }
        return true;
    }

    private Booking findBookingById(String bookingId) {
        for (Booking b : bookings) {
            if (b.getBookingId().equals(bookingId)) {
                return b;
            }
        }
        return null;
    }

    private Room findRoomById(String roomId) {
        for (Room r : rooms) {
            if (r.getRoomId().equals(roomId)) {
                return r;
            }
        }
        return null;
    }

    private void saveBookings() {
        CSVHelper.saveBookings(BOOKING_CSV, bookings);
    }

    private void saveRooms() {
        CSVHelper.saveRooms(ROOM_CSV, rooms);
    }

    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms);
    }

    public Room getRoomById(String roomId) {
        return findRoomById(roomId);
    }

    public List<String> getAvailableBuildings() {
        Set<String> buildings = new HashSet<>();
        for (Room room : rooms) {
            buildings.add(room.getBuilding());
        }
        return new ArrayList<>(buildings);
    }

    public List<String> getAvailableEquipment() {
        Set<String> equipment = new HashSet<>();
        for (Room room : rooms) {
            String[] amenities = room.getAmenities().split(",");
            for (String amenity : amenities) {
                equipment.add(amenity.trim());
            }
        }
        return new ArrayList<>(equipment);
    }
}
