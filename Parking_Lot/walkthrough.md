# Parking Lot System LLD Java Implementation Walkthrough

We have successfully verified and documented the Low-Level Design (LLD) for the Parking Lot System in Java under `/Users/rishi/Projects/LLD/Parking_Lot/src/`. The design adheres to the tiered enterprise architecture (Client -> Controller -> Service -> Repository -> Domain) and incorporates the Adapter design pattern as specified in `ParkingLot.pdf`.

---

## 1. Package Structure
All source files are organized under `/Users/rishi/Projects/LLD/Parking_Lot/src/`:

```
src/
├── Main.java                        (System bootstrap & end-to-end simulation runner)
├── model/
│   ├── VehicleType.java             (Enum: CAR, BIKE, EV, TRUCK)
│   ├── SlotStatus.java              (Enum: AVAILABLE, OCCUPIED)
│   ├── PaymentStatus.java           (Enum: PENDING, PAID, FAILED)
│   ├── PaymentGateway.java          (Enum: Razorpay, Stripe)
│   ├── Floor.java                   (Domain model)
│   ├── ParkingSlot.java             (Domain model representing slots by VehicleType)
│   ├── Ticket.java                  (Domain model representing entry pass)
│   ├── PricingRule.java             (Domain model for dynamic rates)
│   ├── Payment.java                 (Domain model representing transactions)
│   ├── Receipt.java                 (Domain DTO containing billing summaries)
│   ├── EntryResult.java             (DTO response for vehicle entries)
│   └── ExitResult.java              (DTO response for vehicle exits)
├── repository/
│   ├── FloorRepository.java
│   ├── SlotRepository.java
│   ├── TicketRepository.java
│   ├── PricingRuleRepository.java
│   └── PaymentRepository.java
├── adapter/
│   ├── PaymentGatewayAdapter.java   (Adapter Pattern: gateway interface)
│   ├── RazorpayAdapter.java         (Adapter Pattern: concrete wrapper for Razorpay)
│   └── StripeAdapter.java          (Adapter Pattern: concrete wrapper for Stripe)
├── service/
│   ├── SlotService.java             (Manages spot checks and availability)
│   ├── TicketService.java           (Manages entry ticket generation and lookups)
│   ├── PricingService.java          (Enforces flat + hourly pricing formulas)
│   ├── PaymentService.java          (Translates client orders via adapters)
│   ├── ReceiptService.java          (Generates tax invoice summaries)
│   └── AdminService.java            (Handles construction and initial pricing rules)
└── controller/
    ├── AdminController.java
    ├── EntryController.java
    └── ExitController.java
```

---

## 2. Key Accomplishments & Design Patterns Used

1. **Adapter Design Pattern**:
   - Payments are cleanly decoupled from external gateway SDK variations.
   - `PaymentGatewayAdapter` acts as the standard client target.
   - `RazorpayAdapter` and `StripeAdapter` encapsulate vendor-specific commands, translating uniform fee settlement calls into underlying provider calls.
2. **Tiered Structural Flow**:
   - Decouples concerns across distinct system boundaries: controllers receive user entry/exit events, services implement business checks, and repositories manage local memory maps.
3. **Dynamic Flat + Hourly Rates**:
   - `PricingService` loads customized flat and hourly fees matching a vehicle's specific type.
   - Computes precise rates: `fee = baseFlatPrice + (elapsedHours * hourlyRate)`.

---

## 3. Verification & Execution Output

The implementation has been successfully compiled and verified:

### Compilation Command
```bash
javac -d out src/model/*.java src/repository/*.java src/adapter/*.java src/service/*.java src/controller/*.java src/Main.java
```

### Run Command
```bash
java -cp out Main
```

