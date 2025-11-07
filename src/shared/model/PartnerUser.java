package shared.model;

/**
 * Represents an external Partner user in the system.
 * ------------------------------------------------------------
 * Description:
 *  - Extends the shared User class.
 *  - Automatically assigns the user type as "Partner".
 *  - Used by the Factory class to create partner accounts.
 */
public class PartnerUser extends User {

    public PartnerUser(String name, String email, String password) {
        super(name, email, password, "Partner");
    }
}
