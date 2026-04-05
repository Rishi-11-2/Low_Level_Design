package repository;

import model.Driver;
import model.Location;
import model.enums.DriverStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DriverRepository {

    private final Map<Integer, Driver> driversById = new ConcurrentHashMap<>();

    public Driver save(Driver driver) {
        driversById.put(driver.getId(), driver);
        return driver;
    }

    public Optional<Driver> findById(int id) {
        return Optional.ofNullable(driversById.get(id));
    }

    public List<Driver> findByStatus(DriverStatus status) {
        return driversById.values().stream()
                .filter(driver -> driver.getStatus() == status)
                .collect(Collectors.toList());
    }

    public void updateLocation(int driverId, Location location) {
        Driver driver = driversById.get(driverId);
        if (driver != null) {
            driver.setCurrentLocation(location);
        }
    }

    public List<Driver> findAll() {
        return List.copyOf(driversById.values());
    }
}
