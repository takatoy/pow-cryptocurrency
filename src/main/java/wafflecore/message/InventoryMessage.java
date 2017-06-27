package wafflecore.message;

import static wafflecore.constants.Constants.*;
import wafflecore.util.ByteArrayWrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryMessage extends Message {
    @JsonIgnore
    public final MessageType messageType = MessageType.MSG_TYPE_INVENTORY;
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
        int inventoryMessageType,
        ByteArrayWrapper objectId,
        boolean isBlock,
        byte[] data)
    {
        this.inventoryMessageType = inventoryMessageType;
        this.objectId = objectId;
        this.isBlock = isBlock;
        this.data = data;
    }

    public Envelope packToEnvelope() {
        return new Envelope(messageType, MessageUtil.serialize(this));
    }

    // getter
    public int getInventoryMessageType() {
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
    public void setInventoryMessageType(int inventoryMessageType) {
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
