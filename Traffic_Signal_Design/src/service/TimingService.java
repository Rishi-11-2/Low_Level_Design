package service;

import model.Direction;
import model.SignalTiming;
import repository.TimingRepository;
import repository.TrafficRepository;

public class TimingService {
    private final TimingRepository timingRepository;
    private final TrafficRepository trafficRepository;

    public TimingService(TimingRepository timingRepository, TrafficRepository trafficRepository) {
        this.timingRepository = timingRepository;
        this.trafficRepository = trafficRepository;
    }

    public void setSignalTiming(int intersectionId, Direction direction, int greenDuration) {
        timingRepository.updateSignalTiming(intersectionId, direction, greenDuration);
        System.out.println("[TimingService] Signal timing for " + direction + " set manually to: " + greenDuration + "s");
    }

    public void enableDynamicTiming(int intersectionId, Direction direction, boolean enable) {
        SignalTiming timing = timingRepository.getSignalTiming(intersectionId, direction);
        if (timing != null) {
            timing.setDynamic(enable);
            timingRepository.saveSignalTiming(timing);
            System.out.println("[TimingService] Dynamic Traffic-Based Timing for " + direction + " is now: " + (enable ? "ENABLED" : "DISABLED"));
        }
    }

    public SignalTiming getSignalTiming(int intersectionId, Direction direction) {
        return timingRepository.getSignalTiming(intersectionId, direction);
    }

    public void adjustTimingBasedOnTraffic(int intersectionId, Direction direction) {
        SignalTiming timing = timingRepository.getSignalTiming(intersectionId, direction);
        if (timing != null && timing.isDynamic()) {
            int count = trafficRepository.getCount(direction);
            int optimal = calculateOptimalGreenDuration(count);
            System.out.println("[TimingService] Dynamic Sensor Triggered: direction " + direction + " count=" + count + ". Setting green phase duration to: " + optimal + "s");
            timing.setGreenDuration(optimal);
            timingRepository.saveSignalTiming(timing);
        }
    }

    public int calculateOptimalGreenDuration(int vehicleCount) {
        if (vehicleCount < 5) {
            return 10; // Minimum duration
        } else if (vehicleCount <= 15) {
            return 20; // Moderate traffic
        } else {
            return 30; // High traffic
        }
    }
}
