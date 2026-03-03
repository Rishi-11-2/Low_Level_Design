package strategy.payment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Payment Gateway Router — Context/Router in the Strategy Pattern.
 * Manages registered payment providers and selects the appropriate one.
 * Auto-registers all built-in providers on construction.
 */
public class PaymentGatewayRouter {

    private final Map<String, PaymentGatewayProvider> providers = new ConcurrentHashMap<>();

    public PaymentGatewayRouter() {
        // Auto-register built-in providers
        registerProvider(new StripePaymentGatewayProvider());
        registerProvider(new RazorpayPaymentGatewayProvider());
        registerProvider(new PayPalPaymentGatewayProvider());
        registerProvider(new MockPaymentGatewayProvider());
        System.out.println("[PaymentGatewayRouter] Initialized with " + providers.size() + " providers: " + providers.keySet());
    }

    public void registerProvider(PaymentGatewayProvider provider) {
        providers.put(provider.getName(), provider);
    }

    /**
     * Select a provider by preferred gateway name.
     * Falls back to "mock" if the preferred gateway is not found.
     * @return the name of the selected gateway
     */
    public String selectProvider(String preferredGateway, long amount, String currency) {
        if (preferredGateway != null && providers.containsKey(preferredGateway)) {
            System.out.println("[PaymentGatewayRouter] Selected provider: " + preferredGateway);
            return preferredGateway;
        }
        System.out.println("[PaymentGatewayRouter] Preferred gateway '" + preferredGateway
                + "' not found, falling back to 'mock'");
        return "mock";
    }

    /**
     * Resolve a provider by name.
     * @throws IllegalArgumentException if gateway not found
     */
    public PaymentGatewayProvider resolve(String gatewayName) {
        PaymentGatewayProvider provider = providers.get(gatewayName);
        if (provider == null) {
            throw new IllegalArgumentException("Payment gateway not found: " + gatewayName);
        }
        return provider;
    }
}
