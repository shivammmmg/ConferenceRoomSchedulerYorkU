package scenario4.factory;

import shared.model.Room;

/**
 * RoomFactory – Scenario 4 (Factory Pattern for Room Creation)
 * -------------------------------------------------------------------------
 * <p>This class implements a simple <b>Factory Design Pattern</b> to create
 * {@link Room} objects in a clean, centralized manner.</p>
 *
 * <h2>Purpose</h2>
 * <ul>
 *     <li>Encapsulates the creation of fully-initialized Room objects</li>
 *     <li>Ensures consistent construction across Scenario 4 admin workflows</li>
 *     <li>Keeps controller code (AddRoom, ManageRooms, etc.) cleaner and lighter</li>
 * </ul>
 *
 * <h2>Design Pattern Context</h2>
 * <ul>
 *     <li><b>Factory Pattern</b> – isolates object creation logic</li>
 *     <li>Matches D2/D3 diagrams showing RoomFactory as a dedicated creator</li>
 *     <li>Supports future extension if more complex Room initialization is required</li>
 * </ul>
 *
 * <h2>Used By</h2>
 * <ul>
 *     <li>Scenario 4 Admin module (AddRoomController, ManageRoomsController)</li>
 *     <li>Any process needing standardized Room creation logic</li>
 * </ul>
 */


public class RoomFactory {

    /**
     * Creates a new {@link Room} instance with all primary attributes filled.
     *
     * @param id        unique room identifier
     * @param name      human-readable room name
     * @param capacity  maximum occupancy
     * @param location  building location (e.g., "York LSB 120A")
     * @param amenities comma-separated list of equipment/features
     * @param building  building name for filtering and grouping
     *
     * @return a fully constructed Room object
     */

    public Room createRoom(String id,
                           String name,
                           int capacity,
                           String location,
                           String amenities,
                           String building) {

        return new Room(id, name, capacity, location, amenities, building);
    }
}
