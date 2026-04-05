package repository;

import model.Location;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LocationRepository {

    private final Map<Integer, List<Location>> locationHistory = new ConcurrentHashMap<>();

    public void saveLocation(int driverId, Location location) {
        locationHistory.computeIfAbsent(driverId, k -> new ArrayList<>()).add(location);
    }

    public Location getLatestLocation(int driverId) {
        List<Location> history = locationHistory.get(driverId);
        if (history == null || history.isEmpty()) {
            return null;
        }
        return history.get(history.size() - 1);
    }

    public List<Location> getLocationHistory(int driverId) {
        return locationHistory.getOrDefault(driverId, Collections.emptyList());
    }
}
