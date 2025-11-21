package shared.util;

import shared.model.*;
import scenario1.builder.UserBuilder;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * FINAL MERGED CSVHelper
 * ----------------------
 * USERS CSV FORMAT (Sharwin's correct version):
 * 0: userId
 * 1: name
 * 2: email
 * 3: passwordHash
 * 4: userType
 * 5: orgId
 * 6: studentId
 * 7: isActive
 * 8: createdAt
 *
 * BOOKINGS CSV FORMAT (matches Booking.toString()):
 * 0: bookingId
 * 1: roomId
 * 2: userId
 * 3: startTime
 * 4: endTime
 * 5: purpose
 * 6: status
 * 7: paymentStatus
 * 8: depositAmount
 *
 * ROOMS CSV FORMAT (your extended version with status):
 * 0: roomId
 * 1: roomName
 * 2: capacity
 * 3: location
 * 4: amenities
 * 5: building
 * 6: status
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
