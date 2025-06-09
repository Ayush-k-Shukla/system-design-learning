package parkinglot;


import parkinglot.fee.FeeStrategy;
import parkinglot.fee.FlatRateFeeStrategy;
import parkinglot.vehicletype.Vehicle;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingLot {
   private static ParkingLot instance;
   private final List<ParkingFloor> floors;
   private final Map<String,Ticket> issuedActiveTickets=new ConcurrentHashMap<>();
   private FeeStrategy feeStrategy;

   private ParkingLot(){
       floors = new ArrayList<>();
       feeStrategy = new FlatRateFeeStrategy();
   }

   public static synchronized ParkingLot getInstance(){
       if(instance==null){
           instance = new ParkingLot();
       }
       return instance;
   }

   public void addFloor(ParkingFloor floor){
       this.floors.add(floor);
   }

   public void setFeeStrategy(FeeStrategy strategy){
       feeStrategy = strategy;
   }

   // Execute one at a time
   public synchronized Ticket parkVehicle(Vehicle vehicle) throws  Exception{
    for(ParkingFloor floor : floors){
        Optional<ParkingSpot> freeSpot = floor.getSingleAvailableSpot(vehicle.getVehicleType());
        if(freeSpot.isPresent()){
            ParkingSpot spot = freeSpot.get();
            if(spot.park(vehicle)){
                String ticketId = UUID.randomUUID().toString();
                Ticket ticket = new Ticket(ticketId,vehicle,spot);
                issuedActiveTickets.put(ticketId,ticket);
                return ticket;
            }
        }
    }

    throw new Exception("No free spot for vehicle "+vehicle.getVehicleType());
   }

   public Double unParkVehicle(String ticketId) throws  Exception{
       Ticket ticket = issuedActiveTickets.get(ticketId);
       if(ticket==null) throw  new Exception("Invalid Ticket");

       ParkingSpot spot = ticket.getSpot();
       spot.unpark();

       ticket.setExitTimeStamp();
       return feeStrategy.calculateFee(ticket);
   }

   public List<ParkingFloor> getAllFloors(){
       return floors;
   }
}