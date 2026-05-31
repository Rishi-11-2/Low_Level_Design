package service;

import model.Subscription;
import repository.SubscriptionRepository;
import java.util.UUID;

public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public Subscription subscribeToTopic(String topicId, String subscriberId) {
        String id = "SUB-LINK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Subscription subscription = new Subscription(id, topicId, subscriberId);
        subscriptionRepository.save(subscription);
        System.out.println("[SubscriptionService] Subscriber " + subscriberId + " subscribed to Topic " + topicId);
        return subscription;
    }

    public void unsubscribeFromTopic(String topicId, String subscriberId) {
        subscriptionRepository.deactivateSubscription(topicId, subscriberId);
        System.out.println("[SubscriptionService] Subscriber " + subscriberId + " unsubscribed from Topic " + topicId);
    }
}
