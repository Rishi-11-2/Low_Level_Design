package controller;

import model.Direction;
import service.EmergencyService;

public class EmergencyController {
    private final EmergencyService emergencyService;

    public EmergencyController(EmergencyService emergencyService) {
        this.emergencyService = emergencyService;
    }

    public void requestEmergency(int intersectionId, Direction direction, int duration) {
        System.out.println("\n>>> [EmergencyController] Dispatching Emergency Vehicle request direction=" + direction);
        emergencyService.requestEmergency(intersectionId, direction, duration);
    }

    public void endEmergency(int intersectionId) {
        System.out.println("\n>>> [EmergencyController] Dispatching Emergency Clearance");
        emergencyService.endEmergency(intersectionId);
    }
}
