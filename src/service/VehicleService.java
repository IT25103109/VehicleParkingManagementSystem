package service;

import model.vehicle;
import model.car;
import model.bike;
import model.van;
import model.Bus;
import model.Lorry;
import model.SUV;
import model.ThreeWheeler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

public class VehicleService {
    private ArrayList<vehicle> vehicleList = new ArrayList<>();
    private final String FILE_NAME = "vehicles.txt";

    public VehicleService() {
        loadVehiclesFromFile();
    }


    public void addVehicle(vehicle vehicle) {
        if (searchVehicle(vehicle.getVehicleNumber()) != null) {
            System.out.println("Vehicle already exists.");
            return;
        }

        vehicleList.add(vehicle);
        saveVehiclesToFile();
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
            saveVehiclesToFile();
            System.out.println("Vehicle updated successfully.");
        } else {
            System.out.println("Vehicle not found.");
        }
    }

    public void deleteVehicle(String vehicleNumber) {
        vehicle vehicle = searchVehicle(vehicleNumber);

        if (vehicle != null) {
            vehicleList.remove(vehicle);
            saveVehiclesToFile();
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

    private void saveVehiclesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (vehicle vehicle : vehicleList) {
                writer.write(vehicle.getType() + "," +
                        vehicle.getVehicleNumber() + "," +
                        vehicle.getOwnerName() + "," +
                        vehicle.getColor());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving vehicles: " + e.getMessage());
        }
    }

    private void loadVehiclesFromFile() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length == 4) {
                    String type = data[0];
                    String vehicleNumber = data[1];
                    String ownerName = data[2];
                    String color = data[3];

                    vehicle vehicle = createVehicleByType(type, vehicleNumber, ownerName, color);

                    if (vehicle != null) {
                        vehicleList.add(vehicle);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading vehicles: " + e.getMessage());
        }
    }
    private vehicle createVehicleByType(String type, String vehicleNumber, String ownerName, String color) {
        switch (type.toLowerCase()) {
            case "car":
                return new car(vehicleNumber, ownerName, color);
            case "bike":
                return new bike(vehicleNumber, ownerName, color);
            case "van":
                return new van(vehicleNumber, ownerName, color);
            case "bus":
                return new Bus(vehicleNumber, ownerName, color);
            case "lorry":
                return new Lorry(vehicleNumber, ownerName, color);
            case "threewheeler":
                return new ThreeWheeler(vehicleNumber, ownerName, color);
            case "suv":
                return new SUV(vehicleNumber, ownerName, color);
            default:
                return null;
        }
    }
}

