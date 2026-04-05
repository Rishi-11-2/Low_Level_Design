package strategy.payment;

import model.enums.PaymentStatus;

import java.util.Map;
import java.util.UUID;

public class StripePaymentGatewayProvider implements PaymentGatewayProvider {

    @Override
    public String getName() { return "STRIPE"; }

    @Override
    public String initiatePayment(String rideId, long amount, Map<String, String> paymentDetails) {
        String transactionId = "stripe_txn_" + UUID.randomUUID().toString().substring(0, 8);
        System.out.println("[Stripe] Payment initiated for ride " + rideId +
                           ", amount: $" + (amount / 100.0) + ", txnId: " + transactionId);
        return transactionId;
    }

    @Override
    public boolean verifyCallback(String transactionId, PaymentStatus status) {
        System.out.println("[Stripe] Verifying callback for txn: " + transactionId + ", status: " + status);
        return transactionId != null && transactionId.startsWith("stripe_txn_");
    }
}
