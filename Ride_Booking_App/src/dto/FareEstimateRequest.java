package dto;

import model.Location;

public class FareEstimateRequest {
    private Location pickupLocation;
    private Location dropoffLocation;

    public FareEstimateRequest(Location pickupLocation, Location dropoffLocation) {
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
    }

    public Location getPickupLocation() { return pickupLocation; }
    public Location getDropoffLocation() { return dropoffLocation; }
}
