package parkinglot;

import parkinglot.vehicletype.Vehicle;

import java.util.Date;



public class Ticket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private final Long entryTimeStamp;
    private Long exitTimeStamp;

    public Ticket(String ticketId, Vehicle vehicle, ParkingSpot spot) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.spot = spot;
        this.entryTimeStamp = new Date().getTime();
    }

    public String getTicketId() {
        return ticketId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getSpot() {
        return spot;
    }

    public long getEntryTimeStamp() {
        return this.entryTimeStamp;
    }

    public long getExitTimeStamp() {
        return this.exitTimeStamp;
    }

    public void setExitTimeStamp() {
        this.exitTimeStamp = new Date().getTime();
    }
}