package repository;

import model.Elevator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ElevatorRepository {
    private final Map<String, Elevator> elevatorMap = new ConcurrentHashMap<>();

    public Elevator save(Elevator elevator) {
        elevatorMap.put(elevator.getId(), elevator);
        return elevator;
    }

    public Optional<Elevator> findById(String elevatorId) {
        return Optional.ofNullable(elevatorMap.get(elevatorId));
    }

    public List<Elevator> findByBuilding(String buildingId) {
        return elevatorMap.values().stream()
                .filter(e -> e.getBuildingId().equals(buildingId))
                .collect(Collectors.toList());
    }

    public List<Elevator> findAvailableElevators(String buildingId) {
        return elevatorMap.values().stream()
                .filter(e -> e.getBuildingId().equals(buildingId) && e.isActive())
                .collect(Collectors.toList());
    }

    public void deleteById(String elevatorId) {
        elevatorMap.remove(elevatorId);
    }
}
