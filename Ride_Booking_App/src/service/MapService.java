package service;

import dto.DistanceAndDuration;
import model.Location;

/**
 * External map service integration (mock implementation for LLD).
 * In production, this would call Google Maps / Mapbox APIs.
 */
public class MapService {

    public DistanceAndDuration getDistanceAndDuration(Location from, Location to) {
        double distanceKm = calculateHaversineDistance(from, to);
        // Estimate duration: assume average speed of 30 km/h in city
        long durationSeconds = Math.round((distanceKm / 30.0) * 3600);
        return new DistanceAndDuration(Math.round(distanceKm * 100.0) / 100.0, durationSeconds);
    }

    public String geocode(Location location) {
        return "Address near (" + location.getLatitude() + ", " + location.getLongitude() + ")";
    }

    public Location reverseGeocode(double lat, double lon) {
        Location location = new Location(lat, lon);
        location.setAddress("Address near (" + lat + ", " + lon + ")");
        return location;
    }

    private double calculateHaversineDistance(Location loc1, Location loc2) {
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
