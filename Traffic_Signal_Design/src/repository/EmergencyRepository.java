package repository;

import model.EmergencyRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EmergencyRepository {
    private final Map<Integer, EmergencyRequest> requestMap = new ConcurrentHashMap<>();

    public void save(EmergencyRequest request) {
        requestMap.put(request.getId(), request);
    }

    public EmergencyRequest getActiveEmergency(int intersectionId) {
        return requestMap.values().stream()
                .filter(r -> r.getIntersectionId() == intersectionId && r.isActive())
                .findFirst()
                .orElse(null);
    }

    public void updateStatus(int requestId, boolean isActive) {
        EmergencyRequest request = requestMap.get(requestId);
        if (request != null) {
            request.setActive(isActive);
        }
    }
}
