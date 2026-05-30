package model;

public class Elevator {
    private final String id;
    private final String buildingId;
    private int currentFloor;
    private Direction direction;
    private final int capacity; // weight or passenger capacity
    private int currentLoad;
    private boolean isActive;
    private ElevatorStateHandler stateHandler;

    public Elevator(String id, String buildingId, int capacity) {
        this.id = id;
        this.buildingId = buildingId;
        this.currentFloor = 0; // Starts at ground floor (0) by default
        this.direction = Direction.IDLE;
        this.capacity = capacity;
        this.currentLoad = 0;
        this.isActive = true;
        this.stateHandler = new StoppedState(); // Starts stopped
    }

    public String getId() {
        return id;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCurrentLoad() {
        return currentLoad;
    }

    public void setCurrentLoad(int currentLoad) {
        this.currentLoad = currentLoad;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public ElevatorStateHandler getStateHandler() {
        return stateHandler;
    }

    public void setStateHandler(ElevatorStateHandler stateHandler) {
        this.stateHandler = stateHandler;
    }

    public void openDoors() {
        stateHandler.openDoors(this);
    }

    public void closeDoors() {
        stateHandler.closeDoors(this);
    }

    public void enterMaintenance() {
        stateHandler.enterMaintenance(this);
    }

    public void exitMaintenance() {
        stateHandler.exitMaintenance(this);
    }

    public boolean isFull() {
        return currentLoad >= capacity;
    }
}
