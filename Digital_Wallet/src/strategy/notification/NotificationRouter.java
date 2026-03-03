package strategy.notification;

import dto.NotificationMessage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Notification Router — Registry + Single-dispatch.
 * Registers notification channels by name and dispatches messages to a specific channel.
 */
public class NotificationRouter {

    private final Map<String, NotificationChannel> channels = new ConcurrentHashMap<>();

    public void register(String channelName, NotificationChannel channel) {
        channels.put(channelName, channel);
        System.out.println("[NotificationRouter] Registered channel: " + channelName);
    }

    public void send(String channelName, NotificationMessage message) {
        NotificationChannel channel = channels.get(channelName);
        if (channel == null) {
            System.out.println("[NotificationRouter] WARNING: Channel not found: " + channelName);
            return;
        }
        try {
            channel.send(message);
        } catch (Exception e) {
            // Notification failures should never block transaction processing
            System.out.println("[NotificationRouter] ERROR sending via " + channelName + ": " + e.getMessage());
        }
    }
}
