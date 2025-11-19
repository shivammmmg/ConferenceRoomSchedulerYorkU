package scenario3.observer;

import scenario3.model.Room;

/**
 * OBSERVER INTERFACE:
 * Implement this in any class that wants to receive updates about room state changes.
 */
public interface RoomStatusObserver {
    /**
     * Called whenever a room changes its status.
     * Implementations should be thread-safe and use Platform.runLater if updating JavaFX UI.
     */
    void onRoomStatusChanged(Room room);
}
