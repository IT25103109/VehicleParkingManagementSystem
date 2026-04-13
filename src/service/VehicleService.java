package service;

import model.vehicle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    public void displayVehiclesByType(String type) {
        boolean found = false;

        System.out.println("\n--- Vehicles of type: " + type + " ---");
        for (vehicle vehicle : vehicleList) {
            if (vehicle.getType().equalsIgnoreCase(type)) {
                System.out.println(vehicle);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No vehicles found for this type.");
        }
    }

    public void displayVehicleCountSummary() {
        if (vehicleList.isEmpty()) {
            System.out.println("No vehicles available.");
            return;
        }

        Map<String, Integer> countMap = new HashMap<>();

        for (vehicle vehicle : vehicleList) {
            String type = vehicle.getType();
            countMap.put(type, countMap.getOrDefault(type, 0) + 1);
        }

        System.out.println("\n--- Vehicle Count Summary ---");
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("Total Vehicles: " + vehicleList.size());
    }
}