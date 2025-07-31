package linkedin;

import linkedin.Types.UserType;

import java.util.UUID;

public class User {
    private final String name;
    private final String email;
    private final String password;
    private final String id;
    private final UserType type;

    public User(String name, String email, String password, UserType type){
        this.email = email;
        this.name = name;
        this.password = password; // for now plane only
        this.type = type;
        id = UUID.randomUUID().toString();
    }
}
