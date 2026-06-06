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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailSubscriber that = (EmailSubscriber) o;
        return email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
