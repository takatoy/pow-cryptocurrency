package waffle.wafflecore.model;

import waffle.wafflecore.util.ByteArrayWrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OutEntry {
    @JsonProperty("to")
    private byte[] recipientHash;
    @JsonProperty("val")
    private long amount;

    public OutEntry() {
        this.recipientHash = null;
        this.amount = 0;
    }

    public OutEntry(
        byte[] recipientHash,
        long amount)
    {
        this.recipientHash = recipientHash;
        this.amount = amount;
    }

    public String toJson() {
        String json = "";
        ObjectMapper mapper = new ObjectMapper();

        try {
            json = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            System.err.println("Invalid out-entry data detected.");
            e.printStackTrace();
        }

        return json;
    }

    // getter
    public byte[] getRecipientHash() {
        return recipientHash;
    }
    public long getAmount() {
        return amount;
    }

    // setter
    public void setRecipientHash(byte[] recipientHash) {
        this.recipientHash = recipientHash;
    }
    public void setAmount(long amount) {
        this.amount = amount;
    }
}
