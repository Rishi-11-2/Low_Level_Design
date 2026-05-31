package service;

import model.*;
import repository.*;
import java.util.List;
import java.util.UUID;

public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final MessageDeliveryRepository messageDeliveryRepository;
    private final MessageRepository messageRepository;

    public SubscriberService(SubscriberRepository subscriberRepository,
                             MessageDeliveryRepository messageDeliveryRepository,
                             MessageRepository messageRepository) {
        this.subscriberRepository = subscriberRepository;
        this.messageDeliveryRepository = messageDeliveryRepository;
        this.messageRepository = messageRepository;
    }

    public Subscriber registerSubscriber(String email) {
        String id = "SUB-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Subscriber subscriber = new Subscriber(id, email);
        subscriberRepository.save(subscriber);
        System.out.println("[SubscriberService] Registered subscriber: " + email + " (id=" + id + ")");
        return subscriber;
    }

    public void goOnline(String subscriberId, String connectionId) {
        System.out.println("\n>>> [SubscriberService] Subscriber " + subscriberId + " is going ONLINE over connection: " + connectionId);
        subscriberRepository.updateOnlineStatus(subscriberId, true, connectionId);
        pushPendingDeliveries(subscriberId);
    }

    public void goOffline(String subscriberId) {
        System.out.println("\n>>> [SubscriberService] Subscriber " + subscriberId + " went OFFLINE.");
        subscriberRepository.updateOnlineStatus(subscriberId, false, null);
    }

    public void pushPendingDeliveries(String subscriberId) {
        Subscriber subscriber = subscriberRepository.findById(subscriberId).orElse(null);
        if (subscriber == null) return;

        List<MessageDelivery> pending = messageDeliveryRepository.findPendingBySubscriber(subscriberId);
        if (pending.isEmpty()) {
            return;
        }

        System.out.println("[Reconnection Recovery] Pushing " + pending.size() + " deferred realtime messages from pending queue to: " + subscriber.getEmail());
        for (MessageDelivery delivery : pending) {
            messageRepository.findById(delivery.getMessageId()).ifPresent(message -> {
                // Simulate push
                System.out.println("[Reconnection Push] Delivering message: '" + message.getContent() + "' over socket: " + subscriber.getRealtimeConnectionId());
                
                // Update status to DELIVERED
                messageDeliveryRepository.updateDeliveryStatus(delivery.getId(), DeliveryStatus.DELIVERED);
            });
        }
        System.out.println("[Reconnection Recovery] Completed delivery of all queued deferred messages.");
    }
}
