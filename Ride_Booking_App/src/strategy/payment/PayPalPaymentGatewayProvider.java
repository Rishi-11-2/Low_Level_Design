package strategy.payment;

import model.enums.PaymentStatus;

import java.util.Map;
import java.util.UUID;

public class PayPalPaymentGatewayProvider implements PaymentGatewayProvider {

    @Override
    public String getName() { return "PAYPAL"; }

    @Override
    public String initiatePayment(String rideId, long amount, Map<String, String> paymentDetails) {
        String transactionId = "paypal_txn_" + UUID.randomUUID().toString().substring(0, 8);
        System.out.println("[PayPal] Payment initiated for ride " + rideId +
                           ", amount: $" + (amount / 100.0) + ", txnId: " + transactionId);
        return transactionId;
    }

    @Override
    public boolean verifyCallback(String transactionId, PaymentStatus status) {
        System.out.println("[PayPal] Verifying callback for txn: " + transactionId + ", status: " + status);
        return transactionId != null && transactionId.startsWith("paypal_txn_");
    }
}
