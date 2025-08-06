package ShowBooking;

import ShowBooking.Types.UserType;

public class User {
    private final String name;
    private final String email;
    private final UserType type;

    public User(String name, String email, UserType type) {
        this.name = name;
        this.email = email;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public UserType getType() {
        return type;
    }
}
