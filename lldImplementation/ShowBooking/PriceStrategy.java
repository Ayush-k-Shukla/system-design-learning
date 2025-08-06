package ShowBooking;

import java.util.List;

public interface PriceStrategy {
    double calculatePrice(Show show, List<Seat> seats);
}
