package service;

import model.Booking;
import model.CancellationPolicy;
import model.RefundDecision;
import repository.CancellationPolicyRepository;

public class PolicyService {
    private final CancellationPolicyRepository cancellationPolicyRepository;

    public PolicyService(CancellationPolicyRepository cancellationPolicyRepository) {
        this.cancellationPolicyRepository = cancellationPolicyRepository;
    }

    public CancellationPolicy getPolicy(String policyId) {
        if (policyId == null) {
            // Default FLEX policy: 100% refund, no cutoff hours
            return new CancellationPolicy("FLEX-DEFAULT", "FLEX", 100, 0);
        }
        return cancellationPolicyRepository.findById(policyId)
                .orElse(new CancellationPolicy("FLEX-DEFAULT", "FLEX", 100, 0));
    }

    public RefundDecision evaluateCancellation(Booking booking, CancellationPolicy policy, long nowUtc) {
        String name = policy.getName();
        if ("NON_REFUNDABLE".equalsIgnoreCase(name)) {
            return new RefundDecision(false, 0L, "Non-refundable policy applies.");
        }

        if ("FLEX".equalsIgnoreCase(name)) {
            return new RefundDecision(true, booking.getTotalAmountMinor(), "Fully refundable flex policy applied.");
        }

        // PARTIAL policy
        long checkInTime = booking.getCheckInDateUtc();
        double hoursLeft = (double) (checkInTime - nowUtc) / (1000 * 60 * 60);

        if (hoursLeft >= policy.getCutoffHoursBeforeCheckIn()) {
            long refundAmt = (long) (booking.getTotalAmountMinor() * (policy.getRefundPercent() / 100.0));
            return new RefundDecision(true, refundAmt, "Partial refund of " + policy.getRefundPercent() + "% applied. Cutoff threshold satisfied.");
        } else {
            return new RefundDecision(false, 0L, "No refund. Cancellation request was within the " + policy.getCutoffHoursBeforeCheckIn() + "-hour non-refundable cutoff window.");
        }
    }
}
