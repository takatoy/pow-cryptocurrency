package wafflecore.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InEntry {
    @JsonProperty("tx")
    private byte[] transactionId;
    @JsonProperty("outidx")
    private short outEntryIndex;
    @JsonProperty("pub")
    private byte[] publicKey;
    @JsonProperty("sig")
    private byte[] signature;

    public InEntry(
        byte[] transactionId,
        short outEntryIndex,
        byte[] publicKey,
        byte[] signature)
    {
        this.transactionId = transactionId;
        this.outEntryIndex = outEntryIndex;
        this.publicKey = publicKey;
        this.signature = signature;
    }

    public String toJson() {
        String json = "";
        ObjectMapper mapper = new ObjectMapper();

        try {
            json = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            System.err.println("Invalid in-entry data detected.");
            e.printStackTrace();
        }

        return json;
    }

    // getter
    public byte[] getTransactionId() {
        return transactionId;
    }
    public short getOutEntryIndex() {
        return outEntryIndex;
    }
    public byte[] getPublicKey() {
        return publicKey;
    }
    public byte[] getSignature() {
        return signature;
    }

    // setter
    public void setTransactionId(byte[] transactionId)  {
        this.transactionId = transactionId;
    }
    public void setOutEntryIndex(short outEntryIndex) {
        this.outEntryIndex = outEntryIndex;
    }
    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }
    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
}
