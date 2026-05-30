package service;

import model.Floor;
import model.ParkingSlot;
import model.PricingRule;
import model.VehicleType;
import repository.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AdminService {
    private final FloorRepository floorRepository;
    private final SlotRepository slotRepository;
    private final PricingRuleRepository pricingRuleRepository;

    public AdminService(FloorRepository floorRepository, SlotRepository slotRepository,
                        PricingRuleRepository pricingRuleRepository) {
        this.floorRepository = floorRepository;
        this.slotRepository = slotRepository;
        this.pricingRuleRepository = pricingRuleRepository;
    }

    public synchronized Floor addFloor(int floorNumber) {
        Optional<Floor> existingFloor = floorRepository.findByFloorNumber(floorNumber);
        if (existingFloor.isPresent()) {
            return existingFloor.get();
        }
        Floor floor = new Floor(floorNumber);
        return floorRepository.save(floor);
    }

    public synchronized ParkingSlot addSlot(int floorNumber, VehicleType slotType) {
        Floor floor = floorRepository.findByFloorNumber(floorNumber)
                .orElseGet(() -> addFloor(floorNumber));

        ParkingSlot slot = new ParkingSlot(slotType, floorNumber);
        slotRepository.save(slot);
        
        floor.addSlot(slot);
        floorRepository.save(floor);
        
        return slot;
    }

    public PricingRule updatePricing(VehicleType vehicleType, double ratePerHour, double flatRate) {
        PricingRule rule = new PricingRule(vehicleType, ratePerHour, flatRate);
        return pricingRuleRepository.save(rule);
    }

    public Map<String, Object> getParkingLotStatus() {
        Map<String, Object> status = new HashMap<>();
        status.clear();
        
        long totalSlots = slotRepository.findAll().size();
        long occupiedSlots = slotRepository.findAll().stream().filter(ParkingSlot::isOccupied).count();
        long availableSlots = totalSlots - occupiedSlots;
        
        status.put("totalFloors", floorRepository.findAll().size());
        status.put("totalSlots", totalSlots);
        status.put("occupiedSlots", occupiedSlots);
        status.put("availableSlots", availableSlots);
        status.put("availableSlotsByType", slotRepository.getSlotStatistics());
        
        return status;
    }
}
