package PubSub;

import java.util.HashMap;
import java.util.Map;

public class Broker {
    private Map<String, Topic> topicMap;

    public Broker(String name){
        this.topicMap = new HashMap<>();
    }

    public void createTopic(String name){
        Topic topic = new Topic(name);
        if(!topicMap.containsKey(topic)){
            topicMap.put(name, topic);
        }
    }

    public void subscribe(String topicName, Subscriber subscriber){
        Topic topic = topicMap.get(topicName);
        if (topic != null) {
            topic.addSubsciber(subscriber);
        }
    }

    public void unsubscribe(String topicName, Subscriber subscriber){
        Topic topic = topicMap.get(topicName);
        if (topic != null) {
            topic.removeSubscriber(subscriber);
        }
    }

    public void publish(String topicName, Message message){
        Topic topic = topicMap.get(topicName);
        if (topic != null) {
            topic.getSubscribers().forEach(sub->{
                sub.onMessage(message, topic);
            });
        }
    }
}
