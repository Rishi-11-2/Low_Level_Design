package model;

import java.time.LocalDateTime;

public class Location {
    private double latitude;
    private double longitude;
    private String address;
    private LocalDateTime timestamp;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = LocalDateTime.now();
    }

    public Location(double latitude, double longitude, String address) {
        this(latitude, longitude);
        this.address = address;
    }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "Location{lat=" + latitude + ", lon=" + longitude +
               (address != null ? ", address='" + address + "'" : "") + "}";
    }
}
