package model;

import java.util.EnumMap;
import java.util.Map;

public class Intersection {
    private final int id;
    private final String name;
    private final Map<Direction, TrafficLight> trafficLights;
    private boolean isEmergencyMode;
    private Direction emergencyDirection;
    private boolean isCyclePaused;

    public Intersection(int id, String name) {
        this.id = id;
        this.name = name;
        this.trafficLights = new EnumMap<>(Direction.class);
        for (Direction direction : Direction.values()) {
            this.trafficLights.put(direction, new TrafficLight(direction));
        }
        this.isEmergencyMode = false;
        this.emergencyDirection = null;
        this.isCyclePaused = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<Direction, TrafficLight> getTrafficLights() {
        return trafficLights;
    }

    public boolean isEmergencyMode() {
        return isEmergencyMode;
    }

    public void setEmergencyMode(boolean emergencyMode) {
        isEmergencyMode = emergencyMode;
    }

    public Direction getEmergencyDirection() {
        return emergencyDirection;
    }

    public void setEmergencyDirection(Direction emergencyDirection) {
        this.emergencyDirection = emergencyDirection;
    }

    public boolean isCyclePaused() {
        return isCyclePaused;
    }

    public void setCyclePaused(boolean cyclePaused) {
        isCyclePaused = cyclePaused;
    }

    public void setAllSignalsToRed() {
        System.out.println("[Intersection " + id + "] Transitioning all signals to RED.");
        for (TrafficLight light : trafficLights.values()) {
            transitionLightToRed(light);
        }
    }

    public void emergencyTransitionToRed(Direction direction) {
        TrafficLight light = trafficLights.get(direction);
        if (light != null) {
            transitionLightToRed(light);
        }
    }

    private void transitionLightToRed(TrafficLight light) {
        String current = light.getCurrentStateName();
        if ("GREEN".equals(current)) {
            System.out.println("[Safety Transition] Signal " + light.getDirection() + " GREEN -> YELLOW");
            light.turnYellow();
            System.out.println("[Safety Transition] Signal " + light.getDirection() + " YELLOW -> RED");
            light.turnRed();
        } else if ("YELLOW".equals(current)) {
            System.out.println("[Safety Transition] Signal " + light.getDirection() + " YELLOW -> RED");
            light.turnRed();
        } else if ("OFF".equals(current)) {
            System.out.println("[Safety Transition] Signal " + light.getDirection() + " OFF -> RED");
            light.turnRed();
        }
    }

    public void setSignalToGreen(Direction direction) {
        TrafficLight light = trafficLights.get(direction);
        if (light != null) {
            // Check if it's already GREEN
            if ("GREEN".equals(light.getCurrentStateName())) {
                return;
            }
            // Ensure proper transition: RED -> GREEN
            if ("RED".equals(light.getCurrentStateName()) || "OFF".equals(light.getCurrentStateName())) {
                light.turnGreen();
            } else {
                throw new InvalidStateTransitionException("Cannot directly set signal to GREEN from state " + light.getCurrentStateName());
            }
        }
    }

    public void setSignalToYellow(Direction direction) {
        TrafficLight light = trafficLights.get(direction);
        if (light != null) {
            if ("YELLOW".equals(light.getCurrentStateName())) {
                return;
            }
            if ("GREEN".equals(light.getCurrentStateName()) || "OFF".equals(light.getCurrentStateName())) {
                light.turnYellow();
            } else {
                throw new InvalidStateTransitionException("Cannot directly set signal to YELLOW from state " + light.getCurrentStateName());
            }
        }
    }

    public void setSignalToRed(Direction direction) {
        TrafficLight light = trafficLights.get(direction);
        if (light != null) {
            if ("RED".equals(light.getCurrentStateName())) {
                return;
            }
            if ("YELLOW".equals(light.getCurrentStateName()) || "OFF".equals(light.getCurrentStateName())) {
                light.turnRed();
            } else {
                throw new InvalidStateTransitionException("Cannot directly set signal to RED from state " + light.getCurrentStateName());
            }
        }
    }

    public void setSignalToOff(Direction direction) {
        TrafficLight light = trafficLights.get(direction);
        if (light != null) {
            light.turnOff();
        }
    }
}
