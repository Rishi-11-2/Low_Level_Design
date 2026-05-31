package model;

import java.util.List;

public class RoomType {
    private final String id;
    private final String hotelId;
    private String name;
    private int capacity;
    private String bedType;
    private long basePriceMinor;
    private List<String> amenities;
    private int totalRooms;
    private boolean isActive;
    private final long createdAt;

    public RoomType(String id, String hotelId, String name, int capacity, String bedType,
                    long basePriceMinor, List<String> amenities, int totalRooms) {
        this.id = id;
        this.hotelId = hotelId;
        this.name = name;
        this.capacity = capacity;
        this.bedType = bedType;
        this.basePriceMinor = basePriceMinor;
        this.amenities = amenities;
        this.totalRooms = totalRooms;
        this.isActive = true;
        this.createdAt = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public String getHotelId() {
        return hotelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getBedType() {
        return bedType;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public long getBasePriceMinor() {
        return basePriceMinor;
    }

    public void setBasePriceMinor(long basePriceMinor) {
        this.basePriceMinor = basePriceMinor;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public int getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
