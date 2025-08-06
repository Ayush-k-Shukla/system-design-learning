package ShowBooking;

import ShowBooking.Types.BookingStatus;
import java.util.List;
import java.util.UUID;

public class Booking {
    private Show show;
    private Payment payment;
    private List<Seat> seats;
    private final String id;
    private double amount;
    private BookingStatus status;
    private User user;

    public Booking(Show show, Payment payment, List<Seat> seats, double amount, User user, BookingStatus status){
        this.amount = amount;
        this.payment = payment;
        this.seats = seats;
        this.amount = amount;
        this.user = user;
        this.id = UUID.randomUUID().toString();
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public Show getShow() {
        return show;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public double getAmount() {
        return amount;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }
}
