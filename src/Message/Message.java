package Message;

public class Message {

    private final MessageType type = MessageType.RETURN;
    private final String body;

    public Message(String body) {
        this.body=body;
    }
}
