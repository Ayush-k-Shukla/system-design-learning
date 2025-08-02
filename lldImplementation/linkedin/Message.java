package linkedin;

public class Message {
    private final User sender;
    private final User receiver;
    private String content;
    private String dateTime;


    public Message(User sender, User receiver, String dateTime, String content){
        this.sender = sender;
        this.receiver = receiver;
        this.dateTime = dateTime;
        this.content = content;
    }
}
