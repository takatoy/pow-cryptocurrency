package wafflecore;

import static wafflecore.constants.Constants.*;
import wafflecore.message.*;
import wafflecore.util.MessageUtil;

public class MessageHandler {
    public void handleMessage(Message msg, String peerAddr) {
        switch (msg.getMessageType()) {
            case MSG_TYPE_HELLO:
                handleHello(
                    MessageUtil.deserialize(msg.getPayload(), MessageType.MSG_TYPE_HELLO),
                    peerAddr);
                break;
            case MSG_TYPE_INVENTORY:
                handleInventoryMessage(
                    MessagetUtil.deserialize(msg.getPayload(), MessageType.MSG_TYPE_INVENTORY),
                    peerAddr);
                break;
            default:
                break;
        }
    }

    public void handleHello(Hello hello, String peerAddr) {
        return;
    }

    public void handleInventoryMessage(InventoryMessage msg, String peerAddr) {
        switch (msg.getInventoryMessageType()) {
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
    }

    public void handleAdvertise(InventoryMessage msg, String peerAddr) {

    }

    public void handleRequest(InventoryMessage msg, String peerAddr) {

    }

    public void handleContent(InventoryMessage msg, String peerAddr) {

    }
}
