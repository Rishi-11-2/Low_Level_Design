import controller.*;
import model.*;
import repository.*;
import service.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("==============================================================");
        System.out.println("         PUB SUB SYSTEM LOW LEVEL DESIGN BOOT                 ");
        System.out.println("==============================================================");

        // 1. Initialize Repositories
        TopicRepository topicRepository = new TopicRepository();
        SubscriberRepository subscriberRepository = new SubscriberRepository();
        SubscriptionRepository subscriptionRepository = new SubscriptionRepository();
        MessageRepository messageRepository = new MessageRepository();
        MessageDeliveryRepository messageDeliveryRepository = new MessageDeliveryRepository();

        // 2. Initialize Services
        TopicService topicService = new TopicService(topicRepository);
        PublisherService publisherService = new PublisherService(
                messageRepository, topicRepository, messageDeliveryRepository, subscriptionRepository, subscriberRepository
        );
        SubscriberService subscriberService = new SubscriberService(
                subscriberRepository, messageDeliveryRepository, messageRepository
        );
        SubscriptionService subscriptionService = new SubscriptionService(subscriptionRepository);
        MessageService messageService = new MessageService(messageDeliveryRepository);

        // 3. Initialize Controllers
        TopicController topicController = new TopicController(topicService);
        PublisherController publisherController = new PublisherController(publisherService);
        SubscriberController subscriberController = new SubscriberController(subscriberService);
        SubscriptionController subscriptionController = new SubscriptionController(subscriptionService);
        MessageController messageController = new MessageController(messageService);

        // 4. Create Topics (Topic Creation Flow)
        System.out.println("\n--- Setup: Creating Channels ---");
        Topic sportNews = topicController.createTopic("Sports News Feed");
        Topic techNews = topicController.createTopic("Tech Tech Feed");

        // 5. Register Subscribers (User Registration Flow)
        System.out.println("\n--- Setup: Registering Subscribers ---");
        Subscriber alice = subscriberController.registerSubscriber("alice@example.com");
        Subscriber bob = subscriberController.registerSubscriber("bob@example.com");

        // Set live connections (Online status)
        subscriberController.goOnline(alice.getId(), "CONN-ALICE-101");
        subscriberController.goOnline(bob.getId(), "CONN-BOB-202");

        // 6. Subscription Management (Subscription Flow)
        System.out.println("\n--- Setup: Managing Subscriptions ---");
        // Alice subscribes to sports
        subscriptionController.subscribeToTopic(sportNews.getId(), alice.getId());
        // Bob subscribes to sports & tech
        subscriptionController.subscribeToTopic(sportNews.getId(), bob.getId());
        subscriptionController.subscribeToTopic(techNews.getId(), bob.getId());

        // 7. Simulation 1: Message Publishing to Sports (Standard Online Flow)
        System.out.println("\n--- Simulation 1: Live Broadcast Sports Update (Everyone Online) ---");
        Message msg1 = publisherController.publishMessage(sportNews.getId(), "Real Madrid wins Champions League!");

        // 8. Simulation 2: Bob goes Offline & message is published (Offline Deferral Flow)
        System.out.println("\n--- Simulation 2: Bob goes Offline, Broadcast Tech Update ---");
        subscriberController.goOffline(bob.getId());

        // Publish to Tech News (only Bob is subscribed, and he is offline!)
        Message msg2 = publisherController.publishMessage(techNews.getId(), "Apple launches Vision Pro 2!");

        // Publish to Sports (both Alice & Bob subscribed, Alice online gets live, Bob gets deferred)
        Message msg3 = publisherController.publishMessage(sportNews.getId(), "Djokovic wins French Open!");

        // 9. Simulation 3: Bob comes back online (Offline Reconnection Recovery Queue Flow)
        System.out.println("\n--- Simulation 3: Bob comes Back Online (Triggering Recovery Queue) ---");
        subscriberController.goOnline(bob.getId(), "CONN-BOB-RECONNECTED-909");

        // 10. Simulation 4: Client Message Acknowledgement Flow
        System.out.println("\n--- Simulation 4: Bob acknowledges reconnected message ---");
        messageController.acknowledgeMessage(msg3.getId(), bob.getId());

        System.out.println("\n==============================================================");
        System.out.println("       PUB SUB SYSTEM LOW LEVEL DESIGN SIMULATION COMPLETE    ");
        System.out.println("==============================================================");
    }
}
