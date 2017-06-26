package wafflecore.message;

import static wafflecore.constants.Constants.*;

public class InventoryMessage extends Message {
    @JsonProperty("msgtype")
    private final int messageType = MSG_TYPE_INVENTORY;
    @JsonProperty("invtype")
    private int inventoryMessageType;
    @JsonProperty("id")
    private ByteArrayWrapper objectId;
    @JsonProperty("isblock")
    private boolean isBlock;
    @JsonProperty("data")
    private byte[] data;

    // getter
    @Override
    public static int getMessageType() {
        return messageType;
    }
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
