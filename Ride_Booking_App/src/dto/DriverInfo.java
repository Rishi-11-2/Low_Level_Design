package dto;

import model.Location;

public class DriverInfo {
    private int id;
    private String name;
    private String phoneNumber;
    private String vehicleNumber;
    private Location currentLocation;
    private long eta;

    public DriverInfo(int id, String name, String phoneNumber,
                      String vehicleNumber, Location currentLocation, long eta) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.vehicleNumber = vehicleNumber;
        this.currentLocation = currentLocation;
        this.eta = eta;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getVehicleNumber() { return vehicleNumber; }
    public Location getCurrentLocation() { return currentLocation; }
    public long getEta() { return eta; }

    @Override
    public String toString() {
        return "DriverInfo{id=" + id + ", name='" + name + "', vehicle='" + vehicleNumber + "'}";
    }
}
