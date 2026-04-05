package strategy.payment;

import model.enums.PaymentStatus;

import java.util.Map;
import java.util.UUID;

public class MockPaymentGatewayProvider implements PaymentGatewayProvider {

    @Override
    public String getName() { return "MOCK"; }

    @Override
    public String initiatePayment(String rideId, long amount, Map<String, String> paymentDetails) {
        String transactionId = "mock_txn_" + UUID.randomUUID().toString().substring(0, 8);
        System.out.println("[Mock] Payment initiated for ride " + rideId +
                           ", amount: $" + (amount / 100.0) + ", txnId: " + transactionId);
        return transactionId;
    }

    @Override
    public boolean verifyCallback(String transactionId, PaymentStatus status) {
        System.out.println("[Mock] Verifying callback for txn: " + transactionId + ", status: " + status);
        return true; // Always returns true for testing
    }
}
