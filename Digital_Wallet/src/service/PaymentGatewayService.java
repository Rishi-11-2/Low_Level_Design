package service;

import model.enums.TransactionStatus;
import strategy.payment.PaymentGatewayProvider;
import strategy.payment.PaymentGatewayRouter;
import java.util.Map;

/**
 * PaymentGatewayService — thin wrapper delegating to PaymentGatewayRouter.
 * Provides a clean service-layer interface for payment operations.
 */
public class PaymentGatewayService {

    private final PaymentGatewayRouter gatewayRouter;

    public PaymentGatewayService(PaymentGatewayRouter gatewayRouter) {
        this.gatewayRouter = gatewayRouter;
    }

    /**
     * Initiate a payment through the specified gateway.
     * @return providerRef — unique reference for callback tracking
     */
    public String initiatePayment(String accountNumber, long amount, String paymentMethod,
                                  String paymentGateway, Map<String, String> paymentDetails) {
        String selectedGateway = gatewayRouter.selectProvider(paymentGateway, amount, "TUF");
        PaymentGatewayProvider provider = gatewayRouter.resolve(selectedGateway);
        return provider.initiatePayment(accountNumber, amount, paymentMethod, paymentDetails);
    }

    /**
     * Verify a payment callback.
     * @return true if callback is authentic
     */
    public boolean verifyCallback(String providerRef, TransactionStatus status) {
        // Determine provider from providerRef prefix
        String gatewayName = extractGatewayFromRef(providerRef);
        PaymentGatewayProvider provider = gatewayRouter.resolve(gatewayName);
        return provider.verifyCallback(providerRef, status);
    }

    /**
     * Extract gateway name from providerRef.
     * Format: "stripe_xxxx", "rzp_xxxx", "pp_xxxx", "mock_xxxx"
     */
    private String extractGatewayFromRef(String providerRef) {
        if (providerRef == null) throw new IllegalArgumentException("Provider ref is null");
        if (providerRef.startsWith("stripe_")) return "stripe";
        if (providerRef.startsWith("rzp_")) return "razorpay";
        if (providerRef.startsWith("pp_")) return "paypal";
        if (providerRef.startsWith("mock_")) return "mock";
        throw new IllegalArgumentException("Unknown provider ref format: " + providerRef);
    }
}
