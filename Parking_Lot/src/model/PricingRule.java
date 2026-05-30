package model;

import java.util.UUID;

public class PricingRule {
    private final UUID id;
    private final VehicleType vehicleType;
    private double ratePerHour;
    private double flatRate;

    public PricingRule(VehicleType vehicleType, double ratePerHour, double flatRate) {
        this.id = UUID.randomUUID();
        this.vehicleType = vehicleType;
        this.ratePerHour = ratePerHour;
        this.flatRate = flatRate;
    }

    public UUID getId() {
        return id;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public double getRatePerHour() {
        return ratePerHour;
    }

    public void setRatePerHour(double ratePerHour) {
        this.ratePerHour = ratePerHour;
    }

    public double getFlatRate() {
        return flatRate;
    }

    public void setFlatRate(double flatRate) {
        this.flatRate = flatRate;
    }
}
