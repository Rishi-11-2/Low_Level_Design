package dto;

import model.Location;

import java.time.LocalDateTime;

public class LocationUpdateRequest {
    private int driverId;
    private Location location;
    private LocalDateTime timestamp;

    public LocationUpdateRequest(int driverId, Location location, LocalDateTime timestamp) {
        this.driverId = driverId;
        this.location = location;
        this.timestamp = timestamp;
    }

    public int getDriverId() { return driverId; }
    public Location getLocation() { return location; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
