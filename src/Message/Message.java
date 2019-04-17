package Message;

public class Message {

    private MessageType type;
    private Object body;

    public Message(String body) {
        this.body=body;
    }


    public Message(MessageType type,Object body) {
        this.type=type;
        this.body=body;
    }
}
