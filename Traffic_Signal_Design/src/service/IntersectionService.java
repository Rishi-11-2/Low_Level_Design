package service;

import model.Direction;
import model.Intersection;
import model.IntersectionCycle;
import model.TrafficLight;
import repository.IntersectionRepository;
import repository.TimingRepository;

public class IntersectionService {
    private final IntersectionRepository intersectionRepository;
    private final TimingRepository timingRepository;

    public IntersectionService(IntersectionRepository intersectionRepository, TimingRepository timingRepository) {
        this.intersectionRepository = intersectionRepository;
        this.timingRepository = timingRepository;
    }

    public void createIntersection(int id, String name) {
        Intersection intersection = new Intersection(id, name);
        intersectionRepository.save(intersection);
        System.out.println("[IntersectionService] Created intersection " + id + " (" + name + ")");
    }

    public Intersection getIntersection(int intersectionId) {
        return intersectionRepository.findById(intersectionId);
    }

    public IntersectionCycle getCycle(int intersectionId) {
        return intersectionRepository.getCycle(intersectionId);
    }

    public void pauseCycle(int intersectionId) {
        IntersectionCycle cycle = intersectionRepository.getCycle(intersectionId);
        Intersection intersection = intersectionRepository.findById(intersectionId);
        if (cycle != null && intersection != null) {
            cycle.setPaused(true);
            cycle.setPausedAtPhase(cycle.getCurrentPhase());
            intersection.setCyclePaused(true);
            intersectionRepository.save(intersection);
            intersectionRepository.updateCycle(intersectionId, cycle);
            System.out.println("[IntersectionService] Cycle PAUSED at phase: " + getDirectionFromPhase(cycle.getCurrentPhase()));
        }
    }

    public void resumeCycle(int intersectionId) {
        IntersectionCycle cycle = intersectionRepository.getCycle(intersectionId);
        Intersection intersection = intersectionRepository.findById(intersectionId);
        if (cycle != null && intersection != null) {
            cycle.setPaused(false);
            cycle.setCurrentPhase(cycle.getPausedAtPhase());
            intersection.setCyclePaused(false);
            intersectionRepository.save(intersection);
            intersectionRepository.updateCycle(intersectionId, cycle);
            System.out.println("[IntersectionService] Cycle RESUMED from phase: " + getDirectionFromPhase(cycle.getCurrentPhase()));
        }
    }

    public void setAllSignalsToRed(int intersectionId) {
        Intersection intersection = intersectionRepository.findById(intersectionId);
        if (intersection != null) {
            intersection.setAllSignalsToRed();
            intersectionRepository.save(intersection);
        }
    }

    public void emergencySetAllSignalsToRed(int intersectionId) {
        Intersection intersection = intersectionRepository.findById(intersectionId);
        if (intersection != null) {
            System.out.println("[Emergency preemption] Transitioning all signals to RED with safety transitions.");
            intersection.setAllSignalsToRed();
            intersectionRepository.save(intersection);
        }
    }

    public void setSignalToGreen(int intersectionId, Direction direction) {
        Intersection intersection = intersectionRepository.findById(intersectionId);
        if (intersection != null) {
            intersection.setSignalToGreen(direction);
            intersectionRepository.save(intersection);
        }
    }

    public void setSignalToYellow(int intersectionId, Direction direction) {
        Intersection intersection = intersectionRepository.findById(intersectionId);
        if (intersection != null) {
            intersection.setSignalToYellow(direction);
            intersectionRepository.save(intersection);
        }
    }

    public void setSignalToRed(int intersectionId, Direction direction) {
        Intersection intersection = intersectionRepository.findById(intersectionId);
        if (intersection != null) {
            intersection.setSignalToRed(direction);
            intersectionRepository.save(intersection);
        }
    }

    public void setSignalToOff(int intersectionId, Direction direction) {
        Intersection intersection = intersectionRepository.findById(intersectionId);
        if (intersection != null) {
            intersection.setSignalToOff(direction);
            intersectionRepository.save(intersection);
        }
    }

    public Direction getDirectionFromPhase(int phase) {
        switch (phase) {
            case 0: return Direction.NORTH;
            case 1: return Direction.EAST;
            case 2: return Direction.SOUTH;
            case 3: return Direction.WEST;
            default: throw new IllegalArgumentException("Invalid phase: " + phase);
        }
    }

    // Runs a simulated single step in the automatic cycle transitions (e.g. GREEN -> YELLOW -> RED -> Next GREEN)
    public void runCycleStep(int intersectionId) {
        Intersection intersection = intersectionRepository.findById(intersectionId);
        IntersectionCycle cycle = intersectionRepository.getCycle(intersectionId);

        if (intersection == null || cycle == null || cycle.isPaused()) {
            return;
        }

        Direction activeDirection = getDirectionFromPhase(cycle.getCurrentPhase());
        System.out.println("\n--- [Cycle Phase: " + activeDirection + "] ---");

        // 1. Turn active direction GREEN
        int greenDuration = timingRepository.getSignalTiming(intersectionId, activeDirection).getGreenDuration();
        System.out.println("[IntersectionService] Signal " + activeDirection + " gets GREEN light for " + greenDuration + "s.");
        
        // Safety: Ensure all other signals are RED
        for (Direction dir : Direction.values()) {
            if (dir != activeDirection) {
                TrafficLight light = intersection.getTrafficLights().get(dir);
                if (!"RED".equals(light.getCurrentStateName()) && !"OFF".equals(light.getCurrentStateName())) {
                    intersection.emergencyTransitionToRed(dir);
                }
            }
        }
        
        // Open the current phase green
        setSignalToGreen(intersectionId, activeDirection);

        // 2. Transition current active direction GREEN -> YELLOW (Simulated yellow duration)
        int yellowDuration = timingRepository.getSignalTiming(intersectionId, activeDirection).getYellowDuration();
        System.out.println("[IntersectionService] Signal " + activeDirection + " transitions to YELLOW for " + yellowDuration + "s.");
        setSignalToYellow(intersectionId, activeDirection);

        // 3. Transition active direction YELLOW -> RED
        setSignalToRed(intersectionId, activeDirection);

        // 4. Advance phase index
        int nextPhase = (cycle.getCurrentPhase() + 1) % 4;
        cycle.setCurrentPhase(nextPhase);
        intersectionRepository.updateCycle(intersectionId, cycle);
        System.out.println("[IntersectionService] Advanced to next phase: " + getDirectionFromPhase(nextPhase));
    }
}
