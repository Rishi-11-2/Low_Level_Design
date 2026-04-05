package service;

import dto.NotificationMessage;

/**
 * Notification service for push notifications to riders and drivers.
 * In production, this would integrate with FCM/APNS.
 */
public class NotificationService {

    public void sendToDriver(int driverId, NotificationMessage message) {
        System.out.println("[Notification → Driver " + driverId + "] " +
                           message.getType() + ": " + message.getTitle() + " - " + message.getBody());
    }

    public void sendToRider(int riderId, NotificationMessage message) {
        System.out.println("[Notification → Rider " + riderId + "] " +
                           message.getType() + ": " + message.getTitle() + " - " + message.getBody());
    }
}
