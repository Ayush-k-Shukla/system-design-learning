package ShowBooking.util;

import ShowBooking.Seat;
import ShowBooking.Show;
import ShowBooking.Types.SeatType;

import java.util.List;

public class PriceUtility {
    public static double getPrice(Show show, List<Seat> seats){
        double amount = 0;
        for(Seat seat : seats){
            amount = amount + show.getMovie().getBasePrice();
            if(seat.getType()== SeatType.PREMIUM){
                amount+= (show.getMovie().getBasePrice() * 1.5);
            }
        }
       return amount;
    }
}
