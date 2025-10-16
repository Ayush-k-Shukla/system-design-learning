package PubSub;

public class PubSubDemo {
    public static void run(){
        Broker broker = new Broker();
        broker.createTopic("sports");
        broker.createTopic("news");

        Subscriber s1 = new Subscriber("S1");
        Subscriber s2 = new Subscriber("S2");

        broker.subscribe("sports", s1);
        broker.subscribe("sports", s2);

        Publisher p1 = new Publisher("P1", broker);
        p1.publish( "India won the cricket match!","sports");
    }
}
