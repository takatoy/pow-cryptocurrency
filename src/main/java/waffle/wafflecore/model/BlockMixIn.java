package waffle.wafflecore.model;

import waffle.wafflecore.util.ByteArrayWrapper;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// For serialization.
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BlockMixIn {
    @JsonIgnore
    private byte[] original; // Original Block bytes
    @JsonIgnore
    private ByteArrayWrapper id;
    @JsonProperty("prev")
    private ByteArrayWrapper previousHash;
    @JsonProperty("difficulty")
    private double difficulty;
    @JsonProperty("nonce")
    private long nonce;
    @JsonProperty("timestamp")
    private long timestamp;
    @JsonProperty("txroot")
    private byte[] transactionRootHash;
    @JsonProperty("txids")
    private ArrayList<ByteArrayWrapper> transactionIds;
    @JsonProperty("txs")
    private ArrayList<byte[]> transactions;
    @JsonIgnore
    private int height;
    @JsonIgnore
    private ArrayList<Transaction> parsedTransactions;
    @JsonIgnore
    private double totalDifficulty;

    @JsonCreator
    public BlockMixIn(
        @JsonProperty("prev") ByteArrayWrapper previousHash,
        @JsonProperty("difficulty") double difficulty,
        @JsonProperty("nonce") long nonce,
        @JsonProperty("timestamp") long timestamp,
        @JsonProperty("txroot") byte[] transactionRootHash,
        @JsonProperty("txids") ArrayList<ByteArrayWrapper> transactionIds,
        @JsonProperty("txs") ArrayList<byte[]> transactions
    )
    {}

    @JsonIgnore abstract byte[] getOriginal();
    @JsonIgnore abstract ByteArrayWrapper getId();
    @JsonProperty("prev") abstract ByteArrayWrapper getPreviousHash();
    @JsonProperty("difficulty") abstract double getDifficulty();
    @JsonProperty("nonce") abstract long getNonce();
    @JsonProperty("timestamp") abstract long getTimestamp();
    @JsonProperty("txroot") abstract byte[] getTransactionRootHash();
    @JsonProperty("txids") abstract ArrayList<ByteArrayWrapper> getTransactionIds();
    @JsonProperty("txs") abstract ArrayList<byte[]> getTransactions();
    @JsonIgnore abstract int getHeight();
    @JsonIgnore abstract ArrayList<Transaction> getParsedTransactions();
    @JsonIgnore abstract double getTotalDifficulty();
}
