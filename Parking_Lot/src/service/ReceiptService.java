package service;

import model.PaymentStatus;
import model.Receipt;
import java.util.UUID;

public class ReceiptService {
    public Receipt generateReceipt(UUID ticketId, double totalFee, PaymentStatus status) {
        System.out.println("[ReceiptService] Generating invoice receipt for Ticket ID: " + ticketId);
        return new Receipt(ticketId, totalFee, status);
    }
}
