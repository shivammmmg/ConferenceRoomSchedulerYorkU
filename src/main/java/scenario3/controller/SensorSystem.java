package scenario3.controller;

import javafx.application.Platform;
import scenario3.model.Room;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

public class SensorSystem implements Runnable {

    private volatile boolean running = false;
    private Thread sensorThread;

    private Consumer<String> logCallback;
    private List<Room> rooms;

    private int index = 0;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void configure(List<Room> rooms, Consumer<String> logCallback) {
        this.rooms = rooms;
        this.logCallback = logCallback;
    }

    public void startSimulation() {
        if (running) return;
        if (rooms == null || logCallback == null) return;

        running = true;
        sensorThread = new Thread(this, "SensorSystemThread");
        sensorThread.setDaemon(true);
        sensorThread.start();

    }

    public void stopSimulation() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                if (rooms == null || rooms.isEmpty()) {
                    Thread.sleep(1000);
                    continue;
                }

                // Go through rooms in order
                Room room = rooms.get(index);

                // Report status WITHOUT changing anything
                log("Room " + room.getId() + " -> " + room.getStatus());

                // advance index
                index = (index + 1) % rooms.size();

                Thread.sleep(2000);

            } catch (InterruptedException ignored) { }
        }
    }

    private void log(String msg) {
        if (logCallback == null) return;
        String line = FORMATTER.format(LocalDateTime.now()) + " — " + msg;
        Platform.runLater(() -> logCallback.accept(msg));
    }
}
