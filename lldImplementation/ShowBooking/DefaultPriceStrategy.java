package ShowBooking;

import java.util.List;

import ShowBooking.Types.SeatType;

public class DefaultPriceStrategy implements PriceStrategy {
    @Override
    public double calculatePrice(Show show, List<Seat> seats) {
        double amount = 0;
        for (Seat seat : seats) {
            amount += show.getMovie().getBasePrice();
            if (seat.getType() == SeatType.PREMIUM) {
                amount += (show.getMovie().getBasePrice() * 1.5);
            }
        }
        return amount;
    }
}
