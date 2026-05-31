package repository;

import model.Subscription;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SubscriptionRepository {
    private final Map<String, Subscription> subscriptionMap = new ConcurrentHashMap<>();

    public Subscription save(Subscription subscription) {
        subscriptionMap.put(subscription.getId(), subscription);
        return subscription;
    }

    public List<Subscription> findByTopic(String topicId) {
        return subscriptionMap.values().stream()
                .filter(s -> s.isActive() && s.getTopicId().equals(topicId))
                .collect(Collectors.toList());
    }

    public List<Subscription> findBySubscriber(String subscriberId) {
        return subscriptionMap.values().stream()
                .filter(s -> s.isActive() && s.getSubscriberId().equals(subscriberId))
                .collect(Collectors.toList());
    }

    public void deactivateSubscription(String topicId, String subscriberId) {
        subscriptionMap.values().stream()
                .filter(s -> s.getTopicId().equals(topicId) && s.getSubscriberId().equals(subscriberId))
                .forEach(s -> {
                    s.setActive(false);
                    save(s);
                });
    }

    public void deleteById(String subscriptionId) {
        subscriptionMap.remove(subscriptionId);
    }
}
