package linkedin;

public class Connection {
    private final User user;
    private String connectionDate;


    public Connection(User user, String connectionDate){
        this.user = user;
        this.connectionDate = connectionDate;
    }

    public User getUser() {
        return user;
    }
}
