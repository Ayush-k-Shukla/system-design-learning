package ShowBooking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ShowBooking.Types.BookingStatus;
import ShowBooking.Types.PaymentStatus;
import ShowBooking.Types.SeatStatus;
import ShowBooking.Types.UserType;

public class ShowBookingService {
    private static ShowBookingService instance;

    private Map<String, Show> allShows;
    private List<Theatre> theatres;
    private List<Movie> movies;
    private List<Booking> bookings;
    private PriceStrategy priceStrategy;

    private ShowBookingService() {
        allShows = new HashMap<>();
        theatres = new ArrayList<>();
        movies = new ArrayList<>();
        bookings = new ArrayList<>();
        priceStrategy = new DefaultPriceStrategy();
    }

    public static ShowBookingService getInstance() {
        if (instance == null) {
            instance = new ShowBookingService();
        }
        return instance;
    }

    public List<Show> getAllShows() {
        return allShows.values().stream().toList();
    }

    public List<Seat> getSeats(String showid) {
        Show current = allShows.get(showid);
        return current.getSeats();
    }

    public List<Show> getShowsByMovieName(String name) {
        List<Show> shows = new ArrayList<>();
        allShows.values().forEach(show -> {
            if (show.getMovie().getName().contains(name)) {
                shows.add(show);
            }
        });
        return shows;
    }

    public synchronized Booking bookShow(Show show, List<Seat> seats, User user) {
        if (!areSeatsAvailable(seats)) {
            throw new IllegalStateException("One or more seats are already booked.");
        }
        double price = priceStrategy.calculatePrice(show, seats);
        Payment payment = BookingFactory.createPayment(show, PaymentStatus.PENDING, price);
        Booking booking = BookingFactory.createBooking(show, payment, seats, price, user, BookingStatus.PENDING);
        bookings.add(booking);
        for (Seat seat : seats) {
            seat.setStatus(SeatStatus.FILLED);
        }
        return booking;
    }

    public synchronized void makePayment(Booking booking) {
        Payment payment = booking.getPayment();
        payment.setStatus(PaymentStatus.SUCCESS);
        booking.setPayment(payment);
        booking.setStatus(BookingStatus.BOOKED);
    }

    public synchronized void declinePayment(Booking booking) {
        Payment payment = booking.getPayment();
        payment.setStatus(PaymentStatus.FAILURE);
        booking.setPayment(payment);
        List<Seat> seats = booking.getSeats();
        for (Seat seat : seats) {
            seat.setStatus(SeatStatus.EMPTY);
        }
        booking.setStatus(BookingStatus.CANCELLED);
    }

    public synchronized void cancelBooking(Booking booking) {
        List<Seat> seats = booking.getSeats();
        for (Seat seat : seats) {
            seat.setStatus(SeatStatus.EMPTY);
        }
        booking.setStatus(BookingStatus.CANCELLED);
    }

    public List<Booking> getBookingsOfUser(User user) {
        return bookings.stream().filter(booking -> booking.getUser().equals(user)).toList();
    }

    // Admin methods

    public void addMovie(Movie movie, User user) {
        if(!isAdmin(user)){
            throw new Error("Only Admin are allowed for this operation");
        }
        movies.add(movie);
    }

    public void addTheatre(Theatre theatre, User user) {
        if(!isAdmin(user)){
            throw new Error("Only Admin are allowed for this operation");
        }
        theatres.add(theatre);
        for (Show show : theatre.getShows()) {
            addShow(show, user);
        }
    }

    public void addShow(Show show, User user) {
        if(!isAdmin(user)){
            throw new Error("Only Admin are allowed for this operation");
        }
        allShows.put(show.getId(), show);
    }

    public void setPriceStrategy(PriceStrategy strategy, User user) {
        if(!isAdmin(user)){
            throw new Error("Only Admin are allowed for this operation");
        }
        this.priceStrategy = strategy;
    }

    private boolean isAdmin(User user){
        return user.getType().equals(UserType.ADMIN);
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
