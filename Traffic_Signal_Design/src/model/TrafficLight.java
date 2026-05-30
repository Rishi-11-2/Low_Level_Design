package model;

public class TrafficLight {
    private final Direction direction;
    private TrafficLightState currentState;

    public TrafficLight(Direction direction) {
        this.direction = direction;
        this.currentState = new OffState(); // Starts off by default
    }

    public Direction getDirection() {
        return direction;
    }

    public void setState(TrafficLightState newState) {
        this.currentState = newState;
    }

    public void turnGreen() {
        currentState.turnGreen(this);
    }

    public void turnYellow() {
        currentState.turnYellow(this);
    }

    public void turnRed() {
        currentState.turnRed(this);
    }

    public void turnOff() {
        currentState.turnOff(this);
    }

    public String getCurrentStateName() {
        return currentState.getStateName();
    }

    public boolean canTransitionTo(TrafficLightState newState) {
        return currentState.canTransitionTo(newState);
    }
}
