package controller;

import model.VehicleType;
import service.AdminService;
import java.util.Map;

public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    public void addFloor(int floorNumber) {
        System.out.println("\n>>> [AdminController] Request to add Floor: " + floorNumber);
        adminService.addFloor(floorNumber);
        System.out.println("<<< [AdminController] Floor " + floorNumber + " added successfully.");
    }

    public void addSlot(int floorNumber, VehicleType slotType) {
        System.out.println("\n>>> [AdminController] Request to add " + slotType + " slot on Floor: " + floorNumber);
        adminService.addSlot(floorNumber, slotType);
        System.out.println("<<< [AdminController] " + slotType + " slot added on Floor " + floorNumber + ".");
    }

    public void updatePricing(VehicleType vehicleType, double ratePerHour, double flatRate) {
        System.out.println("\n>>> [AdminController] Updating pricing rules for: " + vehicleType + " (Flat: $" + flatRate + ", Hourly: $" + ratePerHour + ")");
        adminService.updatePricing(vehicleType, ratePerHour, flatRate);
        System.out.println("<<< [AdminController] Pricing rule updated successfully.");
    }

    public void displayParkingLotStatus() {
        System.out.println("\n================ PARKING LOT SYSTEM STATUS ================");
        Map<String, Object> status = adminService.getParkingLotStatus();
        System.out.println("Total Floors:         " + status.get("totalFloors"));
        System.out.println("Total Slots:          " + status.get("totalSlots"));
        System.out.println("Occupied Slots:       " + status.get("occupiedSlots"));
        System.out.println("Available Slots:      " + status.get("availableSlots"));
        System.out.println("Available by Type:    " + status.get("availableSlotsByType"));
        System.out.println("===========================================================");
    }
}
