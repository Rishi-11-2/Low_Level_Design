package service;

import model.*;
import repository.ExternalRequestRepository;
import repository.InternalRequestRepository;

public class RequestService {
    private final ExternalRequestRepository externalRequestRepository;
    private final InternalRequestRepository internalRequestRepository;

    public RequestService(ExternalRequestRepository externalRequestRepository,
                          InternalRequestRepository internalRequestRepository) {
        this.externalRequestRepository = externalRequestRepository;
        this.internalRequestRepository = internalRequestRepository;
    }

    public ExternalRequest createExternalRequest(int floor, Direction direction, String buildingId) {
        ExternalRequest request = new ExternalRequest(floor, buildingId, direction);
        externalRequestRepository.save(request);
        System.out.println("[RequestService] EXTERNAL call generated: Floor " + floor + " requested " + direction);
        return request;
    }

    public InternalRequest createInternalRequest(String elevatorId, int destinationFloor) {
        InternalRequest request = new InternalRequest(elevatorId, destinationFloor);
        internalRequestRepository.save(request);
        System.out.println("[RequestService] INTERNAL call registered: Inside Elevator " + elevatorId + " selected Floor " + destinationFloor);
        return request;
    }

    public void completeRequest(String requestId) {
        externalRequestRepository.findById(requestId).ifPresent(r -> {
            r.setStatus(RequestStatus.COMPLETED);
            externalRequestRepository.save(r);
            System.out.println("[RequestService] Completed external request: Floor " + r.getFloorNumber() + " " + r.getDirection());
        });

        internalRequestRepository.findById(requestId).ifPresent(r -> {
            r.setStatus(RequestStatus.COMPLETED);
            internalRequestRepository.save(r);
            System.out.println("[RequestService] Completed internal request inside Elevator " + r.getElevatorId() + " to Floor " + r.getDestinationFloor());
        });
    }
}
