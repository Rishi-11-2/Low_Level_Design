package strategy.payment;

import model.enums.PaymentStatus;

import java.util.Map;

public interface PaymentGatewayProvider {
    String getName();
    String initiatePayment(String rideId, long amount, Map<String, String> paymentDetails);
    boolean verifyCallback(String transactionId, PaymentStatus status);
}
