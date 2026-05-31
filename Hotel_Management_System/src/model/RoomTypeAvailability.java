package model;

import java.util.List;

public class RoomTypeAvailability {
    private final String roomTypeId;
    private final String roomTypeName;
    private final int capacity;
    private final String bedType;
    private final List<String> amenities;
    private final boolean available;
    private final long totalPrice;
    private final double averagePricePerNight;
    private final List<NightlyPrice> nightlyPrices;

    public RoomTypeAvailability(String roomTypeId, String roomTypeName, int capacity, String bedType,
                                List<String> amenities, boolean available, long totalPrice,
                                double averagePricePerNight, List<NightlyPrice> nightlyPrices) {
        this.roomTypeId = roomTypeId;
        this.roomTypeName = roomTypeName;
        this.capacity = capacity;
        this.bedType = bedType;
        this.amenities = amenities;
        this.available = available;
        this.totalPrice = totalPrice;
        this.averagePricePerNight = averagePricePerNight;
        this.nightlyPrices = nightlyPrices;
    }

    public String getRoomTypeId() {
        return roomTypeId;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getBedType() {
        return bedType;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public boolean isAvailable() {
        return available;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public double getAveragePricePerNight() {
        return averagePricePerNight;
    }

    public List<NightlyPrice> getNightlyPrices() {
        return nightlyPrices;
    }
}
