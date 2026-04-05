import controller.DriverController;
import controller.PaymentController;
import controller.RideController;
import dto.FareEstimateRequest;
import dto.FareEstimateResponse;
import dto.RideRequest;
import dto.RideStatusResponse;
import model.Driver;
import model.Location;
import model.Ride;
import model.Rider;
import model.enums.PaymentStatus;
import model.enums.PaymentType;
import repository.DriverRepository;
import repository.LocationRepository;
import repository.RideRepository;
import repository.RiderRepository;
import service.*;
import strategy.matching.NearestDriverStrategy;
import strategy.payment.PaymentGatewayRouter;
import strategy.pricing.BasePricingStrategy;

/**
 * Main demo class showcasing the complete Ride Booking App flow.
 * Demonstrates: CSR architecture, State Pattern, Strategy Pattern, Repository Pattern.
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=".repeat(70));
        System.out.println("        RIDE BOOKING APP - Low Level Design Demo");
        System.out.println("=".repeat(70));

        // ====== SETUP: Wire all dependencies (DI simulation) ======
        // Repositories
        RideRepository rideRepository = new RideRepository();
        RiderRepository riderRepository = new RiderRepository();
        DriverRepository driverRepository = new DriverRepository();
        LocationRepository locationRepository = new LocationRepository();

        // Infrastructure Services
        LockService lockService = new LockService();
        NotificationService notificationService = new NotificationService();
        MapService mapService = new MapService();

        // Strategy Implementations
        BasePricingStrategy basePricingStrategy = new BasePricingStrategy();
        NearestDriverStrategy nearestDriverStrategy = new NearestDriverStrategy();
        PaymentGatewayRouter paymentGatewayRouter = new PaymentGatewayRouter();

        // Domain Services
        LocationService locationService = new LocationService(driverRepository, locationRepository);
        DriverService driverService = new DriverService(driverRepository);
        PricingService pricingService = new PricingService(basePricingStrategy, mapService);
        MatchingService matchingService = new MatchingService(
                nearestDriverStrategy, driverRepository, rideRepository,
                lockService, notificationService);
        PaymentService paymentService = new PaymentService(
                paymentGatewayRouter, rideRepository, matchingService, notificationService);
        RideService rideService = new RideService(
                rideRepository, riderRepository, pricingService, paymentService,
                matchingService, driverService, locationService, lockService, notificationService);

        // Controllers
        RideController rideController = new RideController(rideService);
        DriverController driverController = new DriverController(rideService, driverService, locationService);
        PaymentController paymentController = new PaymentController(paymentService);

        // ====== SEED DATA ======
        System.out.println("\n--- Seeding Data ---");

        Rider rider1 = new Rider(1, "john_doe", "john@example.com", "+1234567890", "John Doe");
        riderRepository.save(rider1);
        System.out.println("Registered: " + rider1);

        Driver driver1 = new Driver(1, "alice_driver", "alice@driver.com", "+9876543210",
                "Alice Smith", "DL-12345", "KA-01-MN-1234", "Sedan");
        driver1.setCurrentLocation(new Location(12.9716, 77.5946, "MG Road, Bangalore"));
        driverRepository.save(driver1);
        System.out.println("Registered: " + driver1);

        Driver driver2 = new Driver(2, "bob_driver", "bob@driver.com", "+1122334455",
                "Bob Jones", "DL-67890", "KA-02-AB-5678", "SUV");
        driver2.setCurrentLocation(new Location(12.9352, 77.6245, "Koramangala, Bangalore"));
        driverRepository.save(driver2);
        System.out.println("Registered: " + driver2);

        // ====== DEMO FLOW 1: Fare Estimate ======
        System.out.println("\n" + "=".repeat(70));
        System.out.println("  FLOW 1: Fare Estimate");
        System.out.println("=".repeat(70));

        Location pickup = new Location(12.9716, 77.5946, "MG Road, Bangalore");
        Location dropoff = new Location(12.9352, 77.6245, "Koramangala, Bangalore");

        FareEstimateResponse fareEstimate = rideController.getFareEstimate(
                new FareEstimateRequest(pickup, dropoff));
        System.out.println("Fare Estimate: " + fareEstimate);

        // ====== DEMO FLOW 2: POST_PAYMENT (Cash) Ride ======
        System.out.println("\n" + "=".repeat(70));
        System.out.println("  FLOW 2: POST_PAYMENT (Cash) Ride - Full Lifecycle");
        System.out.println("=".repeat(70));

        // Step 1: Drivers go online
        driverController.goOnline(1);
        driverController.goOnline(2);

        // Step 2: Update driver locations
        driverController.updateLocation(1, new Location(12.9716, 77.5946));
        driverController.updateLocation(2, new Location(12.9352, 77.6245));

        // Step 3: Request ride (POST_PAYMENT / Cash)
        System.out.println("\n--- Requesting Ride (Cash) ---");
        Ride ride = rideController.requestRide(new RideRequest(
                1, pickup, dropoff, PaymentType.POST_PAYMENT));
        String rideId = ride.getRideId();
        System.out.println("Ride created: " + rideId);

        // Allow matching thread to run
        Thread.sleep(500);

        // Step 4: Driver accepts the ride
        System.out.println("\n--- Driver Accepts Ride ---");
        driverController.acceptRide(rideId, 1);

        // Step 5: Poll ride status
        System.out.println("\n--- Polling Ride Status ---");
        RideStatusResponse status = rideController.getRideStatus(rideId);
        System.out.println("Status: " + status);

        // Step 6: Start the trip
        System.out.println("\n--- Starting Trip ---");
        driverController.startRide(rideId, 1);

        // Step 7: Driver sends location updates during trip
        System.out.println("\n--- Location Updates During Trip ---");
        driverController.updateLocation(1, new Location(12.9500, 77.6100));
        Thread.sleep(100);
        driverController.updateLocation(1, new Location(12.9400, 77.6200));

        // Step 8: Complete the trip
        System.out.println("\n--- Completing Trip ---");
        driverController.completeRide(rideId, 1);

        // Step 9: Final status
        System.out.println("\n--- Final Status ---");
        RideStatusResponse finalStatus = rideController.getRideStatus(rideId);
        System.out.println("Final Status: " + finalStatus);

        // ====== DEMO FLOW 3: Cancel Ride ======
        System.out.println("\n" + "=".repeat(70));
        System.out.println("  FLOW 3: Ride Cancellation");
        System.out.println("=".repeat(70));

        // Driver goes online again
        driverController.goOnline(1);

        Ride ride2 = rideController.requestRide(new RideRequest(
                1, pickup, dropoff, PaymentType.POST_PAYMENT));
        Thread.sleep(500);
        System.out.println("Ride2 created: " + ride2.getRideId());

        rideController.cancelRide(ride2.getRideId(), 1, "Changed my mind");

        System.out.println("\n" + "=".repeat(70));
        System.out.println("  DEMO COMPLETE");
        System.out.println("=".repeat(70));
    }
}
