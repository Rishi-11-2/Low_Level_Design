package strategy;

import model.Direction;
import model.Elevator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ScanStrategy implements MovementStrategy {
    @Override
    public List<Integer> calculatePath(Elevator elevator, List<Integer> requestedFloors) {
        if (requestedFloors.isEmpty()) {
            return new ArrayList<>();
        }

        int currentFloor = elevator.getCurrentFloor();
        Direction direction = elevator.getDirection();

        List<Integer> path = new ArrayList<>();
        
        List<Integer> upperFloors = requestedFloors.stream()
                .filter(f -> f >= currentFloor)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        List<Integer> lowerFloors = requestedFloors.stream()
                .filter(f -> f < currentFloor)
                .distinct()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());

        if (direction == Direction.UP || direction == Direction.IDLE) {
            // First visit upper floors in ascending order, then lower floors in descending order
            path.addAll(upperFloors);
            path.addAll(lowerFloors);
        } else {
            // First visit lower floors in descending order, then upper floors in ascending order
            path.addAll(lowerFloors);
            path.addAll(upperFloors);
        }

        return path;
    }
}
