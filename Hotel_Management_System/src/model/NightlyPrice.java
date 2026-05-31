package model;

public class NightlyPrice {
    private final long dateUtc;
    private final long priceMinor;

    public NightlyPrice(long dateUtc, long priceMinor) {
        this.dateUtc = dateUtc;
        this.priceMinor = priceMinor;
    }

    public long getDateUtc() {
        return dateUtc;
    }

    public long getPriceMinor() {
        return priceMinor;
    }
}
