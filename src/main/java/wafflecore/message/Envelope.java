package wafflecore.message;

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

    public int getMessageType() {
        return messageType;
    }
    public Message getPayload() {
        return payload;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
    public void setPayload(Message payload) {
        this.payload = payload;
    }
}
