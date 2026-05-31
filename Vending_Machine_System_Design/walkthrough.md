# Vending Machine System LLD Java Implementation Walkthrough

We have successfully implemented and verified the Low-Level Design (LLD) for the Vending Machine System in Java under `/Users/rishi/Projects/LLD/Vending_Machine_System_Design/src/`. The design adheres to the tiered enterprise architecture (Client -> Controller -> Service -> Repository -> Domain) and strictly follows the state machine details in `Vending_Machine_Design.pdf`.

---

## 1. Package Structure Created
All source files are organized under `/Users/rishi/Projects/LLD/Vending_Machine_System_Design/src/`:

```
src/
├── Main.java                        (System bootstrap & end-to-end simulation runner)
├── model/
│   ├── ItemType.java                (Enum: COKE, PEPSI, SODA, JUICE)
│   ├── Coin.java                    (Enum: PENNY(1), NICKEL(5), DIME(10), QUARTER(25), DOLLAR(100))
│   ├── Item.java                    (Domain model)
│   ├── ItemShelf.java               (Domain shelf model)
│   ├── Inventory.java               (Domain repository entity)
│   ├── State.java                   (State Pattern: interface)
│   ├── IdleState.java               (State Pattern: concrete implementation)
│   ├── HasMoneyState.java           (State Pattern: concrete implementation)
│   ├── SelectionState.java          (State Pattern: concrete implementation)
│   ├── DispenseState.java           (State Pattern: concrete implementation)
│   └── VendingMachine.java          (State Pattern: main context class)
├── repository/
│   └── InventoryRepository.java      (Manages memory structures)
├── service/
│   ├── InventoryService.java        (Validates and adjusts shelves)
│   ├── CoinService.java             (Processes coin sums and change returns)
│   └── VendingMachineService.java   (Coordinates state triggers and transitions)
└── controller/
    └── VendingMachineController.java (Client API gateway exposing operations)
```

---

## 2. Key Accomplishments & Design Patterns Used

1. **State Design Pattern**:
   - The system is designed as a finite state machine using the `State` interface and four concrete states: `IdleState`, `HasMoneyState`, `SelectionState`, and `DispenseState`.
   - Transitions are strongly guarded. Doing operations out of order (such as inserting coins during dispensing or selecting items while idle) automatically throws exceptions.
2. **Modular Tiered Enterprise Architecture**:
   - Decoupled code logic where `VendingMachineController` exposes API buttons, `VendingMachineService` directs execution flow, and `InventoryRepository` manages physical counts.
3. **Dynamic Balance & Change Calculation**:
   - Employs currency enums representing fractional values (pennies, nickels, dimes, quarters, and dollars). Calculates total deposited balances, checks pricing limits, returns appropriate change, and refunds money when transactions are cancelled or rejected.

---

## 3. Verification & Execution Output

The implementation has been successfully compiled and verified:

### Compilation Command
```bash
javac -d out src/model/*.java src/repository/*.java src/service/*.java src/controller/*.java src/Main.java
```

### Run Command
```bash
java -cp out Main
```

