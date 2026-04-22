package com.parking.vehicleparkingsystem.model;

public class Vehicle {



        private String vehicleNumber;
        private String ownerName;
        private String color;
        private String type;

        public Vehicle(String vehicleNumber, String ownerName, String color, String type) {
            this.vehicleNumber = vehicleNumber;
            this.ownerName = ownerName;
            this.color = color;
            this.type = type;
        }

        public String getVehicleNumber() {
            return vehicleNumber;
        }

        public void setVehicleNumber(String vehicleNumber) {
            this.vehicleNumber = vehicleNumber;
        }

        public String getOwnerName() {
            return ownerName;
        }

        public void setOwnerName(String ownerName) {
            this.ownerName = ownerName;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return "Vehicle Number: " + vehicleNumber +
                    ", Owner Name: " + ownerName +
                    ", Color: " + color +
                    ", Type: " + type;
        }
    }

