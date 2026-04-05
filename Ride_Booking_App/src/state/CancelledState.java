package state;

import model.Ride;

public class CancelledState implements RideState {

    @Override
    public void accept(Ride ride, int driverId) {
        throw new IllegalStateException("Ride is cancelled. No further transitions allowed.");
    }

    @Override
    public void cancel(Ride ride, int userId, String reason) {
        throw new IllegalStateException("Ride is already cancelled.");
    }

    @Override
    public void start(Ride ride, int driverId) {
        throw new IllegalStateException("Ride is cancelled. Cannot start.");
    }

    @Override
    public void complete(Ride ride, int driverId) {
        throw new IllegalStateException("Ride is cancelled. Cannot complete.");
    }
}
