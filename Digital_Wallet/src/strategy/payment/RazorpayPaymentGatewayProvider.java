package strategy.payment;

import model.enums.TransactionStatus;
import java.util.Map;
import java.util.UUID;

/**
 * Mock Razorpay payment gateway provider (simulation).
 */
public class RazorpayPaymentGatewayProvider implements PaymentGatewayProvider {

    @Override
    public String getName() { return "razorpay"; }

    @Override
    public String initiatePayment(String accountNumber, long amount, String paymentMethod,
                                  Map<String, String> paymentDetails) {
        String providerRef = "rzp_" + UUID.randomUUID().toString().substring(0, 8);
        System.out.println("[Razorpay] Payment initiated: ref=" + providerRef
                + ", amount=" + String.format("%.2f TUF", amount / 100.0)
                + ", method=" + paymentMethod);
        return providerRef;
    }

    @Override
    public boolean verifyCallback(String providerRef, TransactionStatus status) {
        System.out.println("[Razorpay] Verifying callback: ref=" + providerRef + ", status=" + status);
        return providerRef != null && providerRef.startsWith("rzp_");
    }
}
