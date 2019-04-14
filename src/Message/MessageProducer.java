package Message;

import java.util.ArrayList;

public class MessageProducer implements MessageProducerAPI {

    private Message message;    // 消息
    private ArrayList<MessageObserver> listeners;  // 监听器列表

    public MessageProducer(Message message,ArrayList<MessageObserver> listeners) {
        this.message=message;
        this.listeners=listeners;
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
