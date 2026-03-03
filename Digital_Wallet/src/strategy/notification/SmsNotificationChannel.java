package strategy.notification;

import dto.NotificationMessage;

/**
 * Mock SMS notification channel.
 * In production, this would integrate with Twilio, SNS, etc.
 */
public class SmsNotificationChannel implements NotificationChannel {

    @Override
    public void send(NotificationMessage message) {
        System.out.println("[SMS] To: " + message.getTo()
                + " | Body: " + message.getBody());
    }
}
