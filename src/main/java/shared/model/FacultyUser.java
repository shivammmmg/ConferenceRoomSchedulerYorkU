package shared.model;


/**
 * Represents a Faculty user in the system.
 * ------------------------------------------------------------
 * Description:
 *  - Extends the shared User class.
 *  - Automatically assigns the user type as "Faculty".
 *  - Used by the Factory class to create faculty accounts.
 */
public class FacultyUser extends User {

    public FacultyUser(String name, String email, String password) {
        super(name, email, password, "Faculty");
    }
}
