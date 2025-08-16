package PubSub;

import java.util.UUID;

public class Publisher {
    private final String name;
    private final String id;
    private final Broker broker;

    public Publisher(String name, Broker broker){
        this.name = name;
        this.id = UUID.randomUUID().toString();
        this.broker = broker;
    }

    public void publish(String content, String topic){
        Message message = new Message(content);
        broker.publish(topic, message);
    }
}
