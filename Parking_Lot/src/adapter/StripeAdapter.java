package adapter;

import java.util.UUID;

public class StripeAdapter implements PaymentGatewayAdapter {
    @Override
    public boolean processPayment(UUID ticketId, double amount) {
        System.out.println("[Stripe SDK] Initializing payment intent for Ticket ID: " + ticketId + " of amount $" + amount);
        System.out.println("[Stripe SDK] Payment charge completed successfully via Stripe.");
        return true;
    }
}
