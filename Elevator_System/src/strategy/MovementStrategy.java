package strategy;

import model.Elevator;
import java.util.List;

public interface MovementStrategy {
    List<Integer> calculatePath(Elevator elevator, List<Integer> requestedFloors);
}
