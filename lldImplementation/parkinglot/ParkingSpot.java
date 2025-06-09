package parkinglot;


import parkinglot.vehicletype.Vehicle;
import parkinglot.vehicletype.VehicleType;

public class ParkingSpot {
    private final int spotNumber;
    private final VehicleType vehicleType;
    private Vehicle vehicle;
    private boolean isFilled;

    public ParkingSpot(int spotNumber, VehicleType type) {
        this.spotNumber = spotNumber;
        this.vehicleType = type;
        this.isFilled = false;
    }

    public synchronized boolean isAvailable() {
        return !isFilled;
    }

    public synchronized boolean park(Vehicle vehicle) {
        if (isFilled || vehicle.getVehicleType() != vehicleType) {
            return false;
        }

        this.vehicle = vehicle;
        this.isFilled = true;
        return true;
    }

    public synchronized void unpark() {
        vehicle = null;
        isFilled = false;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public int getSpotNumber() {
        return spotNumber;
    }
}