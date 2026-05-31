package repository;

import model.Vehicle;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VehicleRepository {
    private final Map<UUID, Vehicle> vehicleMap = new ConcurrentHashMap<>();
    private final Map<String, Vehicle> licensePlateMap = new ConcurrentHashMap<>();

    public Vehicle save(Vehicle vehicle) {
        vehicleMap.put(vehicle.getId(), vehicle);
        licensePlateMap.put(vehicle.getLicensePlate(), vehicle);
        return vehicle;
    }

    public Optional<Vehicle> findById(UUID id) {
        return Optional.ofNullable(vehicleMap.get(id));
    }

    public Optional<Vehicle> findByLicensePlate(String licensePlate) {
        return Optional.ofNullable(licensePlateMap.get(licensePlate));
    }
}
