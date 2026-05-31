package model;

public class Subscriber {
    private final String id;
    private final String email;
    private String realtimeConnectionId;
    private boolean isOnline;
    private final long createdAt;
    private long lastHeartbeat;

    public Subscriber(String id, String email) {
        this.id = id;
        this.email = email;
        this.realtimeConnectionId = null;
        this.isOnline = true; // Online by default
        this.createdAt = System.currentTimeMillis();
        this.lastHeartbeat = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getRealtimeConnectionId() {
        return realtimeConnectionId;
    }

    public void setRealtimeConnectionId(String realtimeConnectionId) {
        this.realtimeConnectionId = realtimeConnectionId;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(long lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }
}
