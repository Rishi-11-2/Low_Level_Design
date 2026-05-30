package model;

import java.util.UUID;

public class ParkingSlot {
    private final UUID id;
    private final VehicleType slotType;
    private boolean isOccupied;
    private final int floorNumber;

    public ParkingSlot(VehicleType slotType, int floorNumber) {
        this.id = UUID.randomUUID();
        this.slotType = slotType;
        this.isOccupied = false;
        this.floorNumber = floorNumber;
    }

    public UUID getId() {
        return id;
    }

    public VehicleType getSlotType() {
        return slotType;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public int getFloorNumber() {
        return floorNumber;
    }
}
