package wafflecore;

class Block {
    private int index;
    private String prevHash;
    private long timestamp;
    private String data;
    private String hash;

    Block(int index, String prevHash, long timestamp, String data, String hash) {
        this.index = index;
        this.prevHash = prevHash;
        this.timestamp = timestamp;
        this.data = data;
        this.hash = hash;
    }

    public String serialize() {

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

    public static Block getGenesisBlock() {
        return new Block(0, "0", 1495781049, "GenesisBlock", "3ff1d5ad18e3a5c8788b93218290a238aecf76f7b96b07ef2cfcd9eb6c93042a");
    }
}
