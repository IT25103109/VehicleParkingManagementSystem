package app;

import model.SUV;
import model.Bike;
import model.Bus;
import model.Car;
import model.Lorry;
import model.ThreeWheeler;
import model.Van;
import model.Vehicle;
import service.VehicleService;

import java.util.List;
import java.util.Map;
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
            System.out.println("3. Search Vehicle by Number");
            System.out.println("4. Update Vehicle");
            System.out.println("5. Delete Vehicle");
            System.out.println("6. Display Vehicles by Type");
            System.out.println("7. Display Vehicle Count Summary");
            System.out.println("8. Exit");
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
                    Vehicle newVehicle = null;

                    while (true) {
                        System.out.print("Enter vehicle type (Car/Bike/Van/Bus/Lorry/ThreeWheeler/SUV): ");
                        type = input.nextLine().trim();

                        if (type.equalsIgnoreCase("Car") ||
                                type.equalsIgnoreCase("Bike") ||
                                type.equalsIgnoreCase("Van") ||
                                type.equalsIgnoreCase("Bus") ||
                                type.equalsIgnoreCase("Lorry") ||
                                type.equalsIgnoreCase("ThreeWheeler") ||
                                type.equalsIgnoreCase("SUV")) {
                            break;
                        } else {
                            System.out.println("Invalid vehicle type. Please enter a valid type.");
                        }
                    }

                    String vehicleNumber;
                    do {
                        System.out.print("Enter vehicle number: ");
                        vehicleNumber = input.nextLine().trim();
                        if (vehicleNumber.isEmpty()) {
                            System.out.println("Vehicle number cannot be empty.");
                        }
                    } while (vehicleNumber.isEmpty());

                    String ownerName;
                    do {
                        System.out.print("Enter owner name: ");
                        ownerName = input.nextLine().trim();
                        if (ownerName.isEmpty()) {
                            System.out.println("Owner name cannot be empty.");
                        }
                    } while (ownerName.isEmpty());

                    String color;
                    do {
                        System.out.print("Enter color: ");
                        color = input.nextLine().trim();
                        if (color.isEmpty()) {
                            System.out.println("Color cannot be empty.");
                        }
                    } while (color.isEmpty());

                    if (type.equalsIgnoreCase("Car")) {
                        newVehicle = new Car(vehicleNumber, ownerName, color);
                    } else if (type.equalsIgnoreCase("SUV")) {
                        newVehicle = new SUV(vehicleNumber, ownerName, color);
                    } else if (type.equalsIgnoreCase("Bike")) {
                        newVehicle = new Bike(vehicleNumber, ownerName, color);
                    } else if (type.equalsIgnoreCase("Van")) {
                        newVehicle = new Van(vehicleNumber, ownerName, color);
                    } else if (type.equalsIgnoreCase("Bus")) {
                        newVehicle = new Bus(vehicleNumber, ownerName, color);
                    } else if (type.equalsIgnoreCase("Lorry")) {
                        newVehicle = new Lorry(vehicleNumber, ownerName, color);
                    } else if (type.equalsIgnoreCase("ThreeWheeler")) {
                        newVehicle = new ThreeWheeler(vehicleNumber, ownerName, color);
                    }

                    boolean added = vehicleService.addVehicle(newVehicle);
                    if (added) {
                        System.out.println("Vehicle added successfully.");
                    } else {
                        System.out.println("Vehicle already exists.");
                    }
                    break;

                case 2:
                    List<Vehicle> allVehicles = vehicleService.getAllVehicles();

                    if (allVehicles.isEmpty()) {
                        System.out.println("No vehicles available.");
                    } else {
                        System.out.println("\n--- Vehicle List ---");
                        for (Vehicle v : allVehicles) {
                            System.out.println(v);
                        }
                    }
                    break;

                case 3:
                    while (true) {
                        System.out.print("Enter vehicle number to search: ");
                        String searchNumber = input.nextLine().trim();

                        Vehicle foundVehicle = vehicleService.searchVehicle(searchNumber);

                        if (foundVehicle != null) {
                            System.out.println("\nVehicle found:");
                            System.out.println(foundVehicle);
                            break;
                        } else {
                            System.out.println("Vehicle not found.");
                            System.out.print("Do you want to try again? (yes/no): ");
                            String tryAgain = input.nextLine().trim();

                            if (!tryAgain.equalsIgnoreCase("yes")) {
                                break;
                            }
                        }
                    }
                    break;

                case 4:
                    String updateNumber;
                    do {
                        System.out.print("Enter vehicle number to update: ");
                        updateNumber = input.nextLine().trim();
                        if (updateNumber.isEmpty()) {
                            System.out.println("Vehicle number cannot be empty.");
                        }
                    } while (updateNumber.isEmpty());

                    String newOwner;
                    do {
                        System.out.print("Enter new owner name: ");
                        newOwner = input.nextLine().trim();
                        if (newOwner.isEmpty()) {
                            System.out.println("Owner name cannot be empty.");
                        }
                    } while (newOwner.isEmpty());

                    String newColor;
                    do {
                        System.out.print("Enter new color: ");
                        newColor = input.nextLine().trim();
                        if (newColor.isEmpty()) {
                            System.out.println("Color cannot be empty.");
                        }
                    } while (newColor.isEmpty());

                    boolean updated = vehicleService.updateVehicle(updateNumber, newOwner, newColor);
                    if (updated) {
                        System.out.println("Vehicle updated successfully.");
                    } else {
                        System.out.println("Vehicle not found.");
                    }
                    break;

                case 5:
                    String deleteNumber;
                    do {
                        System.out.print("Enter vehicle number to delete: ");
                        deleteNumber = input.nextLine().trim();
                        if (deleteNumber.isEmpty()) {
                            System.out.println("Vehicle number cannot be empty.");
                        }
                    } while (deleteNumber.isEmpty());

                    boolean deleted = vehicleService.deleteVehicle(deleteNumber);
                    if (deleted) {
                        System.out.println("Vehicle deleted successfully.");
                    } else {
                        System.out.println("Vehicle not found.");
                    }
                    break;

                case 6:
                    System.out.print("Enter vehicle type to display: ");
                    String typeToDisplay = input.nextLine().trim();

                    List<Vehicle> filteredVehicles = vehicleService.getVehiclesByType(typeToDisplay);

                    if (filteredVehicles.isEmpty()) {
                        System.out.println("No vehicles found for this type.");
                    } else {
                        System.out.println("\n--- Vehicles of type: " + typeToDisplay + " ---");
                        for (Vehicle v : filteredVehicles) {
                            System.out.println(v);
                        }
                    }
                    break;

                case 7:
                    Map<String, Integer> summary = vehicleService.getVehicleCountSummary();

                    if (summary.isEmpty()) {
                        System.out.println("No vehicles available.");
                    } else {
                        System.out.println("\n--- Vehicle Count Summary ---");
                        int total = 0;
                        for (Map.Entry<String, Integer> entry : summary.entrySet()) {
                            System.out.println(entry.getKey() + ": " + entry.getValue());
                            total += entry.getValue();
                        }
                        System.out.println("Total Vehicles: " + total);
                    }
                    break;

                case 8:
                    System.out.println("Exiting system...");
                    break;

                default:
                    System.out.println("Invalid choice. Please select between 1 and 8.");
            }

        } while (choice != 8);

        input.close();
    }
}