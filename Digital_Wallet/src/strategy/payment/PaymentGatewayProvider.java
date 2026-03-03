package strategy.payment;

import model.enums.TransactionStatus;
import java.util.Map;

/**
 * Strategy interface for payment gateway providers.
 * Each concrete provider (Stripe, Razorpay, PayPal, Mock) implements this interface.
 * Open/Closed Principle: add new gateways without modifying existing code.
 */
public interface PaymentGatewayProvider {

    /**
     * @return The unique name of this payment provider (e.g., "stripe", "razorpay").
     */
    String getName();

    /**
     * Initiate a payment with this provider.
     * @return providerRef — a unique reference ID from the provider, used for callbacks.
     */
    String initiatePayment(String accountNumber, long amount, String paymentMethod,
                           Map<String, String> paymentDetails);

    /**
     * Verify a payment callback from this provider.
     * @param providerRef the reference from initiatePayment
     * @param status the reported status
     * @return true if callback is authentic and valid
     */
    boolean verifyCallback(String providerRef, TransactionStatus status);
}
