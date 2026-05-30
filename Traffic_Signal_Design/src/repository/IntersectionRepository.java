package repository;

import model.Direction;
import model.Intersection;
import model.IntersectionCycle;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IntersectionRepository {
    private final Map<Integer, Intersection> intersectionMap = new ConcurrentHashMap<>();
    private final Map<Integer, IntersectionCycle> cycleMap = new ConcurrentHashMap<>();

    public void save(Intersection intersection) {
        intersectionMap.put(intersection.getId(), intersection);
        if (!cycleMap.containsKey(intersection.getId())) {
            cycleMap.put(intersection.getId(), new IntersectionCycle(intersection.getId()));
        }
    }

    public Intersection findById(int intersectionId) {
        return intersectionMap.get(intersectionId);
    }

    public IntersectionCycle getCycle(int intersectionId) {
        return cycleMap.get(intersectionId);
    }

    public void updateCycle(int intersectionId, IntersectionCycle cycle) {
        cycleMap.put(intersectionId, cycle);
    }

    public void updateEmergencyMode(int intersectionId, boolean emergencyMode, Direction direction) {
        Intersection intersection = intersectionMap.get(intersectionId);
        if (intersection != null) {
            intersection.setEmergencyMode(emergencyMode);
            intersection.setEmergencyDirection(direction);
        }
    }
}
