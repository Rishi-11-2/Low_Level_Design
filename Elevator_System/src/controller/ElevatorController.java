package controller;

import model.Elevator;
import model.SystemState;
import service.BuildingService;
import service.ElevatorService;
import service.MovementService;

public class ElevatorController {
    private final ElevatorService elevatorService;
    private final BuildingService buildingService;
    private final MovementService movementService;

    public ElevatorController(ElevatorService elevatorService,
                              BuildingService buildingService,
                              MovementService movementService) {
        this.elevatorService = elevatorService;
        this.buildingService = buildingService;
        this.movementService = movementService;
    }

    public Elevator createElevator(String buildingId, int capacity) {
        System.out.println("\n>>> [ElevatorController] Request: Create Elevator, capacity=" + capacity);
        return elevatorService.createElevator(buildingId, capacity);
    }

    public void setElevatorMaintenance(String elevatorId, boolean maintenance) {
        System.out.println("\n>>> [ElevatorController] Request: Set Maintenance for Elevator " + elevatorId + " to " + maintenance);
        Elevator elevator = elevatorService.findById(elevatorId);
        if (elevator != null) {
            if (maintenance) {
                elevator.enterMaintenance();
            } else {
                elevator.exitMaintenance();
            }
        }
    }

    public void startElevatorSystem(String buildingId) {
        System.out.println("\n>>> [ElevatorController] Request: Start Elevator System in building: " + buildingId);
        buildingService.setBuildingSystemState(buildingId, SystemState.RUNNING);
        movementService.startElevatorSystem(buildingId);
        System.out.println("<<< [ElevatorController] Elevator System is now running.");
    }

    public void stopElevatorSystem(String buildingId) {
        System.out.println("\n>>> [ElevatorController] Request: Stop Elevator System in building: " + buildingId);
        buildingService.setBuildingSystemState(buildingId, SystemState.STOPPING);
        movementService.stopElevatorSystem(buildingId);
        buildingService.setBuildingSystemState(buildingId, SystemState.STOPPED);
        System.out.println("<<< [ElevatorController] Elevator System has stopped gracefully.");
    }
}
