package wafflecore;

class WaffleBlock {
    private int index;
    private String prevHash;
    private long timestamp;
    private String data;
    private String hash;

    WaffleBlock(int index, String prevHash, long timestamp, String data, String hash) {
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

    public long getTimestamp() {
        return timestamp;
    }

    public String getData() {
        return data;
    }

    public String getHash() {
        return hash;
    }
}
