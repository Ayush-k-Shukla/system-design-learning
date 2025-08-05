package ShowBooking;

import ShowBooking.Types.BookingStatus;
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
    private List<Booking> bookings;


    private ShowBookingService(){
        allShows = new HashMap<>();
        theatres = new ArrayList<>();
        movies = new ArrayList<>();
        bookings = new ArrayList<>();
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

    public List<Show> getShowsByMovieName(String name){
        List<Show> shows = new ArrayList<>();
        allShows.values().forEach(show -> {
            if(show.getMovie().getName().contains(name)){
                shows.add(show);
            }
        });

        return shows;
    }

    public synchronized Booking bookShow(Show show, List<Seat> seats, User user){
        if(!areSeatsAvailable(seats)){
            throw new IllegalStateException("One or more seats are already booked.");
        }
        double price = PriceUtility.getPrice(show, seats);
        Payment payment = new Payment(show, PaymentStatus.PENDING, price);
        Booking booking = new Booking(show, payment, seats, price, user, BookingStatus.PENDING);
        bookings.add(booking);
        for(Seat seat: seats){
            seat.setStatus(SeatStatus.FILLED);
        }
        return booking;
    }

    public synchronized void makePayment(Booking booking){
        Payment payment = booking.getPayment();
        payment.setStatus(PaymentStatus.SUCCESS);
        booking.setPayment(payment);
        booking.setStatus(BookingStatus.BOOKED);
    }

    public synchronized void declinePayment(Booking booking){
        Payment payment = booking.getPayment();
        payment.setStatus(PaymentStatus.FAILURE);
        booking.setPayment(payment);
        List<Seat> seats =  booking.getSeats();

        for(Seat seat: seats){
            seat.setStatus(SeatStatus.EMPTY);
        }
        booking.setStatus(BookingStatus.CANCELLED);
    }

    public synchronized void cancelBooking(Booking booking){
        List<Seat> seats =  booking.getSeats();

        for(Seat seat: seats){
            seat.setStatus(SeatStatus.EMPTY);
        }
        booking.setStatus(BookingStatus.CANCELLED);
    }

    public List<Booking> getBookingsOfUser(User user) {
        return bookings.stream().filter(booking -> booking.getUser().equals(user)).toList();
    }

    private boolean areSeatsAvailable(List<Seat> seats) {
        for (Seat seat : seats) {
            if (seat.getStatus() != SeatStatus.EMPTY) {
                return false;
            }
        }
        return true;
    }

}
