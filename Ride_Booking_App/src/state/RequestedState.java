package state;

import model.Ride;
import model.enums.RideStatus;

import java.time.LocalDateTime;

public class RequestedState implements RideState {

    @Override
    public void accept(Ride ride, int driverId) {
        ride.setDriverId(driverId);
        ride.setAssignedAt(LocalDateTime.now());
        ride.setAcceptedAt(LocalDateTime.now());
        ride.transitionTo(RideStatus.ACCEPTED);
        System.out.println("[State] Ride " + ride.getRideId() + " accepted by driver " + driverId);
    }

    @Override
    public void cancel(Ride ride, int userId, String reason) {
        ride.setCancelledAt(LocalDateTime.now());
        ride.setCancellationReason(reason);
        ride.transitionTo(RideStatus.CANCELLED);
        System.out.println("[State] Ride " + ride.getRideId() + " cancelled. Reason: " + reason);
    }

    @Override
    public void start(Ride ride, int driverId) {
        throw new IllegalStateException("Cannot start a ride that is in REQUESTED state. Must be ACCEPTED first.");
    }

    @Override
    public void complete(Ride ride, int driverId) {
        throw new IllegalStateException("Cannot complete a ride that is in REQUESTED state.");
    }
}
