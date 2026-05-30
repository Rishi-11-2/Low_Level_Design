package repository;

import model.PricingRule;
import model.VehicleType;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PricingRuleRepository {
    private final Map<VehicleType, PricingRule> pricingRuleMap = new ConcurrentHashMap<>();

    public PricingRule save(PricingRule rule) {
        pricingRuleMap.put(rule.getVehicleType(), rule);
        return rule;
    }

    public Optional<PricingRule> findByVehicleType(VehicleType vehicleType) {
        return Optional.ofNullable(pricingRuleMap.get(vehicleType));
    }

    public Collection<PricingRule> findAll() {
        return pricingRuleMap.values();
    }

    public void delete(VehicleType vehicleType) {
        pricingRuleMap.remove(vehicleType);
    }
}
