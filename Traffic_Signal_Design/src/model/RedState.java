package model;

public class RedState implements TrafficLightState {
    @Override
    public void turnGreen(TrafficLight trafficLight) {
        trafficLight.setState(new GreenState());
    }

    @Override
    public void turnYellow(TrafficLight trafficLight) {
        throw new InvalidStateTransitionException("Cannot transition from RED to YELLOW.");
    }

    @Override
    public void turnRed(TrafficLight trafficLight) {
        // Already RED, no change
    }

    @Override
    public void turnOff(TrafficLight trafficLight) {
        trafficLight.setState(new OffState());
    }

    @Override
    public String getStateName() {
        return "RED";
    }

    @Override
    public boolean canTransitionTo(TrafficLightState newState) {
        return newState instanceof GreenState || newState instanceof OffState;
    }
}
