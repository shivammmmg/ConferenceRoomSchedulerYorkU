package scenario1.builder;

import shared.model.SystemUser;
import shared.model.User;
import shared.model.UserType;

/**
 * =============================================================================
 * CLASS: UserBuilder
 * PATTERN: Builder Pattern
 * SCENARIO: 1 — Registration & Account Management
 * =============================================================================
 *
 * PURPOSE:
 *   - Builds SystemUser objects step-by-step.
 *   - Replaces previous approach where separate subclasses existed.
 *   - Supports optional attributes such as studentId and orgId.
 *
 * USAGE (in UserManager):
 *
 *   User u = new UserBuilder()
 *                .setName(name)
 *                .setEmail(email)
 *                .setPassword(password)
 *                .setUserType(typeEnum)
 *                .setStudentId(studentId)
 *                .setOrgId(null)
 *                .build();
 *
 * =============================================================================
 */
public class UserBuilder {

    // Fields to collect
    private String name;
    private String email;
    private String passwordHash;
    private UserType type;
    private String orgId;
    private String studentId;


    // ===========================
    // Fluent setters
    // ===========================
    public UserBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder setPassword(String passwordHash) {
        this.passwordHash = passwordHash;
        return this;
    }

    public UserBuilder setUserType(UserType type) {
        this.type = type;
        return this;
    }

    public UserBuilder setOrgId(String orgId) {
        this.orgId = orgId;
        return this;
    }

    public UserBuilder setStudentId(String studentId) {
        this.studentId = studentId;
        return this;
    }


    // ===========================
    // BUILD
    // ===========================
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
