package repository;

import model.Direction;
import model.SignalTiming;
import java.util.HashMap;
import java.util.Map;

public class TimingRepository {
    // Key: "intersectionId-direction"
    private final Map<String, SignalTiming> timingMap = new HashMap<>();

    private String getKey(int intersectionId, Direction direction) {
        return intersectionId + "-" + direction;
    }

    public void saveSignalTiming(SignalTiming timing) {
        timingMap.put(getKey(timing.getIntersectionId(), timing.getDirection()), timing);
    }

    public SignalTiming getSignalTiming(int intersectionId, Direction direction) {
        String key = getKey(intersectionId, direction);
        if (!timingMap.containsKey(key)) {
            // Default: 10 seconds of green duration
            SignalTiming defaultTiming = new SignalTiming(intersectionId, direction, 10);
            timingMap.put(key, defaultTiming);
        }
        return timingMap.get(key);
    }

    public void updateSignalTiming(int intersectionId, Direction direction, int greenDuration) {
        SignalTiming timing = getSignalTiming(intersectionId, direction);
        timing.setGreenDuration(greenDuration);
        saveSignalTiming(timing);
    }
}
