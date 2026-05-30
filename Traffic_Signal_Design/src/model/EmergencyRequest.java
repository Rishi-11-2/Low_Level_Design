package model;

public class EmergencyRequest {
    private final int id;
    private final int intersectionId;
    private final Direction direction;
    private final int duration;
    private boolean isActive;

    public EmergencyRequest(int id, int intersectionId, Direction direction, int duration) {
        this.id = id;
        this.intersectionId = intersectionId;
        this.direction = direction;
        this.duration = duration;
        this.isActive = true;
    }

    public int getId() {
        return id;
    }

    public int getIntersectionId() {
        return intersectionId;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
