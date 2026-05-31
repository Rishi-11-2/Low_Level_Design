package service;

import model.*;
import repository.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PublisherService {
    private final MessageRepository messageRepository;
    private final TopicRepository topicRepository;
    private final MessageDeliveryRepository messageDeliveryRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriberRepository subscriberRepository;

    public PublisherService(MessageRepository messageRepository,
                            TopicRepository topicRepository,
                            MessageDeliveryRepository messageDeliveryRepository,
                            SubscriptionRepository subscriptionRepository,
                            SubscriberRepository subscriberRepository) {
        this.messageRepository = messageRepository;
        this.topicRepository = topicRepository;
        this.messageDeliveryRepository = messageDeliveryRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.subscriberRepository = subscriberRepository;
    }

    public Message publishMessage(String topicId, String content) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found"));

        if (!topic.isActive()) {
            throw new IllegalStateException("Cannot publish to an inactive topic.");
        }

        String messageId = "MSG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Message message = new Message(messageId, topicId, content);
        messageRepository.save(message);

        System.out.println("\n>>> [PublisherService] Publishing message: '" + content + "' to Topic: '" + topic.getName() + "'");
        processMessageDeliveryAsync(message, topic);
        return message;
    }

    public void processMessageDeliveryAsync(Message message, Topic topic) {
        List<Subscription> subs = subscriptionRepository.findByTopic(topic.getId());
        
        for (Subscription sub : subs) {
            String subId = sub.getSubscriberId();
            Optional<Subscriber> subscriberOpt = subscriberRepository.findById(subId);
            
            if (subscriberOpt.isEmpty()) continue;
            Subscriber subscriber = subscriberOpt.get();

            // 1. Email Delivery channel (Always Delivered)
            String emailDeliveryId = "DLV-EMAIL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            MessageDelivery emailDelivery = new MessageDelivery(emailDeliveryId, message.getId(), subId, DeliveryChannel.EMAIL);
            emailDelivery.setStatus(DeliveryStatus.DELIVERED);
            messageDeliveryRepository.save(emailDelivery);
            
            // Dispatch live update using Subject
            EmailSubscriber emailObserver = new EmailSubscriber(subscriber.getEmail());
            topic.getMessageSubject().addEmailSubscriber(emailObserver);
            topic.getMessageSubject().notifyEmailSubscribers(message);
            topic.getMessageSubject().removeEmailSubscriber(emailObserver); // Clean up transient observer

            // 2. Realtime Delivery channel (Delivered only if online, else Pending)
            String realtimeDeliveryId = "DLV-REALTIME-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            MessageDelivery realtimeDelivery = new MessageDelivery(realtimeDeliveryId, message.getId(), subId, DeliveryChannel.REALTIME);

            if (subscriber.isOnline()) {
                realtimeDelivery.setStatus(DeliveryStatus.DELIVERED);
                messageDeliveryRepository.save(realtimeDelivery);

                RealtimeSubscriber realtimeObserver = new RealtimeSubscriber(subscriber.getRealtimeConnectionId(), subId);
                topic.getMessageSubject().addRealtimeSubscriber(realtimeObserver);
                topic.getMessageSubject().notifyRealtimeSubscribers(message);
                topic.getMessageSubject().removeRealtimeSubscriber(realtimeObserver); // Clean up transient observer
            } else {
                realtimeDelivery.setStatus(DeliveryStatus.PENDING);
                messageDeliveryRepository.save(realtimeDelivery);
                System.out.println("[Deferred Queue] Subscriber " + subscriber.getEmail() + " is currently offline. Deferred Realtime Message ID " + message.getId() + " to Pending Queue.");
            }
        }
    }
}