### Execution Log
```
==============================================================
        VENDING MACHINE LOW LEVEL DESIGN SYSTEM BOOT          
==============================================================

--- Admin Setup: Initializing Product Shelves ---
[InventoryService] Added shelf 101 for COKE @ $1.5 (Qty: 5)
[InventoryService] Added shelf 102 for PEPSI @ $1.25 (Qty: 10)
[InventoryService] Added shelf 103 for SODA @ $0.75 (Qty: 15)
[InventoryService] Added shelf 104 for JUICE @ $2.0 (Qty: 0)

============ VENDING MACHINE INVENTORY ============
Shelf Code: 101   | Product: COKE     | Price: $1.50  | Status: QTY: 5
Shelf Code: 102   | Product: PEPSI    | Price: $1.25  | Status: QTY: 10
Shelf Code: 103   | Product: SODA     | Price: $0.75  | Status: QTY: 15
Shelf Code: 104   | Product: JUICE    | Price: $2.00  | Status: SOLD_OUT
====================================================
[State: Idle] Vending Machine is ready. Click on 'Insert Coin' button to start.

==============================================================
   SIMULATION 1: Successful Pepsi Purchase ($1.25) with Change
==============================================================

>>> [VendingMachineService] Request: Click Insert Coin Button
[State Transition] Clicked Insert Coin Button. Transitioning to HAS_MONEY State.
[State: HasMoney] Insert coins. Click 'Start Selection' when finished.

>>> [VendingMachineService] Request: Insert Coin: DOLLAR
[HasMoney] Inserted: DOLLAR ($1.0). Current total: $1.0

>>> [VendingMachineService] Request: Insert Coin: QUARTER
[HasMoney] Inserted: QUARTER ($0.25). Current total: $1.25

>>> [VendingMachineService] Request: Insert Coin: DIME
[HasMoney] Inserted: DIME ($0.1). Current total: $1.35

>>> [VendingMachineService] Request: Start Product Selection
[State Transition] Starting selection. Transitioning to SELECTION State.
[State: Selection] Select a product by shelf code.

>>> [VendingMachineService] Request: Select Shelf Code 102
[Selection] Successfully chosen: PEPSI ($1.25)
[ChangeReturn] Dispensed exact change back to user: $0.1
[State: Dispense] Dispensing product for shelf code: 102
[Dispenser] Dispensed: PEPSI from shelf 102
[State: Idle] Vending Machine is ready. Click on 'Insert Coin' button to start.

============ VENDING MACHINE INVENTORY ============
Shelf Code: 101   | Product: COKE     | Price: $1.50  | Status: QTY: 5
Shelf Code: 102   | Product: PEPSI    | Price: $1.25  | Status: QTY: 9
Shelf Code: 103   | Product: SODA     | Price: $0.75  | Status: QTY: 15
Shelf Code: 104   | Product: JUICE    | Price: $2.00  | Status: SOLD_OUT
====================================================

==============================================================
   SIMULATION 2: Insufficient Funds for Coke ($1.50)
==============================================================

>>> [VendingMachineService] Request: Click Insert Coin Button
[State Transition] Clicked Insert Coin Button. Transitioning to HAS_MONEY State.
[State: HasMoney] Insert coins. Click 'Start Selection' when finished.

>>> [VendingMachineService] Request: Insert Coin: DOLLAR
[HasMoney] Inserted: DOLLAR ($1.0). Current total: $1.0

>>> [VendingMachineService] Request: Start Product Selection
[State Transition] Starting selection. Transitioning to SELECTION State.
[State: Selection] Select a product by shelf code.

>>> [VendingMachineService] Request: Select Shelf Code 101
[SelectionError] Insufficient balance: Required $1.5, but inserted $1.0
[Refund] Returning all inserted money and returning to IDLE state.
[State: Idle] Vending Machine is ready. Click on 'Insert Coin' button to start.
[VendingMachineService] Transaction failed: Insufficient money inserted.
Current Vending Machine State class: IdleState

==============================================================
   SIMULATION 3: Purchase Sold Out Juice ($2.00)
==============================================================

>>> [VendingMachineService] Request: Click Insert Coin Button
[State Transition] Clicked Insert Coin Button. Transitioning to HAS_MONEY State.
[State: HasMoney] Insert coins. Click 'Start Selection' when finished.

>>> [VendingMachineService] Request: Insert Coin: DOLLAR
[HasMoney] Inserted: DOLLAR ($1.0). Current total: $1.0

>>> [VendingMachineService] Request: Insert Coin: DOLLAR
[HasMoney] Inserted: DOLLAR ($1.0). Current total: $2.0

>>> [VendingMachineService] Request: Start Product Selection
[State Transition] Starting selection. Transitioning to SELECTION State.
[State: Selection] Select a product by shelf code.

>>> [VendingMachineService] Request: Select Shelf Code 104
[SelectionError] Product in shelf 104 is sold out.
[Refund] Returning all inserted money and returning to IDLE state.
[State: Idle] Vending Machine is ready. Click on 'Insert Coin' button to start.
[VendingMachineService] Transaction failed: Product is sold out.

==============================================================
   SIMULATION 4: User Manual Cancel & Refund Flow
==============================================================

>>> [VendingMachineService] Request: Click Insert Coin Button
[State Transition] Clicked Insert Coin Button. Transitioning to HAS_MONEY State.
[State: HasMoney] Insert coins. Click 'Start Selection' when finished.

>>> [VendingMachineService] Request: Insert Coin: QUARTER
[HasMoney] Inserted: QUARTER ($0.25). Current total: $0.25

>>> [VendingMachineService] Request: Insert Coin: DIME
[HasMoney] Inserted: DIME ($0.1). Current total: $0.35

>>> [VendingMachineService] Request: Cancel Transaction & Refund
[Refund] Refunding full money and resetting to IDLE state.
[State: Idle] Vending Machine is ready. Click on 'Insert Coin' button to start.
[CoinService] Refunding 2 coins back to user. Sum: $0.35

==============================================================
   SIMULATION 5: Verifying State Machine Safe Transition Limits
==============================================================
[Client] Attempting to select a product in IDLE state...

>>> [VendingMachineService] Request: Select Shelf Code 103
[VendingMachineService] Transaction failed: Cannot choose product in IDLE state. Insert coins first.
[Client] Attempting to insert coins in IDLE state...

>>> [VendingMachineService] Request: Insert Coin: DOLLAR
[State Pattern Block] Blocked: Cannot insert coin in IDLE state. Click Insert Coin Button first.

==============================================================
       VENDING MACHINE LOW LEVEL DESIGN SIMULATION COMPLETE   
==============================================================
```
