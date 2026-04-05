package strategy.matching;

import model.Driver;
import model.Location;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NearestDriverStrategy implements DriverMatchingStrategy {

    @Override
    public List<Driver> findMatchingDrivers(Location pickup, List<Driver> candidates, int maxResults) {
        return candidates.stream()
                .filter(driver -> driver.getCurrentLocation() != null)
                .sorted(Comparator.comparingDouble(driver ->
                        haversineDistance(pickup, driver.getCurrentLocation())))
                .limit(maxResults)
                .collect(Collectors.toList());
    }

    /**
     * Haversine formula to calculate distance between two GPS coordinates.
     * Returns distance in kilometers.
     */
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
