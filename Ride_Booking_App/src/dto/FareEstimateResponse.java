package dto;

public class FareEstimateResponse {
    private long estimatedFare;
    private double estimatedDistance;
    private long estimatedDuration;
    private String currency;

    public FareEstimateResponse(long estimatedFare, double estimatedDistance,
                                 long estimatedDuration, String currency) {
        this.estimatedFare = estimatedFare;
        this.estimatedDistance = estimatedDistance;
        this.estimatedDuration = estimatedDuration;
        this.currency = currency;
    }

    public long getEstimatedFare() { return estimatedFare; }
    public double getEstimatedDistance() { return estimatedDistance; }
    public long getEstimatedDuration() { return estimatedDuration; }
    public String getCurrency() { return currency; }

    @Override
    public String toString() {
        return "FareEstimate{fare=" + (estimatedFare / 100.0) + " " + currency +
               ", distance=" + estimatedDistance + "km, duration=" + estimatedDuration + "s}";
    }
}
