package strategy.payment;

import model.enums.PaymentStatus;

import java.util.Map;
import java.util.UUID;

public class RazorpayPaymentGatewayProvider implements PaymentGatewayProvider {

    @Override
    public String getName() { return "RAZORPAY"; }

    @Override
    public String initiatePayment(String rideId, long amount, Map<String, String> paymentDetails) {
        String transactionId = "rzp_txn_" + UUID.randomUUID().toString().substring(0, 8);
        System.out.println("[Razorpay] Payment initiated for ride " + rideId +
                           ", amount: ₹" + (amount / 100.0) + ", txnId: " + transactionId);
        return transactionId;
    }

    @Override
    public boolean verifyCallback(String transactionId, PaymentStatus status) {
        System.out.println("[Razorpay] Verifying callback for txn: " + transactionId + ", status: " + status);
        return transactionId != null && transactionId.startsWith("rzp_txn_");
    }
}
