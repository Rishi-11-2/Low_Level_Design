package model;

public class YellowState implements TrafficLightState {
    @Override
    public void turnGreen(TrafficLight trafficLight) {
        throw new InvalidStateTransitionException("Cannot transition from YELLOW to GREEN.");
    }

    @Override
    public void turnYellow(TrafficLight trafficLight) {
        // Already YELLOW, no change
    }

    @Override
    public void turnRed(TrafficLight trafficLight) {
        trafficLight.setState(new RedState());
    }

    @Override
    public void turnOff(TrafficLight trafficLight) {
        trafficLight.setState(new OffState());
    }

    @Override
    public String getStateName() {
        return "YELLOW";
    }

    @Override
    public boolean canTransitionTo(TrafficLightState newState) {
        return newState instanceof RedState || newState instanceof OffState;
    }
}
