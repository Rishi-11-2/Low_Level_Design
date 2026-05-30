package strategy;

import model.Elevator;
import java.util.ArrayList;
import java.util.List;

public class FCFSStrategy implements MovementStrategy {
    @Override
    public List<Integer> calculatePath(Elevator elevator, List<Integer> requestedFloors) {
        // Return unique floors preserving FCFS insertion order
        List<Integer> path = new ArrayList<>();
        for (Integer floor : requestedFloors) {
            if (!path.contains(floor)) {
                path.add(floor);
            }
        }
        return path;
    }
}
