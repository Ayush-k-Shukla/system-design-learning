package TaskManager;

public class User {
    private final String id;
    private final String name;
    private final String email;

    public User(String id, String name, String email){
        this.email = email;
        this.id = id;
        this.name = name;
    }

    public String getId(){
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
