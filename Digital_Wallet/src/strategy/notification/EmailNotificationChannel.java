package strategy.notification;

import dto.NotificationMessage;

/**
 * Mock email notification channel.
 * In production, this would integrate with SMTP or an email service (SendGrid, SES, etc.).
 */
public class EmailNotificationChannel implements NotificationChannel {

    @Override
    public void send(NotificationMessage message) {
        System.out.println("[Email] To: " + message.getTo()
                + " | Subject: " + message.getSubject()
                + " | Body: " + message.getBody());
    }
}
