package repository;

import model.ExternalRequest;
import model.RequestStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ExternalRequestRepository {
    private final Map<String, ExternalRequest> requestMap = new ConcurrentHashMap<>();

    public ExternalRequest save(ExternalRequest request) {
        requestMap.put(request.getId(), request);
        return request;
    }

    public List<ExternalRequest> findPendingRequests(String buildingId) {
        return requestMap.values().stream()
                .filter(r -> r.getBuildingId().equals(buildingId) && r.getStatus() == RequestStatus.PENDING)
                .collect(Collectors.toList());
    }

    public List<ExternalRequest> findQueuedRequests(String buildingId) {
        return requestMap.values().stream()
                .filter(r -> r.getBuildingId().equals(buildingId) && r.getStatus() == RequestStatus.QUEUED)
                .collect(Collectors.toList());
    }

    public void updateRequestStatus(String requestId, RequestStatus status) {
        ExternalRequest request = requestMap.get(requestId);
        if (request != null) {
            request.setStatus(status);
        }
    }

    public Optional<ExternalRequest> findById(String requestId) {
        return Optional.ofNullable(requestMap.get(requestId));
    }

    public List<ExternalRequest> findAll() {
        return new ArrayList<>(requestMap.values());
    }

    public void deleteById(String requestId) {
        requestMap.remove(requestId);
    }
}
