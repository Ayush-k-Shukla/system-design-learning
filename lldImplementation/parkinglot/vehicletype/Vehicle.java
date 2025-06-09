package parkinglot.vehicletype;

public abstract class Vehicle {

    protected String numberPlate;
    protected VehicleType type;

    Vehicle(String numberPlate, VehicleType type) {
        this.numberPlate = numberPlate;
        this.type = type;
    }

    public String getNumberPlate() {
        return this.numberPlate;
    }

    public VehicleType getVehicleType() {
        return this.type;
    }

}