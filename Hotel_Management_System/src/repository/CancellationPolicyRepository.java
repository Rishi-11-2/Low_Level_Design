package repository;

import model.CancellationPolicy;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CancellationPolicyRepository {
    private final Map<String, CancellationPolicy> policyMap = new ConcurrentHashMap<>();

    public CancellationPolicy save(CancellationPolicy policy) {
        policyMap.put(policy.getId(), policy);
        return policy;
    }

    public Optional<CancellationPolicy> findById(String policyId) {
        return Optional.ofNullable(policyMap.get(policyId));
    }
}
