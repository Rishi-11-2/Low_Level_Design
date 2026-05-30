package adapter;

import java.util.UUID;

public class RazorpayAdapter implements PaymentGatewayAdapter {
    private int attemptCount = 0;

    @Override
    public boolean processPayment(UUID ticketId, double amount) {
        System.out.println("[Razorpay SDK] Processing transaction for Ticket ID: " + ticketId + " of amount $" + amount);
        
        // Simulating a transient failure for the retry flow demonstration
        attemptCount++;
        if (attemptCount % 3 == 0) {
            System.out.println("[Razorpay SDK] Connection Timeout. Payment Failed.");
            return false;
        }
        
        System.out.println("[Razorpay SDK] Payment Captured successfully via Razorpay.");
        return true;
    }
}
