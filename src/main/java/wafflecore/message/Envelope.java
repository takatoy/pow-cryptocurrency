package wafflecore.message;

class Envelope {


    public static int getMessageType() {
        return MSG_TYPE_HELLO;
    }

    public static int getPayload() {
        MessageSerializer.serialize(this);
    }
}
