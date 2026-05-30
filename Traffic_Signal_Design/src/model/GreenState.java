package model;

public class GreenState implements TrafficLightState {
    @Override
    public void turnGreen(TrafficLight trafficLight) {
        // Already GREEN, no change
    }

    @Override
    public void turnYellow(TrafficLight trafficLight) {
        trafficLight.setState(new YellowState());
    }

    @Override
    public void turnRed(TrafficLight trafficLight) {
        throw new InvalidStateTransitionException("Cannot transition from GREEN directly to RED.");
    }

    @Override
    public void turnOff(TrafficLight trafficLight) {
        trafficLight.setState(new OffState());
    }

    @Override
    public String getStateName() {
        return "GREEN";
    }

    @Override
    public boolean canTransitionTo(TrafficLightState newState) {
        return newState instanceof YellowState || newState instanceof OffState;
    }
}
