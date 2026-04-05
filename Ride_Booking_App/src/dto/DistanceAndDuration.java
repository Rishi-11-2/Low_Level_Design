package dto;

public class DistanceAndDuration {
    private double distanceKm;
    private long durationSeconds;

    public DistanceAndDuration(double distanceKm, long durationSeconds) {
        this.distanceKm = distanceKm;
        this.durationSeconds = durationSeconds;
    }

    public double getDistanceKm() { return distanceKm; }
    public long getDurationSeconds() { return durationSeconds; }

    @Override
    public String toString() {
        return "DistanceAndDuration{distance=" + distanceKm + "km, duration=" + durationSeconds + "s}";
    }
}
