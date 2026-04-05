package controller;

import model.enums.PaymentStatus;
import service.PaymentService;

/**
 * REST Controller for payment callback handling.
 * Endpoint:
 *   POST /api/payments/callback
 */
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // POST /api/payments/callback
    public void handlePaymentCallback(String transactionId, PaymentStatus status) {
        System.out.println("[API] POST /api/payments/callback (txn: " + transactionId +
                           ", status: " + status + ")");
        paymentService.handlePaymentCallback(transactionId, status);
    }
}
