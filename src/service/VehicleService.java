package service;

import model.vehicle;
import java.util.ArrayList;

public class VehicleService {
    private ArrayList<vehicle> vehicleList = new ArrayList<>();

    public void addVehicle(vehicle vehicle) {
        if (searchVehicle(vehicle.getVehicleNumber()) != null) {
            System.out.println("Vehicle already exists.");
            return;
        }

        vehicleList.add(vehicle);
        System.out.println("Vehicle added successfully.");
    }

    public vehicle searchVehicle(String vehicleNumber) {
        for (vehicle vehicle : vehicleList) {
            if (vehicle.getVehicleNumber().equalsIgnoreCase(vehicleNumber)) {
                return vehicle;
            }
        }
        return null;
    }

    public void updateVehicle(String vehicleNumber, String newOwnerName, String newColor) {
        vehicle vehicle = searchVehicle(vehicleNumber);

        if (vehicle != null) {
            vehicle.setOwnerName(newOwnerName);
            vehicle.setColor(newColor);
            System.out.println("Vehicle updated successfully.");
        } else {
            System.out.println("Vehicle not found.");
        }
    }

    public void deleteVehicle(String vehicleNumber) {
        vehicle vehicle = searchVehicle(vehicleNumber);

        if (vehicle != null) {
            vehicleList.remove(vehicle);
            System.out.println("Vehicle deleted successfully.");
        } else {
            System.out.println("Vehicle not found.");
        }
    }

    public void displayAllVehicles() {
        if (vehicleList.isEmpty()) {
            System.out.println("No vehicles available.");
            return;
        }

        System.out.println("\n--- Vehicle List ---");
        for (vehicle vehicle : vehicleList) {
            System.out.println(vehicle);
        }
    }
}