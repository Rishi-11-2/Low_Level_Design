package dto;

/**
 * Notification message DTO used by NotificationRouter and NotificationChannel.
 */
public class NotificationMessage {

    private String to;
    private String subject;
    private String body;

    public NotificationMessage(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public String getTo() { return to; }
    public String getSubject() { return subject; }
    public String getBody() { return body; }

    @Override
    public String toString() {
        return "NotificationMessage{to='" + to + "', subject='" + subject + "'}";
    }
}
