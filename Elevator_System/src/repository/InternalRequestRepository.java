package repository;

import model.InternalRequest;
import model.RequestStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InternalRequestRepository {
    private final Map<String, InternalRequest> requestMap = new ConcurrentHashMap<>();

    public InternalRequest save(InternalRequest request) {
        requestMap.put(request.getId(), request);
        return request;
    }

    public List<InternalRequest> findByElevator(String elevatorId) {
        return requestMap.values().stream()
                .filter(r -> r.getElevatorId().equals(elevatorId))
                .collect(Collectors.toList());
    }

    public List<InternalRequest> findPendingByElevator(String elevatorId) {
        return requestMap.values().stream()
                .filter(r -> r.getElevatorId().equals(elevatorId) && r.getStatus() == RequestStatus.PENDING)
                .collect(Collectors.toList());
    }

    public Optional<InternalRequest> findById(String requestId) {
        return Optional.ofNullable(requestMap.get(requestId));
    }

    public List<InternalRequest> findAll() {
        return new ArrayList<>(requestMap.values());
    }

    public void updateRequestStatus(String requestId, RequestStatus status) {
        InternalRequest request = requestMap.get(requestId);
        if (request != null) {
            request.setStatus(status);
        }
    }
}
