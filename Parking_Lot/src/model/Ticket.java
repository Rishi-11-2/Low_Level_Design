package model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Ticket {
    private final UUID id;
    private final UUID vehicleId;
    private final UUID slotId;
    private final LocalDateTime entryTime;
    private boolean isActive;

    public Ticket(UUID vehicleId, UUID slotId) {
        this.id = UUID.randomUUID();
        this.vehicleId = vehicleId;
        this.slotId = slotId;
        this.entryTime = LocalDateTime.now();
        this.isActive = true;
    }

    public UUID getId() {
        return id;
    }

    public UUID getVehicleId() {
        return vehicleId;
    }

    public UUID getSlotId() {
        return slotId;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
