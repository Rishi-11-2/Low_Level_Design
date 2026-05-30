package model;

import java.util.UUID;

public class Payment {
    private final UUID id;
    private final UUID ticketId;
    private final double amount;
    private final PaymentGateway gateway;
    private PaymentStatus status;

    public Payment(UUID ticketId, double amount, PaymentGateway gateway, PaymentStatus status) {
        this.id = UUID.randomUUID();
        this.ticketId = ticketId;
        this.amount = amount;
        this.gateway = gateway;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public UUID getTicketId() {
        return ticketId;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentGateway getGateway() {
        return gateway;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
