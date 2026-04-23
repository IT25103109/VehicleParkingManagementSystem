package com.parking.vehicleparkingsystem.controller;

import com.parking.vehicleparkingsystem.model.Bike;
import com.parking.vehicleparkingsystem.model.Bus;
import com.parking.vehicleparkingsystem.model.Car;
import com.parking.vehicleparkingsystem.model.Lorry;
import com.parking.vehicleparkingsystem.model.SUV;
import com.parking.vehicleparkingsystem.model.ThreeWheeler;
import com.parking.vehicleparkingsystem.model.Van;
import com.parking.vehicleparkingsystem.model.Vehicle;
import com.parking.vehicleparkingsystem.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/vehicles")
    public String showVehicles(@RequestParam(required = false) String success,
                               @RequestParam(required = false) String error,
                               Model model) {

        model.addAttribute("vehicles", vehicleService.getAllVehicles());

        if ("added".equals(success)) {
            model.addAttribute("message", "Vehicle added successfully.");
        } else if ("updated".equals(success)) {
            model.addAttribute("message", "Vehicle updated successfully.");
        } else if ("deleted".equals(success)) {
            model.addAttribute("message", "Vehicle deleted successfully.");
        }

        if ("notfound".equals(error)) {
            model.addAttribute("errorMessage", "Vehicle not found.");
        }

        return "vehicles";
    }

    @GetMapping("/vehicles/add")
    public String showAddVehicleForm() {
        return "addVehicle";
    }

    @PostMapping("/vehicles/add")
    public String addVehicle(@RequestParam String type,
                             @RequestParam String vehicleNumber,
                             @RequestParam String ownerName,
                             @RequestParam String color,
                             Model model) {

        Vehicle vehicle = createVehicleByType(type, vehicleNumber.trim(), ownerName.trim(), color.trim());

        if (vehicle == null) {
            model.addAttribute("error", "Invalid vehicle type.");
            return "addVehicle";
        }

        boolean added = vehicleService.addVehicle(vehicle);

        if (!added) {
            model.addAttribute("error", "Vehicle already exists.");
            return "addVehicle";
        }

        return "redirect:/vehicles?success=added";
    }

    @GetMapping("/vehicles/search")
    public String showSearchPage() {
        return "searchVehicle";
    }

    @PostMapping("/vehicles/search")
    public String searchVehicle(@RequestParam String vehicleNumber, Model model) {
        Vehicle vehicle = vehicleService.searchVehicle(vehicleNumber.trim());
        model.addAttribute("searchedVehicle", vehicle);
        model.addAttribute("searchedNumber", vehicleNumber);
        return "searchVehicle";
    }

    @GetMapping("/vehicles/edit")
    public String showEditVehicleForm(@RequestParam String vehicleNumber, Model model) {
        Vehicle vehicle = vehicleService.searchVehicle(vehicleNumber);

        if (vehicle == null) {
            return "redirect:/vehicles?error=notfound";
        }

        model.addAttribute("vehicle", vehicle);
        return "editVehicle";
    }

    @PostMapping("/vehicles/update")
    public String updateVehicle(@RequestParam String vehicleNumber,
                                @RequestParam String ownerName,
                                @RequestParam String color) {

        boolean updated = vehicleService.updateVehicle(vehicleNumber, ownerName.trim(), color.trim());

        if (updated) {
            return "redirect:/vehicles?success=updated";
        } else {
            return "redirect:/vehicles?error=notfound";
        }
    }

    @GetMapping("/vehicles/delete")
    public String deleteVehicle(@RequestParam String vehicleNumber) {
        boolean deleted = vehicleService.deleteVehicle(vehicleNumber);

        if (deleted) {
            return "redirect:/vehicles?success=deleted";
        } else {
            return "redirect:/vehicles?error=notfound";
        }
    }

    @GetMapping("/vehicles/summary")
    public String showVehicleSummary(Model model) {
        model.addAttribute("summary", vehicleService.getVehicleCountSummary());
        model.addAttribute("totalVehicles", vehicleService.getAllVehicles().size());
        return "vehicleSummary";
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