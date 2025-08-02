package linkedin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import linkedin.Types.UserType;

public class User {
    private final String name;
    private final String email;
    private final String password;
    private final String id;
    private final UserType type;
    private UserProfile profile;

    private List<Connection> connections;
    private List<Message> messages;

    public User(String name, String email, String password, UserType type) {
        this.email = email;
        this.name = name;
        this.password = password; // for now plane only
        this.type = type;
        id = UUID.randomUUID().toString();

        connections = new ArrayList<>();
        messages = new ArrayList<>();
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public void addConnection(Connection newc) {
        connections.add(newc);
    }

    public void removeConnection(Connection c) {
        connections.remove(c);
    }
    public void addMessage(Message message) {
        messages.add(message);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserType getType() {
        return type;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
