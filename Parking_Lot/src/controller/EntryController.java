package controller;

import model.EntryResult;
import model.ParkingSlot;
import model.Ticket;
import model.Vehicle;
import model.VehicleType;
import repository.VehicleRepository;
import service.SlotService;
import service.TicketService;
import java.util.Optional;

public class EntryController {
    private final SlotService slotService;
    private final TicketService ticketService;
    private final VehicleRepository vehicleRepository;

    public EntryController(SlotService slotService, TicketService ticketService, VehicleRepository vehicleRepository) {
        this.slotService = slotService;
        this.ticketService = ticketService;
        this.vehicleRepository = vehicleRepository;
    }

    public EntryResult enterVehicle(String licensePlate, VehicleType vehicleType) {
        System.out.println("\n>>> [EntryController] Processing entry request for vehicle " + licensePlate + " (" + vehicleType + ")");
        
        Optional<ParkingSlot> allocatedSlot = slotService.allocateSlot(vehicleType);
        if (allocatedSlot.isEmpty()) {
            System.out.println("<<< [EntryController] Rejected: No slots available for vehicle type: " + vehicleType);
            return new EntryResult(false, null, null, "No available slots for vehicle type: " + vehicleType);
        }

        ParkingSlot slot = allocatedSlot.get();
        
        // Find existing vehicle or create and persist a new one to prevent data leaks
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate)
                .orElseGet(() -> vehicleRepository.save(new Vehicle(licensePlate, vehicleType)));

        Ticket ticket = ticketService.generateTicket(vehicle.getId(), slot.getId());

        System.out.println("<<< [EntryController] Success: Ticket ID " + ticket.getId() + " generated, assigned to Slot ID " + slot.getId() + " on Floor " + slot.getFloorNumber());
        return new EntryResult(true, ticket.getId(), slot.getId(), "Vehicle successfully parked!");
    }
}
