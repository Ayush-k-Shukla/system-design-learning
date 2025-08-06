package ShowBooking;

import java.util.List;

import ShowBooking.Types.BookingStatus;
import ShowBooking.Types.PaymentStatus;

public class BookingFactory {
    public static Booking createBooking(Show show, Payment payment, List<Seat> seats, double amount, User user,
            BookingStatus status) {
        return new Booking(show, payment, seats, amount, user, status);
    }

    public static Payment createPayment(Show show, PaymentStatus status, double totalPrice) {
        return new Payment(show, status, totalPrice);
    }
}
