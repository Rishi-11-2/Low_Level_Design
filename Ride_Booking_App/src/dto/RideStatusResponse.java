package dto;

import model.Location;
import model.enums.RideStatus;

import java.time.LocalDateTime;

public class RideStatusResponse {
    private String rideId;
    private RideStatus status;
    private DriverInfo driver;
    private Location currentLocation;
    private Long estimatedFare;
    private Location pickupLocation;
    private Location dropoffLocation;
    private LocalDateTime requestedAt;

    public RideStatusResponse(String rideId, RideStatus status, Location pickupLocation,
                               Location dropoffLocation, LocalDateTime requestedAt) {
        this.rideId = rideId;
        this.status = status;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.requestedAt = requestedAt;
    }

    public String getRideId() { return rideId; }
    public RideStatus getStatus() { return status; }

    public DriverInfo getDriver() { return driver; }
    public void setDriver(DriverInfo driver) { this.driver = driver; }

    public Location getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(Location currentLocation) { this.currentLocation = currentLocation; }

    public Long getEstimatedFare() { return estimatedFare; }
    public void setEstimatedFare(Long estimatedFare) { this.estimatedFare = estimatedFare; }

    public Location getPickupLocation() { return pickupLocation; }
    public Location getDropoffLocation() { return dropoffLocation; }
    public LocalDateTime getRequestedAt() { return requestedAt; }

    @Override
    public String toString() {
        return "RideStatus{rideId='" + rideId + "', status=" + status +
               (driver != null ? ", driver=" + driver : "") + "}";
    }
}
