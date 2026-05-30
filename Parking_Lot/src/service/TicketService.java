package service;

import model.Ticket;
import repository.TicketRepository;
import java.util.Optional;
import java.util.UUID;

public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket generateTicket(UUID vehicleId, UUID slotId) {
        Ticket ticket = new Ticket(vehicleId, slotId);
        return ticketRepository.save(ticket);
    }

    public Optional<Ticket> getTicket(UUID ticketId) {
        return ticketRepository.findById(ticketId);
    }

    public Optional<Ticket> getActiveTicketByVehicleId(UUID vehicleId) {
        return ticketRepository.findActiveTicketByVehicleId(vehicleId);
    }

    public void deactivateTicket(UUID ticketId) {
        ticketRepository.findById(ticketId).ifPresent(ticket -> {
            ticket.setActive(false);
            ticketRepository.save(ticket);
        });
    }
}
