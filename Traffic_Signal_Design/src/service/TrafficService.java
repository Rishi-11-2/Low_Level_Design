package service;

import model.Direction;
import repository.TrafficRepository;

public class TrafficService {
    private final TrafficRepository trafficRepository;

    public TrafficService(TrafficRepository trafficRepository) {
        this.trafficRepository = trafficRepository;
    }

    public void updateVehicleCount(Direction direction, int count) {
        System.out.println("[TrafficService] Direction " + direction + " reports " + count + " active vehicles waiting.");
        trafficRepository.updateCount(direction, count);
    }

    public int getVehicleCount(Direction direction) {
        return trafficRepository.getCount(direction);
    }
}
