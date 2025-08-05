package ShowBooking;

import ShowBooking.Types.PaymentStatus;
import ShowBooking.Types.SeatStatus;
import ShowBooking.util.PriceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowBookingService {
    private static ShowBookingService instance;

    private Map<String, Show> allShows;
    private List<Theatre> theatres;
    private List<Movie> movies;


    private ShowBookingService(){
        allShows = new HashMap<>();
        theatres = new ArrayList<>();
        movies = new ArrayList<>();
    }

    public static ShowBookingService getInstance() {
        if(instance==null){
            instance = new ShowBookingService();
        }
        return instance;
    }

    public List<Show> getAllShows(){
        return allShows.values().stream().toList();
    }

    public List<Seat> getSeats(String showid){
        Show current = allShows.get(showid);
        return current.getSeats();
    }

    public synchronized Booking bookShow(Show show, List<Seat> seats, User user){
        if(!areSeatsAvailable(seats)){
            throw new IllegalStateException("One or more seats are already booked.");
        }
        double price = PriceUtility.getPrice(show, seats);
        Payment payment = new Payment(show, PaymentStatus.PENDING, price);
        Booking booking = new Booking(show, payment, seats, price, user);
        for(Seat seat: seats){
            seat.setStatus(SeatStatus.FILLED);
        }
        return booking;
    }

    public synchronized void makePayment(Booking booking){
        Payment payment = booking.getPayment();
        payment.setStatus(PaymentStatus.SUCCESS);
        booking.setPayment(payment);
    }

    public synchronized void declinePayment(Booking booking){
        Payment payment = booking.getPayment();
        payment.setStatus(PaymentStatus.FAILURE);
        booking.setPayment(payment);
        List<Seat> seats =  booking.getSeats();

        for(Seat seat: seats){
            seat.setStatus(SeatStatus.EMPTY);
        }
    }

    private boolean areSeatsAvailable(List<Seat> seats) {
        for (Seat seat : seats) {
            if (seat.getStatus() != SeatStatus.AVAILABLE) {
                return false;
            }
        }
        return true;
    }

}
