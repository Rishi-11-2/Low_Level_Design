package strategy;

import model.Elevator;
import model.ExternalRequest;
import java.util.List;

public class LoadBalancingStrategy implements ElevatorSelectionStrategy {
    @Override
    public Elevator selectElevator(ExternalRequest request, List<Elevator> elevators) {
        Elevator bestElevator = null;
        int minLoad = Integer.MAX_VALUE;

        for (Elevator elevator : elevators) {
            if (!elevator.isActive() || !elevator.getStateHandler().canAcceptExternalRequests(elevator)) {
                continue;
            }

            int load = elevator.getCurrentLoad();
            if (load < minLoad) {
                minLoad = load;
                bestElevator = elevator;
            }
        }
        return bestElevator;
    }
}
