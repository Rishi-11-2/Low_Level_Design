package repository;

import model.ParkingSlot;
import model.VehicleType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SlotRepository {
    private final Map<UUID, ParkingSlot> slotMap = new ConcurrentHashMap<>();

    public ParkingSlot save(ParkingSlot slot) {
        slotMap.put(slot.getId(), slot);
        return slot;
    }

    public Optional<ParkingSlot> findById(UUID id) {
        return Optional.ofNullable(slotMap.get(id));
    }

    public List<ParkingSlot> findAvailableSlots(VehicleType slotType) {
        return slotMap.values().stream()
                .filter(slot -> slot.getSlotType() == slotType && !slot.isOccupied())
                .collect(Collectors.toList());
    }

    public Collection<ParkingSlot> findAll() {
        return slotMap.values();
    }

    public Map<VehicleType, Long> getSlotStatistics() {
        return slotMap.values().stream()
                .filter(slot -> !slot.isOccupied())
                .collect(Collectors.groupingBy(ParkingSlot::getSlotType, Collectors.counting()));
    }
}
