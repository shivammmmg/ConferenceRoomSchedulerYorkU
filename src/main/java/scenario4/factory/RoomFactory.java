package scenario4.factory;

import shared.model.Room;

public class RoomFactory {

    public Room createRoom(String id, int capacity, String location) {
        return new Room(id, capacity, location);
    }
}
