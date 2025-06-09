package parkinglot;

import parkinglot.vehicletype.VehicleType;

import java.util.List;
import java.util.Optional;


public class ParkingFloor {

    private final int floorNumber;
    private final List<ParkingSpot> parkingSpots;

    ParkingFloor(int floorNumber, List<ParkingSpot> parkingSpots) {
        this.floorNumber = floorNumber;
        this.parkingSpots = parkingSpots;
    }

    // thread safe
    public synchronized Optional<ParkingSpot> getSingleAvailableSpot(VehicleType type) {
        return parkingSpots.stream()
                .filter(spot -> spot.isAvailable() && spot.getVehicleType() == type)
                .findFirst();
    }

    public List<Integer> getAllAvailableSpots(VehicleType type) {
        return parkingSpots.stream()
                .filter(spot -> spot.isAvailable() && spot.getVehicleType() == type)
                .map(ParkingSpot::getSpotNumber)
                .toList();
    }

    public int getFloorNumber() {
        return this.floorNumber;
    }

    public List<ParkingSpot> getParkingSpots() {
        return this.parkingSpots;
    }
}