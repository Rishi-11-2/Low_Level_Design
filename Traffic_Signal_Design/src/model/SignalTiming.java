package model;

public class SignalTiming {
    private final int intersectionId;
    private final Direction direction;
    private int greenDuration;
    private boolean isDynamic;
    private final int yellowDuration; // Constant 3 seconds for safety

    public SignalTiming(int intersectionId, Direction direction, int greenDuration) {
        this.intersectionId = intersectionId;
        this.direction = direction;
        this.greenDuration = greenDuration;
        this.isDynamic = false;
        this.yellowDuration = 3;
    }

    public int getIntersectionId() {
        return intersectionId;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getGreenDuration() {
        return greenDuration;
    }

    public void setGreenDuration(int greenDuration) {
        this.greenDuration = greenDuration;
    }

    public boolean isDynamic() {
        return isDynamic;
    }

    public void setDynamic(boolean dynamic) {
        isDynamic = dynamic;
    }

    public int getYellowDuration() {
        return yellowDuration;
    }
}
