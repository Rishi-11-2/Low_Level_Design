package state;

import model.Ride;

public class CompletedState implements RideState {

    @Override
    public void accept(Ride ride, int driverId) {
        throw new IllegalStateException("Ride is already completed. No further transitions allowed.");
    }

    @Override
    public void cancel(Ride ride, int userId, String reason) {
        throw new IllegalStateException("Ride is already completed. Cannot cancel.");
    }

    @Override
    public void start(Ride ride, int driverId) {
        throw new IllegalStateException("Ride is already completed. Cannot start.");
    }

    @Override
    public void complete(Ride ride, int driverId) {
        throw new IllegalStateException("Ride is already completed.");
    }
}
