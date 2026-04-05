package service;

import model.Driver;
import model.enums.DriverStatus;
import repository.DriverRepository;

public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public void goOnline(int driverId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found: " + driverId));

        if (driver.getStatus() == DriverStatus.ON_RIDE) {
            throw new IllegalStateException("Driver " + driverId + " is currently on a ride. Cannot go online.");
        }

        driver.setStatus(DriverStatus.ONLINE);
        driverRepository.save(driver);
        System.out.println("[Driver] Driver " + driverId + " is now ONLINE");
    }

    public void goOffline(int driverId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found: " + driverId));

        if (driver.getStatus() == DriverStatus.ON_RIDE) {
            throw new IllegalStateException("Driver " + driverId + " is currently on a ride. Cannot go offline.");
        }

        driver.setStatus(DriverStatus.OFFLINE);
        driverRepository.save(driver);
        System.out.println("[Driver] Driver " + driverId + " is now OFFLINE");
    }

    public Driver getById(int driverId) {
        return driverRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found: " + driverId));
    }

    public boolean isAvailable(int driverId) {
        return driverRepository.findById(driverId)
                .map(driver -> driver.getStatus() == DriverStatus.ONLINE)
                .orElse(false);
    }
}
