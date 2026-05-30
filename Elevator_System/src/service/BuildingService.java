package service;

import model.Building;
import model.SystemState;
import repository.BuildingRepository;
import java.util.UUID;

public class BuildingService {
    private final BuildingRepository buildingRepository;

    public BuildingService(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    public Building createBuilding(String name, int minFloor, int maxFloor, int totalElevators) {
        String id = UUID.randomUUID().toString();
        Building building = new Building(id, name, minFloor, maxFloor, totalElevators);
        buildingRepository.save(building);
        System.out.println("[BuildingService] Created building '" + name + "' (id=" + id + ") floors: [" + minFloor + " to " + maxFloor + "], total expected elevators: " + totalElevators);
        return building;
    }

    public boolean isValidFloor(String buildingId, int floor) {
        return buildingRepository.findById(buildingId)
                .map(b -> floor >= b.getMinFloor() && floor <= b.getMaxFloor())
                .orElse(false);
    }

    public void setBuildingSystemState(String buildingId, SystemState state) {
        buildingRepository.findById(buildingId).ifPresent(b -> {
            b.setSystemState(state);
            buildingRepository.save(b);
            System.out.println("[BuildingService] Building " + buildingId + " system state updated to: " + state);
        });
    }

    public boolean isSystemRunning(String buildingId) {
        return buildingRepository.findById(buildingId)
                .map(b -> b.getSystemState() == SystemState.RUNNING)
                .orElse(false);
    }

    public Building findById(String buildingId) {
        return buildingRepository.findById(buildingId).orElse(null);
    }
}
