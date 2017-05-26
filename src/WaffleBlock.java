import java.util.Instant;

class WaffleBlock {
    private int index;
    private String prevHash;
    private Instant timestamp;
    private String data;
    private String hash;

    WaffleBlock(int index, String prevHash, Instant timestamp, String data, String hash) {
        this.index = index;
        this.prevHash = prevHash;
        this.timestamp = timestamp;
        this.data = data;
        this.hash = hash;
    }

    public int getIndex() {
        return index;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getData() {
        return data;
    }

    public String getHash() {
        return hash;
    }

    public void addBlock() {

    }
}
