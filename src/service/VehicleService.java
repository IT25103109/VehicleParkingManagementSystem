package service;


import model.vehicle;
import java.util.ArrayList;

public class VehicleService {
    private ArrayList<vehicle> vehicleList = new ArrayList<>();

    public void addVehicle(vehicle vehicle) {
        vehicleList.add(vehicle);
        System.out.println("Vehicle added successfully.");
    }

    public void displayAllVehicles() {
        for (vehicle vehicle : vehicleList) {
            System.out.println(vehicle);
        }
    }
}

