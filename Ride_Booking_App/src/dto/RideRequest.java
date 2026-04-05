package dto;

import model.Location;
import model.enums.PaymentType;

public class RideRequest {
    private int riderId;
    private Location pickupLocation;
    private Location dropoffLocation;
    private PaymentType paymentType;

    public RideRequest(int riderId, Location pickupLocation, Location dropoffLocation, PaymentType paymentType) {
        this.riderId = riderId;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.paymentType = paymentType;
    }

    public int getRiderId() { return riderId; }
    public Location getPickupLocation() { return pickupLocation; }
    public Location getDropoffLocation() { return dropoffLocation; }
    public PaymentType getPaymentType() { return paymentType; }
}
