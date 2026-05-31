package service;

import dto.*;
import model.Driver;
import model.Location;
import model.Ride;
import model.Rider;
import model.enums.*;
import repository.RideRepository;
import repository.RiderRepository;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Core RideService implementing all ride lifecycle use cases.
 */
public class RideService {

    private final RideRepository rideRepository;
    private final RiderRepository riderRepository;
    private final PricingService pricingService;
    private final PaymentService paymentService;
    private final MatchingService matchingService;
    private final DriverService driverService;
    private final LocationService locationService;
    private final LockService lockService;
    private final NotificationService notificationService;

    private static final long RIDE_LOCK_TIMEOUT_MS = 500;

    public RideService(RideRepository rideRepository, RiderRepository riderRepository,
                       PricingService pricingService, PaymentService paymentService,
                       MatchingService matchingService, DriverService driverService,
                       LocationService locationService, LockService lockService,
                       NotificationService notificationService) {
        this.rideRepository = rideRepository;
        this.riderRepository = riderRepository;
        this.pricingService = pricingService;
        this.paymentService = paymentService;
        this.matchingService = matchingService;
        this.driverService = driverService;
        this.locationService = locationService;
        this.lockService = lockService;
        this.notificationService = notificationService;
    }

    // ==================== 1. REQUEST RIDE ====================
    public Ride requestRide(RideRequest request) {
        // Validate rider exists
        Rider rider = riderRepository.findById(request.getRiderId())
                .orElseThrow(() -> new IllegalArgumentException("Rider not found: " + request.getRiderId()));

        // Check for active rides
        if (rideRepository.hasActiveRide(request.getRiderId())) {
            throw new IllegalStateException("Rider " + request.getRiderId() +
                    " already has an active ride. Complete or cancel it first.");
        }

        // Validate pickup != dropoff
        Location pickup = request.getPickupLocation();
        Location dropoff = request.getDropoffLocation();
        if (pickup.getLatitude() == dropoff.getLatitude() &&
            pickup.getLongitude() == dropoff.getLongitude()) {
            throw new IllegalArgumentException("Pickup and dropoff locations cannot be the same.");
        }

        // Calculate estimated fare
        FareEstimateResponse fareEstimate = pricingService.calculateFare(pickup, dropoff);

        // Create ride
        Ride ride = new Ride(
                request.getRiderId(), pickup, dropoff,
                fareEstimate.getEstimatedFare(),
                fareEstimate.getEstimatedDistance(),
                fareEstimate.getEstimatedDuration(),
                request.getPaymentType()
        );

        rideRepository.save(ride);
        System.out.println("[RideService] Ride created: " + ride.getRideId() +
                           " (type: " + request.getPaymentType() + ")");

        // Handle based on payment type
        if (request.getPaymentType() == PaymentType.PRE_PAYMENT) {
            // Initiate payment — matching starts on callback
            String txnId = paymentService.initiatePayment(ride.getRideId(), ride.getEstimatedFare());
            ride.setPaymentId(txnId);
            ride.setPaymentStatus(PaymentStatus.PENDING);
            rideRepository.save(ride);
            System.out.println("[RideService] PRE_PAYMENT initiated, waiting for callback...");
        } else {
            // POST_PAYMENT (cash) — start matching immediately
            ride.setPaymentStatus(PaymentStatus.PENDING);
            rideRepository.save(ride);
            matchingService.matchDriverAsync(ride);
            System.out.println("[RideService] POST_PAYMENT ride, matching started immediately");
        }

        return ride;
    }

    // ==================== 2. GET RIDE STATUS (Polling) ====================
    public RideStatusResponse getRideStatus(String rideId) {
        Ride ride = rideRepository.findByRideId(rideId)
                .orElseThrow(() -> new IllegalArgumentException("Ride not found: " + rideId));

        RideStatusResponse response = new RideStatusResponse(
                ride.getRideId(), ride.getStatus(),
                ride.getPickupLocation(), ride.getDropoffLocation(),
                ride.getRequestedAt()
        );

        response.setEstimatedFare(ride.getEstimatedFare());

        // Add driver info if assigned
        if (ride.getDriverId() != null) {
            Driver driver = driverService.getById(ride.getDriverId());
            Location driverLocation = locationService.getDriverLocation(driver.getId());
            long eta = 0;
            if (driverLocation != null && ride.getStatus() == RideStatus.IN_PROGRESS) {
                eta = locationService.calculateETA(driverLocation, ride.getDropoffLocation());
            } else if (driverLocation != null) {
                eta = locationService.calculateETA(driverLocation, ride.getPickupLocation());
            }

            DriverInfo driverInfo = new DriverInfo(
                    driver.getId(), driver.getName(), driver.getPhoneNumber(),
                    driver.getVehicleNumber(), driverLocation, eta
            );
            response.setDriver(driverInfo);
            response.setCurrentLocation(driverLocation);
        }

        return response;
    }

