package dto;

public class PricingContext {
    private double surgeMultiplier;
    private String vehicleType;
    private String currency;

    public PricingContext() {
        this.surgeMultiplier = 1.0;
        this.currency = "USD";
    }

    public PricingContext(double surgeMultiplier, String vehicleType, String currency) {
        this.surgeMultiplier = surgeMultiplier;
        this.vehicleType = vehicleType;
        this.currency = currency;
    }

    public double getSurgeMultiplier() { return surgeMultiplier; }
    public void setSurgeMultiplier(double surgeMultiplier) { this.surgeMultiplier = surgeMultiplier; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
