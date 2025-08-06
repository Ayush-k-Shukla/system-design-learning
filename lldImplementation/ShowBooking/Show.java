package ShowBooking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Show {
    private final String id;
    private final Movie movie;
    private List<Seat> seats;
    private Theatre theatre;
    private LocalDateTime starTime;
    private LocalDateTime endTime;

    public Show(Movie movie, List<Seat> seats, LocalDateTime starTime, LocalDateTime endTime, Theatre theatre){
        this.seats = seats;
        this.movie = movie;
        this.starTime = starTime;
        this.endTime = endTime;
        this.id = UUID.randomUUID().toString();
        this.theatre = theatre;
    }

    public String getId() {
        return id;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}
