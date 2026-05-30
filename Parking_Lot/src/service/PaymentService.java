package service;

import adapter.PaymentGatewayAdapter;
import adapter.RazorpayAdapter;
import adapter.StripeAdapter;
import model.Payment;
import model.PaymentGateway;
import model.PaymentStatus;
import repository.PaymentRepository;
import java.util.UUID;

public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final RazorpayAdapter razorpayAdapter;
    private final StripeAdapter stripeAdapter;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
        this.razorpayAdapter = new RazorpayAdapter();
        this.stripeAdapter = new StripeAdapter();
    }

    public boolean processPayment(UUID ticketId, double amount, PaymentGateway gateway) {
        PaymentGatewayAdapter adapter;
        if (gateway == PaymentGateway.RAZORPAY) {
            adapter = razorpayAdapter;
        } else {
            adapter = stripeAdapter;
        }

        boolean success = adapter.processPayment(ticketId, amount);
        PaymentStatus status = success ? PaymentStatus.COMPLETED : PaymentStatus.FAILED;

        Payment payment = new Payment(ticketId, amount, gateway, status);
        paymentRepository.save(payment);

        return success;
    }
}
