package repository;

import model.Ride;
import model.enums.RideStatus;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RideRepository {

    private final Map<String, Ride> ridesByRideId = new ConcurrentHashMap<>();

    public Ride save(Ride ride) {
        ridesByRideId.put(ride.getRideId(), ride);
        return ride;
    }

    public Optional<Ride> findByRideId(String rideId) {
        return Optional.ofNullable(ridesByRideId.get(rideId));
    }

    public Optional<Ride> findByPaymentId(String paymentId) {
        return ridesByRideId.values().stream()
                .filter(ride -> paymentId.equals(ride.getPaymentId()))
                .findFirst();
    }

    public List<Ride> findByRiderId(int riderId) {
        return ridesByRideId.values().stream()
                .filter(ride -> ride.getRiderId() == riderId)
                .collect(Collectors.toList());
    }

    public List<Ride> findByDriverId(int driverId) {
        return ridesByRideId.values().stream()
                .filter(ride -> ride.getDriverId() != null && ride.getDriverId() == driverId)
                .collect(Collectors.toList());
    }

    public List<Ride> findByStatus(RideStatus status) {
        return ridesByRideId.values().stream()
                .filter(ride -> ride.getStatus() == status)
                .collect(Collectors.toList());
    }

    public boolean hasActiveRide(int riderId) {
        return ridesByRideId.values().stream()
                .anyMatch(ride -> ride.getRiderId() == riderId &&
                        (ride.getStatus() == RideStatus.REQUESTED ||
                         ride.getStatus() == RideStatus.ASSIGNED ||
                         ride.getStatus() == RideStatus.ACCEPTED ||
                         ride.getStatus() == RideStatus.IN_PROGRESS));
    }
}
