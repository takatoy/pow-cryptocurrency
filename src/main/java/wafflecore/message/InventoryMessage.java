package wafflecore.message;

import static wafflecore.constants.Constants.*;
import static wafflecore.message.type.MessageType.*;
import static wafflecore.message.type.InventoryMessageType.*;
import wafflecore.message.type.MessageType;
import wafflecore.message.type.InventoryMessageType;
import wafflecore.util.ByteArrayWrapper;
import wafflecore.util.MessageUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryMessage extends Message {
    @JsonIgnore
    public final MessageType messageType = INVENTORY;
    @JsonProperty("invtype")
    private InventoryMessageType inventoryMessageType;
    @JsonProperty("id")
    private ByteArrayWrapper objectId;
    @JsonProperty("isblock")
    private boolean isBlock;
    @JsonProperty("data")
    private byte[] data;

    @JsonCreator
    public InventoryMessage(
        @JsonProperty("invtype") InventoryMessageType inventoryMessageType,
        @JsonProperty("id") ByteArrayWrapper objectId,
        @JsonProperty("isblock") boolean isBlock,
        @JsonProperty("data") byte[] data)
    {
        this.inventoryMessageType = inventoryMessageType;
        this.objectId = objectId;
        this.isBlock = isBlock;
        this.data = data;
    }

    public InventoryMessage() {}

    public String toJson() {
        String json = "";
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            json = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    public Envelope packToEnvelope() {
        return new Envelope(messageType, this);
    }

    // getter
    public InventoryMessageType getInventoryMessageType() {
        return inventoryMessageType;
    }
    public ByteArrayWrapper getObjectId() {
        return objectId;
    }
    public boolean getIsBlock() {
        return isBlock;
    }
    public byte[] getData() {
        return data;
    }

    // setter
    public void setInventoryMessageType(InventoryMessageType inventoryMessageType) {
        this.inventoryMessageType = inventoryMessageType;
    }
    public void setObjectId(ByteArrayWrapper objectId) {
        this.objectId = objectId;
    }
    public void setIsBlock(boolean isBlock) {
        this.isBlock = isBlock;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
}
