package service;

import model.Driver;
import model.Location;
import model.enums.DriverStatus;
import repository.DriverRepository;
import repository.LocationRepository;

public class LocationService {

    private final DriverRepository driverRepository;
    private final LocationRepository locationRepository;

    public LocationService(DriverRepository driverRepository, LocationRepository locationRepository) {
        this.driverRepository = driverRepository;
        this.locationRepository = locationRepository;
    }

    public void updateDriverLocation(int driverId, Location location) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found: " + driverId));

        driver.setCurrentLocation(location);
        driverRepository.save(driver);
        locationRepository.saveLocation(driverId, location);

        System.out.println("[Location] Updated driver " + driverId + " location: " + location);
    }

    public Location getDriverLocation(int driverId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found: " + driverId));
        return driver.getCurrentLocation();
    }

    public double calculateDistance(Location loc1, Location loc2) {
        return haversineDistance(loc1, loc2);
    }

    public long calculateETA(Location from, Location to) {
        double distanceKm = haversineDistance(from, to);
        // Assume average speed of 30 km/h in city
        return Math.round((distanceKm / 30.0) * 3600);
    }

    private double haversineDistance(Location loc1, Location loc2) {
        final double EARTH_RADIUS_KM = 6371.0;

        double lat1 = Math.toRadians(loc1.getLatitude());
        double lat2 = Math.toRadians(loc2.getLatitude());
        double deltaLat = Math.toRadians(loc2.getLatitude() - loc1.getLatitude());
        double deltaLon = Math.toRadians(loc2.getLongitude() - loc1.getLongitude());

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                 + Math.cos(lat1) * Math.cos(lat2)
                 * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }
}