    // ==================== 3. DRIVER ACCEPT ====================
    public void driverAccept(String rideId, int driverId) {
        String lockKey = "ride_lock_" + rideId;
        if (!lockService.acquire(lockKey, RIDE_LOCK_TIMEOUT_MS)) {
            throw new IllegalStateException("Could not acquire lock on ride: " + rideId);
        }

        try {
            Ride ride = rideRepository.findByRideId(rideId)
                    .orElseThrow(() -> new IllegalArgumentException("Ride not found: " + rideId));

            Driver driver = driverService.getById(driverId);

            // Use State Pattern for transition
            ride.accept(driverId);

            // Update driver status
            driver.setStatus(DriverStatus.ON_RIDE);

            rideRepository.save(ride);
            System.out.println("[RideService] Ride " + rideId + " accepted by driver " + driverId);

            // Notify rider
            notificationService.sendToRider(ride.getRiderId(),
                    new NotificationMessage("RIDE_ACCEPTED", "Driver Found!",
                            "Driver " + driver.getName() + " (" + driver.getVehicleNumber() +
                            ") is on the way!", rideId));
        } finally {
            lockService.release(lockKey);
        }
    }

    // ==================== 4. DRIVER DECLINE ====================
    public void driverDecline(String rideId, int driverId) {
        String lockKey = "ride_lock_" + rideId;
        if (!lockService.acquire(lockKey, RIDE_LOCK_TIMEOUT_MS)) {
            throw new IllegalStateException("Could not acquire lock on ride: " + rideId);
        }

        try {
            Ride ride = rideRepository.findByRideId(rideId)
                    .orElseThrow(() -> new IllegalArgumentException("Ride not found: " + rideId));

            if (ride.getStatus() == RideStatus.REQUESTED) {
                // Not yet assigned, matching will continue to next driver
                ride.addDeclinedDriver(driverId);
                rideRepository.save(ride);
                System.out.println("[RideService] Driver " + driverId +
                                   " declined unassigned ride " + rideId);
                return;
            }

            if (ride.getStatus() == RideStatus.ASSIGNED) {
                // Release driver and re-trigger matching
                if (ride.getDriverId() != null && ride.getDriverId() == driverId) {
                    matchingService.releaseDriver(driverId);
                    ride.setDriverId(null);
                    ride.setAssignedAt(null);
                    ride.transitionTo(RideStatus.REQUESTED);
                    rideRepository.save(ride);

                    System.out.println("[RideService] Driver " + driverId +
                                       " declined assigned ride " + rideId + ", re-matching...");

                    // Re-trigger matching
                    matchingService.matchDriverAsync(ride);
                }
            }
        } finally {
            lockService.release(lockKey);
        }
    }

    // ==================== 5. START RIDE ====================
    public void startRide(String rideId, int driverId) {
        String lockKey = "ride_lock_" + rideId;
        if (!lockService.acquire(lockKey, RIDE_LOCK_TIMEOUT_MS)) {
            throw new IllegalStateException("Could not acquire lock on ride: " + rideId);
        }

        try {
            Ride ride = rideRepository.findByRideId(rideId)
                    .orElseThrow(() -> new IllegalArgumentException("Ride not found: " + rideId));

            // Use State Pattern for transition
            ride.start(driverId);
            rideRepository.save(ride);

            System.out.println("[RideService] Ride " + rideId + " started by driver " + driverId);

            // Notify rider
            notificationService.sendToRider(ride.getRiderId(),
                    new NotificationMessage("RIDE_STARTED", "Trip Started",
                            "Your trip has begun! Tracking is now active.", rideId));
        } finally {
            lockService.release(lockKey);
        }
    }

