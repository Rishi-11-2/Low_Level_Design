package controller;

import model.Location;
import service.DriverService;
import service.LocationService;
import service.RideService;

/**
 * REST Controller for driver operations.
 * Endpoints:
 *   POST /api/rides/{rideId}/accept
 *   POST /api/rides/{rideId}/decline
 *   POST /api/rides/{rideId}/start
 *   POST /api/rides/{rideId}/complete
 *   POST /api/drivers/{driverId}/location
 *   POST /api/drivers/{driverId}/online
 *   POST /api/drivers/{driverId}/offline
 */
public class DriverController {

    private final RideService rideService;
    private final DriverService driverService;
    private final LocationService locationService;

    public DriverController(RideService rideService, DriverService driverService,
                            LocationService locationService) {
        this.rideService = rideService;
        this.driverService = driverService;
        this.locationService = locationService;
    }

    // POST /api/rides/{rideId}/accept
    public void acceptRide(String rideId, int driverId) {
        System.out.println("[API] POST /api/rides/" + rideId + "/accept (driver: " + driverId + ")");
        rideService.driverAccept(rideId, driverId);
    }

    // POST /api/rides/{rideId}/decline
    public void declineRide(String rideId, int driverId) {
        System.out.println("[API] POST /api/rides/" + rideId + "/decline (driver: " + driverId + ")");
        rideService.driverDecline(rideId, driverId);
    }

    // POST /api/rides/{rideId}/start
    public void startRide(String rideId, int driverId) {
        System.out.println("[API] POST /api/rides/" + rideId + "/start (driver: " + driverId + ")");
        rideService.startRide(rideId, driverId);
    }

    // POST /api/rides/{rideId}/complete
    public void completeRide(String rideId, int driverId) {
        System.out.println("[API] POST /api/rides/" + rideId + "/complete (driver: " + driverId + ")");
        rideService.completeRide(rideId, driverId);
    }

    // POST /api/drivers/{driverId}/location
    public void updateLocation(int driverId, Location location) {
        System.out.println("[API] POST /api/drivers/" + driverId + "/location");
        locationService.updateDriverLocation(driverId, location);
    }

    // POST /api/drivers/{driverId}/online
    public void goOnline(int driverId) {
        System.out.println("[API] POST /api/drivers/" + driverId + "/online");
        driverService.goOnline(driverId);
    }

    // POST /api/drivers/{driverId}/offline
    public void goOffline(int driverId) {
        System.out.println("[API] POST /api/drivers/" + driverId + "/offline");
        driverService.goOffline(driverId);
    }
}
