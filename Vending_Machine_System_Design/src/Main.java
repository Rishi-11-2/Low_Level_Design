import controller.VendingMachineController;
import model.*;
import repository.InventoryRepository;
import service.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("==============================================================");
        System.out.println("        VENDING MACHINE LOW LEVEL DESIGN SYSTEM BOOT          ");
        System.out.println("==============================================================");

        // 1. Initialize Repositories
        InventoryRepository inventoryRepository = new InventoryRepository();

        // 2. Initialize Services
        InventoryService inventoryService = new InventoryService(inventoryRepository);
        CoinService coinService = new CoinService();
        VendingMachineService vendingMachineService = new VendingMachineService(coinService);

        // 3. Initialize Controllers
        VendingMachineController controller = new VendingMachineController(vendingMachineService, inventoryService);

        // 4. Setup Vending Machine Inventory (Admin Construct Flow)
        System.out.println("\n--- Admin Setup: Initializing Product Shelves ---");
        inventoryService.addShelf(101, ItemType.COKE, 150, 5);   // $1.50 Coke (5 in stock)
        inventoryService.addShelf(102, ItemType.PEPSI, 125, 10); // $1.25 Pepsi (10 in stock)
        inventoryService.addShelf(103, ItemType.SODA, 75, 15);   // $0.75 Soda (15 in stock)
        inventoryService.addShelf(104, ItemType.JUICE, 200, 0);  // $2.00 Juice (0 in stock - SOLD OUT)

        // Display current inventory status
        controller.displayInventory();

        // 5. Initialize the Vending Machine Context
        VendingMachine vendingMachine = new VendingMachine(inventoryRepository.get());
        vendingMachine.setVendingMachineState(new IdleState());

        // ==============================================================
        // SIMULATION FLOW 1: Successful Purchase of PEPSI with change return
        // ==============================================================
        System.out.println("\n==============================================================");
        System.out.println("   SIMULATION 1: Successful Pepsi Purchase ($1.25) with Change");
        System.out.println("==============================================================");

        controller.clickInsertCoinButton(vendingMachine);           // IDLE -> HAS_MONEY
        controller.insertCoin(vendingMachine, Coin.DOLLAR);         // Insert $1.00
        controller.insertCoin(vendingMachine, Coin.QUARTER);        // Insert $0.25
        controller.insertCoin(vendingMachine, Coin.DIME);           // Insert $0.10 (Total $1.35)
        controller.startSelection(vendingMachine);                  // HAS_MONEY -> SELECTION
        controller.selectProduct(vendingMachine, 102);              // Select PEPSI ($1.25), expects $0.10 change

        controller.displayInventory(); // Confirm inventory decremented

        // ==============================================================
        // SIMULATION FLOW 2: Insufficient money entered (rejection and auto-refund)
        // ==============================================================
        System.out.println("\n==============================================================");
        System.out.println("   SIMULATION 2: Insufficient Funds for Coke ($1.50)");
        System.out.println("==============================================================");

        controller.clickInsertCoinButton(vendingMachine);           // IDLE -> HAS_MONEY
        controller.insertCoin(vendingMachine, Coin.DOLLAR);         // Insert $1.00 (Required $1.50)
        controller.startSelection(vendingMachine);                  // HAS_MONEY -> SELECTION
        
        try {
            controller.selectProduct(vendingMachine, 101);          // Select COKE, expects refund and reset to IDLE
        } catch (Exception e) {
            System.out.println("[Client Alert] Insufficient funds caught by client: " + e.getMessage());
        }

        // Confirm state reset to Idle
        System.out.println("Current Vending Machine State class: " + vendingMachine.getState().getClass().getSimpleName());

        // ==============================================================
        // SIMULATION FLOW 3: Attempting to buy a Sold Out product (rejection and auto-refund)
        // ==============================================================
        System.out.println("\n==============================================================");
        System.out.println("   SIMULATION 3: Purchase Sold Out Juice ($2.00)");
        System.out.println("==============================================================");

        controller.clickInsertCoinButton(vendingMachine);
        controller.insertCoin(vendingMachine, Coin.DOLLAR);
        controller.insertCoin(vendingMachine, Coin.DOLLAR);         // Insert $2.00
        controller.startSelection(vendingMachine);
        
        try {
            controller.selectProduct(vendingMachine, 104);          // Select JUICE (sold out), expects refund and reset to IDLE
        } catch (Exception e) {
            System.out.println("[Client Alert] Sold out caught by client: " + e.getMessage());
        }

        // ==============================================================
        // SIMULATION FLOW 4: Transaction Cancellation Flow
        // ==============================================================
        System.out.println("\n==============================================================");
        System.out.println("   SIMULATION 4: User Manual Cancel & Refund Flow");
        System.out.println("==============================================================");

        controller.clickInsertCoinButton(vendingMachine);
        controller.insertCoin(vendingMachine, Coin.QUARTER);
        controller.insertCoin(vendingMachine, Coin.DIME);           // Total $0.35 inserted
        controller.cancelAndRefund(vendingMachine);                 // Triggers manual cancellation, refund, reset to IDLE

        // ==============================================================
        // SIMULATION FLOW 5: State Transition Safeguard Verification
        // ==============================================================
        System.out.println("\n==============================================================");
        System.out.println("   SIMULATION 5: Verifying State Machine Safe Transition Limits");
        System.out.println("==============================================================");

        try {
            System.out.println("[Client] Attempting to select a product in IDLE state...");
            controller.selectProduct(vendingMachine, 103);
        } catch (IllegalStateException e) {
            System.out.println("[State Pattern Block] Blocked: " + e.getMessage());
        }

        try {
            System.out.println("[Client] Attempting to insert coins in IDLE state...");
            controller.insertCoin(vendingMachine, Coin.DOLLAR);
        } catch (IllegalStateException e) {
            System.out.println("[State Pattern Block] Blocked: " + e.getMessage());
        }

        System.out.println("\n==============================================================");
        System.out.println("       VENDING MACHINE LOW LEVEL DESIGN SIMULATION COMPLETE   ");
        System.out.println("==============================================================");
    }
}
