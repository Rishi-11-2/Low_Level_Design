package controller;

import model.*;
import service.*;
import java.util.Optional;
import java.util.UUID;

public class ExitController {
    private final TicketService ticketService;
    private final PricingService pricingService;
    private final PaymentService paymentService;
    private final SlotService slotService;
    private final ReceiptService receiptService;

    public ExitController(TicketService ticketService, PricingService pricingService,
                          PaymentService paymentService, SlotService slotService,
                          ReceiptService receiptService) {
        this.ticketService = ticketService;
        this.pricingService = pricingService;
        this.paymentService = paymentService;
        this.slotService = slotService;
        this.receiptService = receiptService;
    }

    public ExitResult exitVehicle(UUID ticketId, PaymentGateway gateway, VehicleType vehicleType) {
        System.out.println("\n>>> [ExitController] Processing checkout request for Ticket ID: " + ticketId + " via " + gateway);

        Optional<Ticket> ticketOpt = ticketService.getTicket(ticketId);
        if (ticketOpt.isEmpty()) {
            System.out.println("<<< [ExitController] Rejected: Invalid or unrecognised ticket ID");
            return new ExitResult(false, null, "Invalid or unrecognised ticket ID.");
        }

        Ticket ticket = ticketOpt.get();
        if (!ticket.isActive()) {
            System.out.println("<<< [ExitController] Rejected: Ticket has already been settled and is inactive");
            return new ExitResult(false, null, "Ticket has already been settled and is inactive.");
        }

        double fee = pricingService.calculateFee(ticket, vehicleType);
        System.out.println("[ExitController] Total outstanding fee calculated: $" + fee);

        boolean paymentSuccess = paymentService.processPayment(ticketId, fee, gateway);
        if (!paymentSuccess) {
            System.out.println("<<< [ExitController] Payment declined. Please attempt payment again or choose another gateway");
            return new ExitResult(false, null, "Payment declined. Ticket remains active. Please try again.");
        }

        // Settlement
        ticketService.deactivateTicket(ticketId);
        slotService.releaseSlot(ticket.getSlotId());
        Receipt receipt = receiptService.generateReceipt(ticketId, fee, PaymentStatus.COMPLETED);

        System.out.println("<<< [ExitController] Settlement complete: Slot released. Receipt issued ID " + receipt.getId());
        return new ExitResult(true, receipt, "Checkout processed successfully!");
    }
}
