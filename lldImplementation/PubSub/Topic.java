package PubSub;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Topic {
    private final String name;
    private Set<Subscriber> subscribers;

    public Topic(String name){
        this.name = name;
        subscribers = new HashSet<>();
    }

    public List<Subscriber> getSubscribers() {
        return subscribers.stream().toList();
    }

    public void addSubsciber(Subscriber subscriber){
        subscribers.add(subscriber);
    }

    public void removeSubscriber(Subscriber subscriber){
        subscribers.remove(subscriber);
    }

    public String getName() {
        return name;
    }
}
