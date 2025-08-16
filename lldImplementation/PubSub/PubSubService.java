package PubSub;

import java.util.HashMap;
import java.util.Map;

public class PubSubService {

    private static PubSubService instance;

    private Map<String, Publisher> publisherMap;
    private Map<String, Subscriber> subscriberMap;

    private PubSubService(){
        publisherMap = new HashMap<>();
        subscriberMap = new HashMap<>();
    }

    public static PubSubService getInstance() {
        if(instance==null){
            instance = new PubSubService();
        }
        return instance;
    }



}
