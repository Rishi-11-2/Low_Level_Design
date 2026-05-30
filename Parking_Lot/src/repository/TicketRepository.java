package repository;

import model.Ticket;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TicketRepository {
    private final Map<UUID, Ticket> ticketMap = new ConcurrentHashMap<>();

    public Ticket save(Ticket ticket) {
        ticketMap.put(ticket.getId(), ticket);
        return ticket;
    }

    public Optional<Ticket> findById(UUID id) {
        return Optional.ofNullable(ticketMap.get(id));
    }

    public Optional<Ticket> findActiveTicketByVehicleId(UUID vehicleId) {
        return ticketMap.values().stream()
                .filter(t -> t.getVehicleId().equals(vehicleId) && t.isActive())
                .findFirst();
    }
}
