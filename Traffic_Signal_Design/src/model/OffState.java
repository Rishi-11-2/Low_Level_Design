package model;

public class OffState implements TrafficLightState {
    @Override
    public void turnGreen(TrafficLight trafficLight) {
        trafficLight.setState(new GreenState());
    }

    @Override
    public void turnYellow(TrafficLight trafficLight) {
        trafficLight.setState(new YellowState());
    }

    @Override
    public void turnRed(TrafficLight trafficLight) {
        trafficLight.setState(new RedState());
    }

    @Override
    public void turnOff(TrafficLight trafficLight) {
        // Already OFF, no change
    }

    @Override
    public String getStateName() {
        return "OFF";
    }

    @Override
    public boolean canTransitionTo(TrafficLightState newState) {
        return true; // From OFF we can boot into any state
    }
}
