package strategy.payment;

import model.enums.TransactionStatus;
import java.util.Map;
import java.util.UUID;

/**
 * Mock PayPal payment gateway provider (simulation).
 */
public class PayPalPaymentGatewayProvider implements PaymentGatewayProvider {

    @Override
    public String getName() { return "paypal"; }

    @Override
    public String initiatePayment(String accountNumber, long amount, String paymentMethod,
                                  Map<String, String> paymentDetails) {
        String providerRef = "pp_" + UUID.randomUUID().toString().substring(0, 8);
        System.out.println("[PayPal] Payment initiated: ref=" + providerRef
                + ", amount=" + String.format("%.2f TUF", amount / 100.0)
                + ", method=" + paymentMethod);
        return providerRef;
    }

    @Override
    public boolean verifyCallback(String providerRef, TransactionStatus status) {
        System.out.println("[PayPal] Verifying callback: ref=" + providerRef + ", status=" + status);
        return providerRef != null && providerRef.startsWith("pp_");
    }
}