    // ==================== 6. COMPLETE RIDE ====================
    public void completeRide(String rideId, int driverId) {
        String lockKey = "ride_lock_" + rideId;
        if (!lockService.acquire(lockKey, RIDE_LOCK_TIMEOUT_MS)) {
            throw new IllegalStateException("Could not acquire lock on ride: " + rideId);
        }

        try {
            Ride ride = rideRepository.findByRideId(rideId)
                    .orElseThrow(() -> new IllegalArgumentException("Ride not found: " + rideId));

            // Use State Pattern for transition
            ride.complete(driverId);

            // Capture actual metrics (analytics)
            if (ride.getStartedAt() != null) {
                long actualDuration = Duration.between(ride.getStartedAt(), ride.getCompletedAt()).getSeconds();
                ride.setActualDuration(actualDuration);
            }

            // Handle payment completion
            if (ride.getPaymentType() == PaymentType.POST_PAYMENT) {
                // Cash ride — driver collects the locked estimatedFare
                ride.setPaymentStatus(PaymentStatus.COMPLETED);
                System.out.println("[RideService] Cash payment of $" + (ride.getEstimatedFare() / 100.0) +
                                   " to be collected by driver");
            } else {
                // PRE_PAYMENT — already paid
                System.out.println("[RideService] Pre-payment already processed for ride " + rideId);
            }

            rideRepository.save(ride);

            // Release driver
            matchingService.releaseDriver(driverId);

            System.out.println("[RideService] Ride " + rideId + " completed");

            // Notify rider with receipt
            notificationService.sendToRider(ride.getRiderId(),
                    new NotificationMessage("RIDE_COMPLETED", "Trip Completed",
                            "Your trip is complete. Fare: $" + (ride.getEstimatedFare() / 100.0),
                            rideId));

        } finally {
            lockService.release(lockKey);
        }
    }

    // ==================== 7. CANCEL RIDE ====================
    public void cancelRide(String rideId, int userId, String reason) {
        String lockKey = "ride_lock_" + rideId;
        if (!lockService.acquire(lockKey, RIDE_LOCK_TIMEOUT_MS)) {
            throw new IllegalStateException("Could not acquire lock on ride: " + rideId);
        }

        try {
            Ride ride = rideRepository.findByRideId(rideId)
                    .orElseThrow(() -> new IllegalArgumentException("Ride not found: " + rideId));

            // Validate cancellation is allowed
            if (ride.getStatus() == RideStatus.COMPLETED || ride.getStatus() == RideStatus.CANCELLED) {
                throw new IllegalStateException("Cannot cancel a ride that is " + ride.getStatus());
            }

            // Release driver if assigned
            Integer assignedDriverId = ride.getDriverId();
            if (assignedDriverId != null) {
                matchingService.releaseDriver(assignedDriverId);
            }

            // Use State Pattern for transition
            ride.cancel(userId, reason);

            // Handle refund for PRE_PAYMENT
            if (ride.getPaymentType() == PaymentType.PRE_PAYMENT &&
                ride.getPaymentStatus() == PaymentStatus.COMPLETED) {
                ride.setPaymentStatus(PaymentStatus.REFUNDED);
                System.out.println("[RideService] Refund initiated for ride " + rideId);
            }

            rideRepository.save(ride);
            System.out.println("[RideService] Ride " + rideId + " cancelled. Reason: " + reason);

            // Notify both parties
            notificationService.sendToRider(ride.getRiderId(),
                    new NotificationMessage("RIDE_CANCELLED", "Ride Cancelled",
                            "Your ride has been cancelled. Reason: " + reason, rideId));

            if (assignedDriverId != null) {
                notificationService.sendToDriver(assignedDriverId,
                        new NotificationMessage("RIDE_CANCELLED", "Ride Cancelled",
                                "The ride has been cancelled. Reason: " + reason, rideId));
            }
        } finally {
            lockService.release(lockKey);
        }
    }

    // ==================== 8. GET FARE ESTIMATE ====================
    public FareEstimateResponse getFareEstimate(Location pickup, Location dropoff) {
        return pricingService.calculateFare(pickup, dropoff);
    }
}
