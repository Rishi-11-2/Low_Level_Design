package controller;

import model.Direction;
import service.TrafficService;

public class TrafficController {
    private final TrafficService trafficService;

    public TrafficController(TrafficService trafficService) {
        this.trafficService = trafficService;
    }

    public void updateVehicleCount(Direction direction, int count) {
        System.out.println("\n>>> [TrafficController] Updating vehicle sensors for direction=" + direction + " to " + count);
        trafficService.updateVehicleCount(direction, count);
    }

    public int getVehicleCount(Direction direction) {
        return trafficService.getVehicleCount(direction);
    }
}
