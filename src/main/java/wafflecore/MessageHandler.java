package wafflecore;

import wafflecore.message.*;

public class MessageHandler {
    public void handleMessage(Message msg, String peerAddr) {
        switch (msg.getMessageType()) {
            case MSG_TYPE_HELLO:
                handleHello(msg.getPayload(), peerAddr);
                break;
            case MSG_TYPE_INVENTORY:
                handleInventoryMessage(msg.getPayload(), peerAddr);
                break;
            default:
                break;
        }

        return;
    }

    public void handleHello(Hello hello, String peerAddr) {
        return;
    }

    public void handleInventoryMessage(Message msg, String peerAddr) {
        switch (msg.getMessageType()) {
            case MSG_TYPE_ADVERTISE:
                handleAdvertise(msg, peerAddr);
                break;
            case MSG_TYPE_REQUEST:
                handleRequest(msg, peerAddr);
                break;
            case MSG_TYPE_CONTENT:
                handleContent(msg, peerAddr);
                break;
            default:
                break;
        }

        return;
    }

    public void handleAdvertise(Message msg, String peerAddr) {

    }

    public void handleRequest(Message msg, String peerAddr) {

    }

    public void handleContent(Message msg, String peerAddr) {

    }
}
