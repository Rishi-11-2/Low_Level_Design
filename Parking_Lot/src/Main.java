import controller.AdminController;
import controller.EntryController;
import controller.ExitController;
import model.*;
import repository.*;
import service.*;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        System.out.println("===========================================================");
        System.out.println("         PARKING LOT LOW LEVEL DESIGN SYSTEM BOOT          ");
        System.out.println("===========================================================");

        // 1. Initialize Repositories
        FloorRepository floorRepository = new FloorRepository();
        SlotRepository slotRepository = new SlotRepository();
        TicketRepository ticketRepository = new TicketRepository();
        PricingRuleRepository pricingRuleRepository = new PricingRuleRepository();
        PaymentRepository paymentRepository = new PaymentRepository();

        // 2. Initialize Services
        SlotService slotService = new SlotService(slotRepository);
        TicketService ticketService = new TicketService(ticketRepository);
        PricingService pricingService = new PricingService(pricingRuleRepository);
        PaymentService paymentService = new PaymentService(paymentRepository);
        ReceiptService receiptService = new ReceiptService();
        AdminService adminService = new AdminService(floorRepository, slotRepository, pricingRuleRepository);

        // 3. Initialize Controllers
        AdminController adminController = new AdminController(adminService);
        EntryController entryController = new EntryController(slotService, ticketService);
        ExitController exitController = new ExitController(
                ticketService, pricingService, paymentService, slotService, receiptService
        );

        // 4. Setup Parking Lot Infrastructure (Admin Flow)
        System.out.println("\n--- Admin: Constructing Floors and Adding Parking Slots ---");
        adminController.addFloor(1);
        adminController.addFloor(2);

        // Floor 1 slots
        adminController.addSlot(1, VehicleType.BIKE);
        adminController.addSlot(1, VehicleType.CAR);
        adminController.addSlot(1, VehicleType.EV);

        // Floor 2 slots
        adminController.addSlot(2, VehicleType.CAR);
        adminController.addSlot(2, VehicleType.TRUCK);

        // Configure Pricing Rules
        System.out.println("\n--- Admin: Defining Pricing Rules ---");
        adminController.updatePricing(VehicleType.BIKE, 5.0, 2.0);    // Flat $2 + Hourly $5
        adminController.updatePricing(VehicleType.CAR, 10.0, 5.0);    // Flat $5 + Hourly $10
        adminController.updatePricing(VehicleType.EV, 8.0, 4.0);       // Flat $4 + Hourly $8
        adminController.updatePricing(VehicleType.TRUCK, 20.0, 10.0);  // Flat $10 + Hourly $20

        // Display Initial Status
        adminController.displayParkingLotStatus();

        // 5. Entry Flow Simulation
        System.out.println("\n--- Entry Flow: Vehicles Arriving ---");
        
        // MH-12-AB-1234 (CAR)
        EntryResult entryCar1 = entryController.enterVehicle("MH-12-AB-1234", VehicleType.CAR);
        UUID car1TicketId = entryCar1.getTicketId();

        // DL-3C-CD-5678 (BIKE)
        EntryResult entryBike1 = entryController.enterVehicle("DL-3C-CD-5678", VehicleType.BIKE);
        UUID bike1TicketId = entryBike1.getTicketId();

        // KA-01-EF-9012 (CAR)
        EntryResult entryCar2 = entryController.enterVehicle("KA-01-EF-9012", VehicleType.CAR);
        UUID car2TicketId = entryCar2.getTicketId();

        // TX-CAR-7890 (CAR) - Trying to occupy a 3rd CAR slot, but only 2 were added (Floor 1, Floor 2)
        EntryResult entryCar3 = entryController.enterVehicle("TX-CAR-7890", VehicleType.CAR);

        // Display occupied status
        adminController.displayParkingLotStatus();

        // 6. Exit Flow Simulation (with Simulated Passage of Time & Gateway Failures/Retries)
        System.out.println("\n--- Exit Flow: Vehicles Checking Out ---");

        // Simulate some time passing by setting entryTime back manually for the first car to calculate higher fee
        if (entryCar1.isSuccess() && car1TicketId != null) {
            ticketService.getTicket(car1TicketId).ifPresent(ticket -> {
                // Set entry time 3 hours ago
                java.time.LocalDateTime simulatedEntryTime = java.time.LocalDateTime.now().minusHours(3);
                // We access the entryTime using reflection or simply we can just use the standard elapsed time in seconds.
                // Wait! Since our PricingService uses actual elapsed seconds in real-time as hours:
                // Let's sleep for 2 seconds to simulate 2 hours of elapsed parking time!
                System.out.println("[Simulation] Simulating active parking duration...");
                try {
                    Thread.sleep(2000); // 2 seconds of sleep
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // Checkout CAR 1 via Razorpay (This will capture payment successfully or fail depending on transient counter)
        System.out.println("\n--- Checkout Attempt 1: CAR 1 via Razorpay ---");
        ExitResult exitCar1 = exitController.exitVehicle(car1TicketId, PaymentGateway.RAZORPAY, VehicleType.CAR);
        
        // Checkout BIKE 1 via Razorpay (This will trigger the simulated transient connection failure on 3rd attempt!)
        System.out.println("\n--- Checkout Attempt 2: BIKE 1 via Razorpay (Should Trigger Transient Connection Failure) ---");
        ExitResult exitBikeAttempt1 = exitController.exitVehicle(bike1TicketId, PaymentGateway.RAZORPAY, VehicleType.BIKE);
        
        if (!exitBikeAttempt1.isSuccess()) {
            System.out.println("[Simulation] Fallback flow: Retrying checkout for BIKE 1 using STRIPE gateway...");
            ExitResult exitBikeAttempt2 = exitController.exitVehicle(bike1TicketId, PaymentGateway.STRIPE, VehicleType.BIKE);
        }

        // Checkout CAR 2 via Stripe (Should succeed)
        System.out.println("\n--- Checkout Attempt 3: CAR 2 via Stripe ---");
        ExitResult exitCar2 = exitController.exitVehicle(car2TicketId, PaymentGateway.STRIPE, VehicleType.CAR);

        // Display final status - all spots should be vacant again!
        adminController.displayParkingLotStatus();

        System.out.println("\n===========================================================");
        System.out.println("       PARKING LOT LOW LEVEL DESIGN SIMULATION COMPLETE    ");
        System.out.println("===========================================================");
    }
}
