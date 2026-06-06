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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MobileAppSubscriber that = (MobileAppSubscriber) o;
        return deviceToken.equals(that.deviceToken);
    }

    @Override
    public int hashCode() {
        return deviceToken.hashCode();
    }
}
