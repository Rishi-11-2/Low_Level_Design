package state;

import model.Ride;
import model.enums.RideStatus;

import java.time.LocalDateTime;

public class AssignedState implements RideState {

    @Override
    public void accept(Ride ride, int driverId) {
        if (ride.getDriverId() == null || ride.getDriverId() != driverId) {
            throw new IllegalStateException("Driver " + driverId + " is not the assigned driver for this ride.");
        }
        ride.setAcceptedAt(LocalDateTime.now());
        ride.transitionTo(RideStatus.ACCEPTED);
        System.out.println("[State] Ride " + ride.getRideId() + " accepted by assigned driver " + driverId);
    }

    @Override
    public void cancel(Ride ride, int userId, String reason) {
        ride.setCancelledAt(LocalDateTime.now());
        ride.setCancellationReason(reason);
        ride.setDriverId(null);
        ride.transitionTo(RideStatus.CANCELLED);
        System.out.println("[State] Assigned ride " + ride.getRideId() + " cancelled. Reason: " + reason);
    }

    @Override
    public void start(Ride ride, int driverId) {
        throw new IllegalStateException("Cannot start a ride that is in ASSIGNED state. Must be ACCEPTED first.");
    }

    @Override
    public void complete(Ride ride, int driverId) {
        throw new IllegalStateException("Cannot complete a ride that is in ASSIGNED state.");
    }
}
