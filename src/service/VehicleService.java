package service;

import model.*;
import model.Vehicle;
import model.Car;
import model.Bike;
import model.Van;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;

public class VehicleService {
    private ArrayList<Vehicle> vehicleList = new ArrayList<>();
    private final String FILE_NAME = "vehicles.txt";

    public VehicleService() {
        loadVehiclesFromFile();
    }

    public boolean addVehicle(Vehicle vehicle) {
        if (searchVehicle(vehicle.getVehicleNumber()) != null) {
            return false;
        }

        vehicleList.add(vehicle);
        saveVehiclesToFile();
        return true;
    }

    public Vehicle searchVehicle(String vehicleNumber) {
        for (Vehicle vehicle : vehicleList) {
            if (vehicle.getVehicleNumber().equalsIgnoreCase(vehicleNumber)) {
                return vehicle;
            }
        }
        return null;
    }

    public boolean updateVehicle(String vehicleNumber, String newOwnerName, String newColor) {
        Vehicle vehicle = searchVehicle(vehicleNumber);

        if (vehicle != null) {
            vehicle.setOwnerName(newOwnerName);
            vehicle.setColor(newColor);
            saveVehiclesToFile();
            return true;
        }

        return false;
    }

    public boolean deleteVehicle(String vehicleNumber) {
        Vehicle vehicle = searchVehicle(vehicleNumber);

        if (vehicle != null) {
            vehicleList.remove(vehicle);
            saveVehiclesToFile();
            return true;
        }

        return false;
    }

    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>(vehicleList);
    }

    public List<Vehicle> getVehiclesByType(String type) {
        List<Vehicle> filteredVehicles = new ArrayList<>();

        for (Vehicle vehicle : vehicleList) {
            if (vehicle.getType().equalsIgnoreCase(type)) {
                filteredVehicles.add(vehicle);
            }
        }

        return filteredVehicles;
    }

    public Map<String, Integer> getVehicleCountSummary() {
        Map<String, Integer> countMap = new HashMap<>();

        for (Vehicle vehicle : vehicleList) {
            String type = vehicle.getType();
            countMap.put(type, countMap.getOrDefault(type, 0) + 1);
        }

        return countMap;
    }

    private void saveVehiclesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Vehicle vehicle : vehicleList) {
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

                    Vehicle vehicle = createVehicleByType(type, vehicleNumber, ownerName, color);

                    if (vehicle != null) {
                        vehicleList.add(vehicle);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading vehicles: " + e.getMessage());
        }
    }

    private Vehicle createVehicleByType(String type, String vehicleNumber, String ownerName, String color) {
        switch (type.toLowerCase()) {
            case "car":
                return new Car(vehicleNumber, ownerName, color);
            case "bike":
                return new Bike(vehicleNumber, ownerName, color);
            case "van":
                return new Van(vehicleNumber, ownerName, color);
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