package service;

import model.ParkingSlot;
import model.VehicleType;
import repository.SlotRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SlotService {
    private final SlotRepository slotRepository;

    public SlotService(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    public synchronized Optional<ParkingSlot> allocateSlot(VehicleType vehicleType) {
        List<ParkingSlot> availableSlots = slotRepository.findAvailableSlots(vehicleType);
        if (availableSlots.isEmpty()) {
            return Optional.empty();
        }
        
        // Strategy: choose the first available slot (Default strategy)
        ParkingSlot slot = availableSlots.get(0);
        slot.setOccupied(true);
        slotRepository.save(slot);
        return Optional.of(slot);
    }

    public synchronized void releaseSlot(UUID slotId) {
        slotRepository.findById(slotId).ifPresent(slot -> {
            slot.setOccupied(false);
            slotRepository.save(slot);
        });
    }
}
