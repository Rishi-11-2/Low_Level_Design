package strategy.payment;

import model.enums.TransactionStatus;
import java.util.Map;
import java.util.UUID;

/**
 * Default mock payment gateway provider — always succeeds.
 * Used for demo/testing purposes.
 */
public class MockPaymentGatewayProvider implements PaymentGatewayProvider {

    @Override
    public String getName() { return "mock"; }

    @Override
    public String initiatePayment(String accountNumber, long amount, String paymentMethod,
                                  Map<String, String> paymentDetails) {
        String providerRef = "mock_" + UUID.randomUUID().toString().substring(0, 8);
        System.out.println("[MockGateway] Payment initiated (auto-success): ref=" + providerRef
                + ", amount=" + String.format("%.2f TUF", amount / 100.0));
        return providerRef;
    }

    @Override
    public boolean verifyCallback(String providerRef, TransactionStatus status) {
        System.out.println("[MockGateway] Verifying callback (always valid): ref=" + providerRef);
        return true; // Mock always verifies successfully
    }
}
