package PubSub;

public class Message {
    private final String content;
    private final long timestamp;

    public Message(String content){
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }
}
