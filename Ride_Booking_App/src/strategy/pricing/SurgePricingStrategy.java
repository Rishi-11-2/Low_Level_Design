package strategy.pricing;

import dto.PricingContext;

/**
 * Surge pricing: wraps BasePricingStrategy and applies a surge multiplier.
 * Multiplier comes from PricingContext (e.g., 1.5x, 2.0x during peak hours).
 */
public class SurgePricingStrategy implements PricingStrategy {

    private final PricingStrategy basePricingStrategy;

    public SurgePricingStrategy(PricingStrategy basePricingStrategy) {
        this.basePricingStrategy = basePricingStrategy;
    }

    @Override
    public long calculateFare(double distanceKm, long durationSeconds, PricingContext context) {
        long baseFare = basePricingStrategy.calculateFare(distanceKm, durationSeconds, context);
        double surgeMultiplier = context.getSurgeMultiplier();

        if (surgeMultiplier <= 1.0) {
            return baseFare;
        }

        return Math.round(baseFare * surgeMultiplier);
    }
}
