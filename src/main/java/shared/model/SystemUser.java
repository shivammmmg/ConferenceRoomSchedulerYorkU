package shared.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * SystemUser – Concrete User Model (All Scenarios)
 * =============================================================================
 * <p>This class represents the <b>concrete</b> user object stored, loaded,
 * and manipulated throughout the entire Conference Room Scheduler system.</p>
 *
 * <h2>Purpose</h2>
 * <ul>
 *     <li>Implements the full user profile used during runtime.</li>
 *     <li>Extends {@link User} by adding role-specific attributes.</li>
 *     <li>Acts as the universal user type for ALL scenarios.</li>
 * </ul>
 *
 * <h2>Used In</h2>
 * <ul>
 *     <li><b>Scenario 1:</b> Registration, Login, Profile Editing</li>
 *     <li><b>Scenario 2:</b> Booking creation (stores userId)</li>
 *     <li><b>Scenario 3:</b> Check-in verification / sensor events</li>
 *     <li><b>Scenario 4:</b> Admin account creation & management</li>
 * </ul>
 *
 * <h2>Replaces Old Design</h2>
 * <p>This class unifies all previously separate user subclasses:</p>
 * <ul>
 *     <li>StudentUser</li>
 *     <li>FacultyUser</li>
 *     <li>StaffUser</li>
 *     <li>PartnerUser</li>
 * </ul>
 * <p>→ All user roles are now handled via {@link UserType} + optional fields.</p>
 *
 * <h2>Fields Added Beyond User</h2>
 * <ul>
 *     <li><b>UserType type</b> – role (Student, Admin, Staff, etc.)</li>
 *     <li><b>orgId</b> – optional organization identifier</li>
 *     <li><b>studentId</b> – required only for STUDENT accounts</li>
 * </ul>
 *
 * <h2>Persistence Notes</h2>
 * <ul>
 *     <li>Mapped directly to users.csv via {@link shared.util.CSVHelper}.</li>
 *     <li>Setters like setUserId() and setCreatedAt() are required when
 *         reloading saved data from CSV.</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li><b>Builder Pattern</b> – {@code UserBuilder} constructs SystemUser safely.</li>
 *     <li><b>Singleton Pattern</b> – UserManager stores all SystemUser instances.</li>
 *     <li><b>Observer (Scenario 3)</b> – User identity used to validate check-ins.</li>
 * </ul>
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
