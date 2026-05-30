package model;

import java.util.UUID;

public class InternalRequest {
    private final String id;
    private final String elevatorId;
    private final int destinationFloor;
    private final long timestamp;
    private RequestStatus status;

    public InternalRequest(String elevatorId, int destinationFloor) {
        this.id = UUID.randomUUID().toString();
        this.elevatorId = elevatorId;
        this.destinationFloor = destinationFloor;
        this.timestamp = System.currentTimeMillis();
        this.status = RequestStatus.PENDING;
    }

    public String getId() {
        return id;
    }

    public String getElevatorId() {
        return elevatorId;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
