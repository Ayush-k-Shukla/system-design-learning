
package ShowBooking;

import java.util.List;

import ShowBooking.Types.UserType;

public class ShowBookingDemo {
    public static void run() {
        ShowBookingService service = ShowBookingService.getInstance();

        // Create users
        User admin = new User("admin1", "a@y.com", UserType.ADMIN);
        User user = new User("user1", "a@g.com", UserType.NORMAL);

        // Create seats
        Seat seat1 = new Seat(1, 1, ShowBooking.Types.SeatStatus.EMPTY, ShowBooking.Types.SeatType.NORMAL);
        Seat seat2 = new Seat(1, 2, ShowBooking.Types.SeatStatus.EMPTY, ShowBooking.Types.SeatType.PREMIUM);
        List<Seat> seatsList = java.util.Arrays.asList(seat1, seat2);

        // Create movie, theatre, show
        Movie movie = new Movie("Inception", "Mind-bending thriller", 100.0);
        Theatre theatre = new Theatre("PVR", java.util.Arrays.asList());
        Show show = new Show(movie, seatsList, null, null, theatre);
        theatre.setShows(java.util.Arrays.asList(show));

        // Admin functionalities
        try {
            service.addMovie(movie, admin);
            service.addTheatre(theatre, admin);
            service.addShow(show, admin);
            service.setPriceStrategy(new DefaultPriceStrategy(), admin);
            System.out.println("Admin actions succeeded.");
        } catch (Exception e) {
            System.out.println("Admin actions failed: " + e.getMessage());
        }

        // Non-admin should fail for admin actions
        try {
            service.addMovie(new Movie("Avatar", "Epic Sci-fi", 120.0), user);
        } catch (Error e) {
            System.out.println("Non-admin addMovie failed as expected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Non-admin addMovie threw unexpected exception: " + e.getMessage());
        }
        try {
            service.addTheatre(new Theatre("IMAX", java.util.Arrays.asList()), user);
        } catch (Error e) {
            System.out.println("Non-admin addTheatre failed as expected: " + e.getMessage());
        }
        try {
            service.addShow(new Show(movie, seatsList, null, null, theatre), user);
        } catch (Error e) {
            System.out.println("Non-admin addShow failed as expected: " + e.getMessage());
        }
        try {
            service.setPriceStrategy(new DefaultPriceStrategy(), user);
        } catch (Error e) {
            System.out.println("Non-admin setPriceStrategy failed as expected: " + e.getMessage());
        }

        // Normal functionalities
        java.util.List<Show> allShows = service.getAllShows();
        System.out.println("All shows: " + allShows.size());

        java.util.List<Seat> seats = service.getSeats(show.getId());
        System.out.println("Seats for show: " + seats.size());

        java.util.List<Show> showsByName = service.getShowsByMovieName("Inception");
        System.out.println("Shows by movie name: " + showsByName.size());

        // Book show (success)
        Booking booking = service.bookShow(show, java.util.Arrays.asList(seat1), user);
        System.out.println("Booking created: " + booking.getId() + ", status: " + booking.getStatus());
        assert seat1.getStatus() == ShowBooking.Types.SeatStatus.FILLED : "Seat should be filled after booking";

        // Try booking already filled seat (should fail)
        try {
            service.bookShow(show, java.util.Arrays.asList(seat1), user);
            System.out.println("Error: Should not allow booking already filled seat!");
        } catch (Exception e) {
            System.out.println("Booking already filled seat failed as expected: " + e.getMessage());
        }

        // Make payment
        service.makePayment(booking);
        System.out.println("Payment made: " + booking.getPayment().getStatus());
        assert booking.getStatus() == ShowBooking.Types.BookingStatus.BOOKED : "Booking should be BOOKED after payment";

        // Cancel booking
        service.cancelBooking(booking);
        System.out.println("Booking cancelled: " + booking.getStatus());
        assert seat1.getStatus() == ShowBooking.Types.SeatStatus.EMPTY : "Seat should be empty after cancellation";
        assert booking.getStatus() == ShowBooking.Types.BookingStatus.CANCELLED
                : "Booking should be CANCELLED after cancellation";

        // Decline payment (simulate new booking)
        Booking booking2 = service.bookShow(show, java.util.Arrays.asList(seat2), user);
        service.declinePayment(booking2);
        System.out.println("Payment declined: " + booking2.getPayment().getStatus());
        assert seat2.getStatus() == ShowBooking.Types.SeatStatus.EMPTY : "Seat should be empty after payment declined";
        assert booking2.getStatus() == ShowBooking.Types.BookingStatus.CANCELLED
                : "Booking should be CANCELLED after payment declined";

        // Get bookings of user
        java.util.List<Booking> userBookings = service.getBookingsOfUser(user);
        System.out.println("User bookings: " + userBookings.size());

        // Try booking with empty seat list (should fail)
        try {
            service.bookShow(show, java.util.Arrays.asList(), user);
            System.out.println("Error: Should not allow booking with empty seat list!");
        } catch (Exception e) {
            System.out.println("Booking with empty seat list failed as expected: " + e.getMessage());
        }

        // Try booking with null seat (should fail)
        try {
            service.bookShow(show, null, user);
            System.out.println("Error: Should not allow booking with null seat list!");
        } catch (Exception e) {
            System.out.println("Booking with null seat list failed as expected: " + e.getMessage());
        }
    }
}
