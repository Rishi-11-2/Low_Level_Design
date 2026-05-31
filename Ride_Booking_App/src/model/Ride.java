package model;

import model.enums.PaymentStatus;
import model.enums.PaymentType;
import model.enums.RideStatus;
import state.*;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Ride {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    private int id;
    private String rideId;
    private int riderId;
    private Integer driverId;
    private Location pickupLocation;
    private Location dropoffLocation;
    private RideStatus status;
    private long estimatedFare;
    private double estimatedDistance;
    private Double actualDistance;
    private long estimatedDuration;
    private Long actualDuration;
    private LocalDateTime requestedAt;
    private LocalDateTime assignedAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime cancelledAt;
    private String cancellationReason;
    private PaymentType paymentType;
    private String paymentId;
    private PaymentStatus paymentStatus;
    private final java.util.Set<Integer> declinedDrivers = new java.util.concurrent.CopyOnWriteArraySet<>();

    private transient RideState currentState;

    public Ride(int riderId, Location pickupLocation, Location dropoffLocation,
                long estimatedFare, double estimatedDistance, long estimatedDuration,
                PaymentType paymentType) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.rideId = UUID.randomUUID().toString();
        this.riderId = riderId;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.estimatedFare = estimatedFare;
        this.estimatedDistance = estimatedDistance;
        this.estimatedDuration = estimatedDuration;
        this.paymentType = paymentType;
        this.status = RideStatus.REQUESTED;
        this.paymentStatus = PaymentStatus.PENDING;
        this.requestedAt = LocalDateTime.now();
        this.currentState = new RequestedState();
    }

    // --- State Pattern Methods ---
    public void accept(int driverId) {
        getCurrentState().accept(this, driverId);
    }

    public void cancel(int userId, String reason) {
        getCurrentState().cancel(this, userId, reason);
    }

    public void start(int driverId) {
        getCurrentState().start(this, driverId);
    }

    public void complete(int driverId) {
        getCurrentState().complete(this, driverId);
    }

    public RideState getCurrentState() {
        if (currentState == null) {
            currentState = resolveState(this.status);
        }
        return currentState;
    }

    public static RideState resolveState(RideStatus status) {
        switch (status) {
            case REQUESTED:   return new RequestedState();
            case ASSIGNED:    return new AssignedState();
            case ACCEPTED:    return new AcceptedState();
            case IN_PROGRESS: return new InProgressState();
            case COMPLETED:   return new CompletedState();
            case CANCELLED:   return new CancelledState();
            default: throw new IllegalArgumentException("Unknown ride status: " + status);
        }
    }

    public void transitionTo(RideStatus newStatus) {
        this.status = newStatus;
        this.currentState = resolveState(newStatus);
    }

    // --- Getters and Setters ---
    public int getId() { return id; }
    public String getRideId() { return rideId; }

    public int getRiderId() { return riderId; }

    public Integer getDriverId() { return driverId; }
    public void setDriverId(Integer driverId) { this.driverId = driverId; }

    public Location getPickupLocation() { return pickupLocation; }
    public Location getDropoffLocation() { return dropoffLocation; }

    public RideStatus getStatus() { return status; }
    public void setStatus(RideStatus status) { this.status = status; }

    public long getEstimatedFare() { return estimatedFare; }
    public void setEstimatedFare(long estimatedFare) { this.estimatedFare = estimatedFare; }

    public double getEstimatedDistance() { return estimatedDistance; }

    public Double getActualDistance() { return actualDistance; }
    public void setActualDistance(Double actualDistance) { this.actualDistance = actualDistance; }

    public long getEstimatedDuration() { return estimatedDuration; }

    public Long getActualDuration() { return actualDuration; }
    public void setActualDuration(Long actualDuration) { this.actualDuration = actualDuration; }

    public LocalDateTime getRequestedAt() { return requestedAt; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }

    public LocalDateTime getAcceptedAt() { return acceptedAt; }
    public void setAcceptedAt(LocalDateTime acceptedAt) { this.acceptedAt = acceptedAt; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    public LocalDateTime getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }

    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }

    public PaymentType getPaymentType() { return paymentType; }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public java.util.Set<Integer> getDeclinedDrivers() { return declinedDrivers; }
    public void addDeclinedDriver(int driverId) { declinedDrivers.add(driverId); }

    @Override
    public String toString() {
        return "Ride{rideId='" + rideId + "', status=" + status +
               ", riderId=" + riderId + ", driverId=" + driverId + "}";
    }
}
