package scenario1.builder;

import shared.model.SystemUser;
import shared.model.User;
import shared.model.UserType;

/**
 * UserBuilder – Builder Pattern (Scenario 1)
 *
 * <p>This class implements the <b>Builder Design Pattern</b> to construct
 * {@link shared.model.SystemUser} objects step-by-step during the
 * registration workflow.</p>
 *
 * <h2>Purpose</h2>
 * <ul>
 *     <li>Encapsulates all user attribute construction logic.</li>
 *     <li>Supports optional fields such as orgId and studentId.</li>
 *     <li>Ensures required fields (name, email, password, type) are validated before build().</li>
 *     <li>Provides a clean, fluent API for Scenario 1 – Registration & Account Management.</li>
 * </ul>
 *
 * <h2>How It Fits the System (Scenario 1)</h2>
 * <ul>
 *     <li>Used by UserManager (Singleton) to create verified user accounts.</li>
 *     <li>Ensures SystemUser is created consistently for all account types.</li>
 *     <li>Prepares data before writing to CSV repositories.</li>
 * </ul>
 *
 * <h2>Design Pattern Justification</h2>
 * <p>The Builder pattern avoids telescoping constructors and allows flexible
 * creation of user objects as registration fields evolve (Req1).</p>
 */

public class UserBuilder {

    // Fields to collect
    private String name;
    private String email;
    private String passwordHash;

    // Default user type = PARTNER so nothing breaks
    private UserType type = UserType.PARTNER;

    // Optional fields
    private String orgId = "";
    private String studentId = "";


    // ===========================
    // Fluent setters
    // ===========================

    /**
     * Sets the user's full name for the builder.
     *
     * @param name the user's name as entered during registration
     * @return this builder instance (fluent API)
     *
     * <p>Required field for creating a SystemUser (Req1).</p>
     */

    public UserBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the email address for the new user.
     *
     * @param email the user's email; must be unique in the system
     * @return this builder instance
     *
     * <p>Used by Scenario 1 for account creation and verification logic.</p>
     */

    public UserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * Sets the password hash for the user being built.
     *
     * @param passwordHash the hashed password (never raw text)
     * @return this builder instance
     *
     * <p>Ensures password strength validation is handled before invoking the builder.</p>
     */

    public UserBuilder setPassword(String passwordHash) {
        this.passwordHash = passwordHash;
        return this;
    }

    /**
     * Sets the UserType (STUDENT, FACULTY, STAFF, PARTNER, ADMIN, CHIEF).
     *
     * @param type the user category
     * @return this builder instance
     *
     * <p>Determines pricing, permissions, and scenario routing.</p>
     */

    public UserBuilder setUserType(UserType type) {
        this.type = type;
        return this;
    }

    /**
     * Adds an optional organization ID (for faculty/staff/partner accounts).
     *
     * @param orgId organizational identifier (may be null)
     * @return this builder instance
     */

    public UserBuilder setOrgId(String orgId) {
        this.orgId = (orgId == null ? "" : orgId);
        return this;
    }

    /**
     * Adds an optional student ID for Student accounts.
     *
     * @param studentId an optional student identifier (may be null)
     * @return this builder instance
     */

    public UserBuilder setStudentId(String studentId) {
        this.studentId = (studentId == null ? "" : studentId);
        return this;
    }


    /**
     * Constructs and returns a fully validated {@link SystemUser} object.
     *
     * <h2>Validation Rules</h2>
     * <ul>
     *     <li>name, email, passwordHash, and type must not be null</li>
     *     <li>Optional fields (orgId, studentId) default to empty strings</li>
     * </ul>
     *
     * @return a new SystemUser instance ready to be saved by UserManager
     * @throws IllegalStateException if required fields are missing
     *
     * <h2>Scenario Mapping</h2>
     * <p>Used directly in Scenario 1 for building user accounts before
     * persisting them in CSV storage.</p>
     */

    public User build() {

        // Validation — ensures no missing critical fields
        if (name == null || email == null || passwordHash == null || type == null) {
            throw new IllegalStateException("Missing fields in UserBuilder — cannot build user.");
        }

        // Now create SystemUser (ONLY concrete user type)
        return new SystemUser(
                name,
                email,
                passwordHash,
                type,
                orgId,
                studentId
        );
    }
}
