package controller;

import model.Direction;
import model.SignalTiming;
import service.TimingService;

public class TimingController {
    private final TimingService timingService;

    public TimingController(TimingService timingService) {
        this.timingService = timingService;
    }

    public void setSignalTiming(int intersectionId, Direction direction, int greenDuration) {
        System.out.println("\n>>> [TimingController] Configuring manual green duration: direction=" + direction + " duration=" + greenDuration + "s");
        timingService.setSignalTiming(intersectionId, direction, greenDuration);
    }

    public void enableDynamicTiming(int intersectionId, Direction direction, boolean enable) {
        System.out.println("\n>>> [TimingController] Configuring dynamic green mode: direction=" + direction + " enabled=" + enable);
        timingService.enableDynamicTiming(intersectionId, direction, enable);
    }

    public void adjustTimingBasedOnTraffic(int intersectionId, Direction direction) {
        System.out.println("\n>>> [TimingController] Triggering dynamic adjustment cycle: direction=" + direction);
        timingService.adjustTimingBasedOnTraffic(intersectionId, direction);
    }

    public SignalTiming getSignalTiming(int intersectionId, Direction direction) {
        return timingService.getSignalTiming(intersectionId, direction);
    }
}
