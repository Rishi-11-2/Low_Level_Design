package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Floor {
    private final UUID id;
    private final int floorNumber;
    private final List<ParkingSlot> slots;

    public Floor(int floorNumber) {
        this.id = UUID.randomUUID();
        this.floorNumber = floorNumber;
        this.slots = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public List<ParkingSlot> getSlots() {
        return slots;
    }

    public void addSlot(ParkingSlot slot) {
        slots.add(slot);
    }
}
