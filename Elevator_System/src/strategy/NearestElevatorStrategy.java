package strategy;

import model.Direction;
import model.Elevator;
import model.ExternalRequest;
import java.util.List;

public class NearestElevatorStrategy implements ElevatorSelectionStrategy {
    @Override
    public Elevator selectElevator(ExternalRequest request, List<Elevator> elevators) {
        Elevator bestElevator = null;
        int minDistance = Integer.MAX_VALUE;

        for (Elevator elevator : elevators) {
            // Check if elevator is active and accepting external requests
            if (!elevator.isActive() || !elevator.getStateHandler().canAcceptExternalRequests(elevator)) {
                continue;
            }

            int distance = Math.abs(elevator.getCurrentFloor() - request.getFloorNumber());
            
            // Secondary criteria: Prefer elevators that are already idle OR moving towards the floor
            boolean isMovingTowards = (elevator.getDirection() == Direction.UP && elevator.getCurrentFloor() <= request.getFloorNumber()) ||
                                      (elevator.getDirection() == Direction.DOWN && elevator.getCurrentFloor() >= request.getFloorNumber()) ||
                                      (elevator.getDirection() == Direction.IDLE);

            int penalty = isMovingTowards ? 0 : 5; // Add penalty if moving away
            int effectiveDistance = distance + penalty;

            if (effectiveDistance < minDistance) {
                minDistance = effectiveDistance;
                bestElevator = elevator;
            }
        }
        return bestElevator;
    }
}
