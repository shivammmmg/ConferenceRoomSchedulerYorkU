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
 * USERS CSV FORMAT:
 * 0: userId (UUID)
 * 1: name
 * 2: email
 * 3: passwordHash
 * 4: userType
 * 5: orgId
 * 6: studentId
 * 7: isActive
 * 8: createdAt (ISO string)
 *
 * BOOKINGS CSV FORMAT (matches Booking.toString()):
 * 0: bookingId
 * 1: roomId
 * 2: userId
 * 3: startTime (yyyy-MM-dd HH:mm)
 * 4: endTime (yyyy-MM-dd HH:mm)
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

                if (line.trim().isEmpty()) continue;

                String[] p = line.split(",");

                String name     = p.length > 0 ? p[0] : "";
                String email    = p.length > 1 ? p[1] : "";
                String pass     = p.length > 2 ? p[2] : "";
                String type     = p.length > 3 ? p[3] : "PARTNER";
                String orgId    = p.length > 4 ? p[4] : "";
                String stuId    = p.length > 5 ? p[5] : "";
                boolean active  = p.length > 6 && Boolean.parseBoolean(p[6]);
                LocalDateTime created = p.length > 7 ? LocalDateTime.parse(p[7]) : LocalDateTime.now();
                String id       = p.length > 8 ? p[8] : UUID.randomUUID().toString();


                // Build SystemUser using Builder
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
                        u.getUserId().toString()     // New last column
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
                        p[0],                                      // bookingId
                        p[1],                                      // roomId
                        p[2],                                      // userId
                        LocalDateTime.parse(p[3].trim()),          // startTime
                        LocalDateTime.parse(p[4].trim()),          // endTime
                        p[5],                                      // purpose
                        p[6],                                      // status
                        p[7],                                      // paymentStatus
                        Double.parseDouble(p[8])                   // depositAmount
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
                        p[0],                                   // roomId
                        p.length > 1 ? p[1] : "",               // roomName
                        p.length > 2 ? Integer.parseInt(p[2]) : 0,
                        p.length > 3 ? p[3] : "",               // location
                        p.length > 4 ? p[4] : "",               // amenities
                        p.length > 5 ? p[5] : ""                // building
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
