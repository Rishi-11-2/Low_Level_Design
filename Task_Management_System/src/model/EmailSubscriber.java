package model;

public class EmailSubscriber implements TaskSubscriber {
    private final String email;

    public EmailSubscriber(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public void update(int taskId, ChangeType changeType, String oldValue, String newValue) {
        System.out.println("[Email Notification] Sending update alert to: " + email + " for Task ID: " + taskId + ". Event: " + changeType + ". Details: '" + oldValue + "' -> '" + newValue + "'");
    }
}
