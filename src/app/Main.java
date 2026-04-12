package app;

import model.SUV;
import model.bike;
import model.Bus;
import model.car;
import model.Lorry;
import model.ThreeWheeler;
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
            System.out.println("4. Update Vehicle");
            System.out.println("5. Delete Vehicle");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            while (!input.hasNextInt()) {
                System.out.print("Please enter a valid number: ");
                input.next();
            }

            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    String type;
                    vehicle vehicle = null;

                    while (true) {
                        System.out.print("Enter vehicle type (Car/Bike/Van/Bus/Lorry/ThreeWheeler/SUV): ");
                        type = input.nextLine().trim();

                        if (type.equalsIgnoreCase("Car") ||
                                type.equalsIgnoreCase("Bike") ||
                                type.equalsIgnoreCase("Van") ||
                                type.equalsIgnoreCase("Bus") ||
                                type.equalsIgnoreCase("Lorry") ||
                                type.equalsIgnoreCase("ThreeWheeler")||
                                type.equalsIgnoreCase("SUV")) {

                            break;
                        } else {
                            System.out.println("Invalid vehicle type. Please enter a valid type.");
                        }
                    }

                    System.out.print("Enter vehicle number: ");
                    String vehicleNumber = input.nextLine();

                    System.out.print("Enter owner name: ");
                    String ownerName = input.nextLine();

                    System.out.print("Enter color: ");
                    String color = input.nextLine();

                    if (type.equalsIgnoreCase("Car")) {
                        vehicle = new car(vehicleNumber, ownerName, color);
                    } else if (type.equalsIgnoreCase("SUV")) {
                        vehicle = new SUV(vehicleNumber, ownerName, color);
                    } else if (type.equalsIgnoreCase("Bike")) {
                        vehicle = new bike(vehicleNumber, ownerName, color);
                    } else if (type.equalsIgnoreCase("Van")) {
                        vehicle = new van(vehicleNumber, ownerName, color);
                    } else if (type.equalsIgnoreCase("Bus")) {
                        vehicle = new Bus(vehicleNumber, ownerName, color);
                    } else if (type.equalsIgnoreCase("Lorry")) {
                        vehicle = new Lorry(vehicleNumber, ownerName, color);
                    } else if (type.equalsIgnoreCase("ThreeWheeler")) {
                        vehicle = new ThreeWheeler(vehicleNumber, ownerName, color);
                    }

                    vehicleService.addVehicle(vehicle);
                    break;

                case 2:
                    vehicleService.displayAllVehicles();
                    break;

                case 3:
                    System.out.print("Enter vehicle number to search: ");
                    String searchNumber = input.nextLine();

                    vehicle foundVehicle = vehicleService.searchVehicle(searchNumber);

                    if (foundVehicle != null) {
                        System.out.println("\nVehicle found:");
                        System.out.println(foundVehicle);
                    } else {
                        System.out.println("Vehicle not found.");
                    }
                    break;

                case 4:
                    System.out.print("Enter vehicle number to update: ");
                    String updateNumber = input.nextLine();

                    System.out.print("Enter new owner name: ");
                    String newOwner = input.nextLine();

                    System.out.print("Enter new color: ");
                    String newColor = input.nextLine();

                    vehicleService.updateVehicle(updateNumber, newOwner, newColor);
                    break;

                case 5:
                    System.out.print("Enter vehicle number to delete: ");
                    String deleteNumber = input.nextLine();

                    vehicleService.deleteVehicle(deleteNumber);
                    break;

                case 6:
                    System.out.println("Exiting system...");
                    break;
                    default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 6);

        input.close();
    }
}