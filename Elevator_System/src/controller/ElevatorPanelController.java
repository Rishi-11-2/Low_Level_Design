package controller;

import model.Elevator;
import service.BuildingService;
import service.ElevatorService;
import service.RequestService;

public class ElevatorPanelController {
    private final RequestService requestService;
    private final ElevatorService elevatorService;
    private final BuildingService buildingService;

    public ElevatorPanelController(RequestService requestService,
                                   ElevatorService elevatorService,
                                   BuildingService buildingService) {
        this.requestService = requestService;
        this.elevatorService = elevatorService;
        this.buildingService = buildingService;
    }

    public void selectFloor(String elevatorId, int destinationFloor) {
        System.out.println("\n>>> [ElevatorPanelController] Request: Cabin " + elevatorId + " requests target Floor " + destinationFloor);
        
        Elevator elevator = elevatorService.findById(elevatorId);
        if (elevator == null) {
            System.out.println("<<< [ElevatorPanelController] Error: Elevator not found");
            return;
        }

        if (!elevator.isActive()) {
            System.out.println("<<< [ElevatorPanelController] Rejected: Elevator " + elevatorId + " is out of service (Maintenance).");
            return;
        }

        if (!buildingService.isValidFloor(elevator.getBuildingId(), destinationFloor)) {
            System.out.println("<<< [ElevatorPanelController] Error: Destination Floor " + destinationFloor + " is outside building bounds.");
            return;
        }

        // Capacity management check
        if (elevator.isFull()) {
            System.out.println("<<< [ElevatorPanelController] REJECTED: Cabin " + elevatorId + " is overloaded! (Current load: " + elevator.getCurrentLoad() + "/" + elevator.getCapacity() + "). Skipping request and displaying 'FULL' on panels.");
            return;
        }

        // Simulate a passenger entering
        elevator.setCurrentLoad(elevator.getCurrentLoad() + 1);
        
        requestService.createInternalRequest(elevatorId, destinationFloor);
    }
}
