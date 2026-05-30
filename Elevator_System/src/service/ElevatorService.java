package service;

import model.*;
import repository.ElevatorRepository;
import java.util.List;

public class ElevatorService {
    private final ElevatorRepository elevatorRepository;
    private int elevatorCounter = 1;

    public ElevatorService(ElevatorRepository elevatorRepository) {
        this.elevatorRepository = elevatorRepository;
    }

    public Elevator createElevator(String buildingId, int capacity) {
        String id = "ELV-" + elevatorCounter++;
        Elevator elevator = new Elevator(id, buildingId, capacity);
        elevatorRepository.save(elevator);
        System.out.println("[ElevatorService] Created elevator cabin " + id + " in building " + buildingId + " with passenger capacity: " + capacity);
        return elevator;
    }

    public void updateElevatorFloor(String elevatorId, int floor) {
        elevatorRepository.findById(elevatorId).ifPresent(e -> {
            e.setCurrentFloor(floor);
            elevatorRepository.save(e);
        });
    }

    public List<Elevator> getAvailableElevators(String buildingId) {
        return elevatorRepository.findAvailableElevators(buildingId);
    }

    public Elevator findById(String elevatorId) {
        return elevatorRepository.findById(elevatorId).orElse(null);
    }

    public List<Elevator> findByBuilding(String buildingId) {
        return elevatorRepository.findByBuilding(buildingId);
    }
}
