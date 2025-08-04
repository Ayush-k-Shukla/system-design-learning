package ShowBooking;

import java.util.List;
import java.util.UUID;

public class Booking {
    private Show show;
    private Payment payment;
    private List<Seat> seats;
    private final String id;
    private double amount;
    private User user;

    public Booking(Show show, Payment payment, List<Seat> seats, double amount, User user){
        this.amount = amount;
        this.payment = payment;
        this.seats = seats;
        this.amount = amount;
        this.user = user;
        this.id = UUID.randomUUID().toString();
    }

    public Show getShow() {
        return show;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public double getAmount() {
        return amount;
    }

    public String getId() {
        return id;
    }
}
