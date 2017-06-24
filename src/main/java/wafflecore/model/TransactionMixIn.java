package wafflecore.model;

import wafflecore.util.ByteArrayWrapper;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

// For serialization.
public abstract class TransactionMixIn {
    @JsonIgnore
    private byte[] original;
    @JsonIgnore
    private ByteArrayWrapper id;
    @JsonProperty("timestamp")
    private long timestamp;
    @JsonProperty("in")
    private ArrayList<InEntry> inEntries;
    @JsonProperty("out")
    private ArrayList<OutEntry> outEntries;
    @JsonIgnore
    private TransactionExecInfo execInfo;

    @JsonCreator
    public TransactionMixIn(
        @JsonProperty("timestamp") long timestamp,
        @JsonProperty("in") ArrayList<InEntry> inEntries,
        @JsonProperty("out") ArrayList<OutEntry> outEntries
    )
    {}

    @JsonIgnore abstract byte[] getOriginal();
    @JsonIgnore abstract ByteArrayWrapper getId();
    @JsonProperty("timestamp") abstract long getTimestamp();
    @JsonProperty("in") abstract ArrayList<InEntry> getInEntries();
    @JsonProperty("out") abstract ArrayList<OutEntry> getOutEntries();
    @JsonIgnore abstract TransactionExecInfo getExecInfo();
}
