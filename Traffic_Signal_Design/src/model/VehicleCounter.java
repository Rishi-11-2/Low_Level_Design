package model;

public class VehicleCounter {
    private final Direction direction;
    private int count;
    private long lastUpdate;

    public VehicleCounter(Direction direction) {
        this.direction = direction;
        this.count = 0;
        this.lastUpdate = System.currentTimeMillis();
    }

    public Direction getDirection() {
        return direction;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        this.lastUpdate = System.currentTimeMillis();
    }

    public long getLastUpdate() {
        return lastUpdate;
    }
}
