package Message;

// An observer pattern, parse and scanner may release message to observer during parsing and scanning.
// We should try to maintain low coupling between observer(whatever that parser and scanner won't be worry about)
// and observable(parser and scanner).
public interface MessageProducerAPI {

    public void addMessageListener(MessageObserver messageObserver);
    public void removeMessageListener(MessageObserver messageObserver);

    //update
    public void sendMessage();
}
