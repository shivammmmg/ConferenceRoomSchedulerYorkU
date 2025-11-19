package shared.util;

import shared.model.*;
import scenario1.builder.UserBuilder;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

/**
 * CSVHelper
 * ----------
 * Handles reading/writing concrete SystemUser, Booking, and Room objects.
 *
 * USERS CSV FORMAT (NEW ORDER):
 * 0: name
 * 1: email
 * 2: passwordHash
 * 3: userType
 * 4: orgId
 * 5: studentId
 * 6: isActive
 * 7: createdAt
 * 8: userId
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
 * ROOMS CSV FORMAT (matches Room.toString()):
 * 0: roomId
 * 1: roomName
 * 2: capacity
 * 3: location
 * 4: amenities
 * 5: building
 */
public class CSVHelper {

    // ============================================================
    // USERS
    // ============================================================

    public static ArrayList<User> loadUsers(String path) throws Exception {

        ArrayList<User> list = new ArrayList<>();
        File file = new File(path);

        System.out.println(">>> [LOAD USERS] Reading from: " + file.getAbsolutePath());

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            return list;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = br.readLine()) != null) {

                // Skip empty rows
                if (line.trim().isEmpty()) continue;

                // Skip header row
                if (line.toLowerCase().startsWith("name,email")) continue;

                String[] p = line.split(",");

                try {
                    String name     = p.length > 0 ? p[0].trim() : "";
                    String email    = p.length > 1 ? p[1].trim() : "";
                    String pass     = p.length > 2 ? p[2].trim() : "";
                    String type     = p.length > 3 ? p[3].trim() : "PARTNER";
                    String orgId    = p.length > 4 ? p[4].trim() : "";
                    String stuId    = p.length > 5 ? p[5].trim() : "";
                    boolean active  = p.length > 6 && Boolean.parseBoolean(p[6].trim());
                    LocalDateTime created =
                            p.length > 7 ? LocalDateTime.parse(p[7].trim()) : LocalDateTime.now();
                    String id       = p.length > 8 ? p[8].trim() : UUID.randomUUID().toString();

                    SystemUser u = (SystemUser) new UserBuilder()
                            .setName(name)
                            .setEmail(email)
                            .setPassword(pass)
                            .setUserType(UserType.valueOf(type.toUpperCase()))
                            .setOrgId(orgId)
                            .setStudentId(stuId)
                            .build();

                    u.setUserId(UUID.fromString(id));
                    u.setCreatedAt(created);

                    if (!active) u.deactivate();

                    list.add(u);
                }
                catch (Exception ex) {
                    System.out.println("[CSV ERROR] Failed to parse user row: " + line);
                    ex.printStackTrace();
                }
            }
        }

        return list;
    }


    public static void saveUsers(String path, ArrayList<User> list) throws Exception {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {

            // NEW HEADER ORDER
            bw.write("name,email,passwordHash,userType,orgId,studentId,isActive,createdAt,userId");
            bw.newLine();

            for (User base : list) {
                SystemUser u = (SystemUser) base;

                bw.write(String.join(",",
                        u.getName(),
                        u.getEmail(),
                        u.getPasswordHash(),
                        u.getType().name(),
                        u.getOrgId(),
                        u.getStudentId() == null ? "" : u.getStudentId(),
                        Boolean.toString(u.isActive()),
                        u.getCreatedAt().toString(),
                        u.getUserId().toString()
                ));
                bw.newLine();
            }
        }
    }


    // ============================================================
    // BOOKINGS
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

                String[] p = line.split(",");

                Booking b = new Booking(
                        p[0],
                        p[1],
                        p[2],
                        LocalDateTime.parse(p[3].trim()),
                        LocalDateTime.parse(p[4].trim()),
                        p[5],
                        p[6],
                        p[7],
                        Double.parseDouble(p[8])
                );

                list.add(b);
            }
        }

        return list;
    }


    public static void saveBookings(String path, ArrayList<Booking> list) throws Exception {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {

            for (Booking b : list) {
                bw.write(b.toString());
                bw.newLine();
            }
        }
    }


    // ============================================================
    // ROOMS
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
                        p.length > 5 ? p[5] : ""
                );

                list.add(r);
            }
        }

        return list;
    }


    public static void saveRooms(String path, ArrayList<Room> list) throws Exception {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {

            for (Room r : list) {
                bw.write(r.toString());
                bw.newLine();
            }
        }
    }

}
