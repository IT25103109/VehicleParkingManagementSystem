package app;


import model.car;
import model.bike;
import model.van;
import service.VehicleService;

public class Main {
    public static void main(String[] args) {
        VehicleService vehicleService = new VehicleService();

        vehicleService.addVehicle(new car("CAR-101", "Nethul", "Black"));
        vehicleService.addVehicle(new bike("BIKE-202", "Amal", "Red"));
        vehicleService.addVehicle(new van("VAN-303", "Kamal", "White"));

        vehicleService.displayAllVehicles();
    }
}

