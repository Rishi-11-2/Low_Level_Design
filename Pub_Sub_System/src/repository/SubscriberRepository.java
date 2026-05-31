package repository;

import model.Subscriber;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SubscriberRepository {
    private final Map<String, Subscriber> subscriberMap = new ConcurrentHashMap<>();

    public Subscriber save(Subscriber subscriber) {
        subscriberMap.put(subscriber.getId(), subscriber);
        return subscriber;
    }

    public Optional<Subscriber> findById(String subscriberId) {
        return Optional.ofNullable(subscriberMap.get(subscriberId));
    }

    public List<Subscriber> findAll() {
        return new ArrayList<>(subscriberMap.values());
    }

    public void updateOnlineStatus(String subscriberId, boolean isOnline, String connectionId) {
        Subscriber subscriber = subscriberMap.get(subscriberId);
        if (subscriber != null) {
            subscriber.setOnline(isOnline);
            subscriber.setRealtimeConnectionId(connectionId);
            subscriber.setLastHeartbeat(System.currentTimeMillis());
            save(subscriber);
        }
    }

    public void deleteById(String subscriberId) {
        subscriberMap.remove(subscriberId);
    }
}
