package model;

public class Building {
    private final String id;
    private final String name;
    private final int minFloor;
    private final int maxFloor;
    private int totalElevators;
    private SystemState systemState;

    public Building(String id, String name, int minFloor, int maxFloor, int totalElevators) {
        this.id = id;
        this.name = name;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.totalElevators = totalElevators;
        this.systemState = SystemState.STOPPED;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMinFloor() {
        return minFloor;
    }

    public int getMaxFloor() {
        return maxFloor;
    }

    public int getTotalElevators() {
        return totalElevators;
    }

    public void setTotalElevators(int totalElevators) {
        this.totalElevators = totalElevators;
    }

    public SystemState getSystemState() {
        return systemState;
    }

    public void setSystemState(SystemState systemState) {
        this.systemState = systemState;
    }
}
