package model;

import model.enums.DriverStatus;
import java.time.LocalDateTime;

public class Driver {
    private int id;
    private String username;
    private String email;
    private String phoneNumber;
    private String name;
    private String licenseNumber;
    private String vehicleNumber;
    private String vehicleType;
    private DriverStatus status;
    private Location currentLocation;
    private LocalDateTime lastLocationUpdate;
    private LocalDateTime createdAt;

    public Driver(int id, String username, String email, String phoneNumber,
                  String name, String licenseNumber, String vehicleNumber, String vehicleType) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.status = DriverStatus.OFFLINE;
        this.createdAt = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public DriverStatus getStatus() { return status; }
    public void setStatus(DriverStatus status) { this.status = status; }

    public boolean isOnline() { return this.status == DriverStatus.ONLINE; }

    public Location getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
        this.lastLocationUpdate = LocalDateTime.now();
    }

    public LocalDateTime getLastLocationUpdate() { return lastLocationUpdate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Driver{id=" + id + ", name='" + name + "', vehicle='" + vehicleNumber +
               "', status=" + status + "}";
    }
}
