package model;

import java.util.UUID;

public class EntryResult {
    private final boolean success;
    private final UUID ticketId;
    private final UUID slotId;
    private final String message;

    public EntryResult(boolean success, UUID ticketId, UUID slotId, String message) {
        this.success = success;
        this.ticketId = ticketId;
        this.slotId = slotId;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public UUID getTicketId() {
        return ticketId;
    }

    public UUID getSlotId() {
        return slotId;
    }

    public String getMessage() {
        return message;
    }
}
