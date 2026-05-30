package model;

public class MobileAppSubscriber implements TaskSubscriber {
    private final String deviceToken;

    public MobileAppSubscriber(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    @Override
    public void update(int taskId, ChangeType changeType, String oldValue, String newValue) {
        System.out.println("[Push Notification] Dispatched mobile alert to device token: " + deviceToken + " for Task ID: " + taskId + ". Event: " + changeType);
    }
}
