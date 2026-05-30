package controller;

import model.Direction;
import model.Intersection;
import model.IntersectionCycle;
import service.IntersectionService;

public class IntersectionController {
    private final IntersectionService intersectionService;

    public IntersectionController(IntersectionService intersectionService) {
        this.intersectionService = intersectionService;
    }

    public void createIntersection(int id, String name) {
        System.out.println("\n>>> [IntersectionController] Request: Create Intersection id=" + id + ", name=" + name);
        intersectionService.createIntersection(id, name);
    }

    public Intersection getIntersection(int intersectionId) {
        return intersectionService.getIntersection(intersectionId);
    }

    public void startCycle(int intersectionId) {
        System.out.println("\n>>> [IntersectionController] Request: Run Cycle Step for Intersection id=" + intersectionId);
        intersectionService.runCycleStep(intersectionId);
    }

    public void displayStatus(int intersectionId) {
        Intersection intersection = intersectionService.getIntersection(intersectionId);
        IntersectionCycle cycle = intersectionService.getCycle(intersectionId);

        if (intersection == null) {
            System.out.println("Intersection " + intersectionId + " not found.");
            return;
        }

        System.out.println("\n================ INTERSECTION STATE DASHBOARD ================");
        System.out.println("Intersection ID:     " + intersection.getId());
        System.out.println("Intersection Name:   " + intersection.getName());
        System.out.println("Emergency Mode:      " + (intersection.isEmergencyMode() ? "ACTIVE (" + intersection.getEmergencyDirection() + ")" : "INACTIVE"));
        System.out.println("Cycle State:         " + (cycle != null && cycle.isPaused() ? "PAUSED (at " + intersectionService.getDirectionFromPhase(cycle.getPausedAtPhase()) + ")" : "RUNNING"));
        System.out.println("Active Phase Index:  " + (cycle != null ? cycle.getCurrentPhase() : "N/A"));
        System.out.println("Signal States:");
        for (Direction direction : Direction.values()) {
            String state = intersection.getTrafficLights().get(direction).getCurrentStateName();
            System.out.println("  - " + direction + " signal light is: " + state);
        }
        System.out.println("==============================================================");
    }
}
