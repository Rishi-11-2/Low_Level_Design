package state;

import model.Ride;
import model.enums.RideStatus;

import java.time.LocalDateTime;

public class AcceptedState implements RideState {

    @Override
    public void accept(Ride ride, int driverId) {
        throw new IllegalStateException("Ride is already accepted.");
    }

    @Override
    public void cancel(Ride ride, int userId, String reason) {
        ride.setCancelledAt(LocalDateTime.now());
        ride.setCancellationReason(reason);
        ride.transitionTo(RideStatus.CANCELLED);
        System.out.println("[State] Accepted ride " + ride.getRideId() + " cancelled. Reason: " + reason);
    }

    @Override
    public void start(Ride ride, int driverId) {
        if (ride.getDriverId() == null || ride.getDriverId() != driverId) {
            throw new IllegalStateException("Driver " + driverId + " is not the assigned driver for this ride.");
        }
        ride.setStartedAt(LocalDateTime.now());
        ride.transitionTo(RideStatus.IN_PROGRESS);
        System.out.println("[State] Ride " + ride.getRideId() + " started by driver " + driverId);
    }

    @Override
    public void complete(Ride ride, int driverId) {
        throw new IllegalStateException("Cannot complete a ride that hasn't started. Current state: ACCEPTED.");
    }
}
