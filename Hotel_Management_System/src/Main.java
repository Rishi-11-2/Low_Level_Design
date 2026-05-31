import controller.*;
import model.*;
import repository.*;
import service.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        System.out.println("==============================================================");
        System.out.println("         HOTEL RESERVATION SYSTEM LOW LEVEL BOOT              ");
        System.out.println("==============================================================");

        // 1. Initialize Repositories
        HotelRepository hotelRepository = new HotelRepository();
        RoomTypeRepository roomTypeRepository = new RoomTypeRepository();
        RoomRepository roomRepository = new RoomRepository();
        BookingRepository bookingRepository = new BookingRepository();
        TransactionRepository transactionRepository = new TransactionRepository();
        SeasonalPriceRepository seasonalPriceRepository = new SeasonalPriceRepository();
        CancellationPolicyRepository cancellationPolicyRepository = new CancellationPolicyRepository();
        UserRepository userRepository = new UserRepository();

        // 2. Initialize Services
        InventoryService inventoryService = new InventoryService(bookingRepository, roomTypeRepository, hotelRepository);
        PricingService pricingService = new PricingService(seasonalPriceRepository, roomTypeRepository);
        PolicyService policyService = new PolicyService(cancellationPolicyRepository);
        TransactionService transactionService = new TransactionService(transactionRepository, bookingRepository, inventoryService);
        BookingService bookingService = new BookingService(
                bookingRepository, roomRepository, inventoryService, pricingService, policyService, transactionService, hotelRepository
        );
        SearchService searchService = new SearchService(hotelRepository, roomTypeRepository, inventoryService, pricingService);
        UserService userService = new UserService(userRepository, bookingRepository);
        SchedulerService schedulerService = new SchedulerService(bookingRepository);

        // 3. Initialize Controllers
        SearchController searchController = new SearchController(searchService);
        BookingController bookingController = new BookingController(bookingService);
        TransactionController transactionController = new TransactionController(transactionService);
        AdminController adminController = new AdminController(
                bookingService, hotelRepository, roomTypeRepository, roomRepository, seasonalPriceRepository, cancellationPolicyRepository
        );
        DashboardController dashboardController = new DashboardController(userService);

        // 4. Setup Mock Infrastructure (Admin Flow)
        System.out.println("\n--- Admin: Setting up Hotel and Policies ---");
        // Create Cancellation Policies
        CancellationPolicy flexPolicy = new CancellationPolicy("POLICY-FLEX", "FLEX", 100, 0); // 100% refund always
        CancellationPolicy partialPolicy = new CancellationPolicy("POLICY-PARTIAL", "PARTIAL", 50, 48); // 50% refund if cancelled > 48h before check-in
        adminController.createOrUpdatePolicy(flexPolicy);
        adminController.createOrUpdatePolicy(partialPolicy);

        // Create Hotel B1 ("The Ritz-Carlton")
        String hotelId = "HTL-RITZ";
        Hotel ritz = new Hotel(hotelId, "The Ritz-Carlton", "100 Central Park S", "New York", "USA", 40.7644, -73.9744, 4.9);
        ritz.setCancellationPolicyId("POLICY-PARTIAL"); // Assign policy
        ritz.setDefaultOverbookPercent(20); // Allow 20% overbooking for capacity management
        adminController.createOrUpdateHotel(ritz);

        // Add Room Types
        String rtDeluxe = "RT-DELUXE";
        RoomType deluxeType = new RoomType(rtDeluxe, hotelId, "Deluxe King Suite", 2, "KING", 30000L, Arrays.asList("WiFi", "TV", "Minibar"), 2); // 2 total rooms
        adminController.createOrUpdateRoomType(deluxeType);

        // Add physical rooms
        adminController.addRoom(new Room("RM-101", hotelId, rtDeluxe, "101"));
        adminController.addRoom(new Room("RM-102", hotelId, rtDeluxe, "102"));

        // Register Users
        User customer1 = new User("USR-ALICE", "Alice Smith", "alice@example.com", UserRole.CUSTOMER);
        User customer2 = new User("USR-BOB", "Bob Jones", "bob@example.com", UserRole.CUSTOMER);
        userRepository.save(customer1);
        userRepository.save(customer2);

        // 5. Search and Real-Time Availability Flow
        System.out.println("\n--- Search Flow: Searching Hotels & Room Availability ---");
        SearchFilter filter = new SearchFilter("New York", "USA", new DateRange(System.currentTimeMillis() + 24 * 60 * 60 * 1000L, System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000L), 0L, 50000L);
        List<Hotel> foundHotels = searchController.searchHotels(filter);
        System.out.println("Found " + foundHotels.size() + " hotel: " + foundHotels.get(0).getName());

        DateRange stayRange = filter.getDateRange();
        List<RoomTypeAvailability> avail = searchController.getAvailability(hotelId, stayRange);
        for (RoomTypeAvailability a : avail) {
            System.out.println("Room Type: " + a.getRoomTypeName() + ", Price/Night: $" + (a.getAveragePricePerNight() / 100.0) + ", Available: " + a.isAvailable());
        }

        // 6. Booking Simulation 1: Alice Books Deluxe Suite (Successful Two-Phase Flow)
        System.out.println("\n--- Booking Flow 1: Alice Books Stay (Successful Checkout) ---");
        
        // Phase 1: Lock prices & create booking hold
        long deluxeTotalPrice = deluxeType.getBasePriceMinor() * stayRange.getNumberOfNights();
        Booking booking1 = bookingController.createBooking(customer1.getId(), hotelId, rtDeluxe, stayRange, deluxeTotalPrice);
        String b1Id = booking1.getId();

        // Phase 2: Initiate Transaction (Transition: CREATED -> HELD)
        Transaction transaction1 = transactionController.initiateTransaction(b1Id);
        String txRef1 = transaction1.getProviderRef();

        // Simulate payment success callback (Transition: HELD -> CONFIRMED)
        transactionController.handleTransactionCallback(txRef1, TransactionStatus.COMPLETED);

        // 7. Booking Simulation 2: Bob Overbooks Stay (Overbooking protection)
        System.out.println("\n--- Booking Flow 2: Bob attempts to book over the limit ---");
        // Deluxe suite has 2 rooms. Since Alice booked 1, and overbook capacity is 20% (ceiling of 2 * 0.2 = 1 room), 
        // total capacity is 3 rooms. Let's register 2 more bookings to see overbooking cap!
        Booking booking2 = bookingController.createBooking(customer2.getId(), hotelId, rtDeluxe, stayRange, deluxeTotalPrice);
        Transaction transaction2 = transactionController.initiateTransaction(booking2.getId());
        transactionController.handleTransactionCallback(transaction2.getProviderRef(), TransactionStatus.COMPLETED); // Occupied 2nd suite

        // Try to book a 3rd suite (allowed under overbooking)
        Booking booking3 = bookingController.createBooking(customer2.getId(), hotelId, rtDeluxe, stayRange, deluxeTotalPrice);
        Transaction transaction3 = transactionController.initiateTransaction(booking3.getId());
        transactionController.handleTransactionCallback(transaction3.getProviderRef(), TransactionStatus.COMPLETED); // Occupied 3rd suite

        try {
            System.out.println("[Client] Attempting to book a 4th suite (Exceeds overbooking limit of 3!)");
            bookingController.createBooking(customer2.getId(), hotelId, rtDeluxe, stayRange, deluxeTotalPrice);
        } catch (IllegalStateException e) {
            System.out.println("[Client] BLOCKED: Exceeded overbooking cap! Message: " + e.getMessage());
        }

        // 8. Policy-Based Cancellation Simulation
        System.out.println("\n--- Cancellation Flow: Alice cancels stay near cutoff ---");
        // stay starts in 24 hours. The policy cutoff is 48 hours for partial refund (50%).
        // So since it is less than 48 hours before check-in, she should get NO refund!
        bookingController.cancelBooking(b1Id, customer1.getId());

        // 9. Admin Check-In & Check-Out Flow
        System.out.println("\n--- Reception Flow: Bob Checks In & Checks Out ---");
        // Bob has booked booking2. Let's check him in!
        adminController.checkIn(booking2.getId(), "RM-102", System.currentTimeMillis());
        // Check out Bob
        adminController.checkOut(booking2.getId(), System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000L);

        // 10. Scheduler Expiry Simulation
        System.out.println("\n--- Expiry Flow: Expiring stale holds ---");
        Booking staleBooking = bookingController.createBooking(customer1.getId(), hotelId, rtDeluxe, stayRange, deluxeTotalPrice);
        Transaction staleTx = transactionController.initiateTransaction(staleBooking.getId());
        
        // Wait, booking is HELD now. Let's manually set hold time to 5 seconds ago to simulate expiration!
        staleBooking.setHoldExpiresAt(System.currentTimeMillis() - 5000);
        bookingRepository.save(staleBooking);

        // Trigger hold expiry scheduler
        schedulerService.processExpiredHolds();

        System.out.println("\n==============================================================");
        System.out.println("       HOTEL RESERVATION LOW LEVEL SYSTEM SIMULATION COMPLETE ");
        System.out.println("==============================================================");
    }
}
