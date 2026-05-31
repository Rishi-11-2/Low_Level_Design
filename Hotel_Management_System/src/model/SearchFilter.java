package model;

public class SearchFilter {
    private final String city;
    private final String country;
    private final DateRange dateRange;
    private final long minPriceMinor;
    private final long maxPriceMinor;

    public SearchFilter(String city, String country, DateRange dateRange, long minPriceMinor, long maxPriceMinor) {
        this.city = city;
        this.country = country;
        this.dateRange = dateRange;
        this.minPriceMinor = minPriceMinor;
        this.maxPriceMinor = maxPriceMinor;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public long getMinPriceMinor() {
        return minPriceMinor;
    }

    public long getMaxPriceMinor() {
        return maxPriceMinor;
    }
}
