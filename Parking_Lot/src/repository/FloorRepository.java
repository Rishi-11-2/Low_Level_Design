package repository;

import model.Floor;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FloorRepository {
    private final Map<UUID, Floor> floorMap = new ConcurrentHashMap<>();

    public Floor save(Floor floor) {
        floorMap.put(floor.getId(), floor);
        return floor;
    }

    public Optional<Floor> findById(UUID id) {
        return Optional.ofNullable(floorMap.get(id));
    }

    public Optional<Floor> findByFloorNumber(int floorNumber) {
        return floorMap.values().stream()
                .filter(f -> f.getFloorNumber() == floorNumber)
                .findFirst();
    }

    public Collection<Floor> findAll() {
        return floorMap.values();
    }

    public void delete(UUID id) {
        floorMap.remove(id);
    }
}
