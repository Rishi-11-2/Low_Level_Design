package controller;

import model.Direction;
import service.BuildingService;
import service.RequestService;

public class FloorPanelController {
    private final RequestService requestService;
    private final BuildingService buildingService;

    public FloorPanelController(RequestService requestService, BuildingService buildingService) {
        this.requestService = requestService;
        this.buildingService = buildingService;
    }

    public void pressUpButton(int floorNumber, String buildingId) {
        System.out.println("\n>>> [FloorPanelController] Request: Floor " + floorNumber + " presses UP button in building: " + buildingId);
        if (!buildingService.isValidFloor(buildingId, floorNumber)) {
            System.out.println("<<< [FloorPanelController] Error: Invalid floor number " + floorNumber);
            return;
        }
        if (!buildingService.isSystemRunning(buildingId)) {
            System.out.println("<<< [FloorPanelController] Rejected: Elevator system is not currently active.");
            return;
        }
        requestService.createExternalRequest(floorNumber, Direction.UP, buildingId);
    }

    public void pressDownButton(int floorNumber, String buildingId) {
        System.out.println("\n>>> [FloorPanelController] Request: Floor " + floorNumber + " presses DOWN button in building: " + buildingId);
        if (!buildingService.isValidFloor(buildingId, floorNumber)) {
            System.out.println("<<< [FloorPanelController] Error: Invalid floor number " + floorNumber);
            return;
        }
        if (!buildingService.isSystemRunning(buildingId)) {
            System.out.println("<<< [FloorPanelController] Rejected: Elevator system is not currently active.");
            return;
        }
        requestService.createExternalRequest(floorNumber, Direction.DOWN, buildingId);
    }
}
