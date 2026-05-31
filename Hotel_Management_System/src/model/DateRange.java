package model;

public class DateRange {
    private final long startDateUtc;
    private final long endDateUtc;

    public DateRange(long startDateUtc, long endDateUtc) {
        if (startDateUtc >= endDateUtc) {
            throw new IllegalArgumentException("Check-in date must be before check-out date.");
        }
        this.startDateUtc = startDateUtc;
        this.endDateUtc = endDateUtc;
    }

    public long getStartDateUtc() {
        return startDateUtc;
    }

    public long getEndDateUtc() {
        return endDateUtc;
    }

    public int getNumberOfNights() {
        // Simple day difference calculation assuming timestamp is in milliseconds
        long diff = endDateUtc - startDateUtc;
        return (int) (diff / (24 * 60 * 60 * 1000));
    }
}
