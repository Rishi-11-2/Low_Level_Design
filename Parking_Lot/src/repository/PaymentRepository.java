package repository;

import model.Payment;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PaymentRepository {
    private final Map<UUID, Payment> paymentMap = new ConcurrentHashMap<>();

    public Payment save(Payment payment) {
        paymentMap.put(payment.getId(), payment);
        return payment;
    }

    public Optional<Payment> findById(UUID id) {
        return Optional.ofNullable(paymentMap.get(id));
    }

    public Optional<Payment> findByTicketId(UUID ticketId) {
        return paymentMap.values().stream()
                .filter(p -> p.getTicketId().equals(ticketId))
                .sorted((p1, p2) -> {
                    if (p1.getStatus() == model.PaymentStatus.COMPLETED && p2.getStatus() != model.PaymentStatus.COMPLETED) {
                        return -1;
                    }
                    if (p1.getStatus() != model.PaymentStatus.COMPLETED && p2.getStatus() == model.PaymentStatus.COMPLETED) {
                        return 1;
                    }
                    return 0;
                })
                .findFirst();
    }
}
