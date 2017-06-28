package wafflecore.message;

import wafflecore.message.type.MessageType;
import wafflecore.util.MessageUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Envelope {
    @JsonProperty("msgtype")
    private MessageType messageType;
    @JsonProperty("payload")
    private Message payload;

    public Envelope(MessageType messageType, Message payload) {
        this.messageType = messageType;
        this.payload = payload;
    }

    public Envelope() {}

    public MessageType getMessageType() {
        return messageType;
    }
    public Message getPayload() {
        return payload;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
    public void setPayload(Message payload) {
        this.payload = payload;
    }
}
