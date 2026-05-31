package service;

import model.DeliveryStatus;
import model.MessageDelivery;
import repository.MessageDeliveryRepository;

public class MessageService {
    private final MessageDeliveryRepository messageDeliveryRepository;

    public MessageService(MessageDeliveryRepository messageDeliveryRepository) {
        this.messageDeliveryRepository = messageDeliveryRepository;
    }

    public void acknowledgeMessage(String messageId, String subscriberId) {
        System.out.println(">>> [MessageService] Subscriber " + subscriberId + " acknowledged Message ID: " + messageId);
        
        // Find the delivery (pending or delivered) and acknowledge it
        messageDeliveryRepository.findAllBySubscriber(subscriberId).stream()
                .filter(d -> d.getMessageId().equals(messageId))
                .findFirst()
                .ifPresentOrElse(
                    d -> {
                        messageDeliveryRepository.updateDeliveryStatus(d.getId(), DeliveryStatus.ACKNOWLEDGED);
                        System.out.println("[MessageService] Message settled and acknowledged successfully.");
                    },
                    () -> {
                        System.out.println("[MessageService] Warning: Active delivery not found for subscriber " + subscriberId + " and message " + messageId);
                    }
                );
        System.out.println("<<< [MessageService] Message ID: " + messageId + " marked as ACKNOWLEDGED.");
    }
}
