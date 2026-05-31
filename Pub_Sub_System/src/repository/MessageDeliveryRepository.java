package repository;

import model.DeliveryStatus;
import model.MessageDelivery;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MessageDeliveryRepository {
    private final Map<String, MessageDelivery> deliveryMap = new ConcurrentHashMap<>();

    public MessageDelivery save(MessageDelivery delivery) {
        deliveryMap.put(delivery.getId(), delivery);
        return delivery;
    }

    public List<MessageDelivery> findPendingBySubscriber(String subscriberId) {
        return deliveryMap.values().stream()
                .filter(d -> d.getSubscriberId().equals(subscriberId) && d.getStatus() == DeliveryStatus.PENDING)
                .collect(Collectors.toList());
    }

    public List<MessageDelivery> findAllBySubscriber(String subscriberId) {
        return deliveryMap.values().stream()
                .filter(d -> d.getSubscriberId().equals(subscriberId))
                .collect(Collectors.toList());
    }

    public void updateDeliveryStatus(String deliveryId, DeliveryStatus status) {
        MessageDelivery delivery = deliveryMap.get(deliveryId);
        if (delivery != null) {
            delivery.setStatus(status);
            if (status == DeliveryStatus.ACKNOWLEDGED) {
                delivery.setAcknowledgedAt(System.currentTimeMillis());
            }
            save(delivery);
        }
    }

    public void deleteById(String deliveryId) {
        deliveryMap.remove(deliveryId);
    }
}
