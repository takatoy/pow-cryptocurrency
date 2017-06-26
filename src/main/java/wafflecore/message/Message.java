package wafflecore.message;

public abstract class Message {
    public static abstract int getMessageType();
    public static Message getPayload();
}
