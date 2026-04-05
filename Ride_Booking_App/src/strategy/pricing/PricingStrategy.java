package strategy.pricing;

import dto.PricingContext;

public interface PricingStrategy {
    long calculateFare(double distanceKm, long durationSeconds, PricingContext context);
}
