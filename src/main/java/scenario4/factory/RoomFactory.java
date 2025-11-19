package scenario4.factory;

import shared.model.Room;

public class RoomFactory {

    public Room createRoom(String id,
                           String name,
                           int capacity,
                           String location,
                           String amenities,
                           String building) {

        return new Room(id, name, capacity, location, amenities, building);
    }
}
