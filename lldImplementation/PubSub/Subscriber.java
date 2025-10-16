package PubSub;

import java.util.UUID;

public class Subscriber {
    private final String name;
    private final String id;

    public Subscriber(String name){
        this.name = name;
        this.id = UUID.randomUUID().toString();
    }

    public void onMessage(Message message, Topic topic){
        System.out.println("Received message "+ message.getContent() + " : "+message.getTimestamp() +" for topic " + topic.getName());
    }
}
