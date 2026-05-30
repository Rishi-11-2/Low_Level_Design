package controller;

import model.EntryResult;
import model.ParkingSlot;
import model.Ticket;
import model.Vehicle;
import model.VehicleType;
import service.SlotService;
import service.TicketService;
import java.util.Optional;

public class EntryController {
    private final SlotService slotService;
    private final TicketService ticketService;

    public EntryController(SlotService slotService, TicketService ticketService) {
        this.slotService = slotService;
        this.ticketService = ticketService;
    }

    public EntryResult enterVehicle(String licensePlate, VehicleType vehicleType) {
        System.out.println("\n>>> [EntryController] Processing entry request for vehicle " + licensePlate + " (" + vehicleType + ")");
        
        Optional<ParkingSlot> allocatedSlot = slotService.allocateSlot(vehicleType);
        if (allocatedSlot.isEmpty()) {
            System.out.println("<<< [EntryController] Rejected: No slots available for vehicle type: " + vehicleType);
            return new EntryResult(false, null, null, "No available slots for vehicle type: " + vehicleType);
        }

        ParkingSlot slot = allocatedSlot.get();
        Vehicle vehicle = new Vehicle(licensePlate, vehicleType);
        Ticket ticket = ticketService.generateTicket(vehicle.getId(), slot.getId());

        System.out.println("<<< [EntryController] Success: Ticket ID " + ticket.getId() + " generated, assigned to Slot ID " + slot.getId() + " on Floor " + slot.getFloorNumber());
        return new EntryResult(true, ticket.getId(), slot.getId(), "Vehicle successfully parked!");
    }
}
