package service;

import dto.DistanceAndDuration;
import dto.FareEstimateResponse;
import dto.PricingContext;
import model.Location;
import strategy.pricing.PricingStrategy;

/**
 * PricingService acts as the strategy context for fare calculation.
 * Uses MapService for distance/duration and PricingStrategy for fare.
 */
public class PricingService {

    private final PricingStrategy pricingStrategy;
    private final MapService mapService;

    public PricingService(PricingStrategy pricingStrategy, MapService mapService) {
        this.pricingStrategy = pricingStrategy;
        this.mapService = mapService;
    }

    public FareEstimateResponse calculateFare(Location pickup, Location dropoff) {
        DistanceAndDuration distAndDur = mapService.getDistanceAndDuration(pickup, dropoff);

        PricingContext context = new PricingContext();
        long fare = pricingStrategy.calculateFare(
                distAndDur.getDistanceKm(),
                distAndDur.getDurationSeconds(),
                context
        );

        System.out.println("[Pricing] Fare calculated: $" + (fare / 100.0) +
                           " for " + distAndDur.getDistanceKm() + "km, " +
                           distAndDur.getDurationSeconds() + "s");

        return new FareEstimateResponse(fare, distAndDur.getDistanceKm(),
                                        distAndDur.getDurationSeconds(), context.getCurrency());
    }
}
