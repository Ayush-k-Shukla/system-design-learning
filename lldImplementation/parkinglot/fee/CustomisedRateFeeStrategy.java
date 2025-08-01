package parkinglot.fee;

import java.util.Map;

import parkinglot.Ticket;
import parkinglot.vehicletype.VehicleType;

public class CustomisedRateFeeStrategy implements FeeStrategy {

    private static final Map<VehicleType, Double> hourlyRates = Map.of(
            VehicleType.CAR, 20.0,
            VehicleType.BIKE, 10.0);

    @Override
    public double calculateFee(Ticket ticket) {
        long duration = ticket.getExitTimeStamp() - ticket.getEntryTimeStamp();
        long hours = (duration / (1000 * 60 * 60)) + 1;
        return hours * hourlyRates.get(ticket.getVehicle().getVehicleType());
    }
}
