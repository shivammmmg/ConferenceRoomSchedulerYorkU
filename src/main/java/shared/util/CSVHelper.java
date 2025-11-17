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
 * Handles reading/writing concrete SystemUser objects.
 *
 * CSV FORMAT (new):
 * 0: userId (UUID)
 * 1: name
 * 2: email
 * 3: passwordHash
 * 4: userType
 * 5: orgId
 * 6: studentId
 * 7: isActive
 * 8: createdAt (ISO string)
 */
public class CSVHelper {

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

                String[] p = line.split(",");

                // Handle older formats safely
                String id       = p.length > 0 ? p[0] : UUID.randomUUID().toString();
                String name     = p.length > 1 ? p[1] : "";
                String email    = p.length > 2 ? p[2] : "";
                String pass     = p.length > 3 ? p[3] : "";
                String type     = p.length > 4 ? p[4] : "PARTNER";
                String orgId    = p.length > 5 ? p[5] : "";
                String stuId    = p.length > 6 ? p[6] : "";
                boolean active  = p.length > 7 && Boolean.parseBoolean(p[7]);

                LocalDateTime created;
                if (p.length > 8) {
                    created = LocalDateTime.parse(p[8]);
                } else {
                    created = LocalDateTime.now();
                }

                // Build object
                SystemUser u = (SystemUser) new UserBuilder()
                        .setName(name)
                        .setEmail(email)
                        .setPassword(pass)
                        .setUserType(UserType.valueOf(type.toUpperCase()))
                        .setOrgId(orgId)
                        .setStudentId(stuId)
                        .build();

                // Overwrite generated fields
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
}
