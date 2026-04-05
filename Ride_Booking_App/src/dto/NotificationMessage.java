package dto;

public class NotificationMessage {
    private String type;
    private String title;
    private String body;
    private String rideId;

    public NotificationMessage(String type, String title, String body, String rideId) {
        this.type = type;
        this.title = title;
        this.body = body;
        this.rideId = rideId;
    }

    public String getType() { return type; }
    public String getTitle() { return title; }
    public String getBody() { return body; }
    public String getRideId() { return rideId; }

    @Override
    public String toString() {
        return "Notification{type='" + type + "', title='" + title + "', body='" + body + "'}";
    }
}