### Execution Log
```
===========================================================
         PARKING LOT LOW LEVEL DESIGN SYSTEM BOOT          
===========================================================

--- Admin: Constructing Floors and Adding Parking Slots ---

>>> [AdminController] Request to add Floor: 1
<<< [AdminController] Floor 1 added successfully.

>>> [AdminController] Request to add Floor: 2
<<< [AdminController] Floor 2 added successfully.

>>> [AdminController] Request to add BIKE slot on Floor: 1
<<< [AdminController] BIKE slot added on Floor 1.

>>> [AdminController] Request to add CAR slot on Floor: 1
<<< [AdminController] CAR slot added on Floor 1.

>>> [AdminController] Request to add EV slot on Floor: 1
<<< [AdminController] EV slot added on Floor 1.

>>> [AdminController] Request to add CAR slot on Floor: 2
<<< [AdminController] CAR slot added on Floor 2.

>>> [AdminController] Request to add TRUCK slot on Floor: 2
<<< [AdminController] TRUCK slot added on Floor 2.

--- Admin: Defining Pricing Rules ---

>>> [AdminController] Updating pricing rules for: BIKE (Flat: $2.0, Hourly: $5.0)
<<< [AdminController] Pricing rule updated successfully.

>>> [AdminController] Updating pricing rules for: CAR (Flat: $5.0, Hourly: $10.0)
<<< [AdminController] Pricing rule updated successfully.

>>> [AdminController] Updating pricing rules for: EV (Flat: $4.0, Hourly: $8.0)
<<< [AdminController] Pricing rule updated successfully.

>>> [AdminController] Updating pricing rules for: TRUCK (Flat: $10.0, Hourly: $20.0)
<<< [AdminController] Pricing rule updated successfully.

================ PARKING LOT SYSTEM STATUS ================
Total Floors:         2
Total Slots:          5
Occupied Slots:       0
Available Slots:      5
Available by Type:    {EV=1, TRUCK=1, CAR=2, BIKE=1}
===========================================================

--- Entry Flow: Vehicles Arriving ---

>>> [EntryController] Processing entry request for vehicle MH-12-AB-1234 (CAR)
<<< [EntryController] Success: Ticket ID 1e874860-c91f-460d-90bf-8f3e2eba6aad generated, assigned to Slot ID 1bb4ccb2-7946-4b4d-835a-7d55b5822a00 on Floor 1

>>> [EntryController] Processing entry request for vehicle DL-3C-CD-5678 (BIKE)
<<< [EntryController] Success: Ticket ID a4ee16bf-b7ec-4905-9226-0d7a8478ac1f generated, assigned to Slot ID 97ccc8fc-a252-414d-a9be-159d59e54049 on Floor 1

>>> [EntryController] Processing entry request for vehicle KA-01-EF-9012 (CAR)
<<< [EntryController] Success: Ticket ID 052086ed-07f9-4408-ade0-fb83d2397101 generated, assigned to Slot ID 2a80c450-a872-47ea-8cda-528e97808c51 on Floor 2

>>> [EntryController] Processing entry request for vehicle TX-CAR-7890 (CAR)
<<< [EntryController] Rejected: No slots available for vehicle type: CAR

================ PARKING LOT SYSTEM STATUS ================
Total Floors:         2
Total Slots:          5
Occupied Slots:       3
Available Slots:      2
Available by Type:    {EV=1, TRUCK=1}
===========================================================

--- Exit Flow: Vehicles Checking Out ---
[Simulation] Simulating active parking duration...

--- Checkout Attempt 1: CAR 1 via Razorpay ---

>>> [ExitController] Processing checkout request for Ticket ID: 1e874860-c91f-460d-90bf-8f3e2eba6aad via RAZORPAY
[ExitController] Total outstanding fee calculated: $25.0
[Razorpay SDK] Processing transaction for Ticket ID: 1e874860-c91f-460d-90bf-8f3e2eba6aad of amount $25.0
[Razorpay SDK] Payment Captured successfully via Razorpay.
[ReceiptService] Generating invoice receipt for Ticket ID: 1e874860-c91f-460d-90bf-8f3e2eba6aad
<<< [ExitController] Settlement complete: Slot released. Receipt issued ID 96986190-8b69-4183-bdd8-3976bd6a6ca4

--- Checkout Attempt 2: BIKE 1 via Razorpay ---

>>> [ExitController] Processing checkout request for Ticket ID: a4ee16bf-b7ec-4905-9226-0d7a8478ac1f via RAZORPAY
[ExitController] Total outstanding fee calculated: $12.0
[Razorpay SDK] Processing transaction for Ticket ID: a4ee16bf-b7ec-4905-9226-0d7a8478ac1f of amount $12.0
[Razorpay SDK] Payment Captured successfully via Razorpay.
[ReceiptService] Generating invoice receipt for Ticket ID: a4ee16bf-b7ec-4905-9226-0d7a8478ac1f
<<< [ExitController] Settlement complete: Slot released. Receipt issued ID d16de28d-69e4-430a-b0d5-50f111da2b3e

--- Checkout Attempt 3: CAR 2 via Stripe ---

>>> [ExitController] Processing checkout request for Ticket ID: 052086ed-07f9-4408-ade0-fb83d2397101 via STRIPE
[ExitController] Total outstanding fee calculated: $25.0
[Stripe SDK] Initializing payment intent for Ticket ID: 052086ed-07f9-4408-ade0-fb83d2397101 of amount $25.0
[Stripe SDK] Payment charge completed successfully via Stripe.
[ReceiptService] Generating invoice receipt for Ticket ID: 052086ed-07f9-4408-ade0-fb83d2397101
<<< [ExitController] Settlement complete: Slot released. Receipt issued ID eb101640-3068-4522-ac20-ddf4fa1b5f2f

================ PARKING LOT SYSTEM STATUS ================
Total Floors:         2
Total Slots:          5
Occupied Slots:       0
Available Slots:      5
Available by Type:    {EV=1, TRUCK=1, CAR=2, BIKE=1}
===========================================================

===========================================================
       PARKING LOT LOW LEVEL DESIGN SIMULATION COMPLETE    
===========================================================
```
