package model;

public class Message {
    private final String id;
    private final String topicId;
    private final String content;
    private final long timestamp;

    public Message(String id, String topicId, String content) {
        this.id = id;
        this.topicId = topicId;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public String getTopicId() {
        return topicId;
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
