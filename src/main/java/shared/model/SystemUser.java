package shared.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * =============================================================================
 * CLASS: SystemUser
 * BASE CLASS: User (abstract)
 * =============================================================================
 *
 * Represents a concrete user object stored in the system.
 * This replaces all old subclasses:
 *   - StudentUser
 *   - FacultyUser
 *   - StaffUser
 *   - PartnerUser
 *
 * Now everything is handled through:
 *   - UserType enum
 *   - Optional studentId
 *   - Optional orgId (for future scenarios)
 *
 * =============================================================================
 */
public class SystemUser extends User {

    private UserType type;
    private String orgId;       // optional (future use)
    private String studentId;   // only used when type = STUDENT

    /**
     * Launcher constructor — used by UserBuilder and CSVHelper.
     */
    public SystemUser(
            String name,
            String email,
            String passwordHash,
            UserType type,
            String orgId,
            String studentId
    ) {
        super(name, email, passwordHash);
        this.type = type;
        this.orgId = (orgId == null ? "" : orgId);
        this.studentId = studentId;
    }

    // ===========================
    // GETTERS
    // ===========================
    public UserType getType() {
        return type;
    }

    public String getOrgId() {
        return orgId;
    }

    public String getStudentId() {
        return studentId;
    }

    // ===========================
    // SETTERS (used for profile editing)
    // ===========================
    public void setType(UserType type) {
        this.type = type;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }


    // ===========================
    // REQUIRED BY CSVHelper
    // (restoring saved data from CSV file)
    // ===========================
    public void setUserId(UUID id) {
        this.userId = id;
    }

    public void setCreatedAt(LocalDateTime time) {
        this.createdAt = time;
    }


    // ===========================
    // EXPORT FOR CSV
    // ===========================
    @Override
    public String toString() {
        return userId + "," +
                name + "," +
                email + "," +
                passwordHash + "," +
                type.name() + "," +
                orgId + "," +
                (studentId == null ? "" : studentId) + "," +
                isActive + "," +
                createdAt;
    }

}
