package parkinglot;

import parkinglot.fee.CustomisedRateFeeStrategy;
import parkinglot.vehicletype.Bike;
import parkinglot.vehicletype.Car;
import parkinglot.vehicletype.Vehicle;
import parkinglot.vehicletype.VehicleType;

import java.util.ArrayList;
import java.util.List;

public class ParkingLotDemo {
    public static void run() {

        System.out.println("Running parking lot...");

        // Singleton
        ParkingLot parkingLot = ParkingLot.getInstance();

        // Parking Spots
        List<ParkingSpot> floor1ParkingSpots = List.of(
                new ParkingSpot(101, VehicleType.CAR),
                new ParkingSpot(102, VehicleType.BIKE),
                new ParkingSpot(103,VehicleType.CAR)
        );
        List<ParkingSpot> floor2ParkingSpots = List.of(
                new ParkingSpot(201, VehicleType.CAR),
                new ParkingSpot(203,VehicleType.CAR)
        );

        // Floors
        ParkingFloor floor1 = new ParkingFloor(1, floor1ParkingSpots);
        ParkingFloor floor2 = new ParkingFloor(2, floor2ParkingSpots);
        parkingLot.addFloor(floor1);
        parkingLot.addFloor(floor2);

        // Set fee
        parkingLot.setFeeStrategy(new CustomisedRateFeeStrategy());

        // Create Vehicles
        Vehicle myCar1 = new Car("APC23213");
        Vehicle myCar2 = new Car("APC243213");
        Vehicle myBike1 = new Bike("UP33242");
        Vehicle myBike2 = new Bike("UP33242");


        System.out.println("Printing Available free Spots For Car...");
        for (ParkingFloor floor : parkingLot.getAllFloors()){
            System.out.println("Floor: "+floor.getFloorNumber()+" "+floor.getAllAvailableSpots(VehicleType.CAR));
        }

        System.out.println("Printing Available free Spots For Bike...");
        for (ParkingFloor floor : parkingLot.getAllFloors()){
            System.out.println("Floor: "+floor.getFloorNumber()+" "+floor.getAllAvailableSpots(VehicleType.BIKE));
        }


        List<String> parkingTickets = new ArrayList<>();

        try {

            Ticket t1 = parkingLot.parkVehicle(myBike1);
            System.out.println("Bike 1 parked "+ t1.getTicketId());
            parkingTickets.add(t1.getTicketId());

            Ticket t2 = parkingLot.parkVehicle(myCar1);
            System.out.println("Car 1 parked "+ t1.getTicketId());
            parkingTickets.add(t2.getTicketId());

            Ticket t3 = parkingLot.parkVehicle(myCar2);
            System.out.println("Car 2 parked "+ t1.getTicketId());
            parkingTickets.add(t3.getTicketId());

            // try to park once all Bike spots are fill
            Ticket t4 = parkingLot.parkVehicle(myBike2);
        } catch (Exception e) {
            System.out.println("Exception "+ e.getMessage());
        }

        // Unpark vehicles
        try {
            double fee = parkingLot.unParkVehicle(parkingTickets.getFirst()); // valid ticket ID
            System.out.println("Ticket: " + parkingTickets.getFirst() + ", Fee: " + fee);

            fee = parkingLot.unParkVehicle("1"); // invalid ticket ID
        } catch (Exception e) {
            System.out.println("Exception while unparking: " + e.getMessage());
        }
    }


}