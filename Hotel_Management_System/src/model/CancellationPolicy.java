package model;

public class CancellationPolicy {
    private final String id;
    private final String name; // NON_REFUNDABLE, PARTIAL, FLEX
    private final int refundPercent; // 0 to 100
    private final int cutoffHoursBeforeCheckIn;
    private final long createdAt;

    public CancellationPolicy(String id, String name, int refundPercent, int cutoffHoursBeforeCheckIn) {
        this.id = id;
        this.name = name;
        this.refundPercent = refundPercent;
        this.cutoffHoursBeforeCheckIn = cutoffHoursBeforeCheckIn;
        this.createdAt = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRefundPercent() {
        return refundPercent;
    }

    public int getCutoffHoursBeforeCheckIn() {
        return cutoffHoursBeforeCheckIn;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
