package shared.util;

import shared.model.*;
import scenario1.builder.UserBuilder;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

/**
 * CSVHelper (FINAL FIXED VERSION)
 *
 * Consistent CSV Format:
 * 0: userId
 * 1: name
 * 2: email
 * 3: passwordHash
 * 4: userType
 * 5: orgId
 * 6: studentId
 * 7: isActive
 * 8: createdAt
 */
public class CSVHelper {

    // ============================================================
    // USERS
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
                    String pass      = p[3].trim(); // already hashed
                    UserType type    = UserType.valueOf(p[4].trim());
                    String orgId     = p[5].trim();
                    String stuId     = p[6].trim();
                    boolean active   = Boolean.parseBoolean(p[7].trim());
                    LocalDateTime createdAt = LocalDateTime.parse(p[8].trim());

                    // Build WITHOUT hashing the password again
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

            // Header
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
