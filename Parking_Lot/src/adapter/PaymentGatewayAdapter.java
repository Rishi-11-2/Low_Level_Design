package adapter;

import java.util.UUID;

public interface PaymentGatewayAdapter {
    boolean processPayment(UUID ticketId, double amount);
}
