package model;

import java.util.UUID;

public class ExternalRequest {
    private final String id;
    private final int floorNumber;
    private final String buildingId;
    private final Direction direction;
    private final long timestamp;
    private RequestStatus status;
    private String assignedElevatorId;

    public ExternalRequest(int floorNumber, String buildingId, Direction direction) {
        this.id = UUID.randomUUID().toString();
        this.floorNumber = floorNumber;
        this.buildingId = buildingId;
        this.direction = direction;
        this.timestamp = System.currentTimeMillis();
        this.status = RequestStatus.PENDING;
        this.assignedElevatorId = null;
    }

    public String getId() {
        return id;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public Direction getDirection() {
        return direction;
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

    public String getAssignedElevatorId() {
        return assignedElevatorId;
    }

    public void setAssignedElevatorId(String assignedElevatorId) {
        this.assignedElevatorId = assignedElevatorId;
    }
}
