package state;

import model.Ride;

public interface RideState {
    void accept(Ride ride, int driverId);
    void cancel(Ride ride, int userId, String reason);
    void start(Ride ride, int driverId);
    void complete(Ride ride, int driverId);
}
