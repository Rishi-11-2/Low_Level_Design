package service;

import model.Elevator;
import model.ExternalRequest;
import model.RequestStatus;
import repository.ElevatorRepository;
import repository.ExternalRequestRepository;
import strategy.ElevatorSelectionStrategy;
import strategy.NearestElevatorStrategy;
import java.util.List;

public class DispatcherService {
    private final ExternalRequestRepository externalRequestRepository;
    private final ElevatorRepository elevatorRepository;
    private ElevatorSelectionStrategy selectionStrategy;

    public DispatcherService(ExternalRequestRepository externalRequestRepository,
                             ElevatorRepository elevatorRepository) {
        this.externalRequestRepository = externalRequestRepository;
        this.elevatorRepository = elevatorRepository;
        this.selectionStrategy = new NearestElevatorStrategy(); // Default strategy
    }

    public void setElevatorSelectionStrategy(ElevatorSelectionStrategy strategy) {
        this.selectionStrategy = strategy;
        System.out.println("[DispatcherService] Elevator selection strategy switched to: " + strategy.getClass().getSimpleName());
    }

    public void queueExternalRequest(ExternalRequest request) {
        request.setStatus(RequestStatus.PENDING);
        externalRequestRepository.save(request);
    }

    public Elevator selectBestElevator(ExternalRequest request, List<Elevator> availableElevators) {
        return selectionStrategy.selectElevator(request, availableElevators);
    }

    public void assignRequestToElevator(ExternalRequest request, Elevator elevator) {
        request.setStatus(RequestStatus.ASSIGNED);
        request.setAssignedElevatorId(elevator.getId());
        externalRequestRepository.save(request);
        System.out.println("[DispatcherService] Assigned external floor call (Floor " + request.getFloorNumber() + " " + request.getDirection() + ") to Elevator: " + elevator.getId());
    }

    public synchronized void processPendingRequests(String buildingId) {
        List<ExternalRequest> pending = externalRequestRepository.findPendingRequests(buildingId);
        if (pending.isEmpty()) {
            return;
        }

        List<Elevator> available = elevatorRepository.findAvailableElevators(buildingId);
        if (available.isEmpty()) {
            System.out.println("[DispatcherService] Warning: No active elevators available to process requests.");
            return;
        }

        for (ExternalRequest request : pending) {
            Elevator best = selectBestElevator(request, available);
            if (best != null) {
                assignRequestToElevator(request, best);
            }
        }
    }
}
