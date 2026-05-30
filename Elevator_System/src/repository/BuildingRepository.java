package repository;

import model.Building;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class BuildingRepository {
    private final Map<String, Building> buildingMap = new ConcurrentHashMap<>();

    public Building save(Building building) {
        buildingMap.put(building.getId(), building);
        return building;
    }

    public Optional<Building> findById(String buildingId) {
        return Optional.ofNullable(buildingMap.get(buildingId));
    }

    public List<Building> findAll() {
        return new ArrayList<>(buildingMap.values());
    }

    public void deleteById(String buildingId) {
        buildingMap.remove(buildingId);
    }
}
