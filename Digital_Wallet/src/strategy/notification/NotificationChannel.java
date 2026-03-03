package strategy.notification;

import dto.NotificationMessage;

/**
 * Strategy interface for notification channels.
 * Concrete implementations: EmailNotificationChannel, SmsNotificationChannel.
 */
public interface NotificationChannel {
    void send(NotificationMessage message);
}
