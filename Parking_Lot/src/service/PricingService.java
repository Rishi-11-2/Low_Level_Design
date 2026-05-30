package service;

import model.PricingRule;
import model.Ticket;
import model.VehicleType;
import repository.PricingRuleRepository;
import java.time.Duration;
import java.time.LocalDateTime;

public class PricingService {
    private final PricingRuleRepository pricingRuleRepository;

    public PricingService(PricingRuleRepository pricingRuleRepository) {
        this.pricingRuleRepository = pricingRuleRepository;
    }

    public double calculateFee(Ticket ticket, VehicleType vehicleType) {
        PricingRule rule = pricingRuleRepository.findByVehicleType(vehicleType)
                .orElse(new PricingRule(vehicleType, 10.0, 5.0)); // Default pricing

        LocalDateTime entryTime = ticket.getEntryTime();
        LocalDateTime exitTime = LocalDateTime.now();
        
        Duration duration = Duration.between(entryTime, exitTime);
        long seconds = duration.getSeconds();
        
        // For testing/simulation purposes:
        // We will treat each second of real-time elapsed as 1 hour of parking time
        // to demonstrate hourly rate calculations without sleeping for hours.
        double billableHours = Math.max(1, seconds); 

        double flatRate = rule.getFlatRate();
        double ratePerHour = rule.getRatePerHour();
        
        return flatRate + (billableHours * ratePerHour);
    }
}
