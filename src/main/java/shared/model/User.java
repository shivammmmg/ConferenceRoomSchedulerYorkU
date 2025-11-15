package shared.model;

/**
 * =============================================================================
 * ABSTRACT CLASS: User
 * =============================================================================
 *
 * Base class for all system users.
 *
 * IMPORTANT:
 *   - No user type logic lives here anymore.
 *   - No student/faculty/staff fields.
 *   - Concrete user object = SystemUser.
 *
 * This class only stores what's common for ALL users:
 *   ✔ name
 *   ✔ email
 *   ✔ passwordHash    (not raw password)
 *
 * SystemUser extends this class and adds:
 *   ✔ UserType type
 *   ✔ String orgId
 *   ✔ String studentId
 *
 * =============================================================================
 */
public abstract class User {

    protected String name;
    protected String email;
    protected String passwordHash;

    // These three fields are added so UserManager can call getters safely
    // Instead of SystemUser-only fields, these act as "placeholders"
    // SystemUser will override them naturally via its own fields.
    protected UserType type;       // will be used by SystemUser
    protected String orgId;        // optional
    protected String studentId;    // only for STUDENT

    // -----------------------------------
    // Constructor
    // -----------------------------------
    public User(String name, String email, String passwordHash) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;

        // Defaults (SystemUser will override)
        this.type = null;
        this.orgId = null;
        this.studentId = null;
    }

    // -----------------------------------
    // GETTERS
    // -----------------------------------
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    // NEW getters — required because UserManager calls them
    public UserType getType() {
        return type;
    }

    public String getOrgId() {
        return orgId;
    }

    public String getStudentId() {
        return studentId;
    }


    // -----------------------------------
    // SETTERS
    // -----------------------------------
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public void setPasswordHash(String newPasswordHash) {
        this.passwordHash = newPasswordHash;
    }

    // NEW setters — required for profile editing & email update
    public void setType(UserType type) {
        this.type = type;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }


    // -----------------------------------
    // Optional — for debugging
    // -----------------------------------
    @Override
    public String toString() {
        return name + "," + email + "," + passwordHash;
    }
}
