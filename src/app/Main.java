package app;

import model.bike;
import model.car;
import model.van;
import model.vehicle;
import service.VehicleService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        VehicleService vehicleService = new VehicleService();

        int choice;

        do {
            System.out.println("\n===== Vehicle Parking Management System =====");
            System.out.println("1. Add Vehicle");
            System.out.println("2. Display All Vehicles");
            System.out.println("3. Search Vehicle");
            System.out.println("4. Delete Vehicle");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            choice = input.nextInt();
            input.nextLine(); // clear newline

            switch (choice) {
                case 1:
                    System.out.print("Enter vehicle type (Car/Bike/Van): ");
                    String type = input.nextLine();

                    System.out.print("Enter vehicle number: ");
                    String vehicleNumber = input.nextLine();

                    System.out.print("Enter owner name: ");
                    String ownerName = input.nextLine();

                    System.out.print("Enter color: ");
                    String color = input.nextLine();

                    vehicle vehicle = null;

                    if (type.equalsIgnoreCase("Car")) {
                        vehicle = new car(vehicleNumber, ownerName, color);
                    } else if (type.equalsIgnoreCase("Bike")) {
                        vehicle = new bike(vehicleNumber, ownerName, color);
                    } else if (type.equalsIgnoreCase("Van")) {
                        vehicle = new van(vehicleNumber, ownerName, color);
                    } else {
                        System.out.println("Invalid vehicle type.");
                    }

                    if (vehicle != null) {
                        vehicleService.addVehicle(vehicle);
                    }
                    break;

                case 2:
                    vehicleService.displayAllVehicles();
                    break;

                case 3:
                    System.out.print("Enter vehicle number to search: ");
                    String searchNumber = input.nextLine();

                    vehicle foundVehicle = vehicleService.searchVehicle(searchNumber);

                    if (foundVehicle != null) {
                        System.out.println("Vehicle found:");
                        System.out.println(foundVehicle);
                    } else {
                        System.out.println("Vehicle not found.");
                    }
                    break;

                case 4:
                    System.out.print("Enter vehicle number to delete: ");
                    String deleteNumber = input.nextLine();
                    vehicleService.deleteVehicle(deleteNumber);
                    break;

                case 5:
                    System.out.println("Exiting system...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 5);

        input.close();
    }
}