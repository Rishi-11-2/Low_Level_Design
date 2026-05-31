package model;

public class Hotel {
    private final String id;
    private String name;
    private String address;
    private String city;
    private String country;
    private double latitude;
    private double longitude;
    private double rating;
    private boolean isActive;
    private int defaultOverbookPercent;
    private String cancellationPolicyId;
    private final long createdAt;

    public Hotel(String id, String name, String address, String city, String country,
                 double latitude, double longitude, double rating) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.isActive = true;
        this.defaultOverbookPercent = 10; // 10% overbooking default limit
        this.cancellationPolicyId = null;
        this.createdAt = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getDefaultOverbookPercent() {
        return defaultOverbookPercent;
    }

    public void setDefaultOverbookPercent(int defaultOverbookPercent) {
        this.defaultOverbookPercent = defaultOverbookPercent;
    }

    public String getCancellationPolicyId() {
        return cancellationPolicyId;
    }

    public void setCancellationPolicyId(String cancellationPolicyId) {
        this.cancellationPolicyId = cancellationPolicyId;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
