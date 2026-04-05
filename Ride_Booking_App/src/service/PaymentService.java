package service;

import dto.NotificationMessage;
import model.Ride;
import model.enums.PaymentStatus;
import model.enums.RideStatus;
import repository.RideRepository;
import strategy.payment.PaymentGatewayRouter;

import java.util.HashMap;

/**
 * PaymentService handles payment initiation and callback processing.
 * Uses PaymentGatewayRouter for provider selection.
 */
public class PaymentService {

    private final PaymentGatewayRouter paymentGatewayRouter;
    private final RideRepository rideRepository;
    private final MatchingService matchingService;
    private final NotificationService notificationService;

    public PaymentService(PaymentGatewayRouter paymentGatewayRouter,
                          RideRepository rideRepository,
                          MatchingService matchingService,
                          NotificationService notificationService) {
        this.paymentGatewayRouter = paymentGatewayRouter;
        this.rideRepository = rideRepository;
        this.matchingService = matchingService;
        this.notificationService = notificationService;
    }

    public String initiatePayment(String rideId, long amount) {
        String transactionId = paymentGatewayRouter.initiatePayment(rideId, amount, new HashMap<>());
        System.out.println("[Payment] Initiated payment for ride " + rideId +
                           ", txnId: " + transactionId);
        return transactionId;
    }

    public void handlePaymentCallback(String transactionId, PaymentStatus status) {
        Ride ride = rideRepository.findByPaymentId(transactionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No ride found for payment transaction: " + transactionId));

        if (status == PaymentStatus.COMPLETED) {
            ride.setPaymentStatus(PaymentStatus.COMPLETED);
            rideRepository.save(ride);

            System.out.println("[Payment] Payment completed for ride " + ride.getRideId());

            // Start async driver matching after successful payment
            matchingService.matchDriverAsync(ride);

            notificationService.sendToRider(ride.getRiderId(),
                    new NotificationMessage("PAYMENT_SUCCESS", "Payment Confirmed",
                            "Your payment has been confirmed. Finding you a driver...",
                            ride.getRideId()));

        } else if (status == PaymentStatus.FAILED) {
            ride.setPaymentStatus(PaymentStatus.FAILED);
            ride.transitionTo(RideStatus.CANCELLED);
            ride.setCancellationReason("Payment failed");
            rideRepository.save(ride);

            System.out.println("[Payment] Payment failed for ride " + ride.getRideId());

            notificationService.sendToRider(ride.getRiderId(),
                    new NotificationMessage("PAYMENT_FAILED", "Payment Failed",
                            "Your payment has failed. Ride has been cancelled.",
                            ride.getRideId()));
        }
    }
}
