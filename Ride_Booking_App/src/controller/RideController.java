package controller;

import dto.FareEstimateRequest;
import dto.FareEstimateResponse;
import dto.RideRequest;
import dto.RideStatusResponse;
import model.Ride;
import service.RideService;

/**
 * REST Controller for ride operations.
 * Endpoints:
 *   GET  /api/rides/fare-estimate
 *   POST /api/rides/request
 *   GET  /api/rides/{rideId}/status
 *   POST /api/rides/{rideId}/cancel
 */
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    // GET /api/rides/fare-estimate
    public FareEstimateResponse getFareEstimate(FareEstimateRequest request) {
        System.out.println("[API] GET /api/rides/fare-estimate");
        return rideService.getFareEstimate(
                request.getPickupLocation(),
                request.getDropoffLocation()
        );
    }

    // POST /api/rides/request
    public Ride requestRide(RideRequest request) {
        System.out.println("[API] POST /api/rides/request");
        return rideService.requestRide(request);
    }

    // GET /api/rides/{rideId}/status
    public RideStatusResponse getRideStatus(String rideId) {
        System.out.println("[API] GET /api/rides/" + rideId + "/status");
        return rideService.getRideStatus(rideId);
    }

    // POST /api/rides/{rideId}/cancel
    public void cancelRide(String rideId, int userId, String reason) {
        System.out.println("[API] POST /api/rides/" + rideId + "/cancel");
        rideService.cancelRide(rideId, userId, reason);
    }
}
