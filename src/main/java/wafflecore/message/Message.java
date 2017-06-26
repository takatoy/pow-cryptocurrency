package wafflecore.message;

public class Message {
    public static abstract int messageType;
    public byte[] payload;

    // getter
    public int getMessageType() {
        return messageType;
    }
    public byte[] getPayload() {
        return payload;
    }

    // setter
    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
}
