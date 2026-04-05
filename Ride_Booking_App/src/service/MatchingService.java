package service;

import dto.NotificationMessage;
import model.Driver;
import model.Ride;
import model.enums.DriverStatus;
import model.enums.RideStatus;
import repository.DriverRepository;
import repository.RideRepository;
import strategy.matching.DriverMatchingStrategy;

import java.util.List;
import java.util.Optional;

/**
 * MatchingService handles async driver matching with distributed locking.
 * Acts as the strategy context for DriverMatchingStrategy.
 */
public class MatchingService {

    private static final int MAX_CANDIDATES = 3;
    private static final long DRIVER_LOCK_TIMEOUT_MS = 200;
    private static final long ACCEPT_TIMEOUT_MS = 30000;
    private static final long POLL_INTERVAL_MS = 500;

    private final DriverMatchingStrategy matchingStrategy;
    private final DriverRepository driverRepository;
    private final RideRepository rideRepository;
    private final LockService lockService;
    private final NotificationService notificationService;

    public MatchingService(DriverMatchingStrategy matchingStrategy,
                           DriverRepository driverRepository,
                           RideRepository rideRepository,
                           LockService lockService,
                           NotificationService notificationService) {
        this.matchingStrategy = matchingStrategy;
        this.driverRepository = driverRepository;
        this.rideRepository = rideRepository;
        this.lockService = lockService;
        this.notificationService = notificationService;
    }

    /**
     * Async driver matching - runs in a separate thread.
     */
    public void matchDriverAsync(Ride ride) {
        Thread matchingThread = new Thread(() -> {
            Optional<Driver> matchedDriver = matchDriver(ride);
            if (matchedDriver.isEmpty()) {
                System.out.println("[Matching] No driver found for ride " + ride.getRideId());
                notificationService.sendToRider(ride.getRiderId(),
                        new NotificationMessage("NO_DRIVER", "No Driver Available",
                                "Sorry, no drivers are available at the moment. Please try again later.",
                                ride.getRideId()));
            }
        }, "matching-thread-" + ride.getRideId());
        matchingThread.setDaemon(true);
        matchingThread.start();
    }

    /**
     * Core matching logic:
     * 1. Find available drivers (ONLINE status)
     * 2. Apply matching strategy (nearest by distance)
     * 3. For each candidate: lock → validate → notify → wait for response
     */
    public Optional<Driver> matchDriver(Ride ride) {
        // Re-fetch ride and ensure it's still REQUESTED
        Ride currentRide = rideRepository.findByRideId(ride.getRideId())
                .orElseThrow(() -> new IllegalStateException("Ride not found: " + ride.getRideId()));

        if (currentRide.getStatus() != RideStatus.REQUESTED) {
            System.out.println("[Matching] Ride " + ride.getRideId() +
                               " is no longer REQUESTED (status: " + currentRide.getStatus() + ")");
            return Optional.empty();
        }

        // Find available drivers
        List<Driver> availableDrivers = driverRepository.findByStatus(DriverStatus.ONLINE);
        if (availableDrivers.isEmpty()) {
            System.out.println("[Matching] No online drivers available");
            return Optional.empty();
        }

        // Apply matching strategy
        List<Driver> candidates = matchingStrategy.findMatchingDrivers(
                currentRide.getPickupLocation(), availableDrivers, MAX_CANDIDATES);

        System.out.println("[Matching] Found " + candidates.size() + " candidate drivers for ride " +
                           ride.getRideId());

        // Try each candidate
        for (Driver candidate : candidates) {
            String driverLockKey = "driver_lock_" + candidate.getId();

            // Acquire distributed lock on driver
            if (!lockService.acquire(driverLockKey, DRIVER_LOCK_TIMEOUT_MS)) {
                System.out.println("[Matching] Could not lock driver " + candidate.getId() + ", skipping");
                continue;
            }

            try {
                // Re-validate driver is still online
                Driver freshDriver = driverRepository.findById(candidate.getId()).orElse(null);
                if (freshDriver == null || freshDriver.getStatus() != DriverStatus.ONLINE) {
                    System.out.println("[Matching] Driver " + candidate.getId() +
                                       " is no longer available, skipping");
                    continue;
                }

                // Push ride notification to driver
                notificationService.sendToDriver(candidate.getId(),
                        new NotificationMessage("RIDE_REQUEST", "New Ride Request",
                                "Pickup: " + currentRide.getPickupLocation() +
                                " → " + currentRide.getDropoffLocation(),
                                currentRide.getRideId()));

                // Wait for driver response (poll ride status)
                boolean accepted = waitForDriverResponse(currentRide, candidate.getId());

                if (accepted) {
                    System.out.println("[Matching] Driver " + candidate.getId() +
                                       " accepted ride " + ride.getRideId());
                    return Optional.of(candidate);
                }

                System.out.println("[Matching] Driver " + candidate.getId() +
                                   " did not accept, trying next");
            } finally {
                lockService.release(driverLockKey);
            }
        }

        return Optional.empty();
    }

    /**
     * Poll ride status waiting for driver to accept/decline.
     * Returns true if driver accepted within timeout.
     */
    private boolean waitForDriverResponse(Ride ride, int driverId) {
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < ACCEPT_TIMEOUT_MS) {
            Ride currentRide = rideRepository.findByRideId(ride.getRideId()).orElse(null);

            if (currentRide == null) return false;

            // Check if ride was cancelled
            if (currentRide.getStatus() == RideStatus.CANCELLED) {
                return false;
            }

            // Check if this driver accepted
            if (currentRide.getStatus() == RideStatus.ACCEPTED &&
                currentRide.getDriverId() != null &&
                currentRide.getDriverId() == driverId) {
                return true;
            }

            // Check if another driver was assigned
            if (currentRide.getDriverId() != null && currentRide.getDriverId() != driverId) {
                return false;
            }

            try {
                Thread.sleep(POLL_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        System.out.println("[Matching] Driver " + driverId + " timed out for ride " + ride.getRideId());
        return false;
    }

    public void releaseDriver(int driverId) {
        Driver driver = driverRepository.findById(driverId).orElse(null);
        if (driver != null && driver.getStatus() == DriverStatus.ON_RIDE) {
            driver.setStatus(DriverStatus.ONLINE);
            driverRepository.save(driver);
            System.out.println("[Matching] Released driver " + driverId + " back to ONLINE");
        }
    }
}
