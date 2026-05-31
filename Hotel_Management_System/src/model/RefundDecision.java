package model;

public class RefundDecision {
    private final boolean refundable;
    private final long refundAmountMinor;
    private final String message;

    public RefundDecision(boolean refundable, long refundAmountMinor, String message) {
        this.refundable = refundable;
        this.refundAmountMinor = refundAmountMinor;
        this.message = message;
    }

    public boolean isRefundable() {
        return refundable;
    }

    public long getRefundAmountMinor() {
        return refundAmountMinor;
    }

    public String getMessage() {
        return message;
    }
}
