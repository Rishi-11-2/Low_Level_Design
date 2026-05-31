package model;

public class SeasonalPrice {
    private final String id;
    private final String hotelId;
    private final String roomTypeId;
    private final long dateUtc;
    private final long priceMinor;
    private final long createdAt;

    public SeasonalPrice(String id, String hotelId, String roomTypeId, long dateUtc, long priceMinor) {
        this.id = id;
        this.hotelId = hotelId;
        this.roomTypeId = roomTypeId;
        this.dateUtc = dateUtc;
        this.priceMinor = priceMinor;
        this.createdAt = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public String getHotelId() {
        return hotelId;
    }

    public String getRoomTypeId() {
        return roomTypeId;
    }

    public long getDateUtc() {
        return dateUtc;
    }

    public long getPriceMinor() {
        return priceMinor;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
