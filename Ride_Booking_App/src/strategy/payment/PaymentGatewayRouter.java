package strategy.payment;

import model.enums.PaymentStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Routes payment requests to the appropriate PaymentGatewayProvider.
 * Dynamically selects provider based on payment details or configuration.
 */
public class PaymentGatewayRouter {

    private final Map<String, PaymentGatewayProvider> providers = new HashMap<>();
    private String defaultProviderName;

    public PaymentGatewayRouter() {
        // Register default providers
        registerProvider(new StripePaymentGatewayProvider());
        registerProvider(new RazorpayPaymentGatewayProvider());
        registerProvider(new PayPalPaymentGatewayProvider());
        registerProvider(new MockPaymentGatewayProvider());
        this.defaultProviderName = "MOCK";
    }

    public void registerProvider(PaymentGatewayProvider provider) {
        providers.put(provider.getName().toUpperCase(), provider);
    }

    public void setDefaultProvider(String providerName) {
        if (!providers.containsKey(providerName.toUpperCase())) {
            throw new IllegalArgumentException("Unknown payment provider: " + providerName);
        }
        this.defaultProviderName = providerName.toUpperCase();
    }

    public PaymentGatewayProvider selectProvider(Map<String, String> paymentDetails) {
        if (paymentDetails != null && paymentDetails.containsKey("provider")) {
            String providerName = paymentDetails.get("provider").toUpperCase();
            if (providers.containsKey(providerName)) {
                return providers.get(providerName);
            }
        }
        return providers.get(defaultProviderName);
    }

    public PaymentGatewayProvider getProviderByTransactionId(String transactionId) {
        for (PaymentGatewayProvider provider : providers.values()) {
            if (provider.verifyCallback(transactionId, PaymentStatus.COMPLETED)) {
                return provider;
            }
        }
        return providers.get(defaultProviderName);
    }

    public String initiatePayment(String rideId, long amount, Map<String, String> paymentDetails) {
        PaymentGatewayProvider provider = selectProvider(paymentDetails);
        System.out.println("[Router] Selected payment provider: " + provider.getName());
        return provider.initiatePayment(rideId, amount, paymentDetails);
    }
}
