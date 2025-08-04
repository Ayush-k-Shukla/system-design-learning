package ShowBooking;

import java.util.UUID;

public class Movie {
    private String name;
    private String description;
    private double basePrice;
    private final String id;

    public Movie(String name, String description, double basePrice){
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.id = UUID.randomUUID().toString();
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public String getId() {
        return id;
    }
}
