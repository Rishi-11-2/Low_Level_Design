package state;

import model.Ride;
import model.enums.RideStatus;

import java.time.LocalDateTime;

public class InProgressState implements RideState {

    @Override
    public void accept(Ride ride, int driverId) {
        throw new IllegalStateException("Ride is already in progress. Cannot accept.");
    }

    @Override
    public void cancel(Ride ride, int userId, String reason) {
        ride.setCancelledAt(LocalDateTime.now());
        ride.setCancellationReason(reason);
        ride.transitionTo(RideStatus.CANCELLED);
        System.out.println("[State] In-progress ride " + ride.getRideId() + " cancelled. Reason: " + reason);
    }

    @Override
    public void start(Ride ride, int driverId) {
        throw new IllegalStateException("Ride is already in progress.");
    }

    @Override
    public void complete(Ride ride, int driverId) {
        if (ride.getDriverId() == null || ride.getDriverId() != driverId) {
            throw new IllegalStateException("Driver " + driverId + " is not the assigned driver for this ride.");
        }
        ride.setCompletedAt(LocalDateTime.now());
        ride.transitionTo(RideStatus.COMPLETED);
        System.out.println("[State] Ride " + ride.getRideId() + " completed by driver " + driverId);
    }
}
