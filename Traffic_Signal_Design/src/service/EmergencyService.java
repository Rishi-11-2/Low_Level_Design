package service;

import model.Direction;
import model.EmergencyRequest;
import model.Intersection;
import repository.EmergencyRepository;
import repository.IntersectionRepository;

public class EmergencyService {
    private final EmergencyRepository emergencyRepository;
    private final IntersectionRepository intersectionRepository;
    private final IntersectionService intersectionService;
    private int requestIdCounter = 1;

    public EmergencyService(EmergencyRepository emergencyRepository,
                            IntersectionRepository intersectionRepository,
                            IntersectionService intersectionService) {
        this.emergencyRepository = emergencyRepository;
        this.intersectionRepository = intersectionRepository;
        this.intersectionService = intersectionService;
    }

    public synchronized void requestEmergency(int intersectionId, Direction direction, int duration) {
        System.out.println("\n>>> [EmergencyService] EMERGENCY Vehicle Request detected in direction: " + direction + " for " + duration + "s");

        Intersection intersection = intersectionRepository.findById(intersectionId);
        if (intersection == null) return;

        // 1. Pause active cycle
        intersectionService.pauseCycle(intersectionId);

        // 2. Safely transition all current active lights to RED
        intersectionService.emergencySetAllSignalsToRed(intersectionId);

        // 3. Open the emergency lane with GREEN light
        System.out.println("[EmergencyService] Opening emergency lane: Setting direction " + direction + " to GREEN.");
        intersectionService.setSignalToGreen(intersectionId, direction);

        // 4. Save state
        intersectionRepository.updateEmergencyMode(intersectionId, true, direction);
        
        EmergencyRequest request = new EmergencyRequest(requestIdCounter++, intersectionId, direction, duration);
        emergencyRepository.save(request);
        
        System.out.println("<<< [EmergencyService] Emergency mode fully engaged. Direction " + direction + " has priority.");
    }

    public synchronized void endEmergency(int intersectionId) {
        System.out.println("\n>>> [EmergencyService] Clear Emergency request received.");

        Intersection intersection = intersectionRepository.findById(intersectionId);
        if (intersection == null || !intersection.isEmergencyMode()) {
            System.out.println("<<< [EmergencyService] No active emergency to clear.");
            return;
        }

        Direction emergencyDir = intersection.getEmergencyDirection();
        System.out.println("[EmergencyService] Closing emergency lane for direction: " + emergencyDir);

        // 1. Safe transition: Emergency GREEN -> YELLOW -> RED
        intersection.emergencyTransitionToRed(emergencyDir);
        intersectionRepository.save(intersection);

        // 2. Clear emergency state in DB
        intersectionRepository.updateEmergencyMode(intersectionId, false, null);

        EmergencyRequest activeReq = emergencyRepository.getActiveEmergency(intersectionId);
        if (activeReq != null) {
            emergencyRepository.updateStatus(activeReq.getId(), false);
        }

        // 3. Resume the paused cycle
        intersectionService.resumeCycle(intersectionId);
        
        System.out.println("<<< [EmergencyService] Emergency cleared. Intersection has returned to standard automatic cycle.");
    }

    public EmergencyRequest getActiveEmergency(int intersectionId) {
        return emergencyRepository.getActiveEmergency(intersectionId);
    }
}
