package strategy;

import model.Elevator;
import model.ExternalRequest;
import java.util.List;

public interface ElevatorSelectionStrategy {
    Elevator selectElevator(ExternalRequest request, List<Elevator> elevators);
}
