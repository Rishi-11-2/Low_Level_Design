package repository;

import model.Direction;
import model.VehicleCounter;
import java.util.EnumMap;
import java.util.Map;

public class TrafficRepository {
    private final Map<Direction, VehicleCounter> counterMap = new EnumMap<>(Direction.class);

    public TrafficRepository() {
        for (Direction direction : Direction.values()) {
            counterMap.put(direction, new VehicleCounter(direction));
        }
    }

    public void updateCount(Direction direction, int count) {
        VehicleCounter counter = counterMap.get(direction);
        if (counter != null) {
            counter.setCount(count);
        }
    }

    public int getCount(Direction direction) {
        VehicleCounter counter = counterMap.get(direction);
        return counter != null ? counter.getCount() : 0;
    }
}
