package shared.util;

import shared.model.*;
import scenario1.builder.UserBuilder;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * CSVHelper – Unified CSV Persistence Engine (All Scenarios)
 * ------------------------------------------------------------------------
 * <p>This utility class provides <b>centralized CSV loading and saving</b> for
 * every data model in the Conference Room Scheduler system:</p>
 *
 * <ul>
 *     <li><b>Users</b> – Scenario 1 (Registration & Account Management)</li>
 *     <li><b>Bookings</b> – Scenario 2 (Room Booking & Payment)</li>
 *     <li><b>Rooms</b> – Scenario 3 & 4 (Live Status, Admin Management)</li>
 * </ul>
 *
 * <h2>Purpose</h2>
 * <ul>
 *     <li>Ensures consistent CSV formats across the entire project.</li>
 *     <li>Prevents file corruption by enforcing strict parsing rules.</li>
 *     <li>Handles all read/write operations for User, Booking, and Room models.</li>
 *     <li>Provides safe escaping mechanisms (e.g., purpose field in bookings).</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li><b>Utility / Facade-Style Class</b>: Central access point for persistence.</li>
 *     <li>Used by Singleton managers (UserManager, BookingManager, RoomRepository).</li>
 *     <li>Supports Builder pattern via {@link scenario1.builder.UserBuilder}.</li>
 * </ul>
 *
 * <h2>CSV Schemas Supported</h2>
 * <p><b>Users CSV</b> — extended format used in Scenario 1 & 4:</p>
 * <pre>
 * userId,name,email,passwordHash,userType,orgId,studentId,isActive,createdAt
 * </pre>
 *
 * <p><b>Bookings CSV</b> — safe format with purpose escaping:</p>
 * <pre>
 * bookingId,roomId,userId,startTime,endTime,purpose,status,paymentStatus,depositAmount
 * </pre>
 *
 * <p><b>Rooms CSV</b> — enriched to include room status (Scenario 3 integration):</p>
 * <pre>
 * roomId,roomName,capacity,location,amenities,building,status
 * </pre>
 *
 * <h2>Scenarios Supported</h2>
 * <ul>
 *     <li><b>Scenario 1</b> – User creation, login, profile updates</li>
 *     <li><b>Scenario 2</b> – Booking creation, payment calculations</li>
 *     <li><b>Scenario 3</b> – No-show detection, live room monitoring</li>
 *     <li><b>Scenario 4</b> – Admin dashboard room/user management</li>
 * </ul>
 *
 * <h2>Notes</h2>
 * <ul>
 *     <li>All file paths are computed externally by managers using this helper.</li>
 *     <li>Handles missing files gracefully by auto-creating them.</li>
 *     <li>Ensures backward compatibility with earlier CSV versions.</li>
 * </ul>
 */

public class CSVHelper {

    // ============================================================
    // USERS  (Sharwin’s correct implementation to prevent corruption)
    // ============================================================

    public static ArrayList<User> loadUsers(String path) throws Exception {

        ArrayList<User> list = new ArrayList<>();
        File file = new File(path);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            return list;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue;
                if (line.toLowerCase().startsWith("userid,")) continue;

                String[] p = line.split(",");

                try {
                    UUID id          = UUID.fromString(p[0].trim());
                    String name      = p[1].trim();
                    String email     = p[2].trim();
                    String pass      = p[3].trim();
                    UserType type    = UserType.valueOf(p[4].trim());
                    String orgId     = p[5].trim();
                    String stuId     = p[6].trim();
                    boolean active   = Boolean.parseBoolean(p[7].trim());
                    LocalDateTime createdAt = LocalDateTime.parse(p[8].trim());

                    SystemUser u = new SystemUser(
                            name,
                            email,
                            pass,
                            type,
                            orgId,
                            stuId.isEmpty() ? null : stuId
                    );

                    u.setUserId(id);
                    u.setCreatedAt(createdAt);
                    if (!active) u.deactivate();

                    list.add(u);
                }
                catch (Exception ex) {
                    System.out.println("[CSV ERROR] Failed: " + line);
                    ex.printStackTrace();
                }
            }
        }

        return list;
    }

    public static void saveUsers(String path, ArrayList<User> list) throws Exception {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {

            bw.write("userId,name,email,passwordHash,userType,orgId,studentId,isActive,createdAt");
            bw.newLine();

            for (User base : list) {
                SystemUser u = (SystemUser) base;

                bw.write(String.join(",",
                        u.getUserId().toString(),
                        u.getName(),
                        u.getEmail(),
                        u.getPasswordHash(),
                        u.getType().name(),
                        u.getOrgId(),
                        u.getStudentId() == null ? "" : u.getStudentId(),
                        Boolean.toString(u.isActive()),
                        u.getCreatedAt().toString()
                ));
                bw.newLine();
            }
        }
    }

    // ============================================================
    // BOOKINGS (Your safe version with purpose escaping)
    // ============================================================

    public static ArrayList<Booking> loadBookings(String path) throws Exception {

        ArrayList<Booking> list = new ArrayList<>();
        File file = new File(path);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            return list;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue;
                if (line.startsWith("bookingId")) continue;

                String[] p = line.split(",");

                if (p.length < 9) continue;

                String purpose = p[5].replace(";", ",");

                Booking b = new Booking(
                        p[0],   // bookingId
                        p[1],   // roomId
                        p[2],   // userId
                        LocalDateTime.parse(p[3].trim()),
                        LocalDateTime.parse(p[4].trim()),
                        purpose,
                        p[6],   // status
                        p[7],   // paymentStatus
                        Double.parseDouble(p[8].trim())
                );

                list.add(b);
            }
        }

        return list;
    }

    public static void saveBookings(String path, List<Booking> list) throws Exception {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {

            bw.write("bookingId,roomId,userId,startTime,endTime,purpose,status,paymentStatus,depositAmount");
            bw.newLine();

            for (Booking b : list) {
                String purpose = b.getPurpose().replace(",", ";");

                bw.write(String.join(",",
                        b.getBookingId(),
                        b.getRoomId(),
                        b.getUserId(),
                        b.getStartTime().toString(),
                        b.getEndTime().toString(),
                        purpose,
                        b.getStatus(),
                        b.getPaymentStatus(),
                        Double.toString(b.getDepositAmount())
                ));
                bw.newLine();
            }
        }
    }

    // ============================================================
    // ROOMS (Your version — includes STATUS column for Scenario 3)
    // ============================================================

    public static ArrayList<Room> loadRooms(String path) throws Exception {

        ArrayList<Room> list = new ArrayList<>();
        File file = new File(path);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            return list;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                String[] p = line.split(",");

                Room r = new Room(
                        p[0],
                        p.length > 1 ? p[1] : "",
                        p.length > 2 ? Integer.parseInt(p[2]) : 0,
                        p.length > 3 ? p[3] : "",
                        p.length > 4 ? p[4] : "",
                        p.length > 5 ? p[5] : "",
                        p.length > 6 ? p[6] : "AVAILABLE"
                );

                list.add(r);
            }
        }

        return list;
    }

    public static void saveRooms(String path, List<Room> list) throws Exception {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {

            for (Room r : list) {
                bw.write(r.toString());
                bw.newLine();
            }
        }
    }
}
