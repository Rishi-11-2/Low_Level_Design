package model;

public class RealtimeSubscriber implements SubscriberObserver {
    private final String connectionId;
    private final String subscriberId;

    public RealtimeSubscriber(String connectionId, String subscriberId) {
        this.connectionId = connectionId;
        this.subscriberId = subscriberId;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    @Override
    public void update(Message message) {
        System.out.println("[Realtime Channel] Pushing live socket message over connection: " + connectionId + " for Subscriber ID: " + subscriberId + " containing: '" + message.getContent() + "'");
    }
}
