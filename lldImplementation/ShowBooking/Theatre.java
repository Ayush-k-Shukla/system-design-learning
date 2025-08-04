package ShowBooking;

import java.util.List;
import java.util.UUID;

public class Theatre {
    private final String name;
    private final String id;
    private List<Show> shows;

    public Theatre(String name, List<Show> shows){
        this.name = name;
        this.shows = shows;
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Show> getShows() {
        return shows;
    }

    public void setShows(List<Show> shows) {
        this.shows = shows;
    }
}
