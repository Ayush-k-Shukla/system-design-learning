package linkedin;

import com.sun.source.doctree.SeeTree;
import linkedin.Types.UserType;

import java.util.*;
import java.util.stream.Stream;

public class LinkedinService {
    private static LinkedinService instance;

    private Map<String, User> users;
    private Set<String> pendingConnectionRequests;

    private List<JobPost> jobPosts;

    private LinkedinService(){
        users = new HashMap<>();
        pendingConnectionRequests = new HashSet<>();
        this.jobPosts = new ArrayList<>();
    }

    // Singleton
    public static LinkedinService getInstance(){
        if(instance==null){
            instance = new LinkedinService();
        }
        return instance;
    }

    public void createUser(String name, String email, String password, UserType type, UserProfile profile){
        User newUser = new User(name, email, password, type);
        newUser.setProfile(profile);
        users.put(newUser.getId(), newUser);
    }

    public User loginUser(String email, String password){
        User user = getUserByEmailAndPassword(email);
        if(user==null) {
            return null;
        }
        if(user.getPassword().equals(password)) return user;
        return null;
    }

    public void sendConnectionRequest(User to, User from){

        String key = to.getId() + "_" + from.getId();
        if(pendingConnectionRequests.contains(key)){
            return;
        }

        pendingConnectionRequests.add(key);
    }

    public void removeConnectionRequest(User to, User from){

        String key = to.getId() + "_" + from.getId();
        if(!pendingConnectionRequests.contains(key)){
            return;
        }
        Optional<Connection> c1 =  to.getConnections().stream().filter(c-> c.getUser().equals(from)).findFirst();
        c1.ifPresent(to::removeConnection);

        Optional<Connection> c2 =  from.getConnections().stream().filter(c-> c.getUser().equals(to)).findFirst();
        c2.ifPresent(from::removeConnection);

        pendingConnectionRequests.remove(key);
    }

    public void acceptConnectionRequest(User to, User from){

        String key = to.getId() + "_" + from.getId();
        if(!pendingConnectionRequests.contains(key)){
            return;
        }
        to.addConnection(new Connection(from, DateUtility.getCurrentTimeString()));
        from.addConnection(new Connection(to, DateUtility.getCurrentTimeString()));

        pendingConnectionRequests.remove(key);
    }

    public void sendMessage(User sender, User receiver, String content){
        Message message = new Message(sender, receiver, DateUtility.getCurrentTimeString(), content);
        sender.addMessage(message);
        receiver.addMessage(message);
    }

    public List<User> searchUser(String match){
        List<User> found = new ArrayList<>();
        for(User u: users.values()){
            if(u.getName().contains(match)){
                found.add(u);
            }
        }
        return found;
    }

    public List<JobPost> searchJobs(String match){
        List<JobPost> found = new ArrayList<>();
        for(JobPost u: jobPosts){
            if(u.getDescription().contains(match) || u.getRequirement().contains(match)){
                found.add(u);
            }
        }
        return found;
    }

    public void addJobPost(User user, JobPost post){
        if(user.getType().equals(UserType.EMPLOYER)){
            jobPosts.add(post);
        }
    }

    public void applyJob(User user, JobPost post){
        post.addApplicant(user);
    }

    private User getUserByEmailAndPassword(String email){
        for(User user: users.values()){
            if(user.getEmail().equals(email)) return user;
        }
        return null;
    }
}
