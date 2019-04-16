package Message;

import java.util.ArrayList;

public class MessageBroadCaster implements MessageBroadCasterAPI {

    private Message message;

    private ArrayList<MessageObserver> listeners = new ArrayList<MessageObserver>();

    public MessageBroadCaster(Message message) {
        this.message=message;
    }


    @Override
    public void addMessageListener(MessageObserver messageObserver) {
        if(!listeners.contains(messageObserver)){
            listeners.add(messageObserver);
        }
    }

    @Override
    public void removeMessageListener(MessageObserver messageObserver) {
        if(listeners.contains(messageObserver)){
            listeners.remove(messageObserver);
        }
    }

    @Override
    public void sendMessage() {
        notifyObservers();
    }

    private void notifyObservers(){
        for (MessageObserver observer : listeners) {
            observer.messageReceived(message);
        }
    }
}
