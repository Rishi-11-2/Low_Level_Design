package strategy.pricing;

import dto.PricingContext;

/**
 * Base pricing: baseFare + (perKmRate * distance) + (perMinuteRate * minutes)
 * All amounts stored as integer cents (actual * 100).
 */
public class BasePricingStrategy implements PricingStrategy {

    private static final long BASE_FARE_CENTS = 200;        // $2.00
    private static final long PER_KM_RATE_CENTS = 150;      // $1.50 per km
    private static final long PER_MINUTE_RATE_CENTS = 25;    // $0.25 per minute
    private static final long MINIMUM_FARE_CENTS = 500;      // $5.00 minimum

    @Override
    public long calculateFare(double distanceKm, long durationSeconds, PricingContext context) {
        long distanceCharge = Math.round(PER_KM_RATE_CENTS * distanceKm);
        long timeCharge = Math.round(PER_MINUTE_RATE_CENTS * (durationSeconds / 60.0));
        long totalFare = BASE_FARE_CENTS + distanceCharge + timeCharge;

        return Math.max(totalFare, MINIMUM_FARE_CENTS);
    }
}
